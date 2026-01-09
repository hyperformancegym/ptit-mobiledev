package com.example.myapplication;

public class Recipe {
    private String name;
    private int calories;
    private String nutritionalInfo;
    private int imageRes;

    public Recipe(String name, int calories, String nutritionalInfo, int imageRes) {
        this.name = name;
        this.calories = calories;
        this.nutritionalInfo = nutritionalInfo;
        this.imageRes = imageRes;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public String getNutritionalInfo() {
        return nutritionalInfo;
    }

    public int getImageRes() {
        return imageRes;
    }
}