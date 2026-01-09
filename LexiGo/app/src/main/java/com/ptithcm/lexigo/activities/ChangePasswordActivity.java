package com.ptithcm.lexigo.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.api.models.ChangePasswordRequest;
import com.ptithcm.lexigo.api.repositories.LexiGoRepository;

/**
 * Activity để đổi mật khẩu
 */
public class ChangePasswordActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextInputEditText etOldPassword;
    private TextInputEditText etNewPassword;
    private TextInputEditText etConfirmPassword;
    private MaterialButton btnChangePassword;
    private ProgressBar loadingIndicator;

    private LexiGoRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Khởi tạo repository
        repository = LexiGoRepository.getInstance(this);

        // Khởi tạo views
        initViews();

        // Thiết lập sự kiện click
        setupClickListeners();
    }

    /**
     * Khởi tạo các view components
     */
    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        loadingIndicator = findViewById(R.id.loadingIndicator);
    }

    /**
     * Thiết lập các sự kiện click
     */
    private void setupClickListeners() {
        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Nút đổi mật khẩu
        btnChangePassword.setOnClickListener(v -> handleChangePassword());
    }

    /**
     * Xử lý đổi mật khẩu
     */
    private void handleChangePassword() {
        String oldPassword = etOldPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(oldPassword)) {
            etOldPassword.setError("Vui lòng nhập mật khẩu cũ");
            etOldPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(newPassword)) {
            etNewPassword.setError("Vui lòng nhập mật khẩu mới");
            etNewPassword.requestFocus();
            return;
        }

        if (newPassword.length() < 6) {
            etNewPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            etNewPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            etConfirmPassword.requestFocus();
            return;
        }

        if (oldPassword.equals(newPassword)) {
            etNewPassword.setError("Mật khẩu mới phải khác mật khẩu cũ");
            etNewPassword.requestFocus();
            return;
        }

        // Gọi API đổi mật khẩu
        showLoading(true);
        ChangePasswordRequest request = new ChangePasswordRequest(oldPassword, newPassword);

        repository.changePassword(request, new LexiGoRepository.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                showLoading(false);
                Toast.makeText(ChangePasswordActivity.this,
                        "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(ChangePasswordActivity.this,
                        "Đổi mật khẩu thất bại: " + error, Toast.LENGTH_LONG).show();
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
        if (btnChangePassword != null) {
            btnChangePassword.setEnabled(!show);
        }
    }
}

