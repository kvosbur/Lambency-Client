package com.lambency.lambency_client.Activities;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.lambency.lambency_client.Adapters.OrgUsersTabsAdapter;
import com.lambency.lambency_client.Fragments.UserListFragment;
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

public class OrgUsersActivity extends AppCompatActivity implements UserListFragment.OnFragmentInteractionListener, SearchView.OnQueryTextListener {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    OrgUsersTabsAdapter orgUsersTabsAdapter;
    Context context;

    List<UserModel> members;
    List<UserModel> organizers;


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
                            setMembers(members);
                            ArrayList<UserModel> organizers = users[1];
                            setOrganizers(organizers);

                            for(UserModel user: members){
                                user.setOrgStatus(UserModel.MEMBER);
                            }

                            for(UserModel user: organizers){
                                user.setOrgStatus(UserModel.ORGANIZER);
                            }

                            updateMembers(members);
                            orgUsersTabsAdapter.getMemberListFragment().isLoading(false);
                            updateOrganizers(organizers);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_search_plain, menu);

        final MenuItem searchAction = menu.findItem( R.id.search );
        SearchView searchView = (SearchView) searchAction.getActionView();
        searchView.setQueryHint("Search users...");
        searchView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<UserModel> filteredMembers = filter(members, newText);
        updateMembers(new ArrayList<UserModel>(filteredMembers));

        final List<UserModel> filteredOrganizers = filter(organizers, newText);
        updateOrganizers(new ArrayList<UserModel>(filteredOrganizers));

        return true;
    }

    private static List<UserModel> filter(List<UserModel> users, String query){
        final String lowercaseQuery = query.toLowerCase();

        final  List<UserModel> filteredUserList = new ArrayList<>();
        for(UserModel user: users){
            final String firstName = user.getFirstName().toLowerCase();
            final String lastName = user.getLastName().toLowerCase();
            final String fullName = firstName + " " + lastName;
            if(firstName.contains(lowercaseQuery) || lastName.contains(lowercaseQuery) || fullName.contains(lowercaseQuery)){
                filteredUserList.add(user);
            }
        }

        return filteredUserList;

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

    public void setMembers(List<UserModel> members) {
        this.members = members;
    }

    public void setOrganizers(List<UserModel> organizers) {
        this.organizers = organizers;
    }

    private void updateMembers(ArrayList<UserModel> members){
        orgUsersTabsAdapter.getMemberListFragment().updateUserList(members);
    }

    private void updateOrganizers(ArrayList<UserModel> organizers){
        orgUsersTabsAdapter.getOrganizerListFragment().updateUserList(organizers);
    }

}
