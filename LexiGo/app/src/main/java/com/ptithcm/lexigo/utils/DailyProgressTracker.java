package com.ptithcm.lexigo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class để theo dõi tiến độ học tập hàng ngày
 * Lưu trữ số bài đã học trong ngày vào SharedPreferences
 */
public class DailyProgressTracker {
    private static final String PREF_NAME = "DailyProgressPrefs";
    private static final String KEY_DAILY_COMPLETED = "daily_completed";
    private static final String KEY_LAST_DATE = "last_date";

    private static DailyProgressTracker instance;
    private SharedPreferences sharedPreferences;

    private DailyProgressTracker(Context context) {
        sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized DailyProgressTracker getInstance(Context context) {
        if (instance == null) {
            instance = new DailyProgressTracker(context);
        }
        return instance;
    }

    /**
     * Lấy ngày hiện tại dạng string (yyyy-MM-dd)
     */
    private String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(new Date());
    }

    /**
     * Tăng số bài đã học trong ngày lên 1
     */
    public void incrementDailyProgress() {
        String today = getTodayDate();
        String lastDate = sharedPreferences.getString(KEY_LAST_DATE, "");

        if (today.equals(lastDate)) {
            // Cùng ngày, tăng số bài đã học
            int currentCount = sharedPreferences.getInt(KEY_DAILY_COMPLETED, 0);
            sharedPreferences.edit()
                    .putInt(KEY_DAILY_COMPLETED, currentCount + 1)
                    .apply();
        } else {
            // Ngày mới, reset về 1
            sharedPreferences.edit()
                    .putInt(KEY_DAILY_COMPLETED, 1)
                    .putString(KEY_LAST_DATE, today)
                    .apply();
        }
    }

    /**
     * Lấy số bài đã học trong ngày
     */
    public int getDailyProgress() {
        String today = getTodayDate();
        String lastDate = sharedPreferences.getString(KEY_LAST_DATE, "");

        if (today.equals(lastDate)) {
            return sharedPreferences.getInt(KEY_DAILY_COMPLETED, 0);
        } else {
            // Ngày mới, reset về 0
            sharedPreferences.edit()
                    .putInt(KEY_DAILY_COMPLETED, 0)
                    .putString(KEY_LAST_DATE, today)
                    .apply();
            return 0;
        }
    }

    /**
     * Reset tiến độ trong ngày (dùng cho testing hoặc reset manual)
     */
    public void resetDailyProgress() {
        sharedPreferences.edit()
                .putInt(KEY_DAILY_COMPLETED, 0)
                .putString(KEY_LAST_DATE, getTodayDate())
                .apply();
    }
}

