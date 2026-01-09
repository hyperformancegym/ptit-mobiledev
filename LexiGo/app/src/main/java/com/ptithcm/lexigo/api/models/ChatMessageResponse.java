package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;

public class ChatMessageResponse {
    @SerializedName("response")
    private String response;
    
    @SerializedName("conversation_id")
    private String conversationId;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
