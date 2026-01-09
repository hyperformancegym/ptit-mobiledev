package com.ptithcm.lexigo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.adapters.ReadingPassageAdapter;
import com.ptithcm.lexigo.api.ApiClient;
import com.ptithcm.lexigo.api.models.ReadingPassage;
import com.ptithcm.lexigo.api.models.ReadingPassagesResponse;
import com.ptithcm.lexigo.api.responses.ApiResponse;
import com.ptithcm.lexigo.api.services.LexiGoApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadingPassagesActivity extends AppCompatActivity {

    private RecyclerView rvReadingPassages;
    private ProgressBar progressBar;
    private MaterialButton btnPreviousPage;
    private MaterialButton btnNextPage;
    private TextView tvPageInfo;
    private ReadingPassageAdapter adapter;
    private LexiGoApiService apiService;

    private int currentPage = 1;
    private int totalPages = 1;
    private static final int PAGE_SIZE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_passages);

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupPagination();

        apiService = ApiClient.getInstance(this).getApiService();

        loadReadingPassages();
    }

    private void initViews() {
        rvReadingPassages = findViewById(R.id.rvReadingPassages);
        progressBar = findViewById(R.id.progressBar);
        btnPreviousPage = findViewById(R.id.btnPreviousPage);
        btnNextPage = findViewById(R.id.btnNextPage);
        tvPageInfo = findViewById(R.id.tvPageInfo);
    }

    private void setupToolbar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new ReadingPassageAdapter(this, new ArrayList<>());
        rvReadingPassages.setLayoutManager(new LinearLayoutManager(this));
        rvReadingPassages.setAdapter(adapter);

        adapter.setOnItemClickListener(passage -> {
            Intent intent = new Intent(this, ReadingPassageDetailActivity.class);
            intent.putExtra("passage_id", passage.getId());
            startActivity(intent);
        });
    }

    private void setupPagination() {
        btnPreviousPage.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                loadReadingPassages();
            }
        });

        btnNextPage.setOnClickListener(v -> {
            if (currentPage < totalPages) {
                currentPage++;
                loadReadingPassages();
            }
        });
    }

    private void loadReadingPassages() {
        progressBar.setVisibility(View.VISIBLE);

        Call<ApiResponse<ReadingPassagesResponse>> call = apiService.getReadingPassages(
                null, // no level filter
                currentPage,
                PAGE_SIZE
        );

        call.enqueue(new Callback<ApiResponse<ReadingPassagesResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ReadingPassagesResponse>> call,
                                 Response<ApiResponse<ReadingPassagesResponse>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    ReadingPassagesResponse data = response.body().getData();
                    List<ReadingPassage> passages = data != null ? data.getItems() : new ArrayList<>();
                    
                    // Update pagination info
                    if (data != null) {
                        int total = data.getTotal();
                        totalPages = (int) Math.ceil((double) total / PAGE_SIZE);
                        updatePaginationUI();
                    }
                    
                    adapter = new ReadingPassageAdapter(ReadingPassagesActivity.this, passages);
                    rvReadingPassages.setAdapter(adapter);

                    adapter.setOnItemClickListener(passage -> {
                        Intent intent = new Intent(ReadingPassagesActivity.this, ReadingPassageDetailActivity.class);
                        intent.putExtra("passage_id", passage.getId());
                        startActivity(intent);
                    });
                    
                    // Scroll to top when page changes
                    rvReadingPassages.scrollToPosition(0);
                } else {
                    Toast.makeText(ReadingPassagesActivity.this,
                            "Không thể tải danh sách bài đọc", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ReadingPassagesResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ReadingPassagesActivity.this,
                        "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePaginationUI() {
        // Update page info text
        tvPageInfo.setText("Trang " + currentPage + "/" + totalPages);
        
        // Enable/disable buttons based on current page
        btnPreviousPage.setEnabled(currentPage > 1);
        btnNextPage.setEnabled(currentPage < totalPages);
    }
}
