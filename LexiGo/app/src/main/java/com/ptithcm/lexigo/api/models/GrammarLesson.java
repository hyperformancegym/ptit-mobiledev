package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GrammarLesson {
    @SerializedName("_id")
    private String id;
    
    @SerializedName("title")
    private String title;
    
    @SerializedName("level")
    private String level; // Beginner, Intermediate, Advanced
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("content")
    private String content;
    
    @SerializedName("rules")
    private List<String> rules;
    
    @SerializedName("examples")
    private List<String> examples;

    @SerializedName("explanation")
    private String explanation;

    @SerializedName("tips")
    private String tips;
    
    @SerializedName("image_url")
    private String imageUrl;

    // Constructors
    public GrammarLesson() {}

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getRules() {
        return rules;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }

    public List<String> getExamples() {
        return examples;
    }

    public void setExamples(List<String> examples) {
        this.examples = examples;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
