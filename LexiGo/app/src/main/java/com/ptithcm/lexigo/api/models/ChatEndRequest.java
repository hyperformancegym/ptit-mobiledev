package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;

public class ChatEndRequest {
    @SerializedName("conversation_id")
    private String conversationId;

    public ChatEndRequest(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
