package com.lambency.lambency_client.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.lambency.lambency_client.Adapters.EventsAdapter;
import com.lambency.lambency_client.Models.EventModel;
import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Utils.CustomLinearLayoutManager;
import com.lambency.lambency_client.Utils.ImageHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationDetailsActivity extends AppCompatActivity {

    /*
    @BindView(R.id.LeaveOrgImg)
    CircleImageView leaveOrgImg;
    */

    @BindView(R.id.mainLayout)
    ScrollView mainLayout;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.upcomingEventsProgress)
    ProgressBar upcomingEventsProgress;

    @BindView(R.id.followUnFollow)
    CheckBox checkBox;

    @BindView(R.id.titleOrg)
    TextView titleOrg;

    @BindView(R.id.descriptionOrg)
    TextView descriptionOrg;

    @BindView(R.id.emailOrg)
    TextView emailOrg;

    @BindView(R.id.addressOrg)
    TextView addressOrg;

    @BindView(R.id.OrgImage)
    CircleImageView orgImage;

    @BindView(R.id.orgRequestJoin)
    Button requestJoin;

    @BindView(R.id.seeMembersButton)
    Button seeMembersButton;

    @BindView(R.id.upcomingEventsContainer)
    RelativeLayout upcomingEventsContainer;

    @BindView(R.id.eventsRecyclerView)
    RecyclerView eventsRecyclerView;

    @BindView(R.id.showAllButton)
    Button showAllButton;

    @BindView(R.id.noEventsText)
    TextView noEventsTextView;


    public static int currentOrgId;
    private Context context;

    public String img;

    private OrganizationModel organizationModel;
    private EventsAdapter eventsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_details);

        ButterKnife.bind(this);
        context = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);


        boolean followed = false;
        for(int i = 0; i < UserModel.myUserModel.getFollowingOrgs().size(); i++)
        {
            if(UserModel.myUserModel.getFollowingOrgs().get(i) == currentOrgId)
            {
                checkBox.setText("Unfollow");
                followed = true;
            }
        }

        if(!followed)
        {
            checkBox.setText("Follow");
        }

        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null) {
            final int org_id = bundle.getInt("org_id");
            currentOrgId = org_id;

            mainLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            LambencyAPIHelper.getInstance().getOrgSearchByID("" + org_id).enqueue(new Callback<OrganizationModel>() {
                @Override
                public void onResponse(Call<OrganizationModel> call, Response<OrganizationModel> response) {
                    if (response.body() == null || response.code() != 200) {
                        System.out.println("ERROR!!!!!");
                        return;
                    }
                    //when response is back
                    OrganizationModel organization= response.body();
                    organizationModel = organization;



                    getUpcomingEvents();

                    if(organization == null){
                        System.out.println("failed to find organization");
                        Toast.makeText(getApplicationContext(), "No Organization Object", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Toast.makeText(getApplicationContext(), "Got Organization Object", Toast.LENGTH_LONG).show();

                    if(organization.checkPermissions(UserModel.myUserModel) == 0){
                        seeMembersButton.setVisibility(View.GONE);
                    }

                    //change screen to show organization information
                    titleOrg.setText(organization.getName());
                    descriptionOrg.setText(organization.getDescription());
                    emailOrg.setText(organization.getEmail());
                    addressOrg.setText(organization.getLocation());


                    img = organization.getImage();

                    //This is the case where the user model is out of date and thinks that there is still a request, but in reality they are offically members
                    if(UserModel.myUserModel.getRequestedJoinOrgIds().contains(org_id) && organization.getMembers().contains(UserModel.myUserModel)){
                        UserModel.myUserModel.removeRequestToJoinOrganization(org_id);
                        requestJoin.setText("Leave Organization");
                        UserModel.myUserModel.joinOrg(org_id);
                    }

                    ImageHelper.loadWithGlide(context,
                            ImageHelper.saveImage(context, organization.getImage(), "orgImage" + organization.getOrgID()),
                            orgImage);
                    /*
                    ImageHelper.loadWithGlide(context,
                            ImageHelper.saveImage(context, organization.getImage(), "orgImage" + organization.getOrgID()),
                            leaveOrgImg);
                            */

                    if(organization.getImage() != null) {
                        ImageHelper.loadWithGlide(context,
                                ImageHelper.saveImage(context, organization.getImage(), "orgImage" + organization.getOrgID()),
                                orgImage);
                    }


                    mainLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<OrganizationModel> call, Throwable throwable) {
                    //when failure
                    System.out.println("FAILED CALL");
                    mainLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            });
            //this checks to see if they are a member
            for(int i = 0; i < UserModel.myUserModel.getMyOrgs().size(); i++)
            {
                if(UserModel.myUserModel.getMyOrgs().get(i) == currentOrgId)
                {

                    requestJoin.setText("Leave Organization");
                }
            }

            // This checks to see if there is a request present
            System.out.println(UserModel.myUserModel.getRequestedJoinOrgIds());
            System.out.println(currentOrgId);
            for(int og_id : UserModel.myUserModel.getRequestedJoinOrgIds()){
                if(og_id == currentOrgId){
                    requestJoin.setText("Cancel Request");
                }
            }

            boolean followed = false;
            for(int i = 0; i < UserModel.myUserModel.getFollowingOrgs().size(); i++)
            {
                if(UserModel.myUserModel.getFollowingOrgs().get(i) == currentOrgId)
                {
                    checkBox.setText("Unfollow");
                    followed = true;
                }
            }

            if(!followed)
            {
                checkBox.setText("Follow");
            }

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()){
                        LambencyAPIHelper.getInstance().getFollowOrg(UserModel.myUserModel.getOauthToken(),"" + currentOrgId).enqueue(new retrofit2.Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                if (response.body() == null || response.code() != 200) {
                                    System.out.println("ERROR!!!!!");
                                    return;
                                }
                                //when response is back
                                Integer ret = response.body();
                                if(ret == 0){
                                    System.out.println("successfully followed organization");
                                    Toast.makeText(getApplicationContext(), "You are now following the organization", Toast.LENGTH_LONG).show();
                                    checkBox.setText("Unfollow");
                                }
                                else if (ret == 1){
                                    System.out.println("failed to find user or organization");
                                    Toast.makeText(getApplicationContext(), "NO ORG OR USER TO FOLLOW", Toast.LENGTH_LONG).show();
                                    checkBox.setChecked(false);
                                }
                                else if (ret == 2){
                                    System.out.println("undetermined error");
                                    Toast.makeText(getApplicationContext(), "Unknown Error", Toast.LENGTH_LONG).show();
                                    checkBox.setChecked(false);
                                }
                            }
                            @Override
                            public void onFailure(Call<Integer> call, Throwable throwable) {
                                //when failure
                                System.out.println("FAILED CALL");
                                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                                checkBox.setChecked(false);
                            }
                        });
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "You un followed the organization", Toast.LENGTH_LONG).show();
                        LambencyAPIHelper.getInstance().getUnfollowOrg(UserModel.myUserModel.getOauthToken(), Integer.toString(currentOrgId)).enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                if (response.body() == null || response.code() != 200) {
                                    System.out.println("ERROR!!!!!");
                                    return;
                                }
                                //when response is back
                                Integer ret = response.body();
                                if(ret == 0){
                                    System.out.println("successfully unfollowed organization");
                                    checkBox.setText("Follow");
                                }
                                else if (ret == 1){
                                    System.out.println("failed to find user or organization");
                                    Toast.makeText(getApplicationContext(), "Failure to find org or user", Toast.LENGTH_LONG).show();
                                    checkBox.setChecked(true);
                                }
                                else if (ret == 2){
                                    System.out.println("undetermined error");
                                    Toast.makeText(getApplicationContext(), "Unkown Error", Toast.LENGTH_LONG).show();
                                    checkBox.setChecked(true);
                                }
                            }
                            @Override
                            public void onFailure(Call<Integer> call, Throwable throwable) {
                                //when failure
                                System.out.println("FAILED CALL");
                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                                checkBox.setChecked(true);
                            }
                        });
                    }
                }
            });

        }else{
            Toast.makeText(getApplicationContext(), "Bundle is broken... :(", Toast.LENGTH_LONG).show();
        }
    }



    public void getUpcomingEvents(){

        showAllButton.setVisibility(View.GONE);
        upcomingEventsContainer.setVisibility(View.GONE);
        upcomingEventsContainer.setVisibility(View.VISIBLE);

        LambencyAPIHelper.getInstance().getEventsByOrg(UserModel.myUserModel.getOauthToken(), organizationModel.getOrgID() + "").enqueue(new Callback<List<EventModel>>() {
            @Override
            public void onResponse(Call<List<EventModel>> call, Response<List<EventModel>> response) {
                if (response.code() != 200) {
                    System.out.println("Error getting org events.");
                    return;
                }
                //when response is back
                List<EventModel> list = response.body();
                if(list == null){
                    System.out.println("Org has no events or error has occurred");
                    noEventsTextView.setVisibility(View.VISIBLE);
                    upcomingEventsProgress.setVisibility(View.GONE);
                }
                else{
                    noEventsTextView.setVisibility(View.GONE);

                    System.out.println("Got list of org events");

                    ArrayList<EventModel> events = new ArrayList<>(list);
                    if(list.size() > 3){
                        events = new ArrayList<>(list.subList(0, 3));
                    }else{
                        events = new ArrayList<>(list);
                    }

                    eventsAdapter = new EventsAdapter(context, events);
                    eventsRecyclerView.setLayoutManager(new CustomLinearLayoutManager(context){
                        @Override
                        public boolean canScrollVertically(){
                            return false;
                        }
                    });
                    eventsRecyclerView.setAdapter(eventsAdapter);

                    upcomingEventsContainer.setVisibility(View.VISIBLE);
                    upcomingEventsProgress.setVisibility(View.GONE);

                    if(list.size() <= 3){
                        showAllButton.setVisibility(View.GONE);
                    }else{
                        showAllButton.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<EventModel>> call, Throwable throwable) {
                //when failure
                System.out.println("FAILED CALL");
            }
        });
    }


    @OnClick(R.id.followUnFollow)
    public void handleCheckClick(){
        if (checkBox.isChecked()){
            LambencyAPIHelper.getInstance().getFollowOrg(UserModel.myUserModel.getOauthToken(),"" + currentOrgId).enqueue(new retrofit2.Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.body() == null || response.code() != 200) {
                        System.out.println("ERROR!!!!!");
                        return;
                    }
                    //when response is back
                    Integer ret = response.body();
                    if(ret == 0){
                        System.out.println("successfully followed organization");
                        Toast.makeText(getApplicationContext(), "You are now following the organization", Toast.LENGTH_LONG).show();
                        checkBox.setText("Unfollow");
                    }
                    else if (ret == 1){
                        System.out.println("failed to find user or organization");
                        Toast.makeText(getApplicationContext(), "NO ORG OR USER TO FOLLOW", Toast.LENGTH_LONG).show();
                        checkBox.setChecked(false);
                    }
                    else if (ret == 2){
                        System.out.println("undetermined error");
                        Toast.makeText(getApplicationContext(), "Unknown Error", Toast.LENGTH_LONG).show();
                        checkBox.setChecked(false);
                    }
                }
                @Override
                public void onFailure(Call<Integer> call, Throwable throwable) {
                    //when failure
                    System.out.println("FAILED CALL");
                    Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                    checkBox.setChecked(false);
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "You un followed the organization", Toast.LENGTH_LONG).show();
            LambencyAPIHelper.getInstance().getUnfollowOrg(UserModel.myUserModel.getOauthToken(), Integer.toString(currentOrgId)).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.body() == null || response.code() != 200) {
                        System.out.println("ERROR!!!!!");
                        return;
                    }
                    //when response is back
                    Integer ret = response.body();
                    if(ret == 0){
                        System.out.println("successfully unfollowed organization");
                        checkBox.setText("Follow");
                    }
                    else if (ret == 1){
                        System.out.println("failed to find user or organization");
                        Toast.makeText(getApplicationContext(), "Failure to find org or user", Toast.LENGTH_LONG).show();
                        checkBox.setChecked(true);
                    }
                    else if (ret == 2){
                        System.out.println("undetermined error");
                        Toast.makeText(getApplicationContext(), "Unkown Error", Toast.LENGTH_LONG).show();
                        checkBox.setChecked(true);
                    }
                }
                @Override
                public void onFailure(Call<Integer> call, Throwable throwable) {
                    //when failure
                    System.out.println("FAILED CALL");
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                    checkBox.setChecked(true);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return true;
        }
    }

    @OnClick(R.id.followUnFollow)
    public void onClickFollow(){
        if(checkBox.isChecked()){
            //it is checked (meaning they followed)
            LambencyAPIHelper.getInstance().getFollowOrg(UserModel.myUserModel.getOauthToken(),Integer.toString(currentOrgId)).enqueue(new retrofit2.Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.body() == null || response.code() != 200) {
                        System.out.println("ERROR!!!!!");
                        return;
                    }
                    //when response is back
                    Integer ret = response.body();
                    if(ret == 0){
                        System.out.println("successfully followed organization");
                        Toast.makeText(getApplicationContext(), "You are now following the organization", Toast.LENGTH_LONG).show();
                    }
                    else if (ret == 1){
                        System.out.println("failed to find user or organization");
                        Toast.makeText(getApplicationContext(), "NO ORG OR USER TO FOLLOW", Toast.LENGTH_LONG).show();
                        checkBox.setChecked(false);
                    }
                    else if (ret == 2){
                        System.out.println("undetermined error");
                        Toast.makeText(getApplicationContext(), "Unkown Error", Toast.LENGTH_LONG).show();
                        checkBox.setChecked(false);
                    }
                }
                @Override
                public void onFailure(Call<Integer> call, Throwable throwable) {
                    //when failure
                    System.out.println("FAILED CALL");
                    Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                    checkBox.setChecked(false);
                }
            });
        }else{
            //is not checked (meaning they unfollowed)
            LambencyAPIHelper.getInstance().getUnfollowOrg(UserModel.myUserModel.getOauthToken(), Integer.toString(currentOrgId)).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.body() == null || response.code() != 200) {
                        System.out.println("ERROR!!!!!");
                        return;
                    }
                    //when response is back
                    Integer ret = response.body();
                    if(ret == 0){
                        System.out.println("successfully unfollowed organization");
                    }
                    else if (ret == 1){
                        System.out.println("failed to find user or organization");
                        Toast.makeText(getApplicationContext(), "Failure to find org or user", Toast.LENGTH_LONG).show();
                        checkBox.setChecked(true);
                    }
                    else if (ret == 2){
                        System.out.println("undetermined error");
                        Toast.makeText(getApplicationContext(), "Unkown Error", Toast.LENGTH_LONG).show();
                        checkBox.setChecked(true);
                    }
                }
                @Override
                public void onFailure(Call<Integer> call, Throwable throwable) {
                    //when failure
                    System.out.println("FAILED CALL");
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                    checkBox.setChecked(true);
                }
            });
            Toast.makeText(getApplicationContext(), "You un followed the organization", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.orgRequestJoin)
    public void onClickRequest(){
        if(requestJoin.getText().equals("Leave Organization") || requestJoin.getText().equals("Cancel Request"))
        {
            AlertDialog alertDialog = new AlertDialog.Builder(OrganizationDetailsActivity.this).create();
            LayoutInflater factory = LayoutInflater.from(OrganizationDetailsActivity.this);
            final View view = factory.inflate(R.layout.dialog_view, null);

            alertDialog.setTitle("Warning!");
            alertDialog.setView(view);

            if(UserModel.myUserModel.getMyOrgs().get(0) == currentOrgId)
            {
                alertDialog.setMessage("WARNING: You are an organizer of this organization! If you are the last member, you" +
                        "organization will be deleted.");
            }

            TextView orgtitle = view.findViewById(R.id.LeaveTitleOrg);
            orgtitle.setText(titleOrg.getText());

            CircleImageView leaveOrgImg = view.findViewById(R.id.LeaveOrgImg);

            ImageHelper.loadWithGlide(context,
                    ImageHelper.saveImage(context, img, "orgImage" + currentOrgId),
                    leaveOrgImg);

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Leave",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            final String JoinText = "Request To Join";
                            LambencyAPIHelper.getInstance().postLeaveOrganization(UserModel.myUserModel.getOauthToken(),currentOrgId).enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                    if (response.body() == null || response.code() != 200) {
                                        Toast.makeText(getApplicationContext(), "Failed to Leave: Connection Error.", Toast.LENGTH_LONG).show();
                                        System.out.println("ERROR!!!!!");
                                        return;
                                    }
                                    //when response is back
                                    Integer ret = response.body();
                                    if(ret == 0){
                                        Toast.makeText(getApplicationContext(), "You have successfully left", Toast.LENGTH_LONG).show();
                                        requestJoin.setText(JoinText);
                                        UserModel.myUserModel.leaveOrg(currentOrgId);
                                        System.out.println("successfully left organization");
                                    }
                                    else if(ret == 100){
                                        Toast.makeText(getApplicationContext(), "Your request has been terminated.", Toast.LENGTH_LONG).show();
                                        requestJoin.setText(JoinText);
                                        UserModel.myUserModel.removeRequestToJoinOrganization(currentOrgId);
                                        System.out.println("Join request canceled");
                                    }
                                    else if (ret == -1){
                                        Toast.makeText(getApplicationContext(), "Failed to Leave: Database Error.", Toast.LENGTH_LONG).show();
                                        System.out.println("Database error caught.");
                                    }
                                    else if (ret == 1){
                                        Toast.makeText(getApplicationContext(), "Failed to Leave: Unknown User.", Toast.LENGTH_LONG).show();
                                        System.out.println("User not found");
                                    }
                                    else if (ret == 2){
                                        Toast.makeText(getApplicationContext(), "Failed to Leave: Unknown Org.", Toast.LENGTH_LONG).show();
                                        System.out.println("Org does not exist");
                                    }
                                    else if(ret == 3){
                                        Toast.makeText(getApplicationContext(), "Failed to Leave: You are not a member.", Toast.LENGTH_LONG).show();
                                        System.out.println("Not a member of the organization, so can not leave.");
                                    }
                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable throwable) {
                                    Toast.makeText(getApplicationContext(), "Failed to Leave: Connection Error.", Toast.LENGTH_LONG).show();
                                    System.out.println("FAILED CALL");
                                }
                            });

                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else
        {
            System.out.println("CURRENT ORG ID: " + currentOrgId);
            LambencyAPIHelper.getInstance().postJoinOrganization(UserModel.myUserModel.getOauthToken(), currentOrgId).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.body() == null || response.code() != 200) {
                        Toast.makeText(getApplicationContext(), "NULL ERROR", Toast.LENGTH_LONG).show();
                        return;
                    }

                    //when response is back
                    Integer status = response.body();
                    System.out.println(status);
                    if(status == 0){
                        Toast.makeText(getApplicationContext(), "Successfully requested to join", Toast.LENGTH_LONG).show();
                        UserModel.myUserModel.requestToJoinOrganization(currentOrgId);
                        requestJoin.setText("Cancel Request");
                    }
                    else if(status == 1){
                        Toast.makeText(getApplicationContext(), "Error requesting to join", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), "Failed call!", Toast.LENGTH_LONG).show();
                    System.out.println("FAILED CALL");
                }
            });
        }
    }


    @OnClick(R.id.showAllButton)
    public void handleShowAllClick(){
        Intent intent = new Intent(context, ListEventsActivity.class);
        intent.putExtra("org_id", organizationModel.getOrgID());
        startActivity(intent);
    }

    @OnClick(R.id.seeMembersButton)
    public void handleSeeMembersClick(){
        Intent intent = new Intent(context, OrgUsersActivity.class);
        intent.putExtra("org_id", organizationModel.getOrgID());
        startActivity(intent);
    }
}
