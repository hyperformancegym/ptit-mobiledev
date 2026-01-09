package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;

public class ReadingAnswer {
    @SerializedName("question_id")
    private String questionId;
    
    @SerializedName("selected_option")
    private int selectedOption;

    public ReadingAnswer(String questionId, int selectedOption) {
        this.questionId = questionId;
        this.selectedOption = selectedOption;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public int getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(int selectedOption) {
        this.selectedOption = selectedOption;
    }
}
