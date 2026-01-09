package com.example.onthigk;

public class Food2 {
    private String name;
    private int cal;
    private double protein;
    private double cabs;
    private double fat;
    private int imgId;

    public Food2(String name, int cal, double protein, double cabs, double fat, int imgId) {
        this.name = name;
        this.cal = cal;
        this.protein = protein;
        this.cabs = cabs;
        this.fat = fat;
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCal() {
        return cal;
    }

    public void setCal(int cal) {
        this.cal = cal;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getCabs() {
        return cabs;
    }

    public void setCabs(double cabs) {
        this.cabs = cabs;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
