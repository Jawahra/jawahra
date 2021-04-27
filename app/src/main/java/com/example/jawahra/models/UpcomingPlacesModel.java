package com.example.jawahra.models;


public class UpcomingPlacesModel {
    private String placeName, placeEmirate;

//    Empty Constructor for Firebase
    public UpcomingPlacesModel() {
    }

    public UpcomingPlacesModel(String placeName, String placeEmirate) {
        this.placeName = placeName;
        this.placeEmirate = placeEmirate;
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
}
