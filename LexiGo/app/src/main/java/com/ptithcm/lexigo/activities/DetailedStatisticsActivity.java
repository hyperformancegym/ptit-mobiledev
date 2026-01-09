package com.ptithcm.lexigo.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.api.TokenManager;
import com.ptithcm.lexigo.api.models.DailyProgress;
import com.ptithcm.lexigo.api.models.ProgressSummary;
import com.ptithcm.lexigo.api.models.Statistics;
import com.ptithcm.lexigo.api.repositories.LexiGoRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Activity hiển thị thống kê chi tiết của người dùng
 */
public class DetailedStatisticsActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;

    // Statistics Views
    private TextView tvTotalCompleted;
    private TextView tvVocabCompleted;
    private TextView tvGrammarCompleted;
    private TextView tvListeningCompleted;
    private TextView tvReadingCompleted;
    private TextView tvLastUpdated;

    private ProgressBar progressTotal;
    private ProgressBar progressVocab;
    private ProgressBar progressGrammar;
    private ProgressBar progressListening;
    private ProgressBar progressReading;

    private ProgressBar loadingIndicator;
    private MaterialCardView cardStatistics;
    private MaterialCardView cardProgressChart;
    private LineChart lineChart;
    private TextView tvChartDescription;

    private TokenManager tokenManager;
    private LexiGoRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_statistics);

        // Khởi tạo managers & repositories
        tokenManager = TokenManager.getInstance(this);
        repository = LexiGoRepository.getInstance(this);

        // Khởi tạo views
        initViews();

        // Thiết lập toolbar
        setupToolbar();

        // Load dữ liệu
        loadStatistics();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh progress data khi quay lại activity
        loadStatistics();
        loadProgressSummary();
        loadDailyProgressChart();
    }

    /**
     * Khởi tạo các view components
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);

        // Statistics
        tvTotalCompleted = findViewById(R.id.tvTotalCompleted);
        tvVocabCompleted = findViewById(R.id.tvVocabCompleted);
        tvGrammarCompleted = findViewById(R.id.tvGrammarCompleted);
        tvListeningCompleted = findViewById(R.id.tvListeningCompleted);
        tvReadingCompleted = findViewById(R.id.tvReadingCompleted);
        tvLastUpdated = findViewById(R.id.tvLastUpdated);


        // Progress bars
        progressTotal = findViewById(R.id.progressTotal);
        progressVocab = findViewById(R.id.progressVocab);
        progressGrammar = findViewById(R.id.progressGrammar);
        progressListening = findViewById(R.id.progressListening);
        progressReading = findViewById(R.id.progressReading);

        loadingIndicator = findViewById(R.id.loadingIndicator);
        cardStatistics = findViewById(R.id.cardStatistics);
        cardProgressChart = findViewById(R.id.cardProgressChart);
        lineChart = findViewById(R.id.lineChart);
        tvChartDescription = findViewById(R.id.tvChartDescription);
    }

    /**
     * Thiết lập toolbar
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Thống kê chi tiết");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    /**
     * Load thống kê người dùng từ API
     */
    private void loadStatistics() {
        String userId = tokenManager.getUserId();
        if (userId == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        showLoading(true);

        repository.getUserStatistics(userId, new LexiGoRepository.ApiCallback<Statistics>() {
            @Override
            public void onSuccess(Statistics statistics) {
                displayStatistics(statistics);
                showLoading(false);
                cardStatistics.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                // Handle 404 gracefully - user doesn't have statistics yet
                if (error.contains("404")) {
                    // Create and display default empty statistics
                    Statistics defaultStats = new Statistics();
                    defaultStats.setTotalCompleted(0);
                    defaultStats.setVocabCompleted(0);
                    defaultStats.setGrammarCompleted(0);
                    defaultStats.setListeningCompleted(0);
                    defaultStats.setReadingCompleted(0);
                    displayStatistics(defaultStats);
                    cardStatistics.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(DetailedStatisticsActivity.this,
                            "Lỗi tải thống kê: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Load tóm tắt tiến độ từ API
     */
    private void loadProgressSummary() {
        String userId = tokenManager.getUserId();
        if (userId == null) return;

        repository.getProgressSummary(userId, new LexiGoRepository.ApiCallback<ProgressSummary>() {
            @Override
            public void onSuccess(ProgressSummary summary) {
                // Hiển thị cả statistics từ progress summary
                displayStatisticsFromProgress(summary);
                cardStatistics.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String error) {
                // Handle 404 gracefully - user doesn't have progress data yet
                if (error.contains("404")) {
                    // Create and display default empty progress summary
                    ProgressSummary defaultSummary = new ProgressSummary();
                    displayStatisticsFromProgress(defaultSummary);
                    cardStatistics.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(DetailedStatisticsActivity.this,
                            "Lỗi tải tiến độ: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Hiển thị thống kê từ ProgressSummary
     */
    private void displayStatisticsFromProgress(ProgressSummary summary) {
        if (summary == null) return;

        int total = summary.getTotalCompleted();
        int vocab = summary.getTotalVocab();
        int grammar = summary.getTotalGrammar();
        int listening = summary.getTotalListening();
        int reading = summary.getTotalReading();

        tvTotalCompleted.setText(String.valueOf(total));
        tvVocabCompleted.setText(String.format("Từ vựng: %d bài", vocab));
        tvGrammarCompleted.setText(String.format("Ngữ pháp: %d bài", grammar));
        tvListeningCompleted.setText(String.format("Nghe: %d bài", listening));
        tvReadingCompleted.setText(String.format("Đọc: %d bài", reading));

        if (summary.getLastUpdated() != null) {
            tvLastUpdated.setText(String.format("Cập nhật: %s", summary.getLastUpdated()));
        } else {
            tvLastUpdated.setText("Cập nhật: Chưa có");
        }

        // Cập nhật progress bars
        int maxValue = Math.max(total, 100); // Giả sử tối đa 100 bài
        progressTotal.setMax(maxValue);
        progressTotal.setProgress(total);

        if (total > 0) {
            progressVocab.setMax(total);
            progressVocab.setProgress(vocab);

            progressGrammar.setMax(total);
            progressGrammar.setProgress(grammar);

            progressListening.setMax(total);
            progressListening.setProgress(listening);

            progressReading.setMax(total);
            progressReading.setProgress(reading);
        } else {
            // Reset về 0 nếu chưa có dữ liệu
            progressVocab.setMax(100);
            progressVocab.setProgress(0);

            progressGrammar.setMax(100);
            progressGrammar.setProgress(0);

            progressListening.setMax(100);
            progressListening.setProgress(0);

            progressReading.setMax(100);
            progressReading.setProgress(0);
        }
    }

    /**
     * Hiển thị thống kê (deprecated - use displayStatisticsFromProgress instead)
     */
    private void displayStatistics(Statistics statistics) {
        if (statistics == null) return;

        int total = statistics.getTotalCompleted();
        int vocab = statistics.getVocabCompleted();
        int grammar = statistics.getGrammarCompleted();
        int listening = statistics.getListeningCompleted();
        int reading = statistics.getReadingCompleted();

        tvTotalCompleted.setText(String.valueOf(total));
        tvVocabCompleted.setText(String.format("Từ vựng: %d bài", vocab));
        tvGrammarCompleted.setText(String.format("Ngữ pháp: %d bài", grammar));
        tvListeningCompleted.setText(String.format("Nghe: %d bài", listening));
        tvReadingCompleted.setText(String.format("Đọc: %d bài", reading));

        if (statistics.getLastUpdated() != null) {
            tvLastUpdated.setText(String.format("Cập nhật: %s", statistics.getLastUpdated()));
        }

        // Cập nhật progress bars
        int maxValue = Math.max(total, 100); // Giả sử tối đa 100 bài
        progressTotal.setMax(maxValue);
        progressTotal.setProgress(total);

        if (total > 0) {
            progressVocab.setMax(total);
            progressVocab.setProgress(vocab);

            progressGrammar.setMax(total);
            progressGrammar.setProgress(grammar);

            progressListening.setMax(total);
            progressListening.setProgress(listening);

            progressReading.setMax(total);
            progressReading.setProgress(reading);
        }
    }


    /**
     * Hiển thị/ẩn loading indicator
     */
    private void showLoading(boolean show) {
        if (loadingIndicator != null) {
            loadingIndicator.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Load tiến độ 7 ngày qua và hiển thị đồ thị
     */
    private void loadDailyProgressChart() {
        String userId = tokenManager.getUserId();
        if (userId == null) return;

        // Tính toán ngày bắt đầu và kết thúc (7 ngày qua)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar calendar = Calendar.getInstance();

        String endDate = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -6); // 7 ngày (bao gồm hôm nay)
        String startDate = dateFormat.format(calendar.getTime());

        repository.getDailyProgressRange(userId, startDate, endDate,
            new LexiGoRepository.ApiCallback<List<DailyProgress>>() {
            @Override
            public void onSuccess(List<DailyProgress> dailyProgressList) {
                if (dailyProgressList != null && !dailyProgressList.isEmpty()) {
                    setupLineChart(dailyProgressList);
                    cardProgressChart.setVisibility(View.VISIBLE);
                } else {
                    // Không có dữ liệu, tạo dữ liệu rỗng cho 7 ngày
                    List<DailyProgress> emptyData = createEmptyDailyProgress(startDate, endDate);
                    setupLineChart(emptyData);
                    cardProgressChart.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String error) {
                // Xử lý lỗi - tạo dữ liệu rỗng
                List<DailyProgress> emptyData = createEmptyDailyProgress(startDate, endDate);
                setupLineChart(emptyData);
                cardProgressChart.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Tạo dữ liệu rỗng cho các ngày không có progress
     */
    private List<DailyProgress> createEmptyDailyProgress(String startDate, String endDate) {
        List<DailyProgress> emptyList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(startDate));
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(dateFormat.parse(endDate));

            while (!calendar.after(endCalendar)) {
                DailyProgress dp = new DailyProgress();
                dp.setDate(dateFormat.format(calendar.getTime()));

                // Initialize empty skill progress objects (all zeros)
                DailyProgress.SkillProgress emptySkill = new DailyProgress.SkillProgress();
                emptySkill.setTotalLessons(0);

                dp.setVocab(emptySkill);
                dp.setGrammar(emptySkill);
                dp.setListening(emptySkill);
                dp.setReading(emptySkill);

                emptyList.add(dp);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return emptyList;
    }

    /**
     * Thiết lập đồ thị đường cho tiến độ hàng ngày
     */
    private void setupLineChart(List<DailyProgress> dailyProgressList) {
        if (lineChart == null || dailyProgressList == null) return;

        // Chuẩn bị dữ liệu
        ArrayList<Entry> entries = new ArrayList<>();
        final ArrayList<String> dateLabels = new ArrayList<>();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM", Locale.US);

        for (int i = 0; i < dailyProgressList.size(); i++) {
            DailyProgress dp = dailyProgressList.get(i);
            entries.add(new Entry(i, dp.getTotalCount()));

            // Format date label
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(inputFormat.parse(dp.getDate()));
                dateLabels.add(outputFormat.format(cal.getTime()));
            } catch (Exception e) {
                dateLabels.add(dp.getDate());
            }
        }

        // Tạo dataset
        LineDataSet dataSet = new LineDataSet(entries, "Số bài học");
        dataSet.setColor(getResources().getColor(R.color.primary, null));
        dataSet.setCircleColor(getResources().getColor(R.color.primary, null));
        dataSet.setCircleRadius(4f);
        dataSet.setCircleHoleRadius(2f);
        dataSet.setLineWidth(2f);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(getResources().getColor(R.color.text_primary, null));
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(getResources().getColor(R.color.primary, null));
        dataSet.setFillAlpha(30);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Đường cong mượt

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        // Customize chart
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.setExtraBottomOffset(10f);

        // Customize X-axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(getResources().getColor(R.color.text_secondary, null));
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < dateLabels.size()) {
                    return dateLabels.get(index);
                }
                return "";
            }
        });

        // Customize left Y-axis
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(getResources().getColor(R.color.divider, null));
        leftAxis.setTextColor(getResources().getColor(R.color.text_secondary, null));
        leftAxis.setGranularity(1f);
        leftAxis.setAxisMinimum(0f);

        // Disable right Y-axis
        lineChart.getAxisRight().setEnabled(false);

        // Customize legend
        lineChart.getLegend().setEnabled(true);
        lineChart.getLegend().setTextColor(getResources().getColor(R.color.text_primary, null));
        lineChart.getLegend().setTextSize(12f);

        // Animate chart
        lineChart.animateX(1000);
        lineChart.invalidate();
    }
}

