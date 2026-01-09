package com.ptithcm.lexigo.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.api.ApiClient;
import com.ptithcm.lexigo.api.models.ReadingAnswer;
import com.ptithcm.lexigo.api.models.ReadingQuestion;
import com.ptithcm.lexigo.api.models.ReadingResult;
import com.ptithcm.lexigo.api.models.ReadingSubmitRequest;
import com.ptithcm.lexigo.api.responses.ApiResponse;
import com.ptithcm.lexigo.api.services.LexiGoApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadingQuizActivity extends AppCompatActivity {

    private TextView tvQuestionProgress;
    private TextView tvQuestionText;
    private RadioGroup rgOptions;
    private MaterialButton btnPrevious;
    private MaterialButton btnNext;
    private MaterialButton btnSubmit;
    private ProgressBar progressBar;

    private LexiGoApiService apiService;
    private String passageId;
    private List<ReadingQuestion> questions;
    private List<Integer> userAnswers; // Store selected options
    private int currentQuestionIndex = 0;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_quiz);

        initViews();
        setupToolbar();

        apiService = ApiClient.getInstance(this).getApiService();

        passageId = getIntent().getStringExtra("passage_id");

        if (passageId != null) {
            loadQuestions();
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy bài đọc", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupButtons();
        startTime = System.currentTimeMillis();
    }

    private void initViews() {
        tvQuestionProgress = findViewById(R.id.tvQuestionProgress);
        tvQuestionText = findViewById(R.id.tvQuestionText);
        rgOptions = findViewById(R.id.rgOptions);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupToolbar() {
        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupButtons() {
        btnPrevious.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                saveCurrentAnswer();
                currentQuestionIndex--;
                displayQuestion();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentQuestionIndex < questions.size() - 1) {
                saveCurrentAnswer();
                currentQuestionIndex++;
                displayQuestion();
            }
        });

        btnSubmit.setOnClickListener(v -> submitAnswers());
    }

    private void loadQuestions() {
        progressBar.setVisibility(View.VISIBLE);

        Call<ApiResponse<List<ReadingQuestion>>> call = apiService.getReadingQuestions(passageId);

        call.enqueue(new Callback<ApiResponse<List<ReadingQuestion>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ReadingQuestion>>> call,
                                 Response<ApiResponse<List<ReadingQuestion>>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    questions = response.body().getData();
                    
                    // Check if questions list is empty
                    if (questions == null || questions.isEmpty()) {
                        Toast.makeText(ReadingQuizActivity.this,
                                "Không có câu hỏi cho bài đọc này", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                    
                    userAnswers = new ArrayList<>();
                    for (int i = 0; i < questions.size(); i++) {
                        userAnswers.add(-1); // -1 means unanswered
                    }
                    displayQuestion();
                } else {
                    Toast.makeText(ReadingQuizActivity.this,
                            "Không thể tải câu hỏi", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ReadingQuestion>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ReadingQuizActivity.this,
                        "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void displayQuestion() {
        if (questions == null || questions.isEmpty()) {
            return;
        }

        ReadingQuestion question = questions.get(currentQuestionIndex);

        // Update progress text
        tvQuestionProgress.setText("Câu hỏi " + (currentQuestionIndex + 1) + "/" + questions.size());

        // Update question text
        tvQuestionText.setText(question.getQuestionText());

        // Clear previous options
        rgOptions.removeAllViews();

        // Add options dynamically
        List<String> options = question.getOptions();
        for (int i = 0; i < options.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(View.generateViewId());
            radioButton.setText(options.get(i));
            radioButton.setTextSize(16);
            radioButton.setPadding(16, 16, 16, 16);
            rgOptions.addView(radioButton);

            // Restore previous answer
            if (userAnswers.get(currentQuestionIndex) == i) {
                radioButton.setChecked(true);
            }
        }

        // Update button visibility
        btnPrevious.setVisibility(currentQuestionIndex > 0 ? View.VISIBLE : View.GONE);
        
        if (currentQuestionIndex == questions.size() - 1) {
            btnNext.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.VISIBLE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);
        }
    }

    private void saveCurrentAnswer() {
        int checkedId = rgOptions.getCheckedRadioButtonId();
        if (checkedId != -1) {
            // Find which option was selected
            for (int i = 0; i < rgOptions.getChildCount(); i++) {
                if (rgOptions.getChildAt(i).getId() == checkedId) {
                    userAnswers.set(currentQuestionIndex, i);
                    break;
                }
            }
        }
    }

    private void submitAnswers() {
        saveCurrentAnswer();

        // Check if all questions are answered
        for (int answer : userAnswers) {
            if (answer == -1) {
                Toast.makeText(this, "Vui lòng trả lời tất cả câu hỏi", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        progressBar.setVisibility(View.VISIBLE);

        // Prepare request
        List<ReadingAnswer> answers = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            answers.add(new ReadingAnswer(questions.get(i).getId(), userAnswers.get(i)));
        }
        ReadingSubmitRequest request = new ReadingSubmitRequest(answers);

        Call<ApiResponse<ReadingResult>> call = apiService.submitReadingAnswers(passageId, request);

        call.enqueue(new Callback<ApiResponse<ReadingResult>>() {
            @Override
            public void onResponse(Call<ApiResponse<ReadingResult>> call,
                                 Response<ApiResponse<ReadingResult>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    ReadingResult result = response.body().getData();
                    
                    // Update progress
                    updateProgress(result);
                    
                    showResults(result);
                } else {
                    Toast.makeText(ReadingQuizActivity.this,
                            "Không thể nộp bài", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ReadingResult>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ReadingQuizActivity.this,
                        "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showResults(ReadingResult result) {
        // Inflate custom dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_quiz_results, null);
        
        // Get views
        TextView tvScore = dialogView.findViewById(R.id.tvScore);
        LinearLayout llQuestionDetails = dialogView.findViewById(R.id.llQuestionDetails);
        
        // Set summary data
        tvScore.setText(result.getScore() + "/100");

        // Add question details
        if (result.getResults() != null && !result.getResults().isEmpty()) {
            for (int i = 0; i < result.getResults().size(); i++) {
                ReadingResult.QuestionResult questionResult = result.getResults().get(i);

                // Create question detail view
                LinearLayout questionView = new LinearLayout(this);
                questionView.setOrientation(LinearLayout.VERTICAL);
                questionView.setPadding(16, 12, 16, 12);
                questionView.setBackgroundColor(questionResult.isCorrect() ? 
                    getResources().getColor(R.color.beginner_color, null) : 
                    getResources().getColor(R.color.advanced_color, null));
                questionView.getBackground().setAlpha(30); // Light background
                
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 16);
                questionView.setLayoutParams(params);
                
                // Question number and status
                TextView tvQuestionStatus = new TextView(this);
                tvQuestionStatus.setText(String.format("Câu %d: %s", 
                    i + 1, 
                    questionResult.isCorrect() ? "✓ Đúng" : "✗ Sai"));
                tvQuestionStatus.setTextSize(14);
                tvQuestionStatus.setTypeface(null, android.graphics.Typeface.BOLD);
                tvQuestionStatus.setTextColor(questionResult.isCorrect() ? 
                    getResources().getColor(R.color.beginner_color, null) : 
                    getResources().getColor(R.color.advanced_color, null));
                questionView.addView(tvQuestionStatus);
                
                // Show correct answer if wrong
                if (!questionResult.isCorrect()) {
                    TextView tvAnswer = new TextView(this);
                    
                    // Get question and options
                    ReadingQuestion question = questions.get(i);
                    String selectedText = question.getOptions().get(questionResult.getSelectedOption());
                    String correctText = question.getOptions().get(questionResult.getCorrectOption());
                    
                    tvAnswer.setText(String.format("Bạn chọn: %s\nĐáp án đúng: %s", 
                        selectedText, correctText));
                    tvAnswer.setTextSize(12);
                    tvAnswer.setTextColor(getResources().getColor(R.color.text_secondary, null));
                    tvAnswer.setPadding(0, 8, 0, 0);
                    questionView.addView(tvAnswer);
                }
                
                llQuestionDetails.addView(questionView);
            }
        }
        
        // Show dialog
        new AlertDialog.Builder(this)
                .setTitle("Kết quả bài làm")
                .setView(dialogView)
                .setPositiveButton("Hoàn thành", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }
    private void updateProgress(ReadingResult result) {
        // Calculate study time in minutes
        long endTime = System.currentTimeMillis();
        int studyTimeMinutes = (int) ((endTime - startTime) / 60000);
        if (studyTimeMinutes < 1) studyTimeMinutes = 1; // Minimum 1 minute

        // Update progress
        com.ptithcm.lexigo.utils.ProgressTracker.updateDetailedProgress(this, 
            com.ptithcm.lexigo.utils.ProgressTracker.ExerciseType.READING,
            passageId, null, result.getScore(), studyTimeMinutes,
            new com.ptithcm.lexigo.utils.ProgressTracker.ProgressUpdateCallback() {
                @Override
                public void onSuccess(com.ptithcm.lexigo.api.models.Progress progress) {
                    android.util.Log.d("ReadingQuizActivity", "Reading progress updated successfully");
                }

                @Override
                public void onError(String message) {
                    android.util.Log.e("ReadingQuizActivity", "Failed to update reading progress: " + message);
                }
            });
    }
}
