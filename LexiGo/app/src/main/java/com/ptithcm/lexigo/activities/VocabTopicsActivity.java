package com.ptithcm.lexigo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.adapters.VocabTopicAdapter;
import com.ptithcm.lexigo.api.ApiClient;
import com.ptithcm.lexigo.api.models.VocabTopic;
import com.ptithcm.lexigo.api.responses.ApiResponse;
import com.ptithcm.lexigo.api.services.LexiGoApiService;
import com.ptithcm.lexigo.utils.QuizLauncher;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VocabTopicsActivity extends AppCompatActivity {
    private static final String TAG = "VocabTopicsActivity";
    
    private RecyclerView recyclerView;
    private VocabTopicAdapter adapter;
    private ProgressBar progressBar;
    private LexiGoApiService apiService;
    
    private String currentLevel = null; // null = all levels

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_topics);
        
        // Get level from intent if provided
        if (getIntent() != null) {
            currentLevel = getIntent().getStringExtra("level");
        }
        
        initViews();
        setupRecyclerView();
        loadVocabTopics();
    }
    
    private void initViews() {
        recyclerView = findViewById(R.id.recycler_vocab_topics);
        progressBar = findViewById(R.id.progress_bar);
        
        // Setup toolbar back button
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }
        
        apiService = ApiClient.getInstance(this).getApiService();
    }
    
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(this, 2));
        adapter = new VocabTopicAdapter(this, new ArrayList<>(), topic -> {
            // Navigate to vocab lessons for this topic
             Intent intent = new Intent(this, VocabLessonsActivity.class);
             intent.putExtra("topic_id", topic.getId());
             intent.putExtra("topic_name", topic.getName());
             startActivity(intent);
            Toast.makeText(this, "Opening topic: " + topic.getName(), Toast.LENGTH_SHORT).show();
        });

        // Add quiz click listener
        adapter.setOnQuizClickListener(topic -> {
            QuizLauncher.launchVocabQuiz(
                this,
                topic.getId(),
                topic.getName(),
                topic.getLevel()
            );
        });

        recyclerView.setAdapter(adapter);
    }
    
    private void loadVocabTopics() {
        progressBar.setVisibility(View.VISIBLE);
        
        Call<ApiResponse<List<VocabTopic>>> call = apiService.getVocabTopics(currentLevel);
        call.enqueue(new Callback<ApiResponse<List<VocabTopic>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<VocabTopic>>> call, 
                                 Response<ApiResponse<List<VocabTopic>>> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<VocabTopic>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        adapter.updateTopics(apiResponse.getData());
                    } else {
                        showError("Failed to load topics: " + apiResponse.getMessage());
                    }
                } else {
                    showError("Failed to load topics");
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<List<VocabTopic>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Error loading topics", t);
                showError("Network error: " + t.getMessage());
            }
        });
    }
    
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
