package com.ptithcm.lexigo.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.api.ApiClient;
import com.ptithcm.lexigo.api.models.Progress;
import com.ptithcm.lexigo.api.models.VocabQuiz;
import com.ptithcm.lexigo.api.responses.ApiResponse;
import com.ptithcm.lexigo.api.services.LexiGoApiService;
import com.ptithcm.lexigo.utils.ProgressTracker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VocabQuizActivity extends AppCompatActivity {
    private static final String TAG = "VocabQuizActivity";
    
    private MaterialToolbar toolbar;
    private TextView tvQuestion, tvQuestionNumber, tvExplanation;
    private RadioGroup radioGroup;
    private Button btnSubmit, btnNext;
    private ProgressBar progressBar;
    
    private LexiGoApiService apiService;
    private List<VocabQuiz> quizList;
    private List<QuizResult> quizResults; // Track results
    private int currentQuestionIndex = 0;
    private int score = 0;
    
    private String topicId;
    private String level;
    private long startTime;

    // Inner class to track quiz results
    private static class QuizResult {
        String question;
        String selectedAnswer;
        String correctAnswer;
        boolean isCorrect;
        String explanation;

        QuizResult(String question, String selectedAnswer, String correctAnswer, boolean isCorrect, String explanation) {
            this.question = question;
            this.selectedAnswer = selectedAnswer;
            this.correctAnswer = correctAnswer;
            this.isCorrect = isCorrect;
            this.explanation = explanation;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_quiz);
        
        // Get topic ID from intent
        if (getIntent() != null) {
            topicId = getIntent().getStringExtra("topic_id");
            level = getIntent().getStringExtra("level");
            String topicName = getIntent().getStringExtra("topic_name");
            
            if (getSupportActionBar() != null && topicName != null) {
                getSupportActionBar().setTitle(topicName + " - Quiz");
            }
        }
        
        quizResults = new ArrayList<>();
        initViews();
        loadQuiz();
        startTime = System.currentTimeMillis();
    }
    
    private void initViews() {
        // Setup toolbar
        toolbar = findViewById(R.id.toolbar);
        if (getIntent() != null) {
            String topicName = getIntent().getStringExtra("topic_name");
            if (topicName != null) {
                toolbar.setTitle(topicName + " - Quiz");
            } else {
                toolbar.setTitle("Từ vựng Quiz");
            }
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        tvQuestionNumber = findViewById(R.id.tv_question_number);
        tvQuestion = findViewById(R.id.tv_question);
        tvExplanation = findViewById(R.id.tv_explanation);
        radioGroup = findViewById(R.id.radio_group);
        btnSubmit = findViewById(R.id.btn_submit);
        btnNext = findViewById(R.id.btn_next);
        progressBar = findViewById(R.id.progress_bar);
        
        apiService = ApiClient.getInstance(this).getApiService();
        
        btnSubmit.setOnClickListener(v -> checkAnswer());
        btnNext.setOnClickListener(v -> loadNextQuestion());
    }
    
    private void loadQuiz() {
        if (topicId == null) {
            showError("Topic ID is required");
            finish();
            return;
        }
        
        progressBar.setVisibility(View.VISIBLE);
        
        Call<ApiResponse<List<VocabQuiz>>> call = apiService.getVocabQuizzes(topicId);
        call.enqueue(new Callback<ApiResponse<List<VocabQuiz>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<VocabQuiz>>> call, 
                                 Response<ApiResponse<List<VocabQuiz>>> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<VocabQuiz>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        quizList = apiResponse.getData();
                        if (!quizList.isEmpty()) {
                            displayQuestion();
                        } else {
                            showError("No quiz questions available");
                        }
                    } else {
                        showError("Failed to load quiz: " + apiResponse.getMessage());
                    }
                } else {
                    showError("Failed to load quiz");
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<List<VocabQuiz>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Error loading quiz", t);
                showError("Network error: " + t.getMessage());
            }
        });
    }
    
    private void displayQuestion() {
        if (quizList == null || currentQuestionIndex >= quizList.size()) {
            showQuizComplete();
            return;
        }
        
        VocabQuiz currentQuiz = quizList.get(currentQuestionIndex);
        
        tvQuestionNumber.setText("Câu " + (currentQuestionIndex + 1) + "/" + quizList.size());
        tvQuestion.setText(currentQuiz.getQuestion());
        tvExplanation.setVisibility(View.GONE);
        
        // Clear previous options
        radioGroup.removeAllViews();
        radioGroup.clearCheck();
        
        // Add new options
        if (currentQuiz.getOptions() != null) {
            for (String option : currentQuiz.getOptions()) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(option);
                radioButton.setTextSize(16);
                radioButton.setPadding(16, 16, 16, 16);
                radioGroup.addView(radioButton);
            }
        }
        
        btnSubmit.setEnabled(true);
        btnSubmit.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.GONE);
    }
    
    private void checkAnswer() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Vui lòng chọn một đáp án", Toast.LENGTH_SHORT).show();
            return;
        }
        
        RadioButton selectedButton = findViewById(selectedId);
        String selectedAnswer = selectedButton.getText().toString();
        
        VocabQuiz currentQuiz = quizList.get(currentQuestionIndex);
        boolean isCorrect = selectedAnswer.equals(currentQuiz.getCorrectAnswer());
        
        // Track result
        quizResults.add(new QuizResult(
            currentQuiz.getQuestion(),
            selectedAnswer,
            currentQuiz.getCorrectAnswer(),
            isCorrect,
            currentQuiz.getExplanation()
        ));
        
        if (isCorrect) {
            score += 10; // 10 points per correct answer
            Toast.makeText(this, "Chính xác! ✓", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sai rồi! Đáp án đúng: " + currentQuiz.getCorrectAnswer(),
                         Toast.LENGTH_LONG).show();
        }
        
        // Show explanation if available
        if (currentQuiz.getExplanation() != null && !currentQuiz.getExplanation().isEmpty()) {
            tvExplanation.setText("Giải thích: " + currentQuiz.getExplanation());
            tvExplanation.setVisibility(View.VISIBLE);
        }
        
        btnSubmit.setEnabled(false);
        btnNext.setVisibility(View.VISIBLE);
    }
    
    private void loadNextQuestion() {
        currentQuestionIndex++;
        displayQuestion();
    }
    
    private void showQuizComplete() {
        // Calculate study time in minutes
        long endTime = System.currentTimeMillis();
        int studyTimeMinutes = (int) ((endTime - startTime) / 60000);
        if (studyTimeMinutes < 1) studyTimeMinutes = 1; // Minimum 1 minute

        // Calculate score (0-100) - score is already in 10-point system
        int maxScore = quizList.size() * 10;
        double finalScore = (double) score / maxScore * 100;

        // Update progress first
        ProgressTracker.updateDetailedProgress(this, ProgressTracker.ExerciseType.VOCAB,
            topicId, topicId, finalScore, studyTimeMinutes,
            new ProgressTracker.ProgressUpdateCallback() {
                @Override
                public void onSuccess(Progress progress) {
                    Log.d(TAG, "Vocab progress updated successfully");
                }

                @Override
                public void onError(String message) {
                    Log.e(TAG, "Failed to update vocab progress: " + message);
                }
            });
        
        // Show enhanced results dialog
        showEnhancedResults();
    }
    
    private void showEnhancedResults() {
        // Inflate custom dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_quiz_results, null);

        // Get views
        TextView tvScore = dialogView.findViewById(R.id.tvScore);
        LinearLayout llQuestionDetails = dialogView.findViewById(R.id.llQuestionDetails);

        // Calculate score (0-100)
        int maxScore = quizList.size() * 10;
        int correctCount = score / 10; // Convert points back to correct answer count
        int finalScore = (int) ((double) score / maxScore * 100);
        int wrongCount = quizList.size() - correctCount;

        // Set summary data
        tvScore.setText(finalScore + "/100");

        // Add question details
        for (int i = 0; i < quizResults.size(); i++) {
            QuizResult result = quizResults.get(i);

            // Create question detail view
            LinearLayout questionView = new LinearLayout(this);
            questionView.setOrientation(LinearLayout.VERTICAL);
            questionView.setPadding(16, 12, 16, 12);
            questionView.setBackgroundColor(result.isCorrect ? 
                getResources().getColor(R.color.beginner_color, null) : 
                getResources().getColor(R.color.advanced_color, null));
            questionView.getBackground().setAlpha(30);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 16);
            questionView.setLayoutParams(params);

            // Question title and status
            TextView tvQuestionStatus = new TextView(this);
            tvQuestionStatus.setText(String.format("Câu %d: %s", 
                i + 1, 
                result.isCorrect ? "✓ Đúng" : "✗ Sai"));
            tvQuestionStatus.setTextSize(14);
            tvQuestionStatus.setTypeface(null, android.graphics.Typeface.BOLD);
            tvQuestionStatus.setTextColor(result.isCorrect ? 
                getResources().getColor(R.color.beginner_color, null) : 
                getResources().getColor(R.color.advanced_color, null));
            questionView.addView(tvQuestionStatus);

            // Show answer details if wrong
            if (!result.isCorrect) {
                TextView tvAnswer = new TextView(this);
                tvAnswer.setText(String.format("Bạn chọn: %s\nĐáp án đúng: %s", 
                    result.selectedAnswer, result.correctAnswer));
                tvAnswer.setTextSize(12);
                tvAnswer.setTextColor(getResources().getColor(R.color.text_secondary, null));
                tvAnswer.setPadding(0, 8, 0, 0);
                questionView.addView(tvAnswer);

                // Show explanation if available
                if (result.explanation != null && !result.explanation.isEmpty()) {
                    TextView tvExplanation = new TextView(this);
                    tvExplanation.setText("Giải thích: " + result.explanation);
                    tvExplanation.setTextSize(12);
                    tvExplanation.setTextColor(getResources().getColor(R.color.text_secondary, null));
                    tvExplanation.setPadding(0, 4, 0, 0);
                    tvExplanation.setTypeface(null, android.graphics.Typeface.ITALIC);
                    questionView.addView(tvExplanation);
                }
            }

            llQuestionDetails.addView(questionView);
        }

        // Show dialog
        new AlertDialog.Builder(this)
                .setTitle("Kết quả bài làm")
                .setView(dialogView)
                .setPositiveButton("Hoàn thành", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }
    
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
