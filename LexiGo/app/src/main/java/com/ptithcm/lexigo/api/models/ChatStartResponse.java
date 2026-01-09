package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;

public class ChatStartResponse {
    @SerializedName("conversation_id")
    private String conversationId;
    
    @SerializedName("topic")
    private ChatTopic topic;
    
    @SerializedName("initial_message")
    private String initialMessage;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public ChatTopic getTopic() {
        return topic;
    }

    public void setTopic(ChatTopic topic) {
        this.topic = topic;
    }

    public String getInitialMessage() {
        return initialMessage;
    }

    public void setInitialMessage(String initialMessage) {
        this.initialMessage = initialMessage;
    }
}
