package com.ptithcm.lexigo.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.adapters.VocabLessonAdapter;
import com.ptithcm.lexigo.api.ApiClient;
import com.ptithcm.lexigo.api.models.VocabLesson;
import com.ptithcm.lexigo.api.responses.ApiResponse;
import com.ptithcm.lexigo.api.services.LexiGoApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VocabLessonsActivity extends AppCompatActivity {
    private static final String TAG = "VocabLessonsActivity";
    
    private RecyclerView recyclerView;
    private VocabLessonAdapter adapter;
    private ProgressBar progressBar;
    private LexiGoApiService apiService;
    
    private String topicId;
    private String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_lessons);
        
        // Get topic ID from intent
        if (getIntent() != null) {
            topicId = getIntent().getStringExtra("topic_id");
            level = getIntent().getStringExtra("level");
            String topicName = getIntent().getStringExtra("topic_name");
            
            if (getSupportActionBar() != null && topicName != null) {
                getSupportActionBar().setTitle(topicName);
            }
        }
        
        initViews();
        setupRecyclerView();
        loadVocabLessons();
    }
    
    private void initViews() {
        recyclerView = findViewById(R.id.recycler_vocab_lessons);
        progressBar = findViewById(R.id.progress_bar);
        
        // Setup toolbar back button
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }
        
        apiService = ApiClient.getInstance(this).getApiService();
    }
    
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VocabLessonAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }
    
    private void loadVocabLessons() {
        if (topicId == null) {
            showError("Topic ID is required");
            finish();
            return;
        }
        
        progressBar.setVisibility(View.VISIBLE);
        
        Call<ApiResponse<List<VocabLesson>>> call = apiService.getVocabLessons(topicId, level);
        call.enqueue(new Callback<ApiResponse<List<VocabLesson>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<VocabLesson>>> call, 
                                 Response<ApiResponse<List<VocabLesson>>> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<VocabLesson>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        adapter.updateLessons(apiResponse.getData());
                    } else {
                        showError("Failed to load lessons: " + apiResponse.getMessage());
                    }
                } else {
                    showError("Failed to load lessons");
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<List<VocabLesson>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Error loading lessons", t);
                showError("Network error: " + t.getMessage());
            }
        });
    }
    
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer when activity is destroyed
        if (adapter != null) {
            adapter.releaseMediaPlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Release MediaPlayer when activity is paused
        if (adapter != null) {
            adapter.releaseMediaPlayer();
        }
    }
}
