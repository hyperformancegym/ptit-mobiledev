package com.ptithcm.lexigo.api;

import android.content.Context;

import com.ptithcm.lexigo.api.services.LexiGoApiService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * API Client để khởi tạo Retrofit và cung cấp API service
 */
public class ApiClient {
     private static final String BASE_URL = "https://lexigo-api.fly.dev/";
    // Sử dụng BASE_URL_DEV cho development
//    private static final String BASE_URL = "http://10.0.2.2:8000/";

    private static ApiClient instance;
    private Retrofit retrofit;

    private LexiGoApiService apiService;

    private ApiClient(Context context) {
        // Logging interceptor để debug
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Auth interceptor để tự động thêm token
        AuthInterceptor authInterceptor = new AuthInterceptor(context);

        // OkHttp client
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        // Retrofit instance
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Tạo API service
        apiService = retrofit.create(LexiGoApiService.class);
    }

    /**
     * Singleton pattern
     */
    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Lấy API service
     */
    public LexiGoApiService getApiService() {
        return apiService;
    }

    /**
     * Reset instance (dùng khi cần khởi tạo lại với context mới)
     */
    public static void resetInstance() {
        instance = null;
    }
}

