package com.ptithcm.lexigo.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.api.models.ForgotPasswordRequest;
import com.ptithcm.lexigo.api.models.VerifyOtpRequest;
import com.ptithcm.lexigo.api.models.ResetPasswordRequest;
import com.ptithcm.lexigo.api.repositories.LexiGoRepository;

/**
 * Activity để quên mật khẩu và reset password
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    private ImageView btnBack;
    private ProgressBar loadingIndicator;

    // Step 1: Nhập email
    private LinearLayout layoutStepEmail;
    private TextInputEditText etEmail;
    private MaterialButton btnSendOtp;

    // Step 2: Nhập OTP
    private LinearLayout layoutStepOtp;
    private TextView tvEmailSent;
    private TextInputEditText etOtp;
    private MaterialButton btnVerifyOtp;
    private MaterialButton btnResendOtp;
    private TextView tvCountdown;

    // Step 3: Nhập mật khẩu mới
    private LinearLayout layoutStepNewPassword;
    private TextInputEditText etNewPassword;
    private TextInputEditText etConfirmPassword;
    private MaterialButton btnResetPassword;

    private LexiGoRepository repository;
    private String userEmail = "";
    private String verifiedOtp = "";
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Khởi tạo repository
        repository = LexiGoRepository.getInstance(this);

        // Khởi tạo views
        initViews();

        // Thiết lập sự kiện click
        setupClickListeners();

        // Hiển thị bước 1
        showStep(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    /**
     * Khởi tạo các view components
     */
    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        loadingIndicator = findViewById(R.id.loadingIndicator);

        // Step 1
        layoutStepEmail = findViewById(R.id.layoutStepEmail);
        etEmail = findViewById(R.id.etEmail);
        btnSendOtp = findViewById(R.id.btnSendOtp);

        // Step 2
        layoutStepOtp = findViewById(R.id.layoutStepOtp);
        tvEmailSent = findViewById(R.id.tvEmailSent);
        etOtp = findViewById(R.id.etOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        btnResendOtp = findViewById(R.id.btnResendOtp);
        tvCountdown = findViewById(R.id.tvCountdown);

        // Step 3
        layoutStepNewPassword = findViewById(R.id.layoutStepNewPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);
    }

    /**
     * Thiết lập các sự kiện click
     */
    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnSendOtp.setOnClickListener(v -> handleSendOtp());
        btnVerifyOtp.setOnClickListener(v -> handleVerifyOtp());
        btnResendOtp.setOnClickListener(v -> handleSendOtp());
        btnResetPassword.setOnClickListener(v -> handleResetPassword());
    }

    /**
     * Hiển thị bước tương ứng
     */
    private void showStep(int step) {
        layoutStepEmail.setVisibility(step == 1 ? View.VISIBLE : View.GONE);
        layoutStepOtp.setVisibility(step == 2 ? View.VISIBLE : View.GONE);
        layoutStepNewPassword.setVisibility(step == 3 ? View.VISIBLE : View.GONE);
    }

    /**
     * Gửi OTP
     */
    private void handleSendOtp() {
        String email = etEmail.getText().toString().trim();

        // Validate
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Vui lòng nhập email");
            etEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            etEmail.requestFocus();
            return;
        }

        userEmail = email;

        // Gọi API
        showLoading(true);
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);

        repository.forgotPassword(request, new LexiGoRepository.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                showLoading(false);
                Toast.makeText(ForgotPasswordActivity.this,
                        "Mã OTP đã được gửi đến email của bạn", Toast.LENGTH_SHORT).show();
                tvEmailSent.setText("Mã OTP đã được gửi đến: " + userEmail);
                showStep(2);
                startCountdown();
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(ForgotPasswordActivity.this,
                        "Gửi OTP thất bại: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Xác thực OTP
     */
    private void handleVerifyOtp() {
        String otp = etOtp.getText().toString().trim();

        // Validate
        if (TextUtils.isEmpty(otp)) {
            etOtp.setError("Vui lòng nhập mã OTP");
            etOtp.requestFocus();
            return;
        }

        if (otp.length() != 6) {
            etOtp.setError("Mã OTP phải có 6 chữ số");
            etOtp.requestFocus();
            return;
        }

        // Gọi API
        showLoading(true);
        VerifyOtpRequest request = new VerifyOtpRequest(userEmail, otp);

        repository.verifyOtp(request, new LexiGoRepository.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                showLoading(false);
                verifiedOtp = otp;
                Toast.makeText(ForgotPasswordActivity.this,
                        "Xác thực OTP thành công", Toast.LENGTH_SHORT).show();
                showStep(3);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(ForgotPasswordActivity.this,
                        "Xác thực OTP thất bại: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Reset mật khẩu
     */
    private void handleResetPassword() {
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validate
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

        // Gọi API
        showLoading(true);
        ResetPasswordRequest request = new ResetPasswordRequest(userEmail, verifiedOtp, newPassword);

        repository.resetPassword(request, new LexiGoRepository.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                showLoading(false);
                Toast.makeText(ForgotPasswordActivity.this,
                        "Đặt lại mật khẩu thành công! Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(ForgotPasswordActivity.this,
                        "Đặt lại mật khẩu thất bại: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Bắt đầu đếm ngược 60 giây cho nút gửi lại OTP
     */
    private void startCountdown() {
        btnResendOtp.setEnabled(false);
        tvCountdown.setVisibility(View.VISIBLE);

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                tvCountdown.setText("Gửi lại OTP sau " + seconds + "s");
            }

            @Override
            public void onFinish() {
                btnResendOtp.setEnabled(true);
                tvCountdown.setVisibility(View.GONE);
            }
        };
        countDownTimer.start();
    }

    /**
     * Hiển thị/ẩn loading indicator
     */
    private void showLoading(boolean show) {
        if (loadingIndicator != null) {
            loadingIndicator.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        // Disable all buttons during loading
        btnSendOtp.setEnabled(!show);
        btnVerifyOtp.setEnabled(!show);
        btnResetPassword.setEnabled(!show);
        if (!show) {
            // Re-enable resend button only if countdown is finished
            if (countDownTimer == null || tvCountdown.getVisibility() == View.GONE) {
                btnResendOtp.setEnabled(true);
            }
        } else {
            btnResendOtp.setEnabled(false);
        }
    }
}

