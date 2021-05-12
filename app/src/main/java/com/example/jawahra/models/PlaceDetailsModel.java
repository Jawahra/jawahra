package com.example.jawahra.models;

public class PlaceDetailsModel {

    private String desc;
    private String history;
    private String map;

    private PlaceDetailsModel(){}

    private PlaceDetailsModel(String desc, String map, String history){
        this.history = history;
        this.desc = desc;
        this.map = map;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
