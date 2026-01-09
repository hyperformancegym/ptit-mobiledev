package com.ptithcm.lexigo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.adapters.LearningCategoryAdapter;
import com.ptithcm.lexigo.api.TokenManager;
import com.ptithcm.lexigo.api.models.ProgressSummary;
import com.ptithcm.lexigo.api.models.User;
import com.ptithcm.lexigo.api.repositories.LexiGoRepository;
import com.ptithcm.lexigo.models.LearningCategory;
import com.ptithcm.lexigo.utils.DailyProgressTracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Màn hình trang chủ của ứng dụng
 * Hiển thị các mục học tập và tiến độ người dùng
 */
public class HomeActivity extends AppCompatActivity {

    // UI Components
    private MaterialCardView cardUserAvatar;
    private com.google.android.material.floatingactionbutton.FloatingActionButton fabChatAI;
    private TextView tvDailyProgress;
    private TextView tvMotivation;
    private LinearProgressIndicator progressDaily;
    private RecyclerView rvLearningCategories;

    // Adapter
    private LearningCategoryAdapter categoryAdapter;

    // Managers & Repositories
    private TokenManager tokenManager;
    private LexiGoRepository repository;

    private int completedLessons = 0;

    private User currentUser;

    private int dailyGoalTarget = 0;
    private int dailyCompletedToday = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Khởi tạo managers & repositories
        tokenManager = TokenManager.getInstance(this);
        repository = LexiGoRepository.getInstance(this);

        initViews();

        // Load user profile để lấy goals
        loadUserProfile();

        // Thiết lập RecyclerView
        setupRecyclerView();

        // Thiết lập các sự kiện click
        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh user profile và progress data khi quay lại activity
        loadUserProfile();
    }

    /**
     * Khởi tạo các view components
     */
    private void initViews() {
        cardUserAvatar = findViewById(R.id.cardUserAvatar);
        fabChatAI = findViewById(R.id.fabChatAI);
        tvDailyProgress = findViewById(R.id.tvDailyProgress);
        tvMotivation = findViewById(R.id.tvMotivation);
        progressDaily = findViewById(R.id.progressDaily);
        rvLearningCategories = findViewById(R.id.rvLearningCategories);
    }

    /**
     * Thiết lập dữ liệu tiến độ học tập
     */
    private void setupProgressData() {
        // Update daily progress display
        if (dailyGoalTarget <= 0) {
            tvDailyProgress.setText("0/5");
            progressDaily.setProgress(0);
            tvMotivation.setText(getString(R.string.motivation_start));
        } else {
            tvDailyProgress.setText(dailyCompletedToday + "/" + dailyGoalTarget);
            int progressPercent = (int) ((dailyCompletedToday * 100.0) / dailyGoalTarget);
            progressDaily.setProgress(Math.min(progressPercent, 100));
            
            if (dailyCompletedToday >= dailyGoalTarget) {
                tvMotivation.setText(getString(R.string.motivation_done));
            } else if (dailyCompletedToday > 0) {
                tvMotivation.setText(getString(R.string.motivation_progress));
            } else {
                tvMotivation.setText(getString(R.string.motivation_start));
            }
        }
    }

    /**
     * Load thông tin user để lấy goals
     */
    private void loadUserProfile() {
        if (!tokenManager.isLoggedIn()) {
            dailyGoalTarget = 0;
            dailyCompletedToday = DailyProgressTracker.getInstance(this).getDailyProgress();
            setupProgressData();
            return;
        }

        repository.getProfile(new LexiGoRepository.ApiCallback<>() {
            @Override
            public void onSuccess(User user) {
                currentUser = user;

                if (user.getGoals() != null) {
                    dailyGoalTarget = user.getGoals().getDailyLessons();
                }

                dailyCompletedToday = DailyProgressTracker.getInstance(HomeActivity.this).getDailyProgress();

                // Sau khi có goals, load progress data
                loadProgressData();
            }

            @Override
            public void onError(String error) {
                dailyCompletedToday = DailyProgressTracker.getInstance(HomeActivity.this).getDailyProgress();
                // Nếu lỗi, vẫn load progress với goal mặc định
                loadProgressData();
            }
        });
    }

    /**
     * Load dữ liệu tiến độ thực từ API
     */
    private void loadProgressData() {
        String userId = tokenManager.getUserId();
        if (userId == null) {
            // Nếu chưa đăng nhập, hiển thị dữ liệu mặc định
            setupProgressData();
            return;
        }

        repository.getProgressSummary(userId, new LexiGoRepository.ApiCallback<>() {
            @Override
            public void onSuccess(ProgressSummary progressSummary) {
                // Cập nhật tổng số bài đã hoàn thành
                completedLessons = progressSummary.getTotalCompleted();

                setupProgressData();
            }

            @Override
            public void onError(String error) {
                // Nếu lỗi, hiển thị dữ liệu mặc định
                setupProgressData();
            }
        });
    }

    /**
     * Thiết lập RecyclerView với danh sách các mục học tập
     */
    private void setupRecyclerView() {
        // Tạo danh sách các mục học tập
        List<LearningCategory> categories = createLearningCategories();

        // Khởi tạo adapter
        categoryAdapter = new LearningCategoryAdapter(this, categories);

        // Thiết lập adapter và layout manager
        androidx.recyclerview.widget.GridLayoutManager gridLayoutManager = new androidx.recyclerview.widget.GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // First 4 items take 1 span (half width), others take 2 spans (full width)
                return position < 4 ? 1 : 2;
            }
        });
        
        rvLearningCategories.setLayoutManager(gridLayoutManager);
        rvLearningCategories.setAdapter(categoryAdapter);

        // Xử lý sự kiện click item
        categoryAdapter.setOnItemClickListener(category -> {
            Intent intent = null;
            String title = category.getTitle();
            
            // Điều hướng đến các màn hình tương ứng
            if (title.equals("Học từ vựng")) {
                intent = new Intent(this, VocabTopicsActivity.class);
            } else if (title.equals("Ngữ pháp")) {
                intent = new Intent(this, GrammarLessonsActivity.class);
            } else if (title.equals("Luyện nghe")) {
                intent = new Intent(this, LevelSelectionActivity.class);
            } else if (title.equals("Luyện đọc")) {
                intent = new Intent(this, ReadingPassagesActivity.class);
            } else if (title.equals("Tra cứu từ điển")) {
                intent = new Intent(this, DictionaryActivity.class);
            }
            
            if (intent != null) {
                startActivity(intent);
            }
        });
    }

    /**
     * Tạo danh sách các mục học tập
     */
    private List<LearningCategory> createLearningCategories() {
        List<LearningCategory> categories = new ArrayList<>();

        categories.add(new LearningCategory(
            "Học từ vựng",
            "100 từ mới",
            R.drawable.ic_category_vocab
        ));

        categories.add(new LearningCategory(
            "Ngữ pháp",
            "50 bài học",
            R.drawable.ic_category_grammar
        ));

        categories.add(new LearningCategory(
            "Luyện nghe",
            "30 bài nghe",
            R.drawable.ic_category_listening
        ));

        categories.add(new LearningCategory(
            "Luyện đọc",
            "40 bài đọc",
            R.drawable.ic_category_reading
        ));

        categories.add(new LearningCategory(
            "Tra cứu từ điển",
            "Tra từ Anh - Việt",
            R.drawable.ic_category_dictionary
        ));

        return categories;
    }

    /**
     * Thiết lập các sự kiện click
     */
    private void setupClickListeners() {
        // User Avatar - open ProfileActivity
        cardUserAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // FAB AI Chat
        fabChatAI.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
            startActivity(intent);
        });
    }
}
