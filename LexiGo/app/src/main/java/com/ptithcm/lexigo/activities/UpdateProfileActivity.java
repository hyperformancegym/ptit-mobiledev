package com.ptithcm.lexigo.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.api.TokenManager;
import com.ptithcm.lexigo.api.models.Goals;
import com.ptithcm.lexigo.api.models.User;
import com.ptithcm.lexigo.api.models.UserUpdateRequest;
import com.ptithcm.lexigo.api.repositories.LexiGoRepository;

/**
 * Activity để cập nhật thông tin người dùng
 */
public class UpdateProfileActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextInputEditText etName;
    private AutoCompleteTextView spinnerLevel;
    private TextInputEditText etDailyWords;
    private TextInputEditText etDailyLessons;
    private MaterialButton btnSave;
    private ProgressBar loadingIndicator;

    private TokenManager tokenManager;
    private LexiGoRepository repository;
    private User currentUser;

    private static final String[] LEVELS = {"Beginner", "Intermediate", "Advanced"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        // Khởi tạo managers & repositories
        tokenManager = TokenManager.getInstance(this);
        repository = LexiGoRepository.getInstance(this);

        // Khởi tạo views
        initViews();

        // Thiết lập toolbar
        setupToolbar();

        // Thiết lập spinner level
        setupLevelSpinner();

        // Load dữ liệu người dùng
        loadUserProfile();

        // Thiết lập sự kiện click
        setupClickListeners();
    }

    /**
     * Khởi tạo các view components
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etName = findViewById(R.id.etName);
        spinnerLevel = findViewById(R.id.spinnerLevel);
        etDailyWords = findViewById(R.id.etDailyWords);
        etDailyLessons = findViewById(R.id.etDailyLessons);
        btnSave = findViewById(R.id.btnSave);
        loadingIndicator = findViewById(R.id.loadingIndicator);
    }

    /**
     * Thiết lập toolbar
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Cập nhật thông tin");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    /**
     * Thiết lập spinner cấp độ
     */
    private void setupLevelSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                LEVELS
        );
        spinnerLevel.setAdapter(adapter);
    }

    /**
     * Load thông tin profile người dùng từ API
     */
    private void loadUserProfile() {
        showLoading(true);

        repository.getProfile(new LexiGoRepository.ApiCallback<User>() {
            @Override
            public void onSuccess(User user) {
                currentUser = user;
                displayUserProfile();
                showLoading(false);
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(UpdateProfileActivity.this,
                        "Lỗi tải thông tin: " + error, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    /**
     * Hiển thị thông tin profile người dùng
     */
    private void displayUserProfile() {
        if (currentUser == null) return;

        etName.setText(currentUser.getName());
        spinnerLevel.setText(currentUser.getLevel() != null ? currentUser.getLevel() : "Beginner", false);

        Goals goals = currentUser.getGoals();
        if (goals != null) {
            etDailyWords.setText(String.valueOf(goals.getDailyWords()));
            etDailyLessons.setText(String.valueOf(goals.getDailyLessons()));
        } else {
            etDailyWords.setText("10");
            etDailyLessons.setText("1");
        }
    }

    /**
     * Thiết lập các sự kiện click
     */
    private void setupClickListeners() {
        btnSave.setOnClickListener(v -> saveProfile());
    }

    /**
     * Lưu thông tin profile
     */
    private void saveProfile() {
        // Validate input
        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String level = spinnerLevel.getText().toString().trim();
        String dailyWordsStr = etDailyWords.getText() != null ? etDailyWords.getText().toString().trim() : "10";
        String dailyLessonsStr = etDailyLessons.getText() != null ? etDailyLessons.getText().toString().trim() : "1";

        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
            etName.requestFocus();
            return;
        }

        if (level.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn cấp độ", Toast.LENGTH_SHORT).show();
            spinnerLevel.requestFocus();
            return;
        }

        int dailyWords = 10;
        int dailyLessons = 1;

        try {
            dailyWords = Integer.parseInt(dailyWordsStr);
            dailyLessons = Integer.parseInt(dailyLessonsStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Mục tiêu phải là số", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dailyWords <= 0 || dailyWords > 100) {
            Toast.makeText(this, "Số từ vựng mỗi ngày từ 1-100", Toast.LENGTH_SHORT).show();
            etDailyWords.requestFocus();
            return;
        }

        if (dailyLessons <= 0 || dailyLessons > 20) {
            Toast.makeText(this, "Số bài học mỗi ngày từ 1-20", Toast.LENGTH_SHORT).show();
            etDailyLessons.requestFocus();
            return;
        }

        // Tạo request
        UserUpdateRequest request = new UserUpdateRequest();
        request.setName(name);
        request.setLevel(level);
        request.setGoals(new Goals(dailyWords, dailyLessons));

        // Gọi API cập nhật
        showLoading(true);

        repository.updateUser(request, new LexiGoRepository.ApiCallback<User>() {
            @Override
            public void onSuccess(User user) {
                showLoading(false);

                // Cập nhật cache
                tokenManager.saveUserInfo(user.getId(), user.getName(), user.getEmail());

                Toast.makeText(UpdateProfileActivity.this,
                        "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(UpdateProfileActivity.this,
                        "Lỗi cập nhật: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Hiển thị/ẩn loading indicator
     */
    private void showLoading(boolean show) {
        if (loadingIndicator != null) {
            loadingIndicator.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        if (btnSave != null) {
            btnSave.setEnabled(!show);
        }
    }
}

