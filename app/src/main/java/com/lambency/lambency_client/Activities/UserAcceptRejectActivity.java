package com.lambency.lambency_client.Activities;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lambency.lambency_client.Adapters.AcceptRejectAdapter;
import com.lambency.lambency_client.Adapters.UserAcceptRejectAdapter;
import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPI;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Utils.SwipeController;
import com.lambency.lambency_client.Utils.SwipeControllerActions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Evan on 3/31/2018.
 */

public class UserAcceptRejectActivity  extends BaseActivity {

    private RecyclerView mRecyclerView;
    private UserAcceptRejectAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ItemTouchHelper itemTouchHelper;
    private SwipeController swipeController;
    private int org_id;
    public int position;

    @BindView(R.id.noCurrentRequests)
    TextView currRequests;

    List<OrganizationModel> organizationModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_reject);
        mRecyclerView = (RecyclerView) findViewById(R.id.acceptRejectRecycle);

        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Your Requests");

        //userList.add(new UserModel("Evan", "Honeysett", "ehoneyse@purdue.edu", null, null, null, null, 0, 0, ""));
        //userList.add(new UserModel("Barack", "Obama", "potus@wh.gov", null, null, null, null, 0, 0, ""));
        //organizationModelList.add(new OrganizationModel(UserModel.myUserModel, "Test Org", "123", 100, "Cool", "a@a.com", UserModel.myUserModel, ""));

        //TODO Initial Retrofit here for getting requests
        LambencyAPIHelper.getInstance().getUserJoinRequests("" + UserModel.myUserModel.getOauthToken()).enqueue(new Callback<List<OrganizationModel>>() {
            @Override
            public void onResponse(Call<List<OrganizationModel>> call, Response<List<OrganizationModel>> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("An error has occurred");
                    return;
                }
                //when response is back
                List<OrganizationModel> ret = response.body();
                if(ret == null ) {
                    Toast.makeText(UserAcceptRejectActivity.this, "Null", Toast.LENGTH_SHORT).show();
                    System.out.println("An error has occurred");
                }
                else {
                    for(int i = 0; i < ret.size(); i++)
                    {
                        organizationModelList.add(ret.get(i));
                    }

                    mAdapter.notifyDataSetChanged(); // how we update
                    if(organizationModelList.size() == 0) {
                        currRequests.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<OrganizationModel>> call, Throwable throwable) {
                //when failure
                System.out.println("FAILED CALL");
            }
        });

        setupRecyclerView();
        //SwipeController swipeController = new SwipeController();
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                callRetrofit(true,position);
                Toast.makeText(getApplicationContext(), "Accepted invite!", Toast.LENGTH_LONG).show();

            }

            public void onLeftClicked(int position) {
                callRetrofit(false,position);
                Toast.makeText(getApplicationContext(), "Rejected invite!", Toast.LENGTH_LONG).show();
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(mRecyclerView);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new UserAcceptRejectAdapter(organizationModelList, this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged(); // how we update

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public int getCardPosition(){
        return this.position;
    }

    //TODO Update retrofit here for adding accept and reject
    private void callRetrofit(final boolean accepted, final int position){
        int orgId = organizationModelList.get(position).getOrgID();
        LambencyAPIHelper.getInstance().getUserRespondToJoinRequest("" + UserModel.myUserModel.getOauthToken(), "" + orgId, "" + accepted).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("An error has occurred");
                    return;
                }
                //when response is back
                Integer ret = response.body();
                if(ret == -1 ) {
                    Toast.makeText(UserAcceptRejectActivity.this, "An error has occured.", Toast.LENGTH_SHORT).show();
                }
                else if (ret == 0) {
                    Toast.makeText(UserAcceptRejectActivity.this, "Successfully joined.", Toast.LENGTH_SHORT).show();
                    organizationModelList.remove(position);
                    mAdapter.notifyDataSetChanged();
                }
                else if (ret == 1) {
                    Toast.makeText(UserAcceptRejectActivity.this, "Successfully rejected.", Toast.LENGTH_SHORT).show();
                    organizationModelList.remove(position);
                    mAdapter.notifyDataSetChanged();
                }

            }
            @Override
            public void onFailure(Call<Integer> call, Throwable throwable) {
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

    private void setupRecyclerView() {

        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }
}
