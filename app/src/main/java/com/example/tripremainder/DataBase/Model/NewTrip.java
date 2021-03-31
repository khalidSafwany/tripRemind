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
    private String tripBackTime;
    @ColumnInfo
    private String tripBackDate;
    @ColumnInfo
    private int state;

    public String getTripBackTime() {
        return tripBackTime;
    }

    public void setTripBackTime(String tripBackTime) {
        this.tripBackTime = tripBackTime;
    }

    public String getTripBackDate() {
        return tripBackDate;
    }

    public void setTripBackDate(String tripBackDate) {
        this.tripBackDate = tripBackDate;
    }

    @ColumnInfo
    private String direction;




    public String getDirection() {
        return direction;
    }


    public void setDirection(String direction) {
        this.direction = direction;
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
