package com.ptithcm.lexigo.utils;

import android.content.Context;
import android.content.Intent;
import com.ptithcm.lexigo.activities.QuizActivity;

/**
 * Utility class to help launch QuizActivity with proper parameters
 */
public class QuizLauncher {

    /**
     * Launch vocabulary quiz
     *
     * @param context Context
     * @param topicId Topic ID for vocabulary quiz
     * @param topicName Topic name (shown in title)
     * @param level Level (Beginner, Intermediate, Advanced) - optional
     */
    public static void launchVocabQuiz(Context context, String topicId, String topicName, String level) {
        Intent intent = new Intent(context, QuizActivity.class);
        intent.putExtra(QuizActivity.EXTRA_QUIZ_TYPE, QuizActivity.QUIZ_TYPE_VOCAB);
        intent.putExtra(QuizActivity.EXTRA_TOPIC_ID, topicId);
        intent.putExtra(QuizActivity.EXTRA_LEVEL, level);
        intent.putExtra(QuizActivity.EXTRA_TITLE, topicName);
        context.startActivity(intent);
    }

    /**
     * Launch grammar quiz (exercises)
     *
     * @param context Context
     * @param lessonId Lesson ID for grammar exercises
     * @param lessonTitle Lesson title (shown in title)
     * @param level Level (Beginner, Intermediate, Advanced) - optional
     */
    public static void launchGrammarQuiz(Context context, String lessonId, String lessonTitle, String level) {
        Intent intent = new Intent(context, QuizActivity.class);
        intent.putExtra(QuizActivity.EXTRA_QUIZ_TYPE, QuizActivity.QUIZ_TYPE_GRAMMAR);
        intent.putExtra(QuizActivity.EXTRA_LESSON_ID, lessonId);
        intent.putExtra(QuizActivity.EXTRA_LEVEL, level);
        intent.putExtra(QuizActivity.EXTRA_TITLE, lessonTitle);
        context.startActivity(intent);
    }
}

