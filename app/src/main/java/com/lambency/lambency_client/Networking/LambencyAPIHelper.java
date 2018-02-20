package com.lambency.lambency_client.Networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
                    .baseUrl("http://kevinv.dlinkddns.com:20000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            lambencyAPI = retrofit.create(LambencyAPI.class);
        }
        return lambencyAPI;
    }
}