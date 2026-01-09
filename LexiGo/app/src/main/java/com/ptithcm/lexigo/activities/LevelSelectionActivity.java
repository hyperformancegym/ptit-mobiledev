package com.ptithcm.lexigo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import androidx.appcompat.widget.Toolbar;
import com.ptithcm.lexigo.R;

/**
 * Activity để chọn cấp độ cho bài tập nghe
 */
public class LevelSelectionActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView cardBeginner, cardIntermediate, cardAdvanced;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);

        initViews();
        setupClickListeners();
    }

    /**
     * Khởi tạo các view components
     */
    private void initViews() {
        // Setup toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Chọn cấp độ");
        toolbar.setNavigationOnClickListener(v -> finish());

        cardBeginner = findViewById(R.id.cardBeginner);
        cardIntermediate = findViewById(R.id.cardIntermediate);
        cardAdvanced = findViewById(R.id.cardAdvanced);
    }

    /**
     * Thiết lập các sự kiện click
     */
    private void setupClickListeners() {
        cardBeginner.setOnClickListener(v -> {
            openListeningExercise("Beginner");
        });

        cardIntermediate.setOnClickListener(v -> {
            openListeningExercise("Intermediate");
        });

        cardAdvanced.setOnClickListener(v -> {
            openListeningExercise("Advanced");
        });
    }

    /**
     * Mở màn hình bài tập listening với cấp độ đã chọn
     */
    private void openListeningExercise(String level) {
        Intent intent = new Intent(this, ListeningExerciseActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
    }
}

