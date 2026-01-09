package com.example.onthigk;

public class Food1 {
    private String name;
    private String description;
    private double price;
    private int imgResId;

    public Food1() {
    }

    public Food1(String name, String description, double price, int imgResId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgResId = imgResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }
}
