package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;

public class VocabLesson {
    @SerializedName("_id")
    private String id;
    
    private String word;
    private String meaning;
    private String example;
    private String pronunciation;

    @SerializedName("image_url")
    private String imageUrl;
    
    @SerializedName("audio_url")
    private String audioUrl;

    private String level;

    @SerializedName("topic_id")
    private String topicId;

    // Getters
    public String getId() { return id; }
    public String getWord() { return word; }
    public String getMeaning() { return meaning; }
    public String getExample() { return example; }
    public String getPronunciation() { return pronunciation; }
    public String getImageUrl() { return imageUrl; }
    public String getAudioUrl() { return audioUrl; }
    public String getLevel() { return level; }
    public String getTopicId() { return topicId; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setWord(String word) { this.word = word; }
    public void setMeaning(String meaning) { this.meaning = meaning; }
    public void setExample(String example) { this.example = example; }
    public void setPronunciation(String pronunciation) { this.pronunciation = pronunciation; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }
    public void setLevel(String level) { this.level = level; }
    public void setTopicId(String topicId) { this.topicId = topicId; }
}
