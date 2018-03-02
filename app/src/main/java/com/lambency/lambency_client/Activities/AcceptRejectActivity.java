package com.lambency.lambency_client.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.widget.TextView;

import com.lambency.lambency_client.Adapters.AcceptRejectAdapter;
import com.lambency.lambency_client.Adapters.UserListAdapter;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptRejectActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private AcceptRejectAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ItemTouchHelper itemTouchHelper;

    List<UserModel> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_reject);
        mRecyclerView = (RecyclerView) findViewById(R.id.acceptRejectRecycle);

        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Pending Requests");

        /*
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            //TODO error check
            int event_id = bundle.getInt("event_id");
            callRetrofit(event_id);
        }
        */

        userList.add(new UserModel("Evan", "Honeysett", "ehoneyse@purdue.edu", null, null, null, null, 0, 0, ""));

        //itemTouchHelper.attachToRecyclerView(mRecyclerView);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new AcceptRejectAdapter(userList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged(); // how we update

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void callRetrofit(int event_id){
        LambencyAPIHelper.getInstance().getListOfUsers(UserModel.myUserModel.getOauthToken(), event_id).enqueue(new Callback<ArrayList<UserModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserModel>> call, Response<ArrayList<UserModel>> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("ERROR!!!!!");
                }

                ArrayList<UserModel> userList = response.body();

                if(userList == null)
                {
                    userList = new ArrayList<UserModel>();
                }

                if (userList.size() == 0) {
                    System.out.println("No users attending");
                } else {
                    System.out.println("Users found");
                }

                //numAttendingView.setText("Looks like " + userList.size() + " people are coming!");

                mAdapter.updateUserList(userList);
            }

            @Override
            public void onFailure(Call<ArrayList<UserModel>> call, Throwable throwable) {
                //when failure
                System.out.println("FAILED CALL");
            }
        });
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
