package com.lambency.lambency_client.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPI;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Utils.ImageHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

    public static int currentOrgId;
    private Context context;
    public String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_details);

        ButterKnife.bind(this);
        context = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);

        for(int i = 0; i < UserModel.myUserModel.getMyOrgs().size(); i++)
        {
            if(UserModel.myUserModel.getMyOrgs().get(i) == currentOrgId)
            {
                requestJoin.setText("Leave Organization");
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

                    if(organization == null){
                        System.out.println("failed to find organization");
                        Toast.makeText(getApplicationContext(), "No Organization Object", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Toast.makeText(getApplicationContext(), "Got Organization Object", Toast.LENGTH_LONG).show();

                    //change screen to show organization information
                    titleOrg.setText(organization.getName());
                    descriptionOrg.setText(organization.getDescription());
                    emailOrg.setText(organization.getEmail());
                    addressOrg.setText(organization.getLocation());

                    img = organization.getImage();

                    ImageHelper.loadWithGlide(context,
                            ImageHelper.saveImage(context, organization.getImage(), "orgImage" + organization.getOrgID()),
                            orgImage);
                    /*
                    ImageHelper.loadWithGlide(context,
                            ImageHelper.saveImage(context, organization.getImage(), "orgImage" + organization.getOrgID()),
                            leaveOrgImg);
                            */

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


            TextView orgtitle = view.findViewById(R.id.LeaveTitleOrg);
            orgtitle.setText(titleOrg.getText());

            CircleImageView leaveOrgImg = view.findViewById(R.id.LeaveOrgImg);

            ImageHelper.loadWithGlide(context,
                    ImageHelper.saveImage(context, img, "orgImage" + currentOrgId),
                    leaveOrgImg);

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Leave",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            final String JoinText = "Join Organization";
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
                                        System.out.println("successfully left organization");
                                    }
                                    else if(ret == 100){
                                        Toast.makeText(getApplicationContext(), "Your request has been terminated.", Toast.LENGTH_LONG).show();
                                        requestJoin.setText(JoinText);
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
}
