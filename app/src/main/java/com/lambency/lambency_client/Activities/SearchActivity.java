package com.lambency.lambency_client.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.lambency.lambency_client.Adapters.SearchTabsAdapter;
import com.lambency.lambency_client.Fragments.FilterDistanceFragment;
import com.lambency.lambency_client.Models.EventModel;
import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class SearchActivity extends AppCompatActivity   {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    SearchTabsAdapter searchTabsAdapter;
    Context context;
    private FusedLocationProviderClient mFusedLocationClient;
    private int MY_PERMISSIONS_ACCESS_COARSE_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        this.context = this;

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        tabLayout.addTab(tabLayout.newTab().setText("Events"));
        tabLayout.addTab(tabLayout.newTab().setText("Organizations"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        searchTabsAdapter = new SearchTabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), this);
        viewPager.setAdapter(searchTabsAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Does not work... color still doesn't switch
                tabLayout.setScrollPosition(tab.getPosition(), 0f, true);

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        searchByLocation();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_search, menu);

        final MenuItem searchAction = menu.findItem( R.id.search );
        SearchView searchView = (SearchView) searchAction.getActionView();
        searchView.setQueryHint("Search Lambency...");
        searchView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        //searchView.setIconifiedByDefault(false);
        searchView.requestFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println(query);

                searchTabsAdapter.setOrgVisiblity(View.VISIBLE, View.GONE);

                LambencyAPIHelper.getInstance().getOrganizationSearch(query).enqueue(new Callback<ArrayList<OrganizationModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<OrganizationModel>> call, Response<ArrayList<OrganizationModel>> response) {
                        searchTabsAdapter.setOrgVisiblity(View.GONE, View.VISIBLE);

                        if (response.body() == null || response.code() != 200) {
                            System.out.println("ERROR!!!!!");
                        }
                        //when response is back
                        ArrayList<OrganizationModel> orgList = response.body();
                        if(orgList == null || orgList.size() == 0){
                            //no results found
                            if(orgList == null){
                                orgList = new ArrayList<OrganizationModel>();
                            }

                            if(searchTabsAdapter == null){
                                searchTabsAdapter = new SearchTabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), context);
                            }
                            searchTabsAdapter.updateOrgs(orgList);
                        }
                        else{
                            //results found
                            System.out.println("Orgs found!");


                            if(searchTabsAdapter == null){
                                searchTabsAdapter = new SearchTabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), context);
                            }
                            //OrgSearchResultFragment orgSearchResultFragment = (OrgSearchResultFragment) getSupportFragmentManager().findFragmentById(R.id.orgSearchResultFragment);
                            searchTabsAdapter.updateOrgs(orgList);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<OrganizationModel>> call, Throwable throwable) {
                        //when failure
                        System.out.println("FAILED CALL");

                        searchTabsAdapter.setOrgVisiblity(View.GONE, View.VISIBLE);
                    }
                });



                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    private void searchByLocation(){

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((SearchActivity) context,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_ACCESS_COARSE_LOCATION
            );

        }else {
            try {
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location == null) {
                                    System.out.println("Null location.");
                                } else {
                                    System.out.println(location.getLongitude() + " " + location.getLatitude());

                                    searchTabsAdapter.setEventVisiblity(View.VISIBLE, View.GONE);

                                    LambencyAPIHelper.getInstance().getEventsWithParams(location.getLatitude(), location.getLongitude(), "", "").enqueue(new Callback<List<EventModel>>() {
                                        @Override
                                        public void onResponse(Call<List<EventModel>> call, Response<List<EventModel>> response) {
                                            List<EventModel> events = response.body();
                                            searchTabsAdapter.updateEvents(events);
                                            searchTabsAdapter.setEventVisiblity(View.GONE, View.VISIBLE);
                                        }

                                        @Override
                                        public void onFailure(Call<List<EventModel>> call, Throwable t) {
                                            searchTabsAdapter.setEventVisiblity(View.GONE, View.VISIBLE);
                                        }
                                    });
                                }
                            }
                        });
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            searchByLocation();
            // permission was granted, yay! Do the
            // contacts-related task you need to do.

        } else {

            // permission denied, boo! Disable the
            // functionality that depends on this permission.
        }
        return;


    // other 'case' lines to check for other
    // permissions this app might request.
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case android.R.id.home:
                Intent intent = new Intent(context, BottomBarActivity.class);
                startActivity(intent);
                break;

            case R.id.location:
                System.out.println("Location Pressed");
                Intent i = new Intent(context,FilterDistanceActivity.class);
                startActivity(i);

                //FragmentManager manager = getSupportFragmentManager();
                //manager.beginTransaction().replace(R.id.fragContainer, new FilterDistanceFragment()).commit();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
