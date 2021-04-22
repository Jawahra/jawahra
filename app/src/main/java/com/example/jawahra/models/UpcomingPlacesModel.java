package com.example.jawahra.models;


public class UpcomingPlacesModel {
    private String name, emirate;

//    Empty Constructor for Firebase
    public UpcomingPlacesModel() {
    }

    public UpcomingPlacesModel(String name, String emirate) {
        this.name = name;
        this.emirate = emirate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmirate() {
        return emirate;
    }

    public void setEmirate(String emirate) {
        this.emirate = emirate;
    }
}
