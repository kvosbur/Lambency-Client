package com.lambency.lambency_client.Models;

import java.io.IOException;
import java.sql.Timestamp;

public class EventModel {

    private String name;
    private int org_id;
    private Timestamp start;
    private Timestamp end;
    private String description;
    private String location;
    private String image_path; // file path for server only
    private byte[] imageFile; // base 64 encoded
    private int event_id;
    private double latitude;
    private double longitude;
    private String clockInCode;
    private String clockOutCode;
    private String orgName;
    private boolean privateEvent = false;
    private boolean pastEvent = false;


    static int x = 54545;
    static Timestamp testTime = new Timestamp((long)x);
    public static EventModel myEventModel = new EventModel("tester",11,testTime,testTime,"fake test","testing123",0,0, "org test Name");



    //Empty constructor for testing
    public EventModel(){

    }

    public EventModel(String name, int org_id, Timestamp start, Timestamp end, String description, String location, String orgName) {
        this.name = name;
        this.org_id = org_id;
        this.start = start;
        this.end = end;
        this.description = description;
        this.location = location;
        this.orgName = orgName;
    }

    public EventModel(String name, int org_id, Timestamp start, Timestamp end, String description, String location, double latitude,
                      double longitude, String orgName) {
        this(name, org_id, start, end, description, location, orgName);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public EventModel(String name, int org_id, Timestamp start, Timestamp end, String description, String location,
                      byte[] imageFile, double latitude, double longitude, String orgName) {

        this(name, org_id, start, end, description, location, latitude, longitude, orgName);
    }

    public EventModel(byte[] imageFile, String name, int org_id, Timestamp start,
                      Timestamp end, String description, String location, String orgName) {
        this(name, org_id, start, end, description, location, orgName);
        this.imageFile = imageFile;
    }




    public EventModel(String name, int org_id, Timestamp start, Timestamp end, String description, String location,
                      String image_path, int event_id, double latitude, double longitude, String clockInCode,
                      String clockOutCode, String orgName) {
        this(name, org_id, start, end, description, location, latitude, longitude, orgName);
        this.image_path = image_path;
        this.event_id = event_id;
        this.clockInCode = clockInCode;
        this.clockOutCode = clockOutCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrg_id() {
        return org_id;
    }

    public void setOrg_id(int org_id) {
        this.org_id = org_id;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage_path() {
        return image_path;
    }

    /** BE CAUTIOUS IF YOU USE THIS. MAKE SURE THIS PATH COMES FROM IMAGEWR
     *
     * @param image_path   image path to the image
     */

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public byte[] getImageFile() {
        return imageFile;
    }

    /**
     * DONT USE THIS METHOD UNLESS YOU HAVE TO. USE setImageFile.
     * This will not update the file path for this image
     *
     * @param imageFile base 64 encoded image string
     */
    public void setImageFile(byte[] imageFile)  {
        this.imageFile = imageFile;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int id){
        this.event_id = id;
    }

    public double getLattitude() {
        return latitude;
    }

    public void setLattitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getClockInCode() {
        return clockInCode;
    }

    public String getClockOutCode() {
        return clockOutCode;
    }



    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public boolean isPrivateEvent() {
        return privateEvent;
    }

    public void setPrivateEvent(boolean privateEvent) {
        this.privateEvent = privateEvent;
    }

    public void setPastEvent(boolean pastEvent) {
        this.pastEvent = pastEvent;
    }

    public boolean isPastEvent() {
        return pastEvent;
    }
}

