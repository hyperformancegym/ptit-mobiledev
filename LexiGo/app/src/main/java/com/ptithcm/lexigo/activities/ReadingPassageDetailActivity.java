package com.ptithcm.lexigo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.api.ApiClient;
import com.ptithcm.lexigo.api.models.ReadingPassage;
import com.ptithcm.lexigo.api.responses.ApiResponse;
import com.ptithcm.lexigo.api.services.LexiGoApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadingPassageDetailActivity extends AppCompatActivity {

    private TextView tvPassageTitle;
    private TextView tvPassageLevel;
    private ImageView ivPassageImage;
    private TextView tvPassageContent;
    private MaterialButton btnStartQuiz;

    private LexiGoApiService apiService;
    private String passageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_passage_detail);

        initViews();
        setupToolbar();

        apiService = ApiClient.getInstance(this).getApiService();

        // Get passage ID from intent
        passageId = getIntent().getStringExtra("passage_id");

        if (passageId != null) {
            loadPassageDetail();
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy bài đọc", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnStartQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReadingQuizActivity.class);
            intent.putExtra("passage_id", passageId);
            startActivity(intent);
        });
    }

    private void initViews() {
        tvPassageTitle = findViewById(R.id.tvPassageTitle);
        tvPassageLevel = findViewById(R.id.tvPassageLevel);
        ivPassageImage = findViewById(R.id.ivPassageImage);
        tvPassageContent = findViewById(R.id.tvPassageContent);
        btnStartQuiz = findViewById(R.id.btnStartQuiz);
    }

    private void setupToolbar() {
        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadPassageDetail() {
        Call<ApiResponse<ReadingPassage>> call = apiService.getReadingPassageDetail(passageId);

        call.enqueue(new Callback<ApiResponse<ReadingPassage>>() {
            @Override
            public void onResponse(Call<ApiResponse<ReadingPassage>> call,
                                 Response<ApiResponse<ReadingPassage>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    ReadingPassage passage = response.body().getData();
                    displayPassage(passage);
                } else {
                    Toast.makeText(ReadingPassageDetailActivity.this,
                            "Không thể tải bài đọc", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ReadingPassage>> call, Throwable t) {
                Toast.makeText(ReadingPassageDetailActivity.this,
                        "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void displayPassage(ReadingPassage passage) {
        tvPassageTitle.setText(passage.getTitle());
        tvPassageLevel.setText(passage.getLevel());
        tvPassageContent.setText(passage.getContent());

        // TODO: Load image using Glide or Picasso if coverImageUrl is available
        if (passage.getCoverImageUrl() != null && !passage.getCoverImageUrl().isEmpty()) {
            ivPassageImage.setVisibility(View.VISIBLE);
            // Glide.with(this).load(passage.getCoverImageUrl()).into(ivPassageImage);
        } else {
            ivPassageImage.setVisibility(View.GONE);
        }
    }
}
