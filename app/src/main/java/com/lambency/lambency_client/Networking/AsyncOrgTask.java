package com.lambency.lambency_client.Networking;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.lambency.lambency_client.Activities.BottomBarActivity;
import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.Models.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lshan on 4/12/2018.
 */

public class AsyncOrgTask extends AsyncTask<Void, Void, Void> {

    public static final int CREATE_MODE = 0;
    public static final int EDIT_MODE = 1;

    private OrganizationModel organizationModel;
    private Context context;
    private String message;
    private int mode;

    public AsyncOrgTask(OrganizationModel organizationModel, Context context, int mode) {
        this.organizationModel = organizationModel;
        this.context = context;
        this.mode = mode;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if(mode == CREATE_MODE) {
            createOrganization(organizationModel);
        }

        if(mode == EDIT_MODE){
            updateOrganization(organizationModel);
        }

        return null;
    }


    private void createOrganization(OrganizationModel orgModel){
        LambencyAPIHelper.getInstance().postCreateOrganization(organizationModel).enqueue(new retrofit2.Callback<OrganizationModel>() {
            @Override
            public void onResponse(Call<OrganizationModel> call, Response<OrganizationModel> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("ERROR!!!!!");
                    Toast.makeText(context, "Error With Server", Toast.LENGTH_SHORT).show();
                    return;
                }
                //when response is back
                OrganizationModel org = response.body();
                int org_id = org.getOrgID();
                UserModel.myUserModel.organizeGroup(org_id);

                if(org.name == null)
                {
                    Toast.makeText(context, "That name is already taken", Toast.LENGTH_SHORT).show();
                    return;
                }


                //What is this?? Do we need it?
                OrganizationModel.myOrgModel = organizationModel;
            }

            @Override
            public void onFailure(Call<OrganizationModel> call, Throwable throwable) {
                //when failure
                System.out.println("FAILED CALL");
            }
        });
    }

    private void updateOrganization(OrganizationModel organizationModel){
        LambencyAPIHelper.getInstance().getEditOrganization(UserModel.myUserModel.getOauthToken(), organizationModel).enqueue(new Callback<OrganizationModel>() {
            @Override
            public void onResponse(Call<OrganizationModel> call, Response<OrganizationModel> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("failed to update org or invalid permissions");
                }
                //when response is back
                OrganizationModel organization= response.body();
                if(organization == null){
                    System.out.println("failed to update organization");
                }
                else{
                    System.out.println("updated org: " + organization.getName());
                }
            }

            @Override
            public void onFailure(Call<OrganizationModel> call, Throwable throwable) {
                //when failure
                System.out.println("FAILED CALL (update organization)");
            }
        });
    }

}
