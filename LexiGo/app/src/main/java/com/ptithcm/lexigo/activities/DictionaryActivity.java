package com.ptithcm.lexigo.activities;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.api.repositories.LexiGoRepository;
import com.ptithcm.lexigo.models.DictionaryEntry;

import java.io.IOException;
import java.util.List;

/**
 * Activity tra cứu từ điển Anh-Việt
 */
public class DictionaryActivity extends AppCompatActivity {

    // UI Components
    private Toolbar toolbar;
    private TextInputEditText etSearchWord;
    private MaterialButton btnSearch;
    private TextView tvLoading;
    private LinearLayout layoutEmptyState;
    private MaterialCardView layoutErrorState;
    private MaterialCardView cardResult;
    private TextView tvWord;
    private TextView tvPhonetic;
    private TextView tvPartOfSpeech;
    private TextView tvTranslation;
    private TextView tvDefinitionsLabel;
    private TextView tvDefinitions;
    private TextView tvExamplesLabel;
    private TextView tvExamples;
    private TextView tvSynonymsLabel;
    private TextView tvSynonyms;
    private TextView tvErrorMessage;
    private MaterialButton ivSpeaker;

    // Repository
    private LexiGoRepository repository;

    // MediaPlayer for audio playback
    private MediaPlayer mediaPlayer;
    private String currentAudioUrl;
    
    // Thinking animation
    private Handler thinkingHandler;
    private Runnable thinkingRunnable;
    private int dotCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        // Khởi tạo repository
        repository = LexiGoRepository.getInstance(this);

