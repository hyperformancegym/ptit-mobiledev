package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Model cho Listening Exercise từ API
 */
public class ListeningExercise {
    @SerializedName("audio_url")
    private String audioUrl;

    @SerializedName("script_with_blank")
    private String scriptWithBlank;

    @SerializedName("answer")
    private String answer;

    @SerializedName("script_id")
    private String scriptId;

    @SerializedName("expires_in")
    private int expiresIn;

    // Constructor
    public ListeningExercise() {
    }

    public ListeningExercise(String audioUrl, String scriptWithBlank, String answer, String scriptId, int expiresIn) {
        this.audioUrl = audioUrl;
        this.scriptWithBlank = scriptWithBlank;
        this.answer = answer;
        this.scriptId = scriptId;
        this.expiresIn = expiresIn;
    }

    // Getters and Setters
    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getScriptWithBlank() {
        return scriptWithBlank;
    }

    public void setScriptWithBlank(String scriptWithBlank) {
        this.scriptWithBlank = scriptWithBlank;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getScriptId() {
        return scriptId;
    }

    public void setScriptId(String scriptId) {
        this.scriptId = scriptId;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}

