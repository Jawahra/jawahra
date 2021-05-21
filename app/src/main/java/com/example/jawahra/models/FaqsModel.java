package com.example.jawahra.models;

import java.util.List;

public class FaqsModel {

    private String website;
    private List<String> activities;
    private List<String> prices;
    private List<String> availability;
    private String attire;

    private FaqsModel(){};

    private FaqsModel(String website, String attire, List<String> activities, List<String> prices, List<String> availability){
        this.website = website;
        this.attire = attire;
        this.activities = activities;
        this.prices = prices;
        this.availability = availability;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }

    public List<String> getPrices() {
        return prices;
    }

    public void setPrices(List<String> prices) {
        this.prices = prices;
    }

    public List<String> getAvailability() {
        return availability;
    }

    public void setAvailability(List<String> availability) {
        this.availability = availability;
    }

    public String getAttire() {
        return attire;
    }

    public void setAttire(String attire) {
        this.attire = attire;
    }
}

