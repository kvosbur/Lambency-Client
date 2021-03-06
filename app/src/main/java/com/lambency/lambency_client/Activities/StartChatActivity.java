package com.lambency.lambency_client.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lambency.lambency_client.Adapters.StartChatAdapter;
import com.lambency.lambency_client.Adapters.UserListAdapter;
import com.lambency.lambency_client.Models.ChatModel;
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
    private ArrayList<ChatModel> chats;

    @BindView(R.id.relatedUsersRecyclerView)
    RecyclerView relatedUsersRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_chat);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;

        Bundle bund = getIntent().getExtras();
        chats = (ArrayList<ChatModel>)bund.getSerializable("chats");
        if(chats == null){
            chats = new ArrayList<>();
        }

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
                ArrayList<ChatModel> emptyChats = new ArrayList<>();
                for(int i = 0; i < chats.size(); i++){
                    if(chats.get(i).getRecent_msg_id() != 0) {
                        int id = chats.get(i).getUserID();
                        for (int j = 0; j < users.size(); j++) {
                            if (id == users.get(j).getUserId()) {
                                users.remove(j);
                                break;
                            }
                        }
                    }else{
                        emptyChats.add(chats.get(i));
                    }
                }

                startAdapter(users, emptyChats);

            }

            @Override
            public void onFailure(Call<ArrayList<UserModel>> call, Throwable t) {
                Log.e("Retrofit", "Get past event attendance error out");
            }
        });
    }

    private void startAdapter(ArrayList<UserModel> users, ArrayList<ChatModel> emptyChats){
        startChatAdapter = new StartChatAdapter(context, users, emptyChats);
        relatedUsersRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        relatedUsersRecyclerView.setAdapter(startChatAdapter);
    }

    public static ChatModel containsEmptyChat(ArrayList<ChatModel> emptyChats, int userID){

        for(ChatModel cm : emptyChats){
            if(cm.getUserID() == userID){
                return cm;
            }
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return true;
        }
    }
}
