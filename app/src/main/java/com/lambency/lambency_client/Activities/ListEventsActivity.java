package com.lambency.lambency_client.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.lambency.lambency_client.Adapters.EventsAdapter;
import com.lambency.lambency_client.Models.EventModel;
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

public class ListEventsActivity extends BaseActivity {

    @BindView(R.id.eventsRecyclerView)
    RecyclerView eventsRecyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private Context context;
    private EventsAdapter eventsAdapter;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);

        ButterKnife.bind(this);
        context = this;

        Bundle b = getIntent().getExtras();
        if(b != null){
            int orgId = b.getInt("org_id");
            getUpcomingEvents(orgId +  "");
        }

        getSupportActionBar().setTitle("Upcoming Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void getUpcomingEvents(String orgId){
        isLoading(true);

        LambencyAPIHelper.getInstance().getEventsByOrg(UserModel.myUserModel.getOauthToken(), orgId).enqueue(new Callback<List<EventModel>>() {
            @Override
            public void onResponse(Call<List<EventModel>> call, Response<List<EventModel>> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("Error getting org events.");
                    return;
                }
                //when response is back
                List<EventModel> list = response.body();
                if(list == null){
                    System.out.println("Org has no events or error has occurred");
                }
                else{
                    System.out.println("Got list of org events");

                    ArrayList<EventModel> events = new ArrayList<>(list);
                    startAdapter(events);
                }

                isLoading(false);
            }

            @Override
            public void onFailure(Call<List<EventModel>> call, Throwable throwable) {
                //when failure
                System.out.println("FAILED CALL");
            }
        });
    }

    private void startAdapter(List<EventModel> events){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        eventsRecyclerView.setLayoutManager(linearLayoutManager);
        eventsAdapter = new EventsAdapter(context, events);
        eventsRecyclerView.setAdapter(eventsAdapter);
    }

    private void isLoading(boolean isLoading){
        if(isLoading){
            progressBar.setVisibility(View.VISIBLE);
            eventsRecyclerView.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            eventsRecyclerView.setVisibility(View.VISIBLE);
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
