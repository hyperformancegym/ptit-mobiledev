package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;

public class Statistics {
    @SerializedName("user_id")
    private String userId;

    @SerializedName("total_completed")
    private int totalCompleted;

    @SerializedName("vocab_completed")
    private int vocabCompleted;

    @SerializedName("grammar_completed")
    private int grammarCompleted;

    @SerializedName("listening_completed")
    private int listeningCompleted;

    @SerializedName("reading_completed")
    private int readingCompleted;

    @SerializedName("last_updated")
    private String lastUpdated;

    public Statistics() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTotalCompleted() {
        return totalCompleted;
    }

    public void setTotalCompleted(int totalCompleted) {
        this.totalCompleted = totalCompleted;
    }

    public int getVocabCompleted() {
        return vocabCompleted;
    }

    public void setVocabCompleted(int vocabCompleted) {
        this.vocabCompleted = vocabCompleted;
    }

    public int getGrammarCompleted() {
        return grammarCompleted;
    }

    public void setGrammarCompleted(int grammarCompleted) {
        this.grammarCompleted = grammarCompleted;
    }

    public int getListeningCompleted() {
        return listeningCompleted;
    }

    public void setListeningCompleted(int listeningCompleted) {
        this.listeningCompleted = listeningCompleted;
    }

    public int getReadingCompleted() {
        return readingCompleted;
    }

    public void setReadingCompleted(int readingCompleted) {
        this.readingCompleted = readingCompleted;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}

