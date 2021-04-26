package com.example.jawahra.models;

public class PlacesModel{

    private String name;

    private PlacesModel() {}

    private PlacesModel(String name) {this.name = name; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
