package com.ptithcm.lexigo.activities;

import android.content.Intent;
import android.graphics.Color;
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

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.appbar.MaterialToolbar;
import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.api.ApiClient;
import com.ptithcm.lexigo.api.models.GrammarExercise;
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

/**
 * Unified Quiz Activity for both Vocabulary and Grammar quizzes
 * Supports multiple question types: multiple_choice, fill_in_blank, etc.
 */
public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";

    // Intent extras constants
    public static final String EXTRA_QUIZ_TYPE = "quiz_type";
    public static final String EXTRA_TOPIC_ID = "topic_id";
    public static final String EXTRA_LESSON_ID = "lesson_id";
    public static final String EXTRA_LEVEL = "level";
    public static final String EXTRA_TITLE = "title";

    public static final String QUIZ_TYPE_VOCAB = "vocab";
    public static final String QUIZ_TYPE_GRAMMAR = "grammar";

    // UI Components
    private MaterialToolbar toolbar;
    private CardView cardProgress;
    private TextView tvQuestionNumber, tvTimer;
    private ProgressBar horizontalProgress, loadingProgress;
    private CardView cardQuestion;
    private TextView tvQuestion, tvQuestionType;
    private RadioGroup radioGroup;
    private com.google.android.material.textfield.TextInputLayout inputLayoutAnswer;
    private com.google.android.material.textfield.TextInputEditText etAnswer;
    private CardView cardExplanation;
    private TextView tvExplanation;
    private LinearLayout buttonLayout;
    private Button btnSubmit, btnNext, btnFinish;

    // Data
    private LexiGoApiService apiService;
    private List<QuizQuestion> quizQuestions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int correctAnswers = 0;

    // Track per-question results for review and final dialog
    private List<QuestionResult> quizResults;

    private String quizType;
    private String topicId;
    private String lessonId;
    private String level;
    private String quizTitle;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            quizType = intent.getStringExtra(EXTRA_QUIZ_TYPE);
            topicId = intent.getStringExtra(EXTRA_TOPIC_ID);
            lessonId = intent.getStringExtra(EXTRA_LESSON_ID);
            level = intent.getStringExtra(EXTRA_LEVEL);
            quizTitle = intent.getStringExtra(EXTRA_TITLE);
        }

        // Set title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(quizTitle != null ? quizTitle + " - Quiz" : "Quiz");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();
        loadQuiz();
        startTime = System.currentTimeMillis();

        // Initialize results list
        quizResults = new ArrayList<>();

        // Handle system back via OnBackPressedDispatcher with confirmation when a quiz is in progress
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (quizQuestions != null && currentQuestionIndex > 0 && currentQuestionIndex < quizQuestions.size()) {
                    new AlertDialog.Builder(QuizActivity.this)
                        .setTitle("Exit Quiz?")
                        .setMessage("Your progress will be lost. Are you sure you want to exit?")
                        .setPositiveButton("Yes", (dialog, which) -> finish())
                        .setNegativeButton("No", null)
                        .show();
                } else {
                    finish();
                }
            }
        });
    }

    private void initViews() {
        // Setup toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(quizTitle != null ? quizTitle + " - Quiz" : "Quiz");
        toolbar.setNavigationOnClickListener(v -> finish());

        // Progress card
        cardProgress = findViewById(R.id.card_progress);
        tvQuestionNumber = findViewById(R.id.tv_question_number);
//        tvTimer = findViewById(R.id.tv_timer);
        horizontalProgress = findViewById(R.id.horizontal_progress);

        // Loading
        loadingProgress = findViewById(R.id.loading_progress);

        // Question card
        cardQuestion = findViewById(R.id.card_question);
        tvQuestion = findViewById(R.id.tv_question);
        tvQuestionType = findViewById(R.id.tv_question_type);
        radioGroup = findViewById(R.id.radio_group);
        inputLayoutAnswer = findViewById(R.id.input_layout_answer);
        etAnswer = findViewById(R.id.et_answer);

        // Explanation card
        cardExplanation = findViewById(R.id.card_explanation);
        tvExplanation = findViewById(R.id.tv_explanation);

        // Buttons
        buttonLayout = findViewById(R.id.button_layout);
        btnSubmit = findViewById(R.id.btn_submit);
        btnNext = findViewById(R.id.btn_next);
        btnFinish = findViewById(R.id.btn_finish);

        // Initialize API service
        apiService = ApiClient.getInstance(this).getApiService();

        // Set click listeners
        btnSubmit.setOnClickListener(v -> checkAnswer());
        btnNext.setOnClickListener(v -> loadNextQuestion());
        btnFinish.setOnClickListener(v -> showFinalResults());

        // Initially hide explanation
        cardExplanation.setVisibility(View.GONE);
    }

    private void loadQuiz() {
        if (quizType == null) {
            showError("Quiz type is required");
            finish();
            return;
        }

        loadingProgress.setVisibility(View.VISIBLE);
        cardQuestion.setVisibility(View.GONE);

        if (QUIZ_TYPE_VOCAB.equals(quizType)) {
            loadVocabQuiz();
        } else if (QUIZ_TYPE_GRAMMAR.equals(quizType)) {
            loadGrammarQuiz();
        } else {
            showError("Invalid quiz type");
            finish();
        }
    }

    private void loadVocabQuiz() {
        if (topicId == null) {
            showError("Topic ID is required for vocab quiz");
            finish();
            return;
        }

        Call<ApiResponse<List<VocabQuiz>>> call = apiService.getVocabQuizzes(topicId);
        call.enqueue(new Callback<ApiResponse<List<VocabQuiz>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<VocabQuiz>>> call,
                                 Response<ApiResponse<List<VocabQuiz>>> response) {
                loadingProgress.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<VocabQuiz>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        List<VocabQuiz> vocabQuizzes = apiResponse.getData();
                        Log.d(TAG, "Received " + vocabQuizzes.size() + " vocab quizzes");
                        quizQuestions = convertVocabQuizzes(vocabQuizzes);

                        if (!quizQuestions.isEmpty()) {
                            cardQuestion.setVisibility(View.VISIBLE);
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
                loadingProgress.setVisibility(View.GONE);
                Log.e(TAG, "Error loading vocab quiz", t);
                showError("Network error: " + t.getMessage());
            }
        });
    }

    private void loadGrammarQuiz() {
        if (lessonId == null) {
            showError("Lesson ID is required for grammar quiz");
            finish();
            return;
        }

        Call<ApiResponse<List<GrammarExercise>>> call = apiService.getGrammarExercisesByGrammarId(lessonId);
        call.enqueue(new Callback<ApiResponse<List<GrammarExercise>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<GrammarExercise>>> call,
                                 Response<ApiResponse<List<GrammarExercise>>> response) {
                loadingProgress.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<GrammarExercise>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        List<GrammarExercise> grammarExercises = apiResponse.getData();
                        quizQuestions = convertGrammarExercises(grammarExercises);

                        if (!quizQuestions.isEmpty()) {
                            cardQuestion.setVisibility(View.VISIBLE);
                            displayQuestion();
                        } else {
                            showError("No exercises available");
                        }
                    } else {
                        showError("Failed to load exercises: " + apiResponse.getMessage());
                    }
                } else {
                    showError("Failed to load exercises");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<GrammarExercise>>> call, Throwable t) {
                loadingProgress.setVisibility(View.GONE);
                Log.e(TAG, "Error loading grammar exercises", t);
                showError("Network error: " + t.getMessage());
            }
        });
    }

    private List<QuizQuestion> convertVocabQuizzes(List<VocabQuiz> vocabQuizzes) {
        List<QuizQuestion> questions = new ArrayList<>();
        for (int i = 0; i < vocabQuizzes.size(); i++) {
            VocabQuiz quiz = vocabQuizzes.get(i);
            Log.d(TAG, "=== VocabQuiz #" + (i + 1) + " ===");
            Log.d(TAG, "Question: " + quiz.getQuestion());
            Log.d(TAG, "QuestionType: " + quiz.getQuestionType());
            Log.d(TAG, "Correct Answer: " + quiz.getCorrectAnswer());
            Log.d(TAG, "Answer Index: " + quiz.getAnswerIndex());
            Log.d(TAG, "Options: " + quiz.getOptions());
            Log.d(TAG, "Explanation: " + quiz.getExplanation());

            QuizQuestion question = new QuizQuestion();
            question.setQuestion(quiz.getQuestion());
            question.setQuestionType(quiz.getQuestionType());
            question.setOptions(quiz.getOptions());

            // Determine correct answer - use correctAnswer if available, otherwise use answerIndex
            String correctAnswer = quiz.getCorrectAnswer();
            if ((correctAnswer == null || correctAnswer.isEmpty()) && quiz.getAnswerIndex() != null) {
                List<String> options = quiz.getOptions();
                int index = quiz.getAnswerIndex();
                if (options != null && index >= 0 && index < options.size()) {
                    correctAnswer = options.get(index);
                    Log.d(TAG, "Using answer from index " + index + ": " + correctAnswer);
                }
            }

            question.setCorrectAnswer(correctAnswer);
            question.setExplanation(quiz.getExplanation());
            questions.add(question);
        }
        return questions;
    }

    private List<QuizQuestion> convertGrammarExercises(List<GrammarExercise> exercises) {
        List<QuizQuestion> questions = new ArrayList<>();
        for (int i = 0; i < exercises.size(); i++) {
            GrammarExercise exercise = exercises.get(i);
            Log.d(TAG, "=== GrammarExercise #" + (i + 1) + " ===");
            Log.d(TAG, "Question: " + exercise.getQuestion());
            Log.d(TAG, "ExerciseType: " + exercise.getExerciseType());
            Log.d(TAG, "Correct Answer: " + exercise.getCorrectAnswer());
            Log.d(TAG, "Answer Index: " + exercise.getAnswerIndex());
            Log.d(TAG, "Options: " + exercise.getOptions());

            QuizQuestion question = new QuizQuestion();
            question.setQuestion(exercise.getQuestion());
            question.setQuestionType(exercise.getExerciseType());

            // Handle options based on exercise type
            List<String> options = exercise.getOptions();
            String exerciseType = exercise.getExerciseType();

            // For true_false type, generate True/False options if not provided
            if ("true_false".equalsIgnoreCase(exerciseType) && (options == null || options.isEmpty())) {
                options = new ArrayList<>();
                options.add("True");
                options.add("False");
                Log.d(TAG, "Generated True/False options for true_false type");
            }

            question.setOptions(options);

            // Determine correct answer - use correctAnswer if available, otherwise use answerIndex
            String correctAnswer = exercise.getCorrectAnswer();
            if ((correctAnswer == null || correctAnswer.isEmpty()) && exercise.getAnswerIndex() != null) {
                int index = exercise.getAnswerIndex();
                if (options != null && index >= 0 && index < options.size()) {
                    correctAnswer = options.get(index);
                    Log.d(TAG, "Using answer from index " + index + ": " + correctAnswer);
                }
            }

            // Normalize true/false answers to match options
            if ("true_false".equalsIgnoreCase(exerciseType) && correctAnswer != null) {
                if ("true".equalsIgnoreCase(correctAnswer)) {
                    correctAnswer = "True";
                } else if ("false".equalsIgnoreCase(correctAnswer)) {
                    correctAnswer = "False";
                }
            }

            question.setCorrectAnswer(correctAnswer);
            question.setExplanation(exercise.getExplanation());
            questions.add(question);
        }
        return questions;
    }

    private void displayQuestion() {
        if (quizQuestions == null || currentQuestionIndex >= quizQuestions.size()) {
            showQuizComplete();
            return;
        }

        QuizQuestion currentQuestion = quizQuestions.get(currentQuestionIndex);

        // Update progress
        tvQuestionNumber.setText("Câu " + (currentQuestionIndex + 1) + "/" + quizQuestions.size());
        horizontalProgress.setMax(quizQuestions.size());
        horizontalProgress.setProgress(currentQuestionIndex + 1);

        // Display question
        tvQuestion.setText(currentQuestion.getQuestion());

        // Display question type
        String questionType = currentQuestion.getQuestionType();
        if (questionType != null && !questionType.isEmpty()) {
            tvQuestionType.setText("Type: " + questionType.replace("_", " ").toUpperCase());
            tvQuestionType.setVisibility(View.VISIBLE);
        } else {
            tvQuestionType.setVisibility(View.GONE);
        }

        // Check if this is a fill_blank question
        boolean isFillBlank = "fill_blank".equalsIgnoreCase(questionType);

        // Clear previous options
        radioGroup.removeAllViews();
        radioGroup.clearCheck();

        // Clear previous input
        if (etAnswer != null) {
            etAnswer.setText("");
        }

        if (isFillBlank) {
            // Show input field for fill_blank type
            inputLayoutAnswer.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.GONE);
        } else {
            // Show radio buttons for other types
            inputLayoutAnswer.setVisibility(View.GONE);
            radioGroup.setVisibility(View.VISIBLE);

            // Add new options
            if (currentQuestion.getOptions() != null && !currentQuestion.getOptions().isEmpty()) {
                for (String option : currentQuestion.getOptions()) {
                    RadioButton radioButton = new RadioButton(this);
                    radioButton.setText(option);
                    radioButton.setTextSize(16);
                    radioButton.setPadding(24, 24, 24, 24);
                    radioButton.setTextColor(getResources().getColor(R.color.text_primary));
                    radioGroup.addView(radioButton);
                }
            }
        }

        // Reset UI state
        cardExplanation.setVisibility(View.GONE);
        btnSubmit.setEnabled(true);
        btnSubmit.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.GONE);
        btnFinish.setVisibility(View.GONE);

        // Re-enable input controls
        radioGroup.setEnabled(true);
        if (etAnswer != null) {
            etAnswer.setEnabled(true);
        }
        if (inputLayoutAnswer != null) {
            inputLayoutAnswer.setError(null);
            inputLayoutAnswer.setBoxStrokeColor(getResources().getColor(R.color.primary));
        }
    }

    private void checkAnswer() {
        QuizQuestion currentQuestion = quizQuestions.get(currentQuestionIndex);
        String questionType = currentQuestion.getQuestionType();
        String selectedAnswer = null;

        // Get answer based on question type
        boolean isFillBlank = "fill_blank".equalsIgnoreCase(questionType);

        if (isFillBlank) {
            // Get answer from EditText
            if (etAnswer != null && etAnswer.getText() != null) {
                selectedAnswer = etAnswer.getText().toString().trim();
            }

            if (selectedAnswer == null || selectedAnswer.isEmpty()) {
                Toast.makeText(this, "Please enter an answer", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            // Get answer from RadioButton
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedButton = findViewById(selectedId);
            selectedAnswer = selectedButton.getText().toString();
        }

        String correctAnswer = currentQuestion.getCorrectAnswer();
        Log.d(TAG, "Checking answer - Selected: " + selectedAnswer + ", Correct: " + correctAnswer);

        // If correctAnswer is null, we can't validate - show error
        if (correctAnswer == null || correctAnswer.isEmpty()) {
            Log.e(TAG, "ERROR: correctAnswer is null or empty for question: " + currentQuestion.getQuestion());
            Toast.makeText(this, "Error: Quiz data incomplete. Cannot validate answer.", Toast.LENGTH_LONG).show();
            // Still allow progression
            btnSubmit.setEnabled(false);
            btnSubmit.setVisibility(View.GONE);
            if (currentQuestionIndex < quizQuestions.size() - 1) {
                btnNext.setVisibility(View.VISIBLE);
            } else {
                btnFinish.setVisibility(View.VISIBLE);
            }
            return;
        }

        // Check answer (case-insensitive for fill_blank)
        boolean isCorrect;
        if (isFillBlank) {
            isCorrect = selectedAnswer.equalsIgnoreCase(correctAnswer);
        } else {
            isCorrect = selectedAnswer.equals(correctAnswer);
        }

        // Save result for final dialog/review
        quizResults.add(new QuestionResult(
            currentQuestion.getQuestion(),
            selectedAnswer,
            correctAnswer,
            isCorrect,
            currentQuestion.getExplanation()
        ));

        // Update score
        if (isCorrect) {
            correctAnswers++;
            score += 10; // 10 points per correct answer

            if (isFillBlank) {
                // Show success with green hint
                inputLayoutAnswer.setBoxStrokeColor(getResources().getColor(R.color.beginner_color));
                inputLayoutAnswer.setHintTextColor(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.beginner_color)));
            } else {
                // Highlight selected answer in green
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedButton = findViewById(selectedId);
                if (selectedButton != null) {
                    selectedButton.setTextColor(getResources().getColor(R.color.beginner_color));
                }
            }

            Toast.makeText(this, "✓ Correct!", Toast.LENGTH_SHORT).show();
        } else {
            if (isFillBlank) {
                // Show error with red hint and display correct answer
                inputLayoutAnswer.setBoxStrokeColor(getResources().getColor(R.color.advanced_color));
                inputLayoutAnswer.setHintTextColor(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.advanced_color)));
                inputLayoutAnswer.setError("Correct answer: " + correctAnswer);
            } else {
                // Highlight selected answer in red
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedButton = findViewById(selectedId);
                if (selectedButton != null) {
                    selectedButton.setTextColor(getResources().getColor(R.color.advanced_color));
                }

                // Highlight correct answer in green
                for (int i = 0; i < radioGroup.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) radioGroup.getChildAt(i);
                    if (rb.getText().toString().equals(correctAnswer)) {
                        rb.setTextColor(getResources().getColor(R.color.beginner_color));
                        break;
                    }
                }
            }

            Toast.makeText(this, "✗ Wrong! Correct: " + (correctAnswer != null ? correctAnswer : "N/A"),
                         Toast.LENGTH_LONG).show();
        }


        // Show explanation if available
        if (currentQuestion.getExplanation() != null && !currentQuestion.getExplanation().isEmpty()) {
            tvExplanation.setText("💡 " + currentQuestion.getExplanation());
            cardExplanation.setVisibility(View.VISIBLE);
        }

        // Disable answer selection
        radioGroup.setEnabled(false);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(false);
        }
        if (etAnswer != null) {
            etAnswer.setEnabled(false);
        }

        btnSubmit.setEnabled(false);
        btnSubmit.setVisibility(View.GONE);

        // Show appropriate next button
        if (currentQuestionIndex < quizQuestions.size() - 1) {
            btnNext.setVisibility(View.VISIBLE);
        } else {
            btnFinish.setVisibility(View.VISIBLE);
        }
    }

    private void loadNextQuestion() {
        currentQuestionIndex++;
        displayQuestion();
    }

    private void showQuizComplete() {
        // This shouldn't be called directly, use showFinalResults instead
        showFinalResults();
    }

    private void showFinalResults() {
        int totalQuestions = quizQuestions.size();
        double percentage = totalQuestions > 0 ? (double) correctAnswers / totalQuestions * 100 : 0;
        int maxScore = totalQuestions * 10;

        // Determine performance level
        String performanceLevel;
        int performanceColor;
        if (percentage >= 80) {
            performanceLevel = "Excellent!";
            performanceColor = getResources().getColor(R.color.beginner_color);
        } else if (percentage >= 60) {
            performanceLevel = "Good!";
            performanceColor = getResources().getColor(R.color.intermediate_color);
        } else {
            performanceLevel = "Keep Practicing!";
            performanceColor = getResources().getColor(R.color.advanced_color);
        }

        // Tự động cập nhật tiến độ dựa trên loại quiz
        updateProgressForQuizCompletion();

        // Inflate the shared dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_quiz_results, null);
        TextView tvScore = dialogView.findViewById(R.id.tvScore);
        LinearLayout llQuestionDetails = dialogView.findViewById(R.id.llQuestionDetails);

        // Set summary data (show percentage as /100)
        int displayScore = (int) Math.round(percentage);
        tvScore.setText(displayScore + "/100");

        // Populate question details from quizResults
        for (int i = 0; i < quizResults.size(); i++) {
            QuestionResult result = quizResults.get(i);

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

            if (!result.isCorrect) {
                TextView tvAnswer = new TextView(this);
                tvAnswer.setText(String.format("Bạn chọn: %s\nĐáp án đúng: %s",
                    result.selectedAnswer, result.correctAnswer));
                tvAnswer.setTextSize(12);
                tvAnswer.setTextColor(getResources().getColor(R.color.text_secondary, null));
                tvAnswer.setPadding(0, 8, 0, 0);
                questionView.addView(tvAnswer);

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

        // Show dialog with same buttons as before
        new AlertDialog.Builder(this)
            .setTitle("Kết quả bài làm")
            .setView(dialogView)
            .setPositiveButton("Review Answers", (dialog, which) -> {
                // Reset to first question for review
                currentQuestionIndex = 0;
                displayQuestion();
                btnSubmit.setVisibility(View.GONE);
                btnNext.setVisibility(View.VISIBLE);
            })
            .setNeutralButton("Về trang chủ", (dialog, which) -> {
                Intent intent = new Intent(QuizActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            })
            .setNegativeButton("Finish", (dialog, which) -> finish())
            .setCancelable(false)
            .show();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Log.e(TAG, message);
    }

    /**
     * Tự động cập nhật tiến độ khi hoàn thành quiz
     */
    private void updateProgressForQuizCompletion() {
        ProgressTracker.ExerciseType exerciseType;
        String id = null;

        if (QUIZ_TYPE_VOCAB.equals(quizType)) {
            exerciseType = ProgressTracker.ExerciseType.VOCAB;
            id = topicId;
        } else if (QUIZ_TYPE_GRAMMAR.equals(quizType)) {
            exerciseType = ProgressTracker.ExerciseType.GRAMMAR;
            id = lessonId;
        } else {
            Log.w(TAG, "Unknown quiz type, skipping progress update");
            return;
        }

        // Calculate study time in minutes
        long endTime = System.currentTimeMillis();
        int studyTimeMinutes = (int) ((endTime - startTime) / 60000);
        if (studyTimeMinutes < 1) studyTimeMinutes = 1; // Minimum 1 minute

        // Calculate score
        int totalQuestions = quizQuestions.size();
        double finalScore = totalQuestions > 0 ? (double) score / (totalQuestions * 10) * 100 : 0;

        ProgressTracker.updateDetailedProgress(this, exerciseType, id, id, finalScore, studyTimeMinutes,
            new ProgressTracker.ProgressUpdateCallback() {
            @Override
            public void onSuccess(Progress progress) {
                Log.d(TAG, "Progress updated successfully after quiz completion");
            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "Failed to update progress: " + message);
                // Không hiển thị lỗi cho user vì đây là background operation
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // remove the old onBackPressed override - handled by OnBackPressedCallback now

    // Inner class to represent a unified quiz question
    private static class QuizQuestion {
        private String question;
        private String questionType;
        private List<String> options;
        private String correctAnswer;
        private String explanation;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getQuestionType() {
            return questionType;
        }

        public void setQuestionType(String questionType) {
            this.questionType = questionType;
        }

        public List<String> getOptions() {
            return options;
        }

        public void setOptions(List<String> options) {
            this.options = options;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public void setCorrectAnswer(String correctAnswer) {
            this.correctAnswer = correctAnswer;
        }

        public String getExplanation() {
            return explanation;
        }

        public void setExplanation(String explanation) {
            this.explanation = explanation;
        }
    }

    // New inner class to store per-question result
    private static class QuestionResult {
        String question;
        String selectedAnswer;
        String correctAnswer;
        boolean isCorrect;
        String explanation;

        QuestionResult(String question, String selectedAnswer, String correctAnswer, boolean isCorrect, String explanation) {
            this.question = question;
            this.selectedAnswer = selectedAnswer;
            this.correctAnswer = correctAnswer;
            this.isCorrect = isCorrect;
            this.explanation = explanation;
        }
    }
}

