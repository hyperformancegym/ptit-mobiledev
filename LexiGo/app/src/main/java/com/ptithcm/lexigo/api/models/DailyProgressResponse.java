package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response wrapper cho daily progress range API
 */
public class DailyProgressResponse {
    @SerializedName("user_id")
    private String userId;

    @SerializedName("start_date")
    private String startDate;

    @SerializedName("end_date")
    private String endDate;

    @SerializedName("total_days")
    private int totalDays;

    @SerializedName("daily_progress")
    private List<DailyProgress> dailyProgress;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public List<DailyProgress> getDailyProgress() {
        return dailyProgress;
    }

    public void setDailyProgress(List<DailyProgress> dailyProgress) {
        this.dailyProgress = dailyProgress;
    }
}

