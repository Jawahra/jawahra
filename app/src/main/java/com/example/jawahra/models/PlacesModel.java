package com.example.jawahra.models;

public class PlacesModel{

    private String name;
    private String coverImg;

    private PlacesModel() {}

    private PlacesModel(String name, String coverImg) {
        this.name = name;
        this.coverImg = coverImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }
}
