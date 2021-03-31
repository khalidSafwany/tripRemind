package com.example.tripremainder.DataBase.Model;



import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NewTrip implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private String email;
    @ColumnInfo
    private String tripName;
    @ColumnInfo
    private String startPoint;
    @ColumnInfo
    private String endPoint;
    @ColumnInfo
    private String tripTime;
    @ColumnInfo
    private String tripDate;
    @ColumnInfo
    private int state;
    @ColumnInfo
    private String stateType;

    public String getStateType() {
        return stateType;
    }

    public void setStateType(String statename) {
        this.stateType = statename;
    }

    @ColumnInfo
    private double startPointLong;
    @ColumnInfo
    private double endPointLong;
    @ColumnInfo
    private double startPointlat;
    @ColumnInfo
    private double endPointlat;

    public double getStartPointLong() {
        return startPointLong;
    }

    public void setStartPointLong(double startPointLong) {
        this.startPointLong = startPointLong;
    }

    public double getEndPointLong() {
        return endPointLong;
    }

    public void setEndPointLong(double endPointLong) {
        this.endPointLong = endPointLong;
    }

    public double getStartPointlat() {
        return startPointlat;
    }

    public void setStartPointlat(double startPointlat) {
        this.startPointlat = startPointlat;
    }

    public double getEndPointlat() {
        return endPointlat;
    }

    public void setEndPointlat(double endPointlat) {
        this.endPointlat = endPointlat;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
