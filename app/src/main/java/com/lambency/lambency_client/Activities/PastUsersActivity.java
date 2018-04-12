package com.lambency.lambency_client.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

public class PastUsersActivity extends AppCompatActivity {

    private Context context;
    private int event_id;
    private UserListAdapter userListAdapter;

    @BindView(R.id.usersRecyclerView)
    RecyclerView usersRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_users);

        ButterKnife.bind(this);

        context = this;

        event_id = getIntent().getIntExtra("event_id", -1);
        if(event_id == -1){
            Log.e("PastUsersActivity", "Error passing event id");
        }else {
            getUsers();
        }

    }


    private void getUsers(){
        LambencyAPIHelper.getInstance().getPastEventAttendence(UserModel.myUserModel.getOauthToken(), event_id).enqueue(new Callback<ArrayList<EventAttendanceModel>>() {
            @Override
            public void onResponse(Call<ArrayList<EventAttendanceModel>> call, Response<ArrayList<EventAttendanceModel>> response) {
                if(response.body() == null){
                    Log.e("Retrofit", "Get past event attendance returned null");
                    return;
                }

                ArrayList<EventAttendanceModel> attendanceModels = response.body();
                ArrayList<UserModel> users = new ArrayList<>();
                for(EventAttendanceModel model : attendanceModels){
                    users.add(model.getUserModel());
                }

                startAdapter(users);

            }

            @Override
            public void onFailure(Call<ArrayList<EventAttendanceModel>> call, Throwable t) {
                Log.e("Retrofit", "Get past event attendance error out");
            }
        });
    }

    private void startAdapter(ArrayList<UserModel> users){
        userListAdapter = new UserListAdapter(context, users);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        usersRecyclerView.setAdapter(userListAdapter);
    }


}
