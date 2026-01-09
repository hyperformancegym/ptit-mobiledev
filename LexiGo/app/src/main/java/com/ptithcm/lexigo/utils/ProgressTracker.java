package com.ptithcm.lexigo.utils;

import android.content.Context;
import android.util.Log;

import com.ptithcm.lexigo.api.TokenManager;
import com.ptithcm.lexigo.api.models.Progress;
import com.ptithcm.lexigo.api.models.ProgressUpdateRequest;
import com.ptithcm.lexigo.api.repositories.LexiGoRepository;

/**
 * Utility class để tự động cập nhật tiến độ học khi người dùng hoàn thành bài tập
 */
public class ProgressTracker {
    private static final String TAG = "ProgressTracker";

    public enum ExerciseType {
        VOCAB,
        GRAMMAR,
        LISTENING,
        READING
    }

    /**
     * Cập nhật tiến độ khi hoàn thành bài tập
     *
     * @param context Context
     * @param exerciseType Loại bài tập (VOCAB, GRAMMAR, LISTENING, READING)
     * @param callback Callback để nhận thông báo kết quả (optional)
     */
    public static void updateProgress(Context context, ExerciseType exerciseType,
                                     final ProgressUpdateCallback callback) {
        // Kiểm tra đăng nhập
        TokenManager tokenManager = TokenManager.getInstance(context);
        if (!tokenManager.isLoggedIn()) {
            Log.w(TAG, "User not logged in, skipping progress update");
            if (callback != null) {
                callback.onError("User not logged in");
            }
            return;
        }

        // Tạo request với increment = 1 cho loại bài tập tương ứng
        ProgressUpdateRequest request = new ProgressUpdateRequest();

        switch (exerciseType) {
            case VOCAB:
                request.setVocabCompleted(1);
                Log.d(TAG, "Updating vocab progress: +1");
                break;
            case GRAMMAR:
                request.setGrammarCompleted(1);
                Log.d(TAG, "Updating grammar progress: +1");
                break;
            case LISTENING:
                request.setListeningCompleted(1);
                Log.d(TAG, "Updating listening progress: +1");
                break;
            case READING:
                request.setReadingCompleted(1);
                Log.d(TAG, "Updating reading progress: +1");
                break;
        }

        // Gọi API update progress
        LexiGoRepository repository = LexiGoRepository.getInstance(context);
        repository.updateProgress(request, new LexiGoRepository.ApiCallback<Progress>() {
            @Override
            public void onSuccess(Progress data) {
                Log.d(TAG, "Progress updated successfully: " + exerciseType);

                // Tăng daily progress trong SharedPreferences
                DailyProgressTracker dailyTracker = DailyProgressTracker.getInstance(context);
                dailyTracker.incrementDailyProgress();

                if (callback != null) {
                    callback.onSuccess(data);
                }
            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "Failed to update progress: " + message);
                if (callback != null) {
                    callback.onError(message);
                }
            }
        });
    }

    /**
     * Cập nhật tiến độ mà không cần callback
     */
    public static void updateProgress(Context context, ExerciseType exerciseType) {
        updateProgress(context, exerciseType, null);
    }

    /**
     * Callback interface cho progress update
     */
    /**
     * Cập nhật tiến độ chi tiết (API mới)
     *
     * @param context Context
     * @param exerciseType Loại bài tập
     * @param lessonId ID bài học/script/passage
     * @param topicId ID chủ đề (optional)
     * @param score Điểm số (0-100)
     * @param studyTimeMinutes Thời gian học (phút)
     * @param callback Callback
     */
    public static void updateDetailedProgress(Context context, ExerciseType exerciseType,
                                            String lessonId, String topicId,
                                            double score, int studyTimeMinutes,
                                            final ProgressUpdateCallback callback) {
        // Kiểm tra đăng nhập
        TokenManager tokenManager = TokenManager.getInstance(context);
        if (!tokenManager.isLoggedIn()) {
            Log.w(TAG, "User not logged in, skipping progress update");
            if (callback != null) {
                callback.onError("User not logged in");
            }
            return;
        }

        ProgressUpdateRequest request = new ProgressUpdateRequest();
        request.setLessonId(lessonId);
        request.setTopicId(topicId);
        request.setScore(score);
        request.setStudyTimeMinutes(studyTimeMinutes);

        switch (exerciseType) {
            case VOCAB:
                request.setSkillType("vocab");
                break;
            case GRAMMAR:
                request.setSkillType("grammar");
                break;
            case LISTENING:
                request.setSkillType("listening");
                break;
            case READING:
                request.setSkillType("reading");
                break;
        }

        Log.d(TAG, "Updating detailed progress: " + exerciseType + ", score=" + score);

        // Gọi API update progress
        LexiGoRepository repository = LexiGoRepository.getInstance(context);
        repository.updateProgress(request, new LexiGoRepository.ApiCallback<Progress>() {
            @Override
            public void onSuccess(Progress data) {
                Log.d(TAG, "Progress updated successfully: " + exerciseType);

                // Tăng daily progress trong SharedPreferences
                DailyProgressTracker dailyTracker = DailyProgressTracker.getInstance(context);
                dailyTracker.incrementDailyProgress();

                if (callback != null) {
                    callback.onSuccess(data);
                }
            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "Failed to update progress: " + message);
                if (callback != null) {
                    callback.onError(message);
                }
            }
        });
    }

    /**
     * Callback interface cho progress update
     */
    public interface ProgressUpdateCallback {
        void onSuccess(Progress progress);
        void onError(String message);
    }
}

