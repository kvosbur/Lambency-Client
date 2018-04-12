package com.lambency.lambency_client.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.lambency.lambency_client.Adapters.LeaderboardAdapter;
import com.lambency.lambency_client.Adapters.UserListAdapter;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPI;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LeaderboardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    List<UserModel> userList = new ArrayList<>();

    static int startVal = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        mRecyclerView = (RecyclerView) findViewById(R.id.leaderboardRecycle);

        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Leaderboard");

            LambencyAPIHelper.getInstance().getLeaderboardRange("" + startVal, "" + startVal+10).enqueue(new Callback<List<UserModel>>() {
                @Override
                public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                    if (response.body() == null || response.code() != 200) {
                        System.out.println("An error has occurred");
                        return;
                    }
                    //when response is back
                    List<UserModel> ret = response.body();
                    if(ret == null || ret.size() == 0) {
                        System.out.println("An error has occurred");
                    }
                    else{
                        UserModel userModel = ret.get(0);
                        int rank = Integer.parseInt(userModel.getOauthToken());
                        // I will set the oAuthToken to the users rank
                        startVal += 10;

                        //TODO Populate recycler view here!
                    }
                }

                @Override
                public void onFailure(Call<List<UserModel>> call, Throwable throwable) {
                    //when failure
                    System.out.println("FAILED CALL");
                }
            });

        //TODO If user is not in current userList then...
        LambencyAPIHelper.getInstance().getLeaderboardAroundUser(UserModel.myUserModel.getOauthToken()).enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("An error has occurred");
                    return;
                }
                //when response is back
                List<UserModel> ret = response.body();
                if(ret == null ) {
                    System.out.println("An error has occurred");
                }
                else{
                    UserModel userModel = ret.get(0);
                    int rank = Integer.parseInt(userModel.getOauthToken());
                    // I will set the oAuthToken to the users rank
                    //TODO Populate recycler view here!
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable throwable) {
                //when failure
                System.out.println("FAILED CALL");
            }
        });

        userList.add(UserModel.myUserModel);
        userList.add(new UserModel("...", null, null, null, null, null,null, 0, 0, null));

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new LeaderboardAdapter(userList, this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged(); // how we update

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //TODO retrofit here
    private void callRetrofit(int event_id){

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
