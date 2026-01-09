package com.example.vuhoangphat_n22dccn058;

public class Food1 {
    private int ImgResId;
    private String name;
    private String time;

    public Food1(int imgResId, String name, String time) {
        ImgResId = imgResId;
        this.name = name;
        this.time = time;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
