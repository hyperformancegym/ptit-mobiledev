package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;

public class UserRegisterRequest {
    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("level")
    private String level;

    @SerializedName("goals")
    private Goals goals;

    public UserRegisterRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.level = "Beginner";
        this.goals = new Goals();
    }

    public UserRegisterRequest(String name, String email, String password, String level, Goals goals) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.level = level;
        this.goals = goals;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

