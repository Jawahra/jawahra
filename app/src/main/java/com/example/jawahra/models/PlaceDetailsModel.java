package com.example.jawahra.models;

public class PlaceDetailsModel {

    private String desc;
    private String history;
    private String media;

    private PlaceDetailsModel(){}

    private PlaceDetailsModel(String desc, String media, String history){
        this.history = history;
        this.desc = desc;
        this.media = media;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }
}
