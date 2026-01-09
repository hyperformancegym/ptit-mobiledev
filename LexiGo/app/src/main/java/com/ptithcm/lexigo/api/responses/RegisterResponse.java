package com.ptithcm.lexigo.api.responses;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName("user_id")
    private String userId;

    public RegisterResponse() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

