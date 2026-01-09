package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;

public class ProgressSummary {
    @SerializedName("total_vocab")
    private int totalVocab;

    @SerializedName("total_grammar")
    private int totalGrammar;

    @SerializedName("total_listening")
    private int totalListening;

    @SerializedName("total_reading")
    private int totalReading;

    @SerializedName("total_completed")
    private int totalCompleted;

    @SerializedName("last_updated")
    private String lastUpdated;

    @SerializedName("total_lessons")
    private int totalLessons;

    @SerializedName("completed_lessons")
    private int completedLessons;

    @SerializedName("in_progress_lessons")
    private int inProgressLessons;

    @SerializedName("total_quizzes")
    private int totalQuizzes;

    @SerializedName("completed_quizzes")
    private int completedQuizzes;

    @SerializedName("average_score")
    private double averageScore;

    @SerializedName("current_streak")
    private int currentStreak;

    public ProgressSummary() {
    }

    public int getTotalVocab() {
        return totalVocab;
    }

    public void setTotalVocab(int totalVocab) {
        this.totalVocab = totalVocab;
    }

    public int getTotalGrammar() {
        return totalGrammar;
    }

    public void setTotalGrammar(int totalGrammar) {
        this.totalGrammar = totalGrammar;
    }

    public int getTotalListening() {
        return totalListening;
    }

    public void setTotalListening(int totalListening) {
        this.totalListening = totalListening;
    }

    public int getTotalReading() {
        return totalReading;
    }

    public void setTotalReading(int totalReading) {
        this.totalReading = totalReading;
    }

    public int getTotalCompleted() {
        return totalCompleted;
    }

    public void setTotalCompleted(int totalCompleted) {
        this.totalCompleted = totalCompleted;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getTotalLessons() {
        return totalLessons;
    }

    public void setTotalLessons(int totalLessons) {
        this.totalLessons = totalLessons;
    }

    public int getCompletedLessons() {
        return completedLessons;
    }

    public void setCompletedLessons(int completedLessons) {
        this.completedLessons = completedLessons;
    }

    public int getInProgressLessons() {
        return inProgressLessons;
    }

    public void setInProgressLessons(int inProgressLessons) {
        this.inProgressLessons = inProgressLessons;
    }

    public int getTotalQuizzes() {
        return totalQuizzes;
    }

    public void setTotalQuizzes(int totalQuizzes) {
        this.totalQuizzes = totalQuizzes;
    }

    public int getCompletedQuizzes() {
        return completedQuizzes;
    }

    public void setCompletedQuizzes(int completedQuizzes) {
        this.completedQuizzes = completedQuizzes;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }
}

