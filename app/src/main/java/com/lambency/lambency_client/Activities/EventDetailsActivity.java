package com.lambency.lambency_client.Activities;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.lambency.lambency_client.Models.EventModel;

import com.lambency.lambency_client.Networking.LambencyAPIHelper;

import com.lambency.lambency_client.Models.UserModel;

import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Utils.ImageHelper;
import com.lambency.lambency_client.Utils.TimeHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailsActivity extends AppCompatActivity {
    String eventName = "";


    private int event_id;
    private TextView text;

    //@BindView(R.id.createEventButton)
    //Button shareEventButton;

    @BindView(R.id.joinButtonText)
    TextView joinButText;

    @BindView(R.id.date)
    TextView dateView;

    @BindView(R.id.description)
    TextView descriptionView;

    @BindView(R.id.time)
    TextView timeView;

    @BindView(R.id.address)
    TextView addressView;

    @BindView(R.id.header)
    ImageView eventImageView;

    @BindView(R.id.whosAttending)
    Button whosAttendingButton;

    @BindView(R.id.editEventButton)
    Button editEventButton;

    private EventModel event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle("Event Title");
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        LinearLayout linearLayout = findViewById(R.id.joinButton);



        linearLayout.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                ImageView imageView = findViewById(R.id.check);
                text = findViewById(R.id.joinButtonText);
                if(text.getText().toString().equals("Join Event")){
                    LambencyAPIHelper.getInstance().getRegisterEvent(UserModel.myUserModel.getOauthToken(),event_id).enqueue(new retrofit2.Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if (response.body() == null || response.code() != 200) {
                                System.out.println("Error cause body returned null or bad response code in register response.");
                                Toast.makeText(getApplicationContext(), "Problem registering.", Toast.LENGTH_LONG).show();
                            }
                            //when response is back
                            Integer ret = response.body();
                            if(ret == 0){
                                System.out.println("successfully registerd for an event");
                                UserModel.myUserModel.registerForEvent(event_id);
                                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                                text.setText("Joined Event");
                            }
                            else if (ret == 1){
                                System.out.println("failed to find user or organization");
                                Toast.makeText(getApplicationContext(), "No Event to follow", Toast.LENGTH_LONG).show();
                            }
                            else if (ret == 2){
                                System.out.println("undetermined error");
                                Toast.makeText(getApplicationContext(), "Unknown Error", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Integer> call, Throwable throwable) {
                            //when failure
                            System.out.println("FAILED CALL");
                            Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "You cant actually unfollow. Sorry", Toast.LENGTH_LONG).show();
                    text.setText("Join Event");
                }
            }
        });
//
//        Button listUser = findViewById(R.id.reg);
//
//        listUser.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(EventDetailsActivity.this, ListUserActivity.class);
//                intent.putExtra("oauth",UserModel.myUserModel.getOauthToken());
//                intent.putExtra("event_id", event_id );
//                startActivity(intent);
//            }
//        });
        System.out.println(UserModel.myUserModel);
        System.out.println(event_id);
        boolean creator = UserModel.myUserModel.isRegisterdForEvent(event_id); // API CALL HERE
        if(creator) {
            whosAttendingButton.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }

        final Button shareButton = findViewById(R.id.shareEvent);

        shareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // if statement for user will go here

                // Start smsActivity.class
                /*Intent myIntent = new Intent(EventDetailsActivity.this,
                        smsActivity.class);
                startActivity(myIntent);*/
                shareIt();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            //TODO error check
            event_id = bundle.getInt("event_id");
            callRetrofit(event_id);
        }
    }

    private void setTitle(String title){
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle(title);
    }

    private void callRetrofit(final int event_id){
        LambencyAPIHelper.getInstance().getEventSearchByID(Integer.toString(event_id)).enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("ERROR!!!!!");
                }
                //when response is back
                EventModel eventModel= response.body();
                if(eventModel == null){
                    System.out.println("failed to event");
                }
                else{
                    System.out.println("Got event data!");

                    setTitle(eventModel.getName());
                    eventName = eventModel.getName();

                    dateView.setText(TimeHelper.dateFromTimestamp(eventModel.getStart()));
                    descriptionView.setText(eventModel.getDescription());
                    timeView.setText(TimeHelper.hourFromTimestamp(eventModel.getStart()) + " - " + TimeHelper.hourFromTimestamp(eventModel.getEnd()));
                    addressView.setText(eventModel.getLocation());

                    BitmapDrawable bd = new BitmapDrawable(getResources(), ImageHelper.stringToBitmap(eventModel.getImageFile()));
                    eventImageView.setBackground(bd);

                    if(! UserModel.myUserModel.getMyOrgs().contains(eventModel.getOrg_id())){
                        editEventButton.setVisibility(View.GONE);
                    }

                    event = eventModel;
                }
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable throwable) {
                //when failure
                System.out.println("FAILED CALL");
            }
        });
    }

    private void shareIt() {
    //sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = eventName + ", This is a cool event I found on Lambency and I think you will be interested in it.";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Lambency event shared");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
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

    @OnClick(R.id.whosAttending)
    public void handleWhosAttendingClick(){
        if(event != null){
            Bundle bundle = new Bundle();
            bundle.putInt("event_id", event.getEvent_id());
            Intent intent = new Intent(this, ListUserActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            System.out.println("Error - no event found.");
        }
    }

}
