package com.example.jawahra.models;

import java.util.Date;

public class UpcomingEventsModel {
    private String eventEmirate, eventName, eventDetails;
    Date eventDate;

//    Empty constructor for firebase
    public UpcomingEventsModel() {

    }

    public UpcomingEventsModel(String eventEmirate,String eventName,String eventDetails, Date eventDate){
        this.eventEmirate = eventEmirate;
        this.eventName = eventName;
        this.eventDetails = eventDetails;
        this.eventDate = eventDate;
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

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }
}
