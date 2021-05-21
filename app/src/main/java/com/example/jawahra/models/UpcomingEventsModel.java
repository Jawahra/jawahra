package com.example.jawahra.models;

import java.util.Date;

public class UpcomingEventsModel {
    private String eventImg, eventEmirate, eventName, eventDetails;
    Date eventToDate;

//    Empty constructor for firebase
    public UpcomingEventsModel() {

    }

    public UpcomingEventsModel(String eventImg, String eventEmirate,String eventName,String eventDetails, Date eventToDate){
        this.eventImg = eventImg;
        this.eventEmirate = eventEmirate;
        this.eventName = eventName;
        this.eventDetails = eventDetails;
        this.eventToDate = eventToDate;
    }

    public String getEventEmirate() {
        return eventEmirate;
    }

    public void setEventEmirate(String eventEmirate) {
        this.eventEmirate = eventEmirate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public Date getEventToDate() {
        return eventToDate;
    }

    public void setEventToDate(Date eventToDate) {
        this.eventToDate = eventToDate;
    }

    public String getEventImg() {
        return eventImg;
    }

    public void setEventImg(String eventImg) {
        this.eventImg = eventImg;
    }
}
