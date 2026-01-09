package com.ptithcm.lexigo.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.adapters.ChatMessageAdapter;
import com.ptithcm.lexigo.api.ApiClient;
import com.ptithcm.lexigo.api.models.ChatEndRequest;
import com.ptithcm.lexigo.api.models.ChatEndResponse;
import com.ptithcm.lexigo.api.models.ChatHistoryResponse;
import com.ptithcm.lexigo.api.models.ChatMessageRequest;
import com.ptithcm.lexigo.api.models.ChatMessageResponse;
import com.ptithcm.lexigo.api.models.ChatStartRequest;
import com.ptithcm.lexigo.api.models.ChatStartResponse;
import com.ptithcm.lexigo.api.responses.ApiResponse;
import com.ptithcm.lexigo.api.services.LexiGoApiService;
import com.ptithcm.lexigo.models.ChatMessage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView rvChat;
    private EditText etMessage;
    private FloatingActionButton btnSend;
    private ProgressBar progressBar;
    private ChatMessageAdapter adapter;
    private List<ChatMessage> messages;
    private LexiGoApiService apiService;
    private String conversationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initViews();
        setupRecyclerView();
        apiService = ApiClient.getInstance(this).getApiService();

        startChat();

        btnSend.setOnClickListener(v -> sendMessage());
        
        // Setup toolbar back button
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }
    }

    private void initViews() {
        rvChat = findViewById(R.id.rvChat);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupRecyclerView() {
        messages = new ArrayList<>();
        adapter = new ChatMessageAdapter(messages);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(adapter);
    }

    private void startChat() {
        progressBar.setVisibility(View.VISIBLE);
        Call<ApiResponse<ChatStartResponse>> call = apiService.startChat(new ChatStartRequest(null));
        call.enqueue(new Callback<ApiResponse<ChatStartResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ChatStartResponse>> call, Response<ApiResponse<ChatStartResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    progressBar.setVisibility(View.GONE);
                    ChatStartResponse data = response.body().getData();
                    conversationId = data.getConversationId();
                    addMessageToChat(data.getInitialMessage(), false);
                } else {
                    // Check if error is due to active conversation
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "";
                        if (response.code() == 400 || response.code() == 409 || errorBody.contains("active conversation")) {
                            // Try to end previous conversation
                            endPreviousAndStartNewChat();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ChatActivity.this, "Failed to start chat: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ChatActivity.this, "Error parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ChatStartResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ChatActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void endPreviousAndStartNewChat() {
        // Get chat history to find active conversation
        apiService.getChatHistory(1, 0).enqueue(new Callback<ApiResponse<ChatHistoryResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ChatHistoryResponse>> call, Response<ApiResponse<ChatHistoryResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<com.ptithcm.lexigo.api.models.Conversation> conversations = response.body().getData().getConversations();
                    if (conversations != null && !conversations.isEmpty()) {
                        // Assuming the first one is the active one or we just end the most recent one
                        String activeConversationId = conversations.get(0).getId();
                        endChat(activeConversationId);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ChatActivity.this, "Could not find active conversation to end", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ChatActivity.this, "Failed to retrieve chat history", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ChatHistoryResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ChatActivity.this, "Error getting history: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void endChat(String chatId) {
        apiService.endChat(new ChatEndRequest(chatId)).enqueue(new Callback<ApiResponse<ChatEndResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ChatEndResponse>> call, Response<ApiResponse<ChatEndResponse>> response) {
                if (response.isSuccessful()) {
                    // Retry starting chat
                    startChat();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ChatActivity.this, "Failed to end previous chat", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ChatEndResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ChatActivity.this, "Error ending chat: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String messageText = etMessage.getText().toString().trim();
        if (messageText.isEmpty()) return;

        if (conversationId == null) {
            Toast.makeText(this, "Chat not started yet", Toast.LENGTH_SHORT).show();
            return;
        }

        addMessageToChat(messageText, true);
        etMessage.setText("");
        rvChat.smoothScrollToPosition(adapter.getItemCount() - 1);

        Call<ApiResponse<ChatMessageResponse>> call = apiService.sendMessage(new ChatMessageRequest(conversationId, messageText));
        call.enqueue(new Callback<ApiResponse<ChatMessageResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ChatMessageResponse>> call, Response<ApiResponse<ChatMessageResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    String botResponse = response.body().getData().getResponse();
                    addMessageToChat(botResponse, false);
                    rvChat.smoothScrollToPosition(adapter.getItemCount() - 1);
                } else {
                    Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ChatMessageResponse>> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMessageToChat(String message, boolean isUser) {
        adapter.addMessage(new ChatMessage(message, isUser));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conversationId != null) {
            // Optional: End chat when destroying activity
            // apiService.endChat(new ChatEndRequest(conversationId)).enqueue(...);
        }
    }
}
