package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class VocabQuiz {
    @SerializedName("_id")
    private String id;
    
    @SerializedName("question")
    private String question;
    
    @SerializedName("question_type")
    private String questionType; // multiple_choice, fill_in_blank, matching
    
    @SerializedName("options")
    private List<String> options;
    
    @SerializedName(value = "correct_answer", alternate = {"correctAnswer", "answer"})
    private String correctAnswer;
    
    @SerializedName(value = "correct_option", alternate = {"answerIndex", "answer_index", "correct_option_index"})
    private Integer answerIndex;

    @SerializedName("topic_id")
    private String topicId;
    
    @SerializedName("level")
    private String level;
    
    @SerializedName("explanation")
    private String explanation;

    // Constructors
    public VocabQuiz() {}

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
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

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Integer getAnswerIndex() {
        return answerIndex;
    }

    public void setAnswerIndex(Integer answerIndex) {
        this.answerIndex = answerIndex;
    }
}
