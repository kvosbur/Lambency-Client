package com.lambency.lambency_client.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lambency.lambency_client.Fragments.ProfileFragment;
import com.lambency.lambency_client.Models.EventModel;
import com.lambency.lambency_client.Models.UserAuthenticatorModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPI;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardViewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    private ArrayList<EventModel> eventModels = new ArrayList<EventModel>();
    private ArrayList<Double> hoursOfEvents = new ArrayList<Double>();
    private boolean eventCheck = true;
    int userIdKey;
    String orgIdKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Intent myIntent = getIntent();
        userIdKey = myIntent.getIntExtra("userIdKey", 0);
        orgIdKey = myIntent.getStringExtra("orgIdKey");


        //TODO: call retrofit and give correct things to getdataset some way to differentiate to retrofits

        if (orgIdKey != null) {
            LambencyAPIHelper.getInstance().getPastEventsInOrg(UserAuthenticatorModel.myAuth, userIdKey + "", orgIdKey).enqueue(new Callback<ArrayList<EventModel>>() {
                @Override
                public void onResponse(Call<ArrayList<EventModel>> call, Response<ArrayList<EventModel>> response) {
                    if (response.body() == null || response.code() != 200) {
                        System.out.println("An error has occurred or the user has no past events or user is not admin of org");
                        // if null is given show that the user has no past events
                        eventCheck = false;
                        return;
                    }
                    //when response is back
                    ArrayList<EventModel> events = response.body();
                    for (EventModel eventModel : events) {
                        // i set hours in description
                        eventModels.add(eventModel);
                        double hours = Double.parseDouble(eventModel.getDescription());
                        hoursOfEvents.add(hours);
                        System.out.println("User worked " + hours + " hours at " + eventModel.getName());
                    }

                    mAdapter = new MyRecyclerViewAdapter(getDataSet());
                    mRecyclerView.setAdapter(mAdapter);
                    ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                            .MyClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Log.i(LOG_TAG, " Clicked on Item " + position);
                        }
                    });
                }

                @Override
                public void onFailure(Call<ArrayList<EventModel>> call, Throwable t) {
                    //when failure
                    System.out.println("FAILED CALL");
                }
            });
        } else {
            System.out.println(" in the profile one    !!!!!");
            LambencyAPIHelper.getInstance().getPastEvents(UserAuthenticatorModel.myAuth).enqueue(new Callback<ArrayList<EventModel>>() {
                @Override
                public void onResponse(Call<ArrayList<EventModel>> call, Response<ArrayList<EventModel>> response) {
                    if (response.body() == null || response.code() != 200) {
                        System.out.println("An error has occurred or the user has no past events");
                        // if null is given show that the user has no past events
                        eventCheck = false;
                        return;
                    }
                    //when response is back
                    ArrayList<EventModel> events = response.body();
                    for (EventModel eventModel : events) {
                        // i set hours in description
                        eventModels.add(eventModel);
                        double hours = Double.parseDouble(eventModel.getDescription());
                        hoursOfEvents.add(hours);
                        System.out.println("User worked " + hours + " hours at " + eventModel.getName());
                    }

                    mAdapter = new MyRecyclerViewAdapter(getDataSet());
                    mRecyclerView.setAdapter(mAdapter);
                    ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                            .MyClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Log.i(LOG_TAG, " Clicked on Item " + position);
                        }
                    });
                }

                @Override
                public void onFailure(Call<ArrayList<EventModel>> call, Throwable t) {
                    //when failure
                    System.out.println("FAILED CALL");
                }
            });
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(mAdapter != null) {
            ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                    .MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Log.i(LOG_TAG, " Clicked on Item " + position);
                }
            });
        }
    }

    private ArrayList<DataObject> getDataSet() {
        if (eventCheck == true && eventModels != null) {
            ArrayList results = new ArrayList<DataObject>();
            for (int index = 0; index < eventModels.size(); index++) {
                DataObject obj = new DataObject(eventModels.get(index).getName(),
                        "hours attended: " + hoursOfEvents.get(index));
                results.add(index, obj);
            }
            return results;
        } else {
            ArrayList results = new ArrayList<DataObject>();
            TextView topView = findViewById(R.id.topTextView);
            topView.setText("You attended 0 events");
            DataObject obj = new DataObject("You have not attended any events", "No hours to show");
            results.add(obj);
            return results;
        }
    }
}
