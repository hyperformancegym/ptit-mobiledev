package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Request model cho API reset mật khẩu
 */
public class ResetPasswordRequest {
    @SerializedName("email")
    private String email;

    @SerializedName("otp")
    private String otp;

    @SerializedName("new_password")
    private String newPassword;

    public ResetPasswordRequest(String email, String otp, String newPassword) {
        this.email = email;
        this.otp = otp;
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

