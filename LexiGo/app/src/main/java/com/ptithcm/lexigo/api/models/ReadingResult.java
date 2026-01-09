package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ReadingResult {
    @SerializedName("score")
    private int score;
    
    @SerializedName("total_questions")
    private int totalQuestions;
    
    @SerializedName("correct_answers")
    private int correctAnswers;
    
    @SerializedName("results")
    private List<QuestionResult> results;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public List<QuestionResult> getResults() {
        return results;
    }

    public void setResults(List<QuestionResult> results) {
        this.results = results;
    }

    public static class QuestionResult {
        @SerializedName("question_id")
        private String questionId;
        
        @SerializedName("is_correct")
        private boolean isCorrect;
        
        @SerializedName("selected_option")
        private int selectedOption;
        
        @SerializedName("correct_option")
        private int correctOption;

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public boolean isCorrect() {
            return isCorrect;
        }

        public void setCorrect(boolean correct) {
            isCorrect = correct;
        }

        public int getSelectedOption() {
            return selectedOption;
        }

        public void setSelectedOption(int selectedOption) {
            this.selectedOption = selectedOption;
        }

        public int getCorrectOption() {
            return correctOption;
        }

        public void setCorrectOption(int correctOption) {
            this.correctOption = correctOption;
        }
    }
}
