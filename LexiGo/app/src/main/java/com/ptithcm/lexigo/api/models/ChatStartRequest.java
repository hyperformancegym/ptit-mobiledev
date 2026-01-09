package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;

public class ChatStartRequest {
    @SerializedName("topic_id")
    private String topicId;

    public ChatStartRequest(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }
}
