package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Conversation {
    @SerializedName("conversation_id")
    private String id;
    
    @SerializedName("topic")
    private ChatTopic topic;
    
    // Assuming messages might be included in history, but API docs don't specify.
    // If not, we can remove this or keep it null.
    // For now, let's just keep basic info.
    
    @SerializedName("created_at")
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChatTopic getTopic() {
        return topic;
    }

    public void setTopic(ChatTopic topic) {
        this.topic = topic;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
