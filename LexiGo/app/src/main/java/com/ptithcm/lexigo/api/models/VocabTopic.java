package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;

public class VocabTopic {
    @SerializedName("_id")
    private String id;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("level")
    private String level; // Beginner, Intermediate, Advanced
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("image_url")
    private String imageUrl;
    
    @SerializedName("word_count")
    private int wordCount;

    // Constructors
    public VocabTopic() {}

    public VocabTopic(String id, String name, String level, String description, String imageUrl, int wordCount) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.description = description;
        this.imageUrl = imageUrl;
        this.wordCount = wordCount;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }
}
