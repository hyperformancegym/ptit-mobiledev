package com.example.myapplication;

public class Food {
    private String name;
    private String desc;
    private int price;
    private int imageRes;

    public Food(String name, String desc, int price, int imageRes) {
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.imageRes = imageRes;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getPrice() {
        return price;
    }

    public int getImageRes() {
        return imageRes;
    }
}