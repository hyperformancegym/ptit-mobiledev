package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;

public class ChatMessageRequest {
    @SerializedName("conversation_id")
    private String conversationId;
    @SerializedName("message")
    private String message;

    public ChatMessageRequest(String conversationId, String message) {
        this.conversationId = conversationId;
        this.message = message;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