        initViews();
        setupToolbar();
        setupListeners();
    }

    /**
     * Khởi tạo views
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etSearchWord = findViewById(R.id.etSearchWord);
        btnSearch = findViewById(R.id.btnSearch);
        tvLoading = findViewById(R.id.tvLoading);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        layoutErrorState = findViewById(R.id.layoutErrorState);
        cardResult = findViewById(R.id.cardResult);
        tvWord = findViewById(R.id.tvWord);
        tvPhonetic = findViewById(R.id.tvPhonetic);
        tvPartOfSpeech = findViewById(R.id.tvPartOfSpeech);
        tvTranslation = findViewById(R.id.tvTranslation);
        tvDefinitionsLabel = findViewById(R.id.tvDefinitionsLabel);
        tvDefinitions = findViewById(R.id.tvDefinitions);
        tvExamplesLabel = findViewById(R.id.tvExamplesLabel);
        tvExamples = findViewById(R.id.tvExamples);
        tvSynonymsLabel = findViewById(R.id.tvSynonymsLabel);
        tvSynonyms = findViewById(R.id.tvSynonyms);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        ivSpeaker = findViewById(R.id.ivSpeaker);
    }

    /**
     * Thiết lập toolbar
     */
    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    /**
     * Thiết lập listeners
     */
    private void setupListeners() {
        // Search button listener
        btnSearch.setOnClickListener(v -> performSearch());

        // Enter key listener on search input
        etSearchWord.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

        // Speaker icon listener - play pronunciation audio
        ivSpeaker.setOnClickListener(v -> playPronunciation());
    }

    /**
     * Thực hiện tra cứu từ
     */
    private void performSearch() {
        String word = etSearchWord.getText() != null ? etSearchWord.getText().toString().trim() : "";

        if (TextUtils.isEmpty(word)) {
            Toast.makeText(this, "Vui lòng nhập từ cần tra", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hide keyboard
        hideKeyboard();

        // Show loading
        showLoading();

        // Call API - chỉ hỗ trợ Anh-Việt
        repository.lookupWord(word, "en-vi", new LexiGoRepository.ApiCallback<DictionaryEntry>() {
            @Override
            public void onSuccess(DictionaryEntry entry) {
                runOnUiThread(() -> showResult(entry));
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> showError(error));
            }
        });
    }

    /**
     * Hiển thị kết quả tra cứu
     */
    private void showResult(DictionaryEntry entry) {
        hideLoading();
        layoutEmptyState.setVisibility(View.GONE);
        layoutErrorState.setVisibility(View.GONE);
        cardResult.setVisibility(View.VISIBLE);

        // Set word
        tvWord.setText(entry.getWord());

        // Save audio URL for playback
        currentAudioUrl = entry.getAudioUrl();

        // Set phonetic and show speaker icon if audio is available
        if (!TextUtils.isEmpty(entry.getPhonetic())) {
            tvPhonetic.setText(entry.getPhonetic());
            tvPhonetic.setVisibility(View.VISIBLE);

            // Show speaker icon only if audio URL is available
            if (!TextUtils.isEmpty(currentAudioUrl)) {
                ivSpeaker.setVisibility(View.VISIBLE);
            } else {
                ivSpeaker.setVisibility(View.GONE);
            }
        } else {
            tvPhonetic.setVisibility(View.GONE);
            ivSpeaker.setVisibility(View.GONE);
        }

        // Set part of speech
        if (!TextUtils.isEmpty(entry.getPartOfSpeech())) {
            tvPartOfSpeech.setText(entry.getPartOfSpeech());
            tvPartOfSpeech.setVisibility(View.VISIBLE);
        } else {
            tvPartOfSpeech.setVisibility(View.GONE);
        }

        // Set translation
        tvTranslation.setText(entry.getTranslation());

        // Set definitions
        if (entry.getDefinitions() != null && !entry.getDefinitions().isEmpty()) {
            tvDefinitionsLabel.setVisibility(View.VISIBLE);
            tvDefinitions.setVisibility(View.VISIBLE);
            tvDefinitions.setText(formatList(entry.getDefinitions()));
        } else {
            tvDefinitionsLabel.setVisibility(View.GONE);
            tvDefinitions.setVisibility(View.GONE);
        }

        // Set examples
        if (entry.getExamples() != null && !entry.getExamples().isEmpty()) {
            tvExamplesLabel.setVisibility(View.VISIBLE);
            tvExamples.setVisibility(View.VISIBLE);
            tvExamples.setText(formatList(entry.getExamples()));
        } else {
            tvExamplesLabel.setVisibility(View.GONE);
            tvExamples.setVisibility(View.GONE);
        }

        // Set synonyms
        if (entry.getSynonyms() != null && !entry.getSynonyms().isEmpty()) {
            tvSynonymsLabel.setVisibility(View.VISIBLE);
            tvSynonyms.setVisibility(View.VISIBLE);
            tvSynonyms.setText(String.join(", ", entry.getSynonyms()));
        } else {
            tvSynonymsLabel.setVisibility(View.GONE);
            tvSynonyms.setVisibility(View.GONE);
        }
    }

    /**
     * Format list thành numbered list
     */
    private String formatList(List<String> items) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            sb.append((i + 1)).append(". ").append(items.get(i));
            if (i < items.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Hiển thị loading với animation "Thinking..."
     */
    private void showLoading() {
        tvLoading.setVisibility(View.VISIBLE);
        layoutEmptyState.setVisibility(View.GONE);
        layoutErrorState.setVisibility(View.GONE);
        cardResult.setVisibility(View.GONE);
        
        // Start thinking animation
        startThinkingAnimation();
    }
    
    /**
     * Dừng animation và ẩn loading
     */
    private void hideLoading() {
        stopThinkingAnimation();
        tvLoading.setVisibility(View.GONE);
    }
    
    /**
     * Bắt đầu animation "Thinking..."
     */
    private void startThinkingAnimation() {
        dotCount = 0;
        thinkingHandler = new Handler(Looper.getMainLooper());
        thinkingRunnable = new Runnable() {
            @Override
            public void run() {
                dotCount = (dotCount % 3) + 1;
                StringBuilder dots = new StringBuilder("Thinking");
                for (int i = 0; i < dotCount; i++) {
                    dots.append(".");
                }
                tvLoading.setText(dots.toString());
                thinkingHandler.postDelayed(this, 400);
            }
        };
        thinkingHandler.post(thinkingRunnable);
    }
    
    /**
     * Dừng animation
     */
    private void stopThinkingAnimation() {
        if (thinkingHandler != null && thinkingRunnable != null) {
            thinkingHandler.removeCallbacks(thinkingRunnable);
        }
    }

    /**
     * Hiển thị error
     */
    private void showError(String message) {
        hideLoading();
        layoutEmptyState.setVisibility(View.GONE);
        layoutErrorState.setVisibility(View.VISIBLE);
        cardResult.setVisibility(View.GONE);
        tvErrorMessage.setText(message);
    }

    /**
     * Hide keyboard
     */
    private void hideKeyboard() {
        android.view.inputmethod.InputMethodManager imm =
                (android.view.inputmethod.InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Phát âm thanh của từ vựng
     */
    private void playPronunciation() {
        if (TextUtils.isEmpty(currentAudioUrl)) {
            Toast.makeText(this, "Không có file âm thanh", Toast.LENGTH_SHORT).show();
            return;
        }

        // Release previous MediaPlayer if exists
        releaseMediaPlayer();

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            );

            mediaPlayer.setDataSource(currentAudioUrl);

            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                Toast.makeText(DictionaryActivity.this, "Đang phát âm...", Toast.LENGTH_SHORT).show();
            });

            mediaPlayer.setOnCompletionListener(mp -> releaseMediaPlayer());

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Toast.makeText(DictionaryActivity.this,
                    "Lỗi khi phát âm thanh", Toast.LENGTH_SHORT).show();
                releaseMediaPlayer();
                return true;
            });

            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            Toast.makeText(this, "Không thể phát âm thanh: " + e.getMessage(),
                Toast.LENGTH_SHORT).show();
            releaseMediaPlayer();
        }
    }

    /**
     * Release MediaPlayer resources
     */
    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Release MediaPlayer when activity is paused
        releaseMediaPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer when activity is destroyed
        releaseMediaPlayer();
    }
}

