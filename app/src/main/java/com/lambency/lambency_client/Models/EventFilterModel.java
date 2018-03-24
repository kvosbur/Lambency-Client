package com.lambency.lambency_client.Models;

import android.util.EventLog;

import java.sql.Timestamp;
import java.util.ArrayList;

public class EventFilterModel {

    private double latitude;
    private double longitude;
    private Timestamp startStamp;
    private Timestamp endStamp;
    private int distanceMiles = -1;

    public static EventFilterModel currentFilter = new EventFilterModel();

    public EventFilterModel()
    {

    }

    public EventFilterModel(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public EventFilterModel(double latitude, double longitude, Timestamp startStamp, Timestamp endStamp){
        this(latitude,longitude);
        this.startStamp = startStamp;
        this.endStamp = endStamp;
    }

    public EventFilterModel(double latitude, double longitude, Timestamp startStamp, Timestamp endStamp, int distanceMiles){
        this(latitude,longitude, startStamp, endStamp);
        this.distanceMiles = distanceMiles;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Timestamp getStartStamp() {
        return startStamp;
    }

    public void setStartStamp(Timestamp startStamp) {
        this.startStamp = startStamp;
    }

    public Timestamp getEndStamp() {
        return endStamp;
    }

    public void setEndStamp(Timestamp endStamp) {
        this.endStamp = endStamp;
    }

    public int getDistanceMiles() {
        return distanceMiles;
    }

    public void setDistanceMiles(int distanceMiles) {
        this.distanceMiles = distanceMiles;
    }
}

