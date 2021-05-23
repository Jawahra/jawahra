package com.example.jawahra.models;

public class DiscoverModel {
    String coverImg, emirate, name, placeId;

    public DiscoverModel() {

    }

    public DiscoverModel(String coverImg, String emirate, String name, String placeId) {
        this.coverImg = coverImg;
        this.emirate = emirate;
        this.name = name;
        this.placeId = placeId;
    }


    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getEmirate() {
        return emirate;
    }

    public void setEmirate(String emirate) {
        this.emirate = emirate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
