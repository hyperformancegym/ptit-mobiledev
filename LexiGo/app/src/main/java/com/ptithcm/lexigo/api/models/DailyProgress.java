package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Model cho tiến độ học hàng ngày
 */
public class DailyProgress {
    @SerializedName("user_id")
    private String userId;

    @SerializedName("date")
    private String date; // Format: YYYY-MM-DD

    @SerializedName("vocab")
    private SkillProgress vocab;

    @SerializedName("grammar")
    private SkillProgress grammar;

    @SerializedName("listening")
    private SkillProgress listening;

    @SerializedName("reading")
    private SkillProgress reading;

    @SerializedName("total_study_time_minutes")
    private int totalStudyTimeMinutes;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    /**
     * Inner class cho progress của từng skill
     */
    public static class SkillProgress {
        @SerializedName("total_lessons")
        private int totalLessons;

        @SerializedName("average_score")
        private double averageScore;

        @SerializedName("study_time_minutes")
        private int studyTimeMinutes;

        public int getTotalLessons() {
            return totalLessons;
        }

        public void setTotalLessons(int totalLessons) {
            this.totalLessons = totalLessons;
        }

        public double getAverageScore() {
            return averageScore;
        }

        public void setAverageScore(double averageScore) {
            this.averageScore = averageScore;
        }

        public int getStudyTimeMinutes() {
            return studyTimeMinutes;
        }

        public void setStudyTimeMinutes(int studyTimeMinutes) {
            this.studyTimeMinutes = studyTimeMinutes;
        }
    }

    public DailyProgress() {
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public SkillProgress getVocab() {
        return vocab;
    }

    public void setVocab(SkillProgress vocab) {
        this.vocab = vocab;
    }

    public SkillProgress getGrammar() {
        return grammar;
    }

    public void setGrammar(SkillProgress grammar) {
        this.grammar = grammar;
    }

    public SkillProgress getListening() {
        return listening;
    }

    public void setListening(SkillProgress listening) {
        this.listening = listening;
    }

    public SkillProgress getReading() {
        return reading;
    }

    public void setReading(SkillProgress reading) {
        this.reading = reading;
    }

    public int getTotalStudyTimeMinutes() {
        return totalStudyTimeMinutes;
    }

    public void setTotalStudyTimeMinutes(int totalStudyTimeMinutes) {
        this.totalStudyTimeMinutes = totalStudyTimeMinutes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Tính tổng số bài học hoàn thành trong ngày
     */
    public int getTotalCount() {
        int total = 0;
        if (vocab != null) total += vocab.getTotalLessons();
        if (grammar != null) total += grammar.getTotalLessons();
        if (listening != null) total += listening.getTotalLessons();
        if (reading != null) total += reading.getTotalLessons();
        return total;
    }
}

