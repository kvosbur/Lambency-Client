package com.lambency.lambency_client.Models;

/**
 * Created by brian on 4/2/18.
 */


import java.util.ArrayList;

public class OrganizationFilterModel {

    private Double latitude;
    private Double longitude;
    private int distanceMiles = -1;
    private String title;
    private String location;

    public static OrganizationFilterModel currentFilter = new OrganizationFilterModel();

    public OrganizationFilterModel(String location, int distanceMiles) {
        this.distanceMiles = distanceMiles;
        this.location = location;
    }

    public OrganizationFilterModel(String location, int distanceMiles, String title){
        this(location,distanceMiles);
        this.title = title;
    }

    public OrganizationFilterModel(String title){
        this.title = title;
    }

    public OrganizationFilterModel(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public OrganizationFilterModel(double latitude, double longitude, String title) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
    }

    public OrganizationFilterModel() {

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

    public int getDistanceMiles() {
        return distanceMiles;
    }

    public void setDistanceMiles(int distanceMiles) {
        this.distanceMiles = distanceMiles;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
