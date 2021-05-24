package com.example.jawahra.adapters;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

//these are called "annotations" that reduce the amount of code to write, ty to Room Wrapper
@Entity(tableName = "favorite_table")

// Favorite java class is the entity that represents a table in the sqlite database
public class Favorite {
    //autogenerates the id for each row / data set
    @PrimaryKey(autoGenerate = true)
    private int id;
    //these represent the columns
    private String title;
    private String description;
    private String history;
    private String website;
    private String attire;
    private List<String> availability;
    private List<String> prices;
    private List<String> activities;

    public Favorite(String title, String description, String history, String website, String attire, List<String> availability, List<String> prices, List<String> activities) {
        this.title = title;
        this.description = description;
        this.history = history;
        this.website = website;
        this.attire = attire;
        this.availability = availability;
        this.prices = prices;
        this.activities = activities;
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

    public List<String> getAvailability() {
        return availability;
    }

    public void setAvailability(List<String> availability) {
        this.availability = availability;
    }

    public List<String> getPrices() {
        return prices;
    }

    public void setPrices(List<String> prices) {
        this.prices = prices;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }

}
