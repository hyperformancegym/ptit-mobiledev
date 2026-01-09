package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;

public class ChatEndResponse {
    @SerializedName("conversation_id")
    private String conversationId;
    
    @SerializedName("total_messages")
    private int totalMessages;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public int getTotalMessages() {
        return totalMessages;
    }

    public void setTotalMessages(int totalMessages) {
        this.totalMessages = totalMessages;
    }
}
