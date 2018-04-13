package com.lambency.lambency_client.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.lambency.lambency_client.Adapters.StartChatAdapter;
import com.lambency.lambency_client.Adapters.UserListAdapter;
import com.lambency.lambency_client.Models.EventAttendanceModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPI;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartChatActivity extends AppCompatActivity{

    private Context context;
    private int event_id;
    private StartChatAdapter startChatAdapter;

    @BindView(R.id.relatedUsersRecyclerView)
    RecyclerView relatedUsersRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_chat);

        ButterKnife.bind(this);

        context = this;

        getUsers();
    }


    private void getUsers(){
        LambencyAPIHelper.getInstance().getRelatedUsers(UserModel.myUserModel.getOauthToken()).enqueue(new Callback<ArrayList<UserModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserModel>> call, Response<ArrayList<UserModel>> response) {
                if(response.body() == null){
                    Log.e("Retrofit", "Get past event attendance returned null");
                    return;
                }

                ArrayList<UserModel> users = response.body();

                startAdapter(users);

            }

            @Override
            public void onFailure(Call<ArrayList<UserModel>> call, Throwable t) {
                Log.e("Retrofit", "Get past event attendance error out");
            }
        });
    }

    private void startAdapter(ArrayList<UserModel> users){
        startChatAdapter = new StartChatAdapter(context, users);
        relatedUsersRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        relatedUsersRecyclerView.setAdapter(startChatAdapter);
    }
}
