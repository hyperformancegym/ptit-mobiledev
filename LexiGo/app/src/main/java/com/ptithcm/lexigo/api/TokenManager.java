package com.ptithcm.lexigo.api;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Manager để lưu trữ và quản lý JWT token
 */
public class TokenManager {
    private static final String PREF_NAME = "LexiGoPrefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";

    private static TokenManager instance;
    private SharedPreferences sharedPreferences;

    private TokenManager(Context context) {
        sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized TokenManager getInstance(Context context) {
        if (instance == null) {
            instance = new TokenManager(context);
        }
        return instance;
    }

    /**
     * Lưu token
     */
    public void saveToken(String token) {
        sharedPreferences.edit()
                .putString(KEY_TOKEN, token)
                .apply();
    }

    /**
     * Lấy token
     */
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    /**
     * Lưu thông tin user
     */
    public void saveUserInfo(String userId, String name, String email) {
        sharedPreferences.edit()
                .putString(KEY_USER_ID, userId)
                .putString(KEY_USER_NAME, name)
                .putString(KEY_USER_EMAIL, email)
                .apply();
    }

    /**
     * Lấy User ID
     */
    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    /**
     * Lấy User Name
     */
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }

    /**
     * Lấy User Email
     */
    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    /**
     * Kiểm tra xem user đã đăng nhập chưa
     */
    public boolean isLoggedIn() {
        return getToken() != null && !getToken().isEmpty();
    }

    /**
     * Xóa token và thông tin user (logout)
     */
    public void clearToken() {
        sharedPreferences.edit()
                .remove(KEY_TOKEN)
                .remove(KEY_USER_ID)
                .remove(KEY_USER_NAME)
                .remove(KEY_USER_EMAIL)
                .apply();
    }
}

