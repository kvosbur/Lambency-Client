package com.lambency.lambency_client.Models;

import java.util.ArrayList;

public class OrganizationModel {
    public UserModel owner;
    public String name;
    public ArrayList<UserModel> members;
    public String location;
    public int orgID;
    public ArrayList<Object> events;
    public String description;
    public String email;
    public UserModel contact;
    public String image;
    public ArrayList<UserModel> organizers;


    public OrganizationModel(){
        //Empty constructor for testing
    }

    public OrganizationModel(UserModel owner, String name, String location, int orgID, String description, String email, UserModel contact, String image) {
        this.owner = owner;
        this.name = name;
        this.location = location;
        this.orgID = orgID;
        this.description = description;
        this.email = email;
        this.contact = contact;
        this.image = image;
        members = new ArrayList<UserModel>();
        members.add(owner);
        events = new ArrayList<>();
        this.organizers = new ArrayList<UserModel>();
        organizers.add(owner);
    }

    public int numFollowing;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<UserModel> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<UserModel> members) {
        this.members = members;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getOrgID() {
        return orgID;
    }

    public void setOrgID(int orgID) {
        this.orgID = orgID;
    }

    public ArrayList<Object> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Object> events) {
        this.events = events;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserModel getContact() {
        return contact;
    }

    public void setContact(UserModel contact) {
        this.contact = contact;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<UserModel> getOrganizers() {
        return organizers;
    }

    public void setOrganizers(ArrayList<UserModel> organizers) {
        this.organizers = organizers;
    }

    public int getNumFollowing(){
        return numFollowing;
    }
    public void incNumFollowing() {
        this.numFollowing += 1;
    }

    public void decNumFollowing(int numFollowing) {
        this.numFollowing -= 1;
    }

    public int checkPermissions(UserModel u){
        return 0;
    }
}
