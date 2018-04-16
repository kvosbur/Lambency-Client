package com.lambency.lambency_client.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.lambency.lambency_client.Adapters.OrgSpinnerAdapter;
import com.lambency.lambency_client.Adapters.OrganizationAdapter;
import com.lambency.lambency_client.Models.EventModel;

import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.Networking.LambencyAPI;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;

import com.lambency.lambency_client.Models.UserModel;

import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Utils.ImageHelper;
import com.lambency.lambency_client.Utils.TimeHelper;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class EventDetailsActivity extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    String eventName = "";

    private final int WRITE_EXTERNAL_STORAGE = 0;

    private int event_id;
    private TextView text;
    private LinearLayout linearLayout;


    @BindView(R.id.endorseText)
    TextView endorseText;

    @BindView(R.id.endorseButton)
    Button endorseButton;

    @BindView(R.id.contentLayout)
    FrameLayout contentLayout;

    @BindView(R.id.mainLayout)
    CoordinatorLayout mainLayout;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

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

    @BindView(R.id.organizationImage)
    CircleImageView orgImageView;

    @BindView(R.id.hostOrgTitle)
    TextView orgTitleView;

    @BindView(R.id.organizationLayout)
    RelativeLayout orgLayout;

    @BindView(R.id.check)
    ImageView checkMark;

    @BindView(R.id.numPeopleAttending)
    TextView numberOfPeopleAttending;

    @BindView(R.id.orgEndorseList)
    RecyclerView orgEndorseList;

    @BindView(R.id.endorseLayout)
    LinearLayout endorseLinLayout;

    @BindView(R.id.shareEvent)
    Button shareButton;

    @BindView(R.id.joinButton)
    LinearLayout joinButton;

    @BindView(R.id.orgListLabel)
    TextView orgListLabel;

    @BindView(R.id.checkinCode)
    TextView checkInCode;

    @BindView(R.id.checkoutCode)
    TextView checkOutCode;

    @BindView(R.id.checkinCodeDisp)
    TextView checkinCodeDisp;

    @BindView(R.id.checkoutCodeDisp)
    TextView checkoutCodeDisp;


    @BindView(R.id.memberOnlyTextDetails)
    TextView memberOnlyText;

    @BindView(R.id.clockInButton)
    Button clockInButton;

    @BindView(R.id.clockOutButton)
    Button clockOutButton;

    @BindView(R.id.whoCameButton)
    Button whoCameButton;

    private EventModel event,eventModel;

    private Context context;
    String addressForGmaps;
    double latitude, longitude;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    private OrganizationAdapter listOfEndorseOrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        ButterKnife.bind(this);

        if(UserModel.myUserModel == null){
            shareButton.setVisibility(View.GONE);
            joinButton.setVisibility(View.GONE);
            endorseButton.setVisibility(View.GONE);
            endorseText.setVisibility(View.GONE);
            orgEndorseList.setVisibility(View.GONE);
            orgListLabel.setVisibility(View.GONE);
        }

        if (ActivityCompat.checkSelfPermission(EventDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(EventDetailsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EventDetailsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        } else {

            //for getting current address
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    // The next two lines tell the new client that “this” current class will handle connection stuff
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    //fourth line adds the LocationServices API endpoint from GooglePlayServices
                    .addApi(LocationServices.API)
                    .build();

            // Create the LocationRequest object
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                    .setFastestInterval(1 * 1000); // 1 second, in milliseconds
            //end -farhan

            context = this;

            Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            mCollapsingToolbarLayout.setTitle("Event Title");
            mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

            linearLayout = findViewById(R.id.joinButton);
            text = findViewById(R.id.joinButtonText);

            if (UserModel.myUserModel == null){
                editEventButton.setVisibility(View.GONE);
            }


            linearLayout.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {


                    ImageView imageView = findViewById(R.id.check);

                    text = findViewById(R.id.joinButtonText);
                    if (text.getText().toString().equals("Join Event")) {
                        LambencyAPIHelper.getInstance().getRegisterEvent(UserModel.myUserModel.getOauthToken(), "" + event_id).enqueue(new retrofit2.Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                if (response.body() == null || response.code() != 200) {
                                    System.out.println("Error cause body returned null or bad response code in register response.");
                                    Toast.makeText(getApplicationContext(), "Problem registering.", Toast.LENGTH_LONG).show();
                                }
                                //when response is back
                                Integer ret = response.body();
                                if (ret == 0) {
                                    System.out.println("successfully registerd for an event");
                                    UserModel.myUserModel.registerForEvent(event_id);
                                    System.out.println("REgistering for an event: " + event_id);
                                    System.out.println("Is it joined: " + UserModel.myUserModel.isRegisterdForEvent(event_id));
                                    text.setText("Joined");
                                    checkMark.setVisibility(View.VISIBLE);

                                    //update num attending
                                    int num = Integer.parseInt((String)numberOfPeopleAttending.getText());
                                    num+=1;
                                    numberOfPeopleAttending.setText("" + num);


                                } else if (ret == 1) {
                                    System.out.println("failed to find user or organization");
                                    Toast.makeText(getApplicationContext(), "No Event to follow", Toast.LENGTH_LONG).show();
                                } else if (ret == 2) {
                                    System.out.println("undetermined error");
                                    Toast.makeText(getApplicationContext(), "Unknown Error", Toast.LENGTH_LONG).show();
                                } else if (ret == 3) {
                                    UserModel.myUserModel.registerForEvent(event_id);
                                    System.out.println("REgistering for an event: " + event_id);
                                    System.out.println("Is it joined: " + UserModel.myUserModel.isRegisterdForEvent(event_id));
                                    Toast.makeText(getApplicationContext(), "Already registered for an event", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable throwable) {
                                //when failure
                                System.out.println("FAILED CALL");
                                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        //Toast.makeText(getApplicationContext(), "You cant actually unfollow. Sorry", Toast.LENGTH_LONG).show();
                        //text.setText("Join Event");
                        LambencyAPIHelper.getInstance().unRegisterForEvent(UserModel.myUserModel.getOauthToken(), "" + event_id).enqueue(new retrofit2.Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                if (response.body() == null || response.code() != 200) {
                                    System.out.println("Error cause body returned null or bad response code in register response.");
                                    Toast.makeText(getApplicationContext(), "Problem deleting registration.", Toast.LENGTH_LONG).show();
                                }
                                //when response is back
                                Integer ret = response.body();
                                if (ret == 0) {
                                    System.out.println("successfully unregistered for event");
                                    UserModel.myUserModel.unregisterForEvent(event_id);
                                    System.out.println("Unregistering for an event: " + event_id);
                                    System.out.println("Is it joined: " + UserModel.myUserModel.isRegisterdForEvent(event_id));
                                    text.setText("Join Event");
                                    checkMark.setVisibility(View.INVISIBLE);

                                    //update num attending
                                    int num = Integer.parseInt((String)numberOfPeopleAttending.getText());
                                    num-=1;
                                    numberOfPeopleAttending.setText("" + num);

                                } else if (ret == 1) {
                                    System.out.println("failed to find user or organization");
                                    Toast.makeText(getApplicationContext(), "No Event to follow", Toast.LENGTH_LONG).show();
                                } else if (ret == 2) {
                                    System.out.println("undetermined error");
                                    Toast.makeText(getApplicationContext(), "Unknown Error", Toast.LENGTH_LONG).show();
                                } else if (ret == 3) {
                                    UserModel.myUserModel.unregisterForEvent(event_id);
                                    System.out.println("Unregistering for an event: " + event_id);
                                    System.out.println("Is it joined: " + UserModel.myUserModel.isRegisterdForEvent(event_id));
                                    System.out.println("Technically they were not registered for the event, but I guess the user object was not updated... so oops. Fail quietly.");
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

            if(UserModel.myUserModel == null){
                orgLayout.setVisibility(View.GONE);
            }

            final Button shareButton = findViewById(R.id.shareEvent);


            if (UserModel.myUserModel != null && UserModel.myUserModel.getMyOrgs().size() == 0) {
                endorseText.setVisibility(View.GONE);
                endorseButton.setVisibility(View.GONE);
            }

            endorseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (endorseButton.getText().equals("Endorse")) {
                        //TODO Endorse retrofit here
                        LambencyAPIHelper.getInstance().getMyOrganizedOrgs(UserModel.myUserModel.getOauthToken()).enqueue(new Callback<ArrayList<OrganizationModel>>() {
                            @Override
                            public void onResponse(Call<ArrayList<OrganizationModel>> call,
                                                   Response<ArrayList<OrganizationModel>> response) {

                                if (response.body() == null || response.code() != 200) {
                                    System.out.println("ERROR!!!!!");
                                    System.out.println("1");
                                    return;
                                }
                                //when response is back
                                final ArrayList<OrganizationModel> ret = response.body();
                                if(ret == null){
                                    System.out.println("Error");
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Pick an organization");
                                    String[] items = new String[UserModel.myUserModel.getMyOrgs().size()];
                                    for(int i = 0; i < UserModel.myUserModel.getMyOrgs().size(); i++)
                                    {
                                        items[i] = ret.get(i).getName();
                                    }
                                    builder.setItems(items, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            LambencyAPIHelper.getInstance().getEndorse(UserModel.myUserModel.getOauthToken(), "" + ret.get(which).getOrgID(), "" + event_id).enqueue(new Callback<Integer>() {
                                                @Override
                                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                    if (response.body() == null || response.code() != 200) {
                                                        System.out.println("ERROR!!!!!");
                                                        return;
                                                    }
                                                    //when response is back
                                                    Integer ret2 = response.body();
                                                    if (ret2 == 0) {
                                                        System.out.println("Success");

                                                        endorseButton.setText("Revoke");
                                                        endorseText.setText("\nClick to no longer endorse this event! ");

                                                        getAllOrgs();

                                                    } else if (ret2 == -1) {
                                                        Toast.makeText(getApplicationContext(), "an error has occurred", Toast.LENGTH_LONG).show();
                                                    } else if (ret2 == -2) {
                                                        Toast.makeText(getApplicationContext(), "already endorsed", Toast.LENGTH_LONG).show();
                                                    } else if (ret2 == -3) {
                                                        Toast.makeText(getApplicationContext(), "invalid arguments", Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Integer> call, Throwable throwable) {
                                                    //when failure
                                                    Toast.makeText(getApplicationContext(), "FAILED CALL", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    });
                                    builder.show();
                                }

                            }

                            @Override
                            public void onFailure(Call<ArrayList<OrganizationModel>> call, Throwable throwable) {
                                //when failure
                                System.out.println("FAILED CALL");
                            }
                        });

                    } else {
                        //TODO Unendorse retrofit here

                        LambencyAPIHelper.getInstance().getMyOrganizedOrgs(UserModel.myUserModel.getOauthToken()).enqueue(new Callback<ArrayList<OrganizationModel>>() {
                            @Override
                            public void onResponse(Call<ArrayList<OrganizationModel>> call,
                                                   Response<ArrayList<OrganizationModel>> response) {

                                if (response.body() == null || response.code() != 200) {
                                    System.out.println("ERROR!!!!!");
                                    System.out.println("2");
                                    return;
                                }
                                //when response is back
                                final ArrayList<OrganizationModel> ret = response.body();
                                if(ret == null){
                                    System.out.println("Error");
                                }else{
                                    //ret is the list of orgs
                                    AlertDialog.Builder builderUn = new AlertDialog.Builder(context);
                                    builderUn.setTitle("Pick an organization");
                                    String[] items = new String[UserModel.myUserModel.getMyOrgs().size()];
                                    for(int i = 0; i < UserModel.myUserModel.getMyOrgs().size(); i++)
                                    {
                                        items[i] = ret.get(i).getName();
                                    }
                                    builderUn.setItems(items, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            LambencyAPIHelper.getInstance().getUnendorse(UserModel.myUserModel.getOauthToken(), "" + ret.get(which).getOrgID(), "" + event_id).enqueue(new Callback<Integer>() {
                                                @Override
                                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                    if (response.body() == null || response.code() != 200) {
                                                        System.out.println("ERROR!!!!!");
                                                        return;
                                                    }
                                                    //when response is back
                                                    Integer ret = response.body();
                                                    if (ret == 0) {
                                                        endorseText.setText("\nEndorse this event as organization! ");
                                                        endorseButton.setText("Endorse");

                                                        getAllOrgs();
                                                    } else if (ret == -1) {
                                                        Toast.makeText(getApplicationContext(), "an error has occurred", Toast.LENGTH_LONG).show();
                                                    } else if (ret == -2) {
                                                        Toast.makeText(getApplicationContext(), "not endorsed", Toast.LENGTH_LONG).show();
                                                    } else if (ret == -3) {
                                                        Toast.makeText(getApplicationContext(), "invalid arguments", Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Integer> call, Throwable throwable) {
                                                    //when failure
                                                    Toast.makeText(getApplicationContext(), "FAILED CALL", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    });
                                    builderUn.show();
                                }

                            }

                            @Override
                            public void onFailure(Call<ArrayList<OrganizationModel>> call, Throwable throwable) {
                                //when failure
                                System.out.println("FAILED CALL");
                            }
                        });

                    }

                }
            });

            shareButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // if statement for user will go here

                    // Start smsActivity.class
                /*Intent myIntent = new Intent(EventDetailsActivity.this,
                        smsActivity.class);
                startActivity(myIntent);*/
                    shareIt(eventModel);
                }
            });

            Bundle bundle = getIntent().getExtras();
                if (bundle != null || data != null) {
                    //TODO error check
                    if (data == null) {
                        event_id = bundle.getInt("event_id");
                    } else {
                        String event_string = data.getQueryParameter("eventid");
                        event_id = Integer.parseInt(event_string);
                    }

                    callRetrofit(event_id);
                }



            if (UserModel.myUserModel != null) {
                System.out.println("This event id is: " + event_id);
                if (UserModel.myUserModel.isRegisterdForEvent(event_id)) {
                    text.setText("Joined");
                    checkMark.setVisibility(View.VISIBLE);
                } else {
                    text.setText("Join Event");
                    checkMark.setVisibility(View.GONE);
                }
            } else {
                text.setText("Login to see");
            }

            //Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));

            if(UserModel.myUserModel != null) {
                getAllOrgs();
            }

            //redirection to google maps when address is clicked
            addressView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //lat and long of starting and destination
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr=" + currentLatitude + "," + currentLongitude +
                                    "&daddr=" + latitude + "," + longitude));
                    startActivity(intent);
                }
            });


        }
    }

    private void setTitle(String title) {
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle(title);
    }

    private void callRetrofit(final int event_id) {

        isLoading(true);

        if (UserModel.myUserModel!=null) {
            //To Dislplay number attending
            LambencyAPIHelper.getInstance().getEventNumAttending(UserModel.myUserModel.getOauthToken(), "" + event_id).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.body() == null || response.code() != 200) {
                        //System.out.println("ERROR!!!!!");
                        numberOfPeopleAttending.setText(0);
                        return;
                    }
                    //when response is back
                    Integer ret = response.body();
                    if (ret == -1) {
                        System.out.println("Error has occurred");
                    } else {
                        System.out.println("the number of users attending this event is" + ret);
                        numberOfPeopleAttending.setText(ret + "");
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    //when failure
                    System.out.println("FAILED CALL");
                }
            });
        }


        LambencyAPIHelper.getInstance().getEventSearchByID(Integer.toString(event_id)).enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("ERROR!!!!!");
                    System.out.println("4");
                }
                //when response is back
                eventModel = response.body();
                if (eventModel == null) {
                    System.out.println("failed to event");

                }else{

                    System.out.println("Got event data!");

                    setTitle(eventModel.getName());
                    eventName = eventModel.getName();

                    String currDate = TimeHelper.dateFromTimestamp(eventModel.getStart());
                    currDate = currDate.substring(0, currDate.length() - 4);
                    currDate += "18";


                    dateView.setText(currDate); //TimeHelper.dateFromTimestamp(eventModel.getStart()));
                    descriptionView.setText(eventModel.getDescription());
                    timeView.setText(TimeHelper.hourFromTimestamp(eventModel.getStart()) + " - " + TimeHelper.hourFromTimestamp(eventModel.getEnd()));
                    addressForGmaps = eventModel.getPrettyAddress();
                    latitude = eventModel.getLattitude();
                    longitude = eventModel.getLongitude();
                    addressView.setText(eventModel.getPrettyAddress());

                    if(eventModel.isPrivateEvent()) {
                        memberOnlyText.setVisibility(View.VISIBLE);
                    }

                    //setting the codes
                    if(eventModel.getClockInCode() != null && eventModel.getClockOutCode() != null
                            && UserModel.myUserModel.getMyOrgs().contains(eventModel.getOrg_id())) {
                        checkInCode.setText(eventModel.getClockInCode());
                        checkOutCode.setText(eventModel.getClockOutCode());
                    }else{
                        checkInCode.setVisibility(View.GONE);
                        checkOutCode.setVisibility(View.GONE);
                        checkinCodeDisp.setVisibility(View.GONE);
                        checkoutCodeDisp.setVisibility(View.GONE);
                        clockOutButton.setVisibility(View.GONE);
                        clockInButton.setVisibility(View.GONE);

                    }

                    RequestOptions requestOptions = new RequestOptions();


                    //ImageHelper.loadWithGlide(context, ImageHelper.saveImage(context, eventModel.getImageFile(), "eventImage" + eventModel.getEvent_id()), eventImageView);
                    ImageHelper.loadWithGlide(context, eventModel.getImage_path(), eventImageView);

                    getOrgInfo(eventModel.getOrg_id());

                    if (UserModel.myUserModel!=null) {
                        if (!UserModel.myUserModel.getMyOrgs().contains(eventModel.getOrg_id())) {
                            editEventButton.setVisibility(View.GONE);
                        }
                    }

                    event = eventModel;


                    progressBar.setVisibility(View.GONE);

                    if (UserModel.myUserModel!=null) {
                        if (UserModel.myUserModel.getMyOrgs().contains(event.getOrg_id())) {
                            //if(creator) {
                            whosAttendingButton.setVisibility(View.VISIBLE);
                            linearLayout.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable throwable) {
                //when failure
                System.out.println("FAILED CALL");

                progressBar.setVisibility(View.GONE);
            }
        });
    }



    private void getAllOrgs()
    {
        LambencyAPIHelper.getInstance().getEndorsedOrgs("" + UserModel.myUserModel.getOauthToken(), "" + event_id).enqueue(new Callback<List<OrganizationModel>>() {
            @Override
            public void onResponse(Call<List<OrganizationModel>> call, Response<List<OrganizationModel>> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("An error has occurred or no organizations were found");
                    return;
                }
                //when response is back
                List<OrganizationModel> orgList = response.body();
                if(orgList == null){
                    Toast.makeText(getApplicationContext(), "Null", Toast.LENGTH_LONG).show();
                }
                else if(orgList.size() == 0){
                    //Toast.makeText(getApplicationContext(), "Size = 0", Toast.LENGTH_LONG).show();
                    endorseLinLayout.setVisibility(View.GONE);
                }
                else
                {
                    // TODO fill here
                    endorseLinLayout.setVisibility(View.VISIBLE);
                    listOfEndorseOrg = new OrganizationAdapter(context, orgList);
                    orgEndorseList.setAdapter(listOfEndorseOrg);
                    orgEndorseList.setLayoutManager(new LinearLayoutManager(context));

                    for(int i = 0; i < UserModel.myUserModel.getMyOrgs().size(); i++)
                    {
                        for (int j = 0; j < orgList.size(); j++)
                        {
                            if(UserModel.myUserModel.getMyOrgs().get(i) == orgList.get(j).getOrgID())
                            {
                                endorseButton.setText("Revoke");
                                endorseText.setText("\nClick to no longer endorse this event! ");
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<OrganizationModel>> call, Throwable throwable) {
                //when failure
                System.out.println("FAILED CALL");
            }
        });
    }

    private void getOrgInfo(int org_id) {
        LambencyAPIHelper.getInstance().getOrgSearchByID("" + org_id).enqueue(new Callback<OrganizationModel>() {
            @Override
            public void onResponse(Call<OrganizationModel> call, Response<OrganizationModel> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("ERROR!!!!!");
                    System.out.println("5");
                    return;
                }

                //when response is back
                OrganizationModel organization = response.body();

                if (organization == null) {
                    System.out.println("failed to find organization");
                    Toast.makeText(getApplicationContext(), "No Organization Object", Toast.LENGTH_LONG).show();
                    return;
                }

                orgTitleView.setText("Host Organization: " + organization.getName());
                ImageHelper.loadWithGlide(context,
                        organization.getImagePath(),
                        orgImageView);

                isLoading(false);

            }

            @Override
            public void onFailure(Call<OrganizationModel> call, Throwable throwable) {
                //when failure
                System.out.println("FAILED ORG CALL");
            }
        });
    }

    private void shareIt(EventModel eventModel) {
        //sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody =
                "http://www.mylambencyclient.com?eventid=" + eventModel.getEvent_id() + "\n" +
                eventName + ": This is a cool event I found on Lambency and I think you will be interested in it. " +
                "Have a look!\n (You must have Lambency installed on your Android phone)";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Lambency event shared");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                  //Intent intent = new Intent(context, SearchActivity.class);
                  //startActivity(intent);
                return true;
            default:
                return true;
        }
    }

    @OnClick(R.id.whosAttending)
    public void handleWhosAttendingClick() {
        if (event != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("event_id", event.getEvent_id());
            Intent intent = new Intent(this, ListUserActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            System.out.println("Error - no event found.");
        }
    }

    @OnClick(R.id.editEventButton)
    public void handleEditClick() {
        Bundle bundle = new Bundle();
        bundle.putInt("event_id", event.getEvent_id());
        Intent intent = new Intent(this, EventCreationActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick(R.id.organizationLayout)
    public void handleOrgClick() {
        Intent intent = new Intent(context, OrganizationDetailsActivity.class);
        Bundle bundle = new Bundle();

        bundle.putInt("org_id", event.getOrg_id());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @OnClick(R.id.whoCameButton)
    public void handleWhoCame() {

    }

    @OnClick(R.id.clockInButton)
    public void handleCheckInClick(){
        handleCodes(event.getClockInCode());
    }

    @OnClick(R.id.clockOutButton)
    public void handleCheckOut(){
        handleCodes(event.getClockOutCode());
    }

    public void handleCodes(String textCode){
        final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(context);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View dialogView = layoutInflater.inflate(R.layout.dialog_qr_code, null);

        try{
            Bitmap bitmap = encodeAsBitmap(textCode, 250, 250);
            ImageView qrCode = dialogView.findViewById(R.id.qrCode);
            qrCode.setImageBitmap(bitmap);
        } catch (WriterException e){
            e.printStackTrace();
        }

        TextView textCodeView = dialogView.findViewById(R.id.textCode);
        textCodeView.setText(textCode);

        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });


        alertDialog.setPositiveButton("Save to Device", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(EventDetailsActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
                }else{
                    saveQR();
                }

            }
        });

        alertDialog.setView(dialogView);

        Dialog dialog = alertDialog.create();
        dialog.show();

    }

    private void saveQR(){
        try{

            Bitmap bitmap = encodeAsBitmap(event.getClockInCode(), 250, 250);
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "QR-" + event.getClockInCode() , "A QR code for a Lambency event.");
            Toast.makeText(context, "QR Saved to Device Photos", Toast.LENGTH_SHORT).show();

        } catch (WriterException e){

            e.printStackTrace();

        }
    }

    private Bitmap encodeAsBitmap(String str, int width, int height) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, width, height, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, w, h);
        return bitmap;
    }


    private void isLoading(boolean loading){
        if(loading){
            contentLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            contentLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    //start methods for getting current location
    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    /**
     * If locationChanges change lat and long
     *
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * If connected get lat and long
     *
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        @SuppressLint("MissingPermission") Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

    
        //If everything went fine lets get latitude and longitude
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }
    //end methods for getting current location


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    saveQR();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
