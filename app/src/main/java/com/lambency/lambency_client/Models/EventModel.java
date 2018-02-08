package com.lambency.lambency_client.Models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lshan on 2/8/2018.
 */

public class EventModel {

    String name, description, image, location;
    Date startTime, endTime;
    OrganizationModel hostOrganization;
    ArrayList<UserModel> usersAttending;
    ArrayList<HourModel> hoursWorked;

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public OrganizationModel getHostOrganization() {
        return hostOrganization;
    }

    public ArrayList<UserModel> getUsersAttending() {
        return usersAttending;
    }

    public ArrayList<HourModel> getHoursWorked() {
        return hoursWorked;
    }
}
