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
import com.ptithcm.lexigo.adapters.GrammarLessonAdapter;
import com.ptithcm.lexigo.api.ApiClient;
import com.ptithcm.lexigo.api.models.GrammarLesson;
import com.ptithcm.lexigo.api.responses.ApiResponse;
import com.ptithcm.lexigo.api.services.LexiGoApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GrammarLessonsActivity extends AppCompatActivity {
    private static final String TAG = "GrammarLessonsActivity";
    
    private RecyclerView recyclerView;
    private GrammarLessonAdapter adapter;
    private ProgressBar progressBar;
    private LexiGoApiService apiService;
    
    private String currentLevel = null; // null = all levels

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_lessons);
        
        // Get level from intent if provided
        if (getIntent() != null) {
            currentLevel = getIntent().getStringExtra("level");
        }
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Grammar Lessons");
        }
        
        initViews();
        setupRecyclerView();
        loadGrammarLessons();
    }
    
    private void initViews() {
        recyclerView = findViewById(R.id.recycler_grammar_lessons);
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
        adapter = new GrammarLessonAdapter(this, new ArrayList<>(), lesson -> {
            // Navigate to grammar lesson detail
            Intent intent = new Intent(this, GrammarLessonDetailActivity.class);
            intent.putExtra("lesson_id", lesson.getId());
            intent.putExtra("lesson_title", lesson.getTitle());
            intent.putExtra("lesson_level", lesson.getLevel());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
    
    private void loadGrammarLessons() {
        progressBar.setVisibility(View.VISIBLE);
        
        Call<ApiResponse<List<GrammarLesson>>> call = apiService.getGrammarLessons(currentLevel);
        call.enqueue(new Callback<ApiResponse<List<GrammarLesson>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<GrammarLesson>>> call, 
                                 Response<ApiResponse<List<GrammarLesson>>> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<GrammarLesson>> apiResponse = response.body();
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
            public void onFailure(Call<ApiResponse<List<GrammarLesson>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Error loading lessons", t);
                showError("Network error: " + t.getMessage());
            }
        });
    }
    
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
