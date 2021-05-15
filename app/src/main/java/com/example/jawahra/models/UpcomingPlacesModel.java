package com.example.jawahra.models;


public class UpcomingPlacesModel {
    private String placeName, placeEmirate, placeImg;

//    Empty Constructor for Firebase
    public UpcomingPlacesModel() {
    }

    public UpcomingPlacesModel(String placeName, String placeEmirate, String placeImg) {
        this.placeName = placeName;
        this.placeEmirate = placeEmirate;
        this.placeImg = placeImg;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceEmirate() {
        return placeEmirate;
    }

    public void setPlaceEmirate(String placeEmirate) {
        this.placeEmirate = placeEmirate;
    }

    public String getPlaceImg() {
        return placeImg;
    }

    public void setPlaceImg(String placeImg) {
        this.placeImg = placeImg;
    }
}
