package com.lambency.lambency_client.Networking;

import com.lambency.lambency_client.Models.UserAuthenticatorModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by kevin on 2/15/2018.
 */

public interface LambencyAPI {

    @GET("User/login/google")
    Call<UserAuthenticatorModel> getGoogleLogin(@Query("id") String id);

    @GET("User/login/facebook")
    Call<UserAuthenticatorModel> getFacebookLogin(@Query("id") String id, @Query("first") String first,
                                                  @Query("last") String last, @Query("email") String email);

}
