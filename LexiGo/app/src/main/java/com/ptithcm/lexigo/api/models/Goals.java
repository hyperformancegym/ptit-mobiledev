package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;

public class Goals {
    @SerializedName("daily_words")
    private int dailyWords;

    @SerializedName("daily_lessons")
    private int dailyLessons;

    public Goals() {
        this.dailyWords = 10;
        this.dailyLessons = 1;
    }

    public Goals(int dailyWords, int dailyLessons) {
        this.dailyWords = dailyWords;
        this.dailyLessons = dailyLessons;
    }

    public int getDailyWords() {
        return dailyWords;
    }

    public void setDailyWords(int dailyWords) {
        this.dailyWords = dailyWords;
    }

    public int getDailyLessons() {
        return dailyLessons;
    }

    public void setDailyLessons(int dailyLessons) {
        this.dailyLessons = dailyLessons;
    }
}

