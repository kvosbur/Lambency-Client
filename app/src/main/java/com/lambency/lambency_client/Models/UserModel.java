package com.lambency.lambency_client.Models;

import java.sql.SQLException;
import java.util.List;

public class UserModel {

    private String firstName;
    private String lastName;
    private String email;
    private List<Integer> myOrgs;
    private List<Integer> eventsAttending;
    private List<Integer> followingOrgs;
    private List<Integer> joinedOrgs;
    private int userId;
    private int hoursWorked;
    private String oauthToken;

    public static UserModel myUserModel;


    public UserModel(String firstName, String lastName, String email, List<Integer> myOrgs, List<Integer> eventsAttending,
                List<Integer> followingOrgs, List<Integer> joinedOrgs, int userId, int hoursWorked, String oauthToken) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.myOrgs = myOrgs;
        this.eventsAttending = eventsAttending;
        this.followingOrgs = followingOrgs;
        this.joinedOrgs = joinedOrgs;
        this.userId = userId;
        this.hoursWorked = hoursWorked;
        this.oauthToken = oauthToken;

    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMyOrgs(List<Integer> myOrgs) {
        this.myOrgs = myOrgs;
    }

    public void setEventsAttending(List<Integer> eventsAttending) {
        this.eventsAttending = eventsAttending;
    }

    public void setFollowingOrgs(List<Integer> followingOrgs) {
        this.followingOrgs = followingOrgs;
    }

    public void setJoinedOrgs(List<Integer> joinedOrgs) {
        this.joinedOrgs = joinedOrgs;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public String getFirstName() {

        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<Integer> getMyOrgs() {
        return myOrgs;
    }

    public List<Integer> getEventsAttending() {
        return eventsAttending;
    }

    public List<Integer> getFollowingOrgs() {
        return followingOrgs;
    }

    public List<Integer> getJoinedOrgs() {
        return joinedOrgs;
    }

    public int getUserId() {
        return userId;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public String getOauthToken() {
        return oauthToken;
    }


    public String toString(){
        /*
        this.hoursWorked = hoursWorked;
         */

        String result = "";
        result += "USER: " + userId + "\n";
        result += "First Name: " + firstName + "\n";
        result += "Last Name: " + lastName + "\n";
        result += "Email: " + email + "\n";
        result += "Hours Worked: " + hoursWorked + "\n";

        if(myOrgs != null){
            result += "My Orgs: ";
            for(Integer i: myOrgs){
                result += i + " ";
            }
            result += "\n";
        }else{
            result += "My Orgs: None\n";
        }

        if(eventsAttending != null){
            result += "Events Attending: ";
            for(Integer i: eventsAttending){
                result += i + " ";
            }
            result += "\n";
        }else{
            result += "Events Attending: None\n";
        }

        if(followingOrgs != null){
            result += "Organizations Following: ";
            for(Integer i: followingOrgs){
                result += i + " ";
            }
            result += "\n";
        }else{
            result += "Organizations Following: None\n";
        }

        if(joinedOrgs != null){
            result += "Organizations Joined: ";
            for(Integer i: joinedOrgs){
                result += i + " ";
            }
            result += "\n";
        }else{
            result += "Organizations Joined: None\n";
        }

        return result;
    }
    /**
     *
     * @param oAuthCode oAuthCode for the user
     * @param orgID id of organization to be followed
     * @return returns 0 on success, 1 if not able to find user or org, 2 on SQLException
     */
//    public static Integer followOrg(String oAuthCode, String orgID){
//        try {
//            //search for user
//            if(LambencyServer.dbc.searchForUser(oAuthCode) == null){
//                System.out.println("User not found");
//                return 1;
//            }
//            //search for organization by ID
//            Organization org = null;//Organization org = LambencyServer.dbc.searchForOrg(orgID);
//            if (org != null) {
//                //if org is found, set to follow in database
//                //LambencyServer.dbc.setFollowing(this.oauthToken, orgID);
//                //
//                return 0;
//            } else {
//                //org is not found, return error
//                System.out.println("Organization not found");
//                return 1;
//            }
//        }
//        catch (SQLException e){
//            System.out.println("SQLExcpetion");
//            return 2;
//        }
//    }
}
