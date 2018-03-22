package com.lambency.lambency_client.Networking;

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

    @GET("Organization/searchByID")
    Call<OrganizationModel> getOrgSearchByID(@Query("id") String org_id);

    @POST("User/requestJoinOrg")
    Call<Integer> postJoinOrganization(@Query("oAuthCode") String oAuthCode, @Query("orgId") int orgID);

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

    @GET("/Event/searchByIDs")
    Call<List<EventModel>> getUserEvents(@Query("oAuthCode") String oAuthCode, @Query("userID") int userID);

    @GET("Organization/events")
    Call<List<EventModel>> getEventsByOrg(@Query("oAuthCode") String oAuthCode, @Query("id") String org_id);

    @GET("User/MyLambency")
    Call<MyLambencyModel> getMyLambencyModel(@Query("oAuthCode") String oAuthCode);

    @GET("Organization/getMembersAndOrganizers")
    Call<ArrayList<UserModel>[]> getMembersAndOrganizers(@Query("oAuthCode") String oAuthCode, @Query("orgID") int orgID);

    @GET("User/eventsFeed")
    Call<List<EventModel>> getEventsFeed(@Query("oAuthCode") String oAuthCode, @Query("latitude") String latitude, @Query("longitude") String longitude);

    @POST("Organization/InviteUser")
    Call<Integer> inviteUser(@Query("oAuthCode") String oAuthCode, @Query("orgID") String orgID, @Query("emailString") String userEmail);
}

