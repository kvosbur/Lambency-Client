package com.lambency.lambency_client.Networking;

import com.lambency.lambency_client.Models.EventModel;
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


}
