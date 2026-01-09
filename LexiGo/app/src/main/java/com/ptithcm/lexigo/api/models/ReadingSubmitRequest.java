package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ReadingSubmitRequest {
    @SerializedName("answers")
    private List<ReadingAnswer> answers;

    public ReadingSubmitRequest(List<ReadingAnswer> answers) {
        this.answers = answers;
    }

    public List<ReadingAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<ReadingAnswer> answers) {
        this.answers = answers;
    }
}
