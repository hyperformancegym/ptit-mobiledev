package com.ptithcm.lexigo.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.activities.AuthActivity;
import com.ptithcm.lexigo.api.models.Goals;
import com.ptithcm.lexigo.api.models.UserRegisterRequest;
import com.ptithcm.lexigo.api.repositories.LexiGoRepository;
import com.ptithcm.lexigo.api.responses.RegisterResponse;

/**
 * Fragment cho tab Đăng ký
 */
public class RegisterFragment extends Fragment {

    private TextInputEditText etFullName;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;
    private MaterialButton btnRegister;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Khởi tạo views
        initViews(view);

        // Thiết lập sự kiện click
        setupClickListeners();

        return view;
    }

    /**
     * Khởi tạo các view components
     */
    private void initViews(View view) {
        etFullName = view.findViewById(R.id.etRegisterFullName);
        etEmail = view.findViewById(R.id.etRegisterEmail);
        etPassword = view.findViewById(R.id.etRegisterPassword);
        etConfirmPassword = view.findViewById(R.id.etRegisterConfirmPassword);
        btnRegister = view.findViewById(R.id.btnRegister);
    }

    /**
     * Thiết lập các sự kiện click
     */
    private void setupClickListeners() {
        // Xử lý sự kiện nút Đăng ký
        btnRegister.setOnClickListener(v -> handleRegister());
    }

    /**
     * Xử lý đăng ký
     */
    private void handleRegister() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validate full name
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Vui lòng nhập họ tên");
            etFullName.requestFocus();
            return;
        }

        if (fullName.length() < 3) {
            etFullName.setError("Họ tên phải có ít nhất 3 ký tự");
            etFullName.requestFocus();
            return;
        }

        // Validate email
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

        // Validate password
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            etPassword.requestFocus();
            return;
        }

        // Validate confirm password
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            etConfirmPassword.requestFocus();
            return;
        }

        // TODO: Implement actual registration logic with backend

// Tạo request với thông tin cơ bản
        UserRegisterRequest request = new UserRegisterRequest(fullName, email, password);

        // Hoặc tạo request với đầy đủ thông tin
        Goals goals = new Goals(20, 2);
        UserRegisterRequest fullRequest = new UserRegisterRequest(fullName, email, password, "Intermediate", goals);

        // Gọi API
        LexiGoRepository repository = LexiGoRepository.getInstance(getContext());
        repository.register(request, new LexiGoRepository.ApiCallback<RegisterResponse>() {
            @Override
            public void onSuccess(RegisterResponse data) {
                String userId = data.getUserId();
                Toast.makeText(getContext(), "Đăng ký thành công! ID: " + userId, Toast.LENGTH_SHORT).show();
                // Chuyển về tab đăng nhập
                if (getActivity() instanceof AuthActivity) {
                    // Có thể chuyển về tab login hoặc tự động đăng nhập
                    Toast.makeText(getContext(), "Chuyển sang tab đăng nhập...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Lỗi: " + error, Toast.LENGTH_SHORT).show();
            }
        });

    }

}

