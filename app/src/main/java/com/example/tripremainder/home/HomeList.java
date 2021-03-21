package com.example.tripremainder.home;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeList implements Serializable {
    private String tripName;
    private String startPoint;
    private String endPoint;
    private String tripTime;
    private String tripDate;
    private ArrayList<String> notes;

    public  HomeList(){

    }

    public HomeList(String tripName, String startPoint, String endPoint, String tripTime, String tripDate) {
        this.tripName = tripName;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.tripTime = tripTime;
        this.tripDate = tripDate;

    }

    public ArrayList<String> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getTripTime() {
        return tripTime;
    }

    public void setTripTime(String tripTime) {
        this.tripTime = tripTime;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }
}
