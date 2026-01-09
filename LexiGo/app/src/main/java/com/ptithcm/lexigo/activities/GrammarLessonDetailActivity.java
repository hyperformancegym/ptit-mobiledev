package com.ptithcm.lexigo.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.api.ApiClient;
import com.ptithcm.lexigo.api.models.GrammarLesson;
import com.ptithcm.lexigo.api.responses.ApiResponse;
import com.ptithcm.lexigo.api.services.LexiGoApiService;
import com.ptithcm.lexigo.utils.QuizLauncher;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GrammarLessonDetailActivity extends AppCompatActivity {
    private static final String TAG = "GrammarLessonDetail";

    private TextView tvTitle, tvDescription, tvContent, tvRules, tvTips;
    private ProgressBar progressBar;
    private Button btnStartExercise;

    private LexiGoApiService apiService;

    private String lessonId;
    private String lessonTitle;
    private String lessonLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_lesson_detail);

        // Get lesson ID from intent
        if (getIntent() != null) {
            lessonId = getIntent().getStringExtra("lesson_id");
            lessonTitle = getIntent().getStringExtra("lesson_title");
            lessonLevel = getIntent().getStringExtra("lesson_level");

            if (getSupportActionBar() != null && lessonTitle != null) {
                getSupportActionBar().setTitle(lessonTitle);
            }
        }

        initViews();
        loadLessonDetail();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tv_lesson_title);
        tvDescription = findViewById(R.id.tv_lesson_description);
        tvContent = findViewById(R.id.tv_lesson_content);
        tvRules = findViewById(R.id.tv_lesson_rules);
        tvTips = findViewById(R.id.tv_lesson_tips);
        progressBar = findViewById(R.id.progress_bar);
        btnStartExercise = findViewById(R.id.btn_start_exercise);

        apiService = ApiClient.getInstance(this).getApiService();

        btnStartExercise.setOnClickListener(v -> {
            QuizLauncher.launchGrammarQuiz(
                this,
                lessonId,
                lessonTitle != null ? lessonTitle : "Grammar Exercise",
                lessonLevel
            );
        });
    }

    private void loadLessonDetail() {
        if (lessonId == null) {
            showError("Lesson ID is required");
            finish();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        Call<ApiResponse<GrammarLesson>> call = apiService.getGrammarLessonDetail(lessonId);
        call.enqueue(new Callback<ApiResponse<GrammarLesson>>() {
            @Override
            public void onResponse(Call<ApiResponse<GrammarLesson>> call,
                                 Response<ApiResponse<GrammarLesson>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<GrammarLesson> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        displayLessonDetail(apiResponse.getData());
                    } else {
                        showError("Failed to load lesson: " + apiResponse.getMessage());
                    }
                } else {
                    showError("Failed to load lesson");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<GrammarLesson>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Error loading lesson", t);
                showError("Network error: " + t.getMessage());
            }
        });
    }

    private void displayLessonDetail(GrammarLesson lesson) {
        tvTitle.setText(lesson.getTitle());

        // Use explanation if description is not available
        String description = lesson.getDescription();
        if (description == null || description.isEmpty()) {
            description = lesson.getExplanation();
        }
        if (description != null && !description.isEmpty()) {
            tvDescription.setText(description);
            tvDescription.setVisibility(View.VISIBLE);
        } else {
            tvDescription.setVisibility(View.GONE);
        }

        // Display content or explanation
        String content = lesson.getContent();
        if (content == null || content.isEmpty()) {
            content = lesson.getExplanation();
        }
        if (content != null && !content.isEmpty()) {
            tvContent.setText(content);
            tvContent.setVisibility(View.VISIBLE);
        } else {
            tvContent.setVisibility(View.GONE);
        }

        // Display rules
        if (lesson.getRules() != null && !lesson.getRules().isEmpty()) {
            StringBuilder rulesText = new StringBuilder("Rules:\n");
            for (int i = 0; i < lesson.getRules().size(); i++) {
                rulesText.append((i + 1)).append(". ").append(lesson.getRules().get(i)).append("\n");
            }
            tvRules.setText(rulesText.toString());
            tvRules.setVisibility(View.VISIBLE);
        } else {
            tvRules.setVisibility(View.GONE);
        }

        // Display examples
        if (lesson.getExamples() != null && !lesson.getExamples().isEmpty()) {
            StringBuilder examplesText = new StringBuilder("Examples:\n");
            for (int i = 0; i < lesson.getExamples().size(); i++) {
                examplesText.append("• ").append(lesson.getExamples().get(i)).append("\n");
            }
            // If tvRules is being used for both rules and examples
            if (lesson.getRules() == null || lesson.getRules().isEmpty()) {
                tvRules.setText(examplesText.toString());
                tvRules.setVisibility(View.VISIBLE);
            } else {
                // Append examples to rules
                String currentText = tvRules.getText().toString();
                tvRules.setText(currentText + "\n" + examplesText.toString());
            }
        }

        // Display tips
        if (lesson.getTips() != null && !lesson.getTips().isEmpty()) {
            tvTips.setText("Tips: " + lesson.getTips());
            tvTips.setVisibility(View.VISIBLE);
        } else {
            tvTips.setVisibility(View.GONE);
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}

