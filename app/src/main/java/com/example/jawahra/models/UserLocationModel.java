package com.example.jawahra.models;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class UserLocationModel {


    private GeoPoint geoPoint;
    private @ServerTimestamp Date timestamp;
    private UserModel userModel;

    public UserLocationModel(GeoPoint geoPoint, Date timestamp, UserModel userModel) {
        this.geoPoint = geoPoint;
        this.timestamp = timestamp;
        this.userModel = userModel;
    }

    public UserLocationModel () {}

    public GeoPoint getGeoPoint() { return geoPoint; }

    public void setGeoPoint(GeoPoint geoPoint) { this.geoPoint = geoPoint; }

    public Date getTimestamp() { return timestamp; }

    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public UserModel getUserModel() { return userModel; }

    public void setUserModel(UserModel userModel) { this.userModel = userModel; }

    @Override
    public String toString() {
        return "UserLocationModel{" +
                "geoPoint=" + geoPoint +
                ", timestamp=" + timestamp +
                ", userModel=" + userModel +
                '}';
    }
}
