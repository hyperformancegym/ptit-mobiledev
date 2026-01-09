package com.ptithcm.lexigo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.api.TokenManager;
import com.ptithcm.lexigo.api.models.ProgressSummary;
import com.ptithcm.lexigo.api.models.User;
import com.ptithcm.lexigo.api.repositories.LexiGoRepository;
import com.ptithcm.lexigo.utils.DailyProgressTracker;

/**
 * Màn hình thông tin tài khoản người dùng
 * Hiển thị thông tin cá nhân, thống kê và các chức năng quản lý
 */
public class ProfileActivity extends AppCompatActivity {

    // UI Components
    private ImageView imgAvatar;
    private TextView tvUserName;
    private TextView tvUserEmail;
    private Chip chipLevel;
    private TextView tvLessonsStatistics;
    private MaterialButton btnUpdateProfile;
    private MaterialButton btnChangePassword;
    private MaterialButton btnDetailedStats;
    private MaterialButton btnLogout;
    private ProgressBar loadingIndicator;
    private TextView tvDailyGoalStatus;

    // Managers & Repositories
    private TokenManager tokenManager;
    private LexiGoRepository repository;

    // Dữ liệu người dùng
    private User currentUser;
    private ProgressSummary userProgress;

    // Daily goal tracking
    private int dailyGoalTarget = 0;
    private int dailyCompletedToday = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Khởi tạo managers & repositories
        tokenManager = TokenManager.getInstance(this);
        repository = LexiGoRepository.getInstance(this);

        // Khởi tạo views
        initViews();

        // Thiết lập sự kiện click
        setupClickListeners();

        // Kiểm tra đăng nhập và load dữ liệu
        checkLoginAndLoadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh progress data khi quay lại activity
        if (tokenManager.isLoggedIn()) {
            loadUserStatistics();
        }
    }

    /**
     * Kiểm tra trạng thái đăng nhập và load dữ liệu
     */
    private void checkLoginAndLoadData() {
        if (!tokenManager.isLoggedIn()) {
            // Chưa đăng nhập, chuyển về màn hình đăng nhập
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        // Đã đăng nhập, load dữ liệu
        loadUserProfile();
        loadUserStatistics();
    }

    /**
     * Khởi tạo các view components
     */
    private void initViews() {
        imgAvatar = findViewById(R.id.imgAvatar);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        chipLevel = findViewById(R.id.chipLevel);
        tvLessonsStatistics = findViewById(R.id.tvLessonsStatistics);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnDetailedStats = findViewById(R.id.btnDetailedStats);
        btnLogout = findViewById(R.id.btnLogout);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        tvDailyGoalStatus = findViewById(R.id.tvDailyGoalStatus);
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
                if (user.getGoals() != null) {
                    dailyGoalTarget = user.getGoals().getDailyLessons();
                }
                dailyCompletedToday = DailyProgressTracker.getInstance(ProfileActivity.this).getDailyProgress();
                displayUserProfile();
                showLoading(false);
            }

            @Override
            public void onError(String error) {
                dailyCompletedToday = DailyProgressTracker.getInstance(ProfileActivity.this).getDailyProgress();
                showLoading(false);
                Toast.makeText(ProfileActivity.this,
                    "Lỗi tải thông tin: " + error, Toast.LENGTH_SHORT).show();

                // Hiển thị thông tin từ cache nếu có
                displayCachedUserInfo();
            }
        });
    }

    /**
     * Load thống kê người dùng từ API
     */
    private void loadUserStatistics() {
        String userId = tokenManager.getUserId();
        if (userId == null) return;

        repository.getProgressSummary(userId, new LexiGoRepository.ApiCallback<ProgressSummary>() {
            @Override
            public void onSuccess(ProgressSummary progressSummary) {
                userProgress = progressSummary;
                displayUserStatistics();
            }

            @Override
            public void onError(String error) {
                // Handle 404 gracefully - user doesn't have progress yet
                if (error.contains("404")) {
                    // Create default empty progress
                    userProgress = new ProgressSummary();
                    displayUserStatistics();
                } else {
                    Toast.makeText(ProfileActivity.this,
                        "Lỗi tải thống kê: " + error, Toast.LENGTH_SHORT).show();
                    // Still display default progress
                    userProgress = new ProgressSummary();
                    displayUserStatistics();
                }
            }
        });
    }

    /**
     * Hiển thị thông tin profile người dùng
     */
    private void displayUserProfile() {
        if (currentUser == null) return;

        // Hiển thị tên và email
        tvUserName.setText(currentUser.getName() != null ? currentUser.getName() : "Người dùng");
        tvUserEmail.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "");

        // Thiết lập cấp độ
        String level = currentUser.getLevel() != null ? currentUser.getLevel() : "Beginner";
        chipLevel.setText(level);
        setLevelColor(level);
    }

    /**
     * Hiển thị thống kê người dùng
     */
    private void displayUserStatistics() {
        if (userProgress == null) return;

        int completedLessons = userProgress.getTotalCompleted();

        // Hiển thị số bài đã hoàn thành
        tvLessonsStatistics.setText(getString(R.string.lessons_statistics, completedLessons));

        // Daily goal status
        if (dailyGoalTarget <= 0) {
            tvDailyGoalStatus.setText(getString(R.string.no_daily_goal_set));
        } else {
            if (dailyCompletedToday >= dailyGoalTarget) {
                tvDailyGoalStatus.setText(getString(R.string.daily_goal_reached, dailyCompletedToday, dailyGoalTarget));
            } else {
                tvDailyGoalStatus.setText(getString(R.string.daily_progress, dailyCompletedToday, dailyGoalTarget));
            }
        }
    }

    /**
     * Hiển thị thông tin từ cache (SharedPreferences)
     */
    private void displayCachedUserInfo() {
        String cachedName = tokenManager.getUserName();
        String cachedEmail = tokenManager.getUserEmail();

        if (cachedName != null) {
            tvUserName.setText(cachedName);
        }
        if (cachedEmail != null) {
            tvUserEmail.setText(cachedEmail);
        }
    }

    /**
     * Hiển thị/ẩn loading indicator
     */
    private void showLoading(boolean show) {
        if (loadingIndicator != null) {
            loadingIndicator.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Thiết lập màu cho chip cấp độ
     */
    private void setLevelColor(String level) {
        int colorResId;
        switch (level) {
            case "Beginner":
                colorResId = R.color.beginner_color;
                break;
            case "Advanced":
                colorResId = R.color.advanced_color;
                break;
            case "Intermediate":
            default:
                colorResId = R.color.intermediate_color;
                break;
        }
        chipLevel.setChipBackgroundColorResource(colorResId);
    }

    /**
     * Thiết lập các sự kiện click
     */
    private void setupClickListeners() {
        // Nút Cập nhật thông tin
        btnUpdateProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
        });

        // Nút Đổi mật khẩu
        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });

        // Nút Thống kê chi tiết
        btnDetailedStats.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, DetailedStatisticsActivity.class);
            startActivity(intent);
        });

        // Nút Đăng xuất
        btnLogout.setOnClickListener(v -> handleLogout());
    }

    /**
     * Xử lý đăng xuất
     */
    private void handleLogout() {
        // Xóa token và thông tin user
        tokenManager.clearToken();

        Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();

        // Chuyển về màn hình đăng nhập
        Intent intent = new Intent(ProfileActivity.this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
