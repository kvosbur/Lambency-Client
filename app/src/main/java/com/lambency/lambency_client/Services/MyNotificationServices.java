package com.lambency.lambency_client.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.lambency.lambency_client.Activities.UserAcceptRejectActivity;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.Utils.SharedPrefsHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lshan on 4/5/2018.
 */

public class MyNotificationServices extends IntentService{

    public static final String JOIN_REQUEST = "joinRequest";
    public static final String ORG_INVITE = "orgInvite";

    // This service is used to handle actions a notification may need to do when the app is closed

    public MyNotificationServices(){
        super("MyNotificationServices");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final String action = intent.getAction();

        int id, user_id, org_id;
        NotificationManager notificationManager;

        switch(action){

            case JOIN_REQUEST:

                //Dismiss the notification
                id = intent.getIntExtra("notif_id", 0);
                notificationManager = (NotificationManager) getApplicationContext()
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(id);

                //Send the join request
                boolean approved = intent.getBooleanExtra("approved", false);
                System.out.println("Approved: " + approved);
                user_id = Integer.parseInt(intent.getStringExtra("user_id"));
                org_id = Integer.parseInt(intent.getStringExtra("org_id"));
                sendJoinRequestRetrofit(approved, user_id, org_id);
                break;


            case ORG_INVITE:

                //Dismiss the notification
                id = intent.getIntExtra("notif_id", 0);
                notificationManager = (NotificationManager) getApplicationContext()
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(id);

                boolean joined = intent.getBooleanExtra("joined", false);
                System.out.println("Joined:" + joined);
                org_id = Integer.parseInt(intent.getStringExtra("org_id"));
                sendInviteResponse(joined, org_id);
                break;

            default:
                throw new IllegalArgumentException("Unsupported action: " + action);
        }
    }

    private void sendJoinRequestRetrofit(final boolean approved, int user_id, int org_id){
        String authToken = SharedPrefsHelper.getSharedPrefs(getApplicationContext()).getString("myauth", "no auth token found!");
        LambencyAPIHelper.getInstance().respondToJoinRequest(authToken, org_id,
                user_id, approved).enqueue(new Callback<Integer>() {

            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("ERROR!!!!!");
                }

                Integer ret = response.body();

                if(ret == -1)
                {
                    //there was an error
                    Toast.makeText(getApplicationContext(), "There was an error with request", Toast.LENGTH_LONG).show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable throwable) {
                //when failure
                System.out.println("FAILED CALL");
            }
        });
    }

    private void sendInviteResponse(final boolean accepted, int orgId){
        LambencyAPIHelper.getInstance().getUserRespondToJoinRequest(SharedPrefsHelper.getOauth(getApplicationContext()), "" + orgId, "" + accepted).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("An error has occurred");
                    return;
                }
                //when response is back
                Integer ret = response.body();
                if(ret == -1 ) {
                    Toast.makeText(getApplicationContext(), "An error has occured.", Toast.LENGTH_SHORT).show();
                }
                else if (ret == 0) {
                    System.out.println("Successfully joined");
                }
                else if (ret == 1) {
                    System.out.println("Successfully ignored");
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
