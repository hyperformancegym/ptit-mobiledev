package com.example.vuhoangphat_n22dccn058;

public class Food2 {
    private int ImgResId;
    private String name;
    private String type;
    private double price;

    public Food2(int imgResId, String name, String type, double price) {
        ImgResId = imgResId;
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public int getImgResId() {
        return ImgResId;
    }

    public void setImgResId(int imgResId) {
        ImgResId = imgResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
