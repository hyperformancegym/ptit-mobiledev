package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;

public class UserUpdateRequest {
    @SerializedName("name")
    private String name;

    @SerializedName("level")
    private String level;

    @SerializedName("goals")
    private Goals goals;

    public UserUpdateRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Goals getGoals() {
        return goals;
    }

    public void setGoals(Goals goals) {
        this.goals = goals;
    }
}

