package com.ptithcm.lexigo.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.api.models.Goals;
import com.ptithcm.lexigo.api.models.User;
import com.ptithcm.lexigo.api.models.UserUpdateRequest;
import com.ptithcm.lexigo.api.repositories.LexiGoRepository;

/**
 * Dialog để thiết lập mục tiêu học tập hàng ngày
 */
public class SetGoalsDialog extends Dialog {

    private TextInputEditText etDailyWords;
    private TextInputEditText etDailyLessons;
    private MaterialButton btnSave;
    private MaterialButton btnCancel;
    private ProgressBar loadingIndicator;

    private LexiGoRepository repository;
    private GoalsUpdateListener listener;
    private User currentUser;

    public interface GoalsUpdateListener {
        void onGoalsUpdated(Goals goals);
    }

    public SetGoalsDialog(@NonNull Context context, User currentUser, GoalsUpdateListener listener) {
        super(context);
        this.currentUser = currentUser;
        this.listener = listener;
        this.repository = LexiGoRepository.getInstance(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_set_goals);

        initViews();
        displayCurrentGoals();
        setupClickListeners();
    }

    /**
     * Khởi tạo views
     */
    private void initViews() {
        etDailyWords = findViewById(R.id.etDailyWords);
        etDailyLessons = findViewById(R.id.etDailyLessons);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        loadingIndicator = findViewById(R.id.loadingIndicator);
    }

    /**
     * Hiển thị mục tiêu hiện tại
     */
    private void displayCurrentGoals() {
        if (currentUser != null && currentUser.getGoals() != null) {
            Goals goals = currentUser.getGoals();
            etDailyWords.setText(String.valueOf(goals.getDailyWords()));
            etDailyLessons.setText(String.valueOf(goals.getDailyLessons()));
        } else {
            etDailyWords.setText("10");
            etDailyLessons.setText("1");
        }
    }

    /**
     * Thiết lập sự kiện click
     */
    private void setupClickListeners() {
        btnSave.setOnClickListener(v -> saveGoals());
        btnCancel.setOnClickListener(v -> dismiss());
    }

    /**
     * Lưu mục tiêu
     */
    private void saveGoals() {
        String dailyWordsStr = etDailyWords.getText() != null ?
                etDailyWords.getText().toString().trim() : "10";
        String dailyLessonsStr = etDailyLessons.getText() != null ?
                etDailyLessons.getText().toString().trim() : "1";

        int dailyWords;
        int dailyLessons;

        try {
            dailyWords = Integer.parseInt(dailyWordsStr);
            dailyLessons = Integer.parseInt(dailyLessonsStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Mục tiêu phải là số", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dailyWords <= 0 || dailyWords > 100) {
            Toast.makeText(getContext(), "Số từ vựng mỗi ngày từ 1-100", Toast.LENGTH_SHORT).show();
            etDailyWords.requestFocus();
            return;
        }

        if (dailyLessons <= 0 || dailyLessons > 20) {
            Toast.makeText(getContext(), "Số bài học mỗi ngày từ 1-20", Toast.LENGTH_SHORT).show();
            etDailyLessons.requestFocus();
            return;
        }

        // Tạo request với thông tin hiện tại của user
        UserUpdateRequest request = new UserUpdateRequest();
        if (currentUser != null) {
            request.setName(currentUser.getName());
            request.setLevel(currentUser.getLevel());
        }
        Goals newGoals = new Goals(dailyWords, dailyLessons);
        request.setGoals(newGoals);

        // Gọi API cập nhật
        showLoading(true);

        repository.updateUser(request, new LexiGoRepository.ApiCallback<User>() {
            @Override
            public void onSuccess(User user) {
                showLoading(false);
                Toast.makeText(getContext(), "Đã lưu mục tiêu!", Toast.LENGTH_SHORT).show();

                if (listener != null) {
                    listener.onGoalsUpdated(user.getGoals());
                }
                dismiss();
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(getContext(), "Lỗi: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Hiển thị/ẩn loading
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

