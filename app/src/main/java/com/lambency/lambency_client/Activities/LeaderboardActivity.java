package com.lambency.lambency_client.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lambency.lambency_client.Adapters.LeaderboardAdapter;
import com.lambency.lambency_client.Adapters.UserListAdapter;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPI;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private static LeaderboardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    static List<UserModel> userList = new ArrayList<>();

    @BindView(R.id.leaderboardPos)
    TextView leaderboardPos;

    static int startVal = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        mRecyclerView = (RecyclerView) findViewById(R.id.leaderboardRecycle);

        startVal = 1;
        userList = new ArrayList<>();

        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Leaderboard");

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new LeaderboardAdapter(userList, this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged(); // how we update

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LambencyAPIHelper.getInstance().getLeaderboardRange("" + startVal, "" + startVal).enqueue(new Callback<List<UserModel>>() {
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
                        Toast.makeText(LeaderboardActivity.this, "" + ret.size(), Toast.LENGTH_SHORT).show();
                        // I will set the oAuthToken to the users rank
                        startVal += ret.size();

                        for(int i = 0; i < ret.size(); i++)
                        {
                            userList.add(ret.get(i));
                        }

                        userList.add(new UserModel("...", null, null, null, null, null,null, 0, 0, null));

                        mAdapter.notifyDataSetChanged(); // how we update

                        //TODO Populate recycler view here!

                        if(!userList.contains(UserModel.myUserModel))
                        {
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

                                        for(int i = 0; i < ret.size(); i++)
                                        {
                                            userList.add(ret.get(i));
                                        }

                                    }
                                }

                                @Override
                                public void onFailure(Call<List<UserModel>> call, Throwable throwable) {
                                    //when failure
                                    System.out.println("FAILED CALL");
                                }
                            });
                        }

                        for(int i = 0; i < userList.size(); i++)
                        {
                            if(userList.get(i).getUserId() == UserModel.myUserModel.getUserId())
                            {
                                leaderboardPos.setText("You are rank " + userList.get(i).getOauthToken() + " in the world!");
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<List<UserModel>> call, Throwable throwable) {
                    //when failure
                    System.out.println("FAILED CALL");
                }
            });


    }

    //TODO retrofit here
    private void callRetrofit(int event_id){

    }

    public static void update() {
        userList.remove(userList.size()-1);
        mAdapter.notifyDataSetChanged(); // how we update
        startVal-=1;

        LambencyAPIHelper.getInstance().getLeaderboardRange("" + startVal, "" + startVal+10).enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("An error has occurred");
                    return;
                }
                //when response is back
                List<UserModel> ret = response.body();
                if (ret == null || ret.size() == 0) {
                    System.out.println("An error has occurred");
                } else {
                    UserModel userModel = ret.get(0);
                    int rank = Integer.parseInt(userModel.getOauthToken());
                    //Toast.makeText(LeaderboardActivity.this, "" + ret.size(), Toast.LENGTH_SHORT).show();
                    // I will set the oAuthToken to the users rank
                    startVal += ret.size();

                    for (int i = 0; i < ret.size(); i++) {
                        userList.add(ret.get(i));
                    }

                    userList.add(new UserModel("...", null, null, null, null, null, null, 0, 0, null));

                    for(int i = 0; i < userList.size(); i++)
                    {
                        UserModel u = userList.get(i);
                        if(u.getFirstName().compareTo("...") == 0 && i != userList.size()-1)
                        {
                            userList.remove(i);
                            i--;
                        }
                    }

                    mAdapter.notifyDataSetChanged(); // how we update
                }
            }
            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable throwable) {
                //when failure
                System.out.println("FAILED CALL");
            }
        });

        for(int i = 0; i < userList.size(); i++)
        {
            UserModel u = userList.get(i);
            if(u.getFirstName().compareTo("...") == 0 && i != userList.size()-1)
            {
                userList.remove(i);
                i--;
            }
        }
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
