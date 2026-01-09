package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Model cho Script từ API
 */
public class Script {
    @SerializedName("script_id")
    private String scriptId;

    @SerializedName("text")
    private String text;

    @SerializedName("level")
    private String level;

    @SerializedName("source")
    private String source;

    // Constructor
    public Script() {
    }

    public Script(String scriptId, String text, String level, String source) {
        this.scriptId = scriptId;
        this.text = text;
        this.level = level;
        this.source = source;
    }

    // Getters and Setters
    public String getScriptId() {
        return scriptId;
    }

    public void setScriptId(String scriptId) {
        this.scriptId = scriptId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}

