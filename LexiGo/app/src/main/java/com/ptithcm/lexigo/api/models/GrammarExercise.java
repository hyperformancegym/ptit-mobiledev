package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GrammarExercise {
    @SerializedName("_id")
    private String id;
    
    @SerializedName(value = "lesson_id", alternate = {"lessonId", "grammar_id", "grammarId"})
    private String lessonId;
    
    @SerializedName("question")
    private String question;
    
    @SerializedName(value = "exercise_type", alternate = {"exerciseType", "type"})
    private String exerciseType; // fill_in_blank, multiple_choice, sentence_correction, translation, true_false

    @SerializedName("options")
    private List<String> options;
    
    @SerializedName(value = "correct_answer", alternate = {"correctAnswer", "answer"})
    private String correctAnswer;
    
    @SerializedName(value = "correct_option", alternate = {"answerIndex", "answer_index", "correct_option_index"})
    private Integer answerIndex;

    @SerializedName("explanation")
    private String explanation;
    
    @SerializedName(value = "difficulty", alternate = {"level"})
    private String difficulty;

    // Constructors
    public GrammarExercise() {}

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getAnswerIndex() {
        return answerIndex;
    }

    public void setAnswerIndex(Integer answerIndex) {
        this.answerIndex = answerIndex;
    }
}
