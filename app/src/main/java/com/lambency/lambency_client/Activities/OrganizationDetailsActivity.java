package com.lambency.lambency_client.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lambency.lambency_client.Adapters.EventsAdapter;
import com.lambency.lambency_client.Models.EventModel;
import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Utils.CustomLinearLayoutManager;
import com.lambency.lambency_client.Utils.ImageHelper;

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

    @BindView(R.id.inviteUsersToJoin)
    Button inviteUsers;

    public static int currentOrgId;
    private Context context;
    private OrganizationModel organizationModel;
    private EventsAdapter eventsAdapter;
    private String usersEmail = "";

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
            int org_id = bundle.getInt("org_id");
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

                    //hiding or showing invite button
                    if (UserModel.myUserModel.getMyOrgs().contains(organizationModel.getOrgID())){
                        inviteUsers.setVisibility(View.VISIBLE);
                    }
                    else inviteUsers.setVisibility(View.GONE);

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

    @OnClick(R.id.inviteUsersToJoin)
    public void onInviteJoin(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter email to send invite");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                usersEmail = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @OnClick(R.id.orgRequestJoin)
    public void onClickRequest(){
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
                    requestJoin.setText("Request pending");
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
        return;
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
