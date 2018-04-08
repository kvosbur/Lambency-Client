package com.lambency.lambency_client.Networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.Models.UserAuthenticatorModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kevin on 2/15/2018.
 */

public class LambencyAPIHelper {

    private static LambencyAPI lambencyAPI;

    public static LambencyAPI getInstance() {
        if(lambencyAPI == null) {
            Gson gson = new GsonBuilder().setLenient().create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://kevinvosburgh:20000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            lambencyAPI = retrofit.create(LambencyAPI.class);
        }
        return lambencyAPI;
    }


}
