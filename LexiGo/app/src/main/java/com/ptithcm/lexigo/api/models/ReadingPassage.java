package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ReadingPassage {
    @SerializedName("_id")
    private String id;
    
    @SerializedName("title")
    private String title;
    
    @SerializedName("excerpt")
    private String excerpt;
    
    @SerializedName("content_blocks")
    private List<ContentBlock> contentBlocks;
    
    @SerializedName("level")
    private String level;
    
    @SerializedName("category")
    private String category;
    
    @SerializedName("tags")
    private List<String> tags;
    
    @SerializedName("cover_image_url")
    private String coverImageUrl;
    
    @SerializedName("audio_url")
    private String audioUrl;
    
    @SerializedName("published_at")
    private String publishedAt;

    // Helper method to get full content from content_blocks
    public String getContent() {
        if (contentBlocks == null || contentBlocks.isEmpty()) {
            return excerpt != null ? excerpt : "";
        }
        StringBuilder content = new StringBuilder();
        for (ContentBlock block : contentBlocks) {
            if (block.getText() != null) {
                content.append(block.getText()).append("\n\n");
            }
        }
        return content.toString().trim();
    }

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

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public List<ContentBlock> getContentBlocks() {
        return contentBlocks;
    }

    public void setContentBlocks(List<ContentBlock> contentBlocks) {
        this.contentBlocks = contentBlocks;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    // Inner class for content blocks
    public static class ContentBlock {
        @SerializedName("type")
        private String type;
        
        @SerializedName("text")
        private String text;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
