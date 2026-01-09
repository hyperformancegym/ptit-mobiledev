package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;

public class ProgressUpdateRequest {
    @SerializedName("vocab_completed")
    private Integer vocabCompleted;

    @SerializedName("grammar_completed")
    private Integer grammarCompleted;

    @SerializedName("listening_completed")
    private Integer listeningCompleted;

    @SerializedName("reading_completed")
    private Integer readingCompleted;

    @SerializedName("skill_type")
    private String skillType;

    @SerializedName("lesson_id")
    private String lessonId;

    @SerializedName("topic_id")
    private String topicId;

    @SerializedName("score")
    private Double score;

    @SerializedName("study_time_minutes")
    private Integer studyTimeMinutes;

    public ProgressUpdateRequest() {
    }

    public Integer getVocabCompleted() {
        return vocabCompleted;
    }

    public void setVocabCompleted(Integer vocabCompleted) {
        this.vocabCompleted = vocabCompleted;
    }

    public Integer getGrammarCompleted() {
        return grammarCompleted;
    }

    public void setGrammarCompleted(Integer grammarCompleted) {
        this.grammarCompleted = grammarCompleted;
    }

    public Integer getListeningCompleted() {
        return listeningCompleted;
    }

    public void setListeningCompleted(Integer listeningCompleted) {
        this.listeningCompleted = listeningCompleted;
    }

    public Integer getReadingCompleted() {
        return readingCompleted;
    }

    public void setReadingCompleted(Integer readingCompleted) {
        this.readingCompleted = readingCompleted;
    }

    public String getSkillType() {
        return skillType;
    }

    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getStudyTimeMinutes() {
        return studyTimeMinutes;
    }

    public void setStudyTimeMinutes(Integer studyTimeMinutes) {
        this.studyTimeMinutes = studyTimeMinutes;
    }
}

