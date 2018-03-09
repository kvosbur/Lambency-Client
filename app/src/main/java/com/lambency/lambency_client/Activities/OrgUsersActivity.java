package com.lambency.lambency_client.Activities;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.lambency.lambency_client.Adapters.OrgUsersTabsAdapter;
import com.lambency.lambency_client.Fragments.UserListFragment;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrgUsersActivity extends AppCompatActivity implements UserListFragment.OnFragmentInteractionListener{

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    OrgUsersTabsAdapter orgUsersTabsAdapter;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_users);

        context = this;
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);

        tabLayout.addTab(tabLayout.newTab().setText("Members"));
        tabLayout.addTab(tabLayout.newTab().setText("Organizers"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        orgUsersTabsAdapter = new OrgUsersTabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), this);
        viewPager.setAdapter(orgUsersTabsAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Bundle b = getIntent().getExtras();
        if(b != null){
            int orgId = b.getInt("org_id");
            getUsers(orgId);
        }

    }


    private void getUsers(int orgId){

        //orgUsersTabsAdapter.getMemberListFragment().isLoading(true);
        //orgUsersTabsAdapter.getOrganizerListFragment().isLoading(true);

        LambencyAPIHelper.getInstance().getMembersAndOrganizers(UserModel.myUserModel.getOauthToken(), orgId)
                .enqueue(new Callback<ArrayList<UserModel>[]>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserModel>[]> call, Response<ArrayList<UserModel>[]> response) {
                        if (response.body() == null || response.code() != 200) {
                            System.out.println("ERROR!!!!!");
                            return;
                        }
                        //when response is back
                        ArrayList<UserModel>[] users = response.body();
                        if(users == null ){
                            System.out.println("Server returned null to getting members and organizers");
                        }
                        else{
                            ArrayList<UserModel> members = users[0];
                            ArrayList<UserModel> organizers = users[1];

                            for(UserModel user: members){
                                user.setOrgStatus(UserModel.MEMBER);
                            }

                            for(UserModel user: organizers){
                                user.setOrgStatus(UserModel.ORGANIZER);
                            }

                            orgUsersTabsAdapter.getMemberListFragment().updateUserList(members);
                            orgUsersTabsAdapter.getMemberListFragment().isLoading(false);
                            orgUsersTabsAdapter.getOrganizerListFragment().updateUserList(organizers);
                            orgUsersTabsAdapter.getOrganizerListFragment().isLoading(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserModel>[]> call, Throwable throwable) {
                        System.out.println("FAILED CALL in MEMBERS AND ORGANIZERS GETTING");
                    }
                });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
}
