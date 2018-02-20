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
                    .baseUrl("http://kevinv.dlinkddns.com:20000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            lambencyAPI = retrofit.create(LambencyAPI.class);
        }
        return lambencyAPI;
    }

    public void createOrganizationRetrofit(OrganizationModel org){

        LambencyAPIHelper.getInstance().postCreateOrganization(org).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("ERROR!!!!!");
                }
                //when response is back
                Integer status = response.body();
                System.out.println(status);
                if(status == 0){
                    //System.out.println("SUCCESS");
                }
                else if(status == 1){
                    //System.out.println("BAD USER ID");
                }
                else if(status == 2){
                    //System.out.println("NON DETERMINANT ERROR");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable throwable) {
                //when failure
                System.out.println("FAILED CALL");
            }
        });
    }

}
