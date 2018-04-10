package com.lambency.lambency_client.Networking;


import com.lambency.lambency_client.Models.EventFilterModel;

import com.lambency.lambency_client.Models.EventAttendanceModel;

import com.lambency.lambency_client.Models.EventModel;
import com.lambency.lambency_client.Models.MyLambencyModel;
import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.Models.UserAuthenticatorModel;
import com.lambency.lambency_client.Models.UserModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by kevin on 2/15/2018.
 */

public interface LambencyAPI {

    @GET("User/login/google")
    Call<UserAuthenticatorModel> getGoogleLogin(@Query("idToken") String idToken);

    @GET("User/login/facebook")
    Call<UserAuthenticatorModel> getFacebookLogin(@Query("id") String id, @Query("first") String first,
                                                  @Query("last") String last, @Query("email") String email);

    @POST("Organization/create")
    Call<OrganizationModel> postCreateOrganization(@Body OrganizationModel org);

    @GET("User/search")
    Call<UserModel> userSearch(@Query("oAuthToken") String oAuthToken, @Query("id") String userID);


    @GET("Organization/search")
    Call<ArrayList<OrganizationModel>> getOrganizationSearch(@Query("name") String name);

    @GET("Event/search")
    Call<List<EventModel>> getEventsWithParams(@Query("lat") double lat, @Query("long") double longitude,
                                               @Query("name") String name, @Query("org_idStr") String org_idStr);

    @POST("User/changeInfo")
    Call<UserModel> getChangeAccountInfo(@Body UserModel u);

    @GET("Event/searchByID")
    Call<EventModel> getEventSearchByID(@Query("id") String event_id);

    @POST("Event/create")
    Call<EventModel> createEvent(@Body EventModel eventModel);

    @GET("Event/numAttending")
    Call<Integer> getEventNumAttending(@Query("oAuthCode") String oAuthCode, @Query("id") String event_id);

    @GET("Organization/searchByID")
    Call<OrganizationModel> getOrgSearchByID(@Query("id") String org_id);

    @POST("User/requestJoinOrg")
    Call<Integer> postJoinOrganization(@Query("oAuthCode") String oAuthCode, @Query("orgId") int orgID);

    @POST("User/leaveOrg")
    Call<Integer> postLeaveOrganization(@Query("oAuthCode") String oAuthCode, @Query("orgID") int orgID);

    @GET("User/followOrg")
    Call<Integer> getFollowOrg(@Query("oAuthCode") String oAuthCode, @Query("orgID") String orgID);

    @GET("User/unfollowOrg")
    Call<Integer> getUnfollowOrg(@Query("oAuthCode") String oAuthCode, @Query("org_id") String orgID);

    @GET("Event/users")
    Call<ArrayList<UserModel>> getListOfUsers(@Query("oauthcode") String oAuthCode, @Query("event_id") int eventId);

    @POST("Event/update")
    Call<Integer> postUpdateEvent(@Body EventModel event);

    @GET("/User/registerForEvent")
    Call<Integer> getRegisterEvent(@Query("oAuthCode") String oAuthCode, @Query("eventID") String eventID);

    @GET("/User/unregisterForEvent")
    Call<Integer> unRegisterForEvent(@Query("oAuthCode") String oAuthCode, @Query("eventID") String eventID);

    @GET("/Event/searchByIDs")
    Call<List<EventModel>> getUserEvents(@Query("oAuthCode") String oAuthCode, @Query("userID") int userID);

    @GET("Organization/events")
    Call<List<EventModel>> getEventsByOrg(@Query("oAuthCode") String oAuthCode, @Query("id") String org_id);

    @GET("Organization/joinRequests")
    Call<ArrayList<UserModel>> getRequestsToJoin(@Query("oAuthCode") String oAuthCode, @Query("orgID") int org_id);

    @GET("Organization/endorse")
    Call<Integer> getEndorse(@Query("oAuthCode") String oAuthCode, @Query("orgID") String org_id, @Query("eventID") String event_id);

    @GET("Organization/unendorse")
    Call<Integer> getUnendorse(@Query("oAuthCode") String oAuthCode, @Query("orgID") String org_id, @Query("eventID") String event_id);

    @GET("Event/endorsedOrgs")
    Call<List<OrganizationModel>> getEndorsedOrgs(@Query("oAuthCode") String oAuthCode, @Query("eventId") String eventID);

    @GET("User/MyLambency")
    Call<MyLambencyModel> getMyLambencyModel(@Query("oAuthCode") String oAuthCode);

    @POST("Event/searchWithFilter")
    Call<List<EventModel>> getEventsFromFilter(@Body EventFilterModel efm, @Query("oAuthCode") String oAuthCode);

    @GET("Organization/getMembersAndOrganizers")
    Call<ArrayList<UserModel>[]> getMembersAndOrganizers(@Query("oAuthCode") String oAuthCode, @Query("orgID") int orgID);

    @GET("User/eventsFeed")
    Call<List<EventModel>> getEventsFeed(@Query("oAuthCode") String oAuthCode, @Query("latitude") String latitude, @Query("longitude") String longitude);

    @POST("Organization/InviteUser")
    Call<Integer> inviteUser(@Query("oAuthCode") String oAuthCode, @Query("orgID") String orgID, @Query("emailString") String userEmail);

    @GET("User/getOrgs")
    Call<ArrayList<OrganizationModel>> getMyOrganizedOrgs(@Query("oAuthCode") String oAuthCode);

    @GET("/Organization/changeUserPermissions")
    Call<Integer> getChangeUserPermissions(@Query("oAuthCode") String oAuthCode, @Query("orgID") String org_id, @Query("userChanged") String changedID, @Query("type") String type);

    @GET("Organization/respondToJoinRequest")
    Call<Integer> respondToJoinRequest(@Query("oAuthCode") String oAuthCode, @Query("orgID") int orgID, @Query("userID") int userID, @Query("approved") boolean
            approved);

    @POST("/User/ClockInOut")
    Call<Integer> sendClockInCode(@Query("oAuthCode") String oAuthCode, @Body EventAttendanceModel eventAttendanceModel);

    @GET("/User/register")
    Call<Integer> registerUser(@Query("email") String email, @Query("first") String firstName, @Query("last") String LastName,
                               @Query("passwd") String password);

    @POST("/User/verifyEmail")
    Call<Integer> verifyEmail(@Query("userID") int userid, @Query("code") String verificationCode);

    @GET("/User/login/lambency")
    Call<UserAuthenticatorModel> loginUser(@Query("email") String email, @Query("password") String password);

    @POST("/User/changePassword")
    Call<Integer> changePassword(@Query("newPassword") String password, @Query("confirmPassword") String confirmPass,
                                 @Query("oAuthToken") String oAuthToken, @Query("oldPassword") String oldPassword);

    @POST("/User/beginRecovery")
    Call<Integer> beginPasswordRecovery(@Query("email") String email);

    @POST("/User/endRecovery")
    Call<Integer> endPasswordRecovery(@Query("newPassword") String password, @Query("confirmPassword") String confirmPass,
                                 @Query("verification") String oAuthToken, @Query("userID") int userID);



    @GET("Event/deleteEvent")
    Call<Integer> getDeleteEvent(@Query("oAuthCode") String oAuthCode, @Query("eventID") String eventID, @Query("message") String message);

    @POST("User/setFirebase")
    Call<Integer> setFirebaseCode(@Query("oAuthCode") String oAuthCode, @Query("firebase") String fireBaseCode);

    @GET("User/leaderboardRange")
    Call<List<UserModel>> getLeaderboardRange(@Query("start") String start, @Query("end") String end);

    @GET("User/leaderboardAroundUser")
    Call<List<UserModel>> getLeaderboardAroundUser(@Query("oAuthCode") String oAuthCode);

    @GET("User/setNotificationPreference")
    Call<Integer> updateNotificationPreference(@Query("oAuthCode") String oAuthCode, @Query("preference") int preference);
}

