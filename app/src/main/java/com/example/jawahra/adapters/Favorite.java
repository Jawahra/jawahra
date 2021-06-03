package com.example.jawahra.adapters;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//these are called "annotations" that reduce the amount of code to write, ty to Room Wrapper
@Entity(tableName = "favorite_table")

// Favorite java class is the entity that represents a table in the sqlite database
public class Favorite {
    //autogenerates the id for each row / data set
    @PrimaryKey(autoGenerate = true)
    private int id;
    //these represent the columns
    private String title;
    private String emirate;
    private String description;
    private String history;
    private String website;
    private String attire;
    private String availability;
    private String prices;
    private String activities;

    private String mediaReference;

    public Favorite(String title, String emirate, String description, String history, String website, String attire, String availability, String prices, String activities, String mediaReference) {
        this.title = title;
        this.emirate = emirate;
        this.description = description;
        this.history = history;
        this.website = website;
        this.attire = attire;
        this.availability = availability;
        this.prices = prices;
        this.activities = activities;
        this.mediaReference = mediaReference;
    }


    public String getMediaReference() {
        return mediaReference;
    }

    public void setMediaReference(String mediaReference) {
        this.mediaReference = mediaReference;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAttire() {
        return attire;
    }

    public void setAttire(String attire) {
        this.attire = attire;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getEmirate() {
        return emirate;
    }

    public void setEmirate(String emirate) {
        this.emirate = emirate;
    }
}
