package com.lambency.lambency_client.Activities;

import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.lambency.lambency_client.Adapters.SearchTabsAdapter;
import com.lambency.lambency_client.Fragments.OrgSearchResultFragment;
import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity   {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    SearchTabsAdapter searchTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);

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


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_search, menu);

        final MenuItem searchAction = menu.findItem( R.id.search );
        SearchView searchView = (SearchView) searchAction.getActionView();
        searchView.setQueryHint("Search Lambency...");
        searchView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println(query);

                ArrayList<OrganizationModel> orgList = new ArrayList<>();
                orgList.add(new OrganizationModel());
                searchTabsAdapter.updateOrgs(orgList);

                /*

                LambencyAPIHelper.getInstance().getOrganizationSearch(query).enqueue(new Callback<ArrayList<OrganizationModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<OrganizationModel>> call, Response<ArrayList<OrganizationModel>> response) {
                        if (response.body() == null || response.code() != 200) {
                            System.out.println("ERROR!!!!!");
                        }
                        //when response is back
                        ArrayList<OrganizationModel> orgList = response.body();
                        if(orgList.size() == 0){
                            //no results found
                        }
                        else{
                            //results found
                            System.out.println("Orgs found!");

                            //OrgSearchResultFragment orgSearchResultFragment = (OrgSearchResultFragment) getSupportFragmentManager().findFragmentById(R.id.orgSearchResultFragment);
                            searchTabsAdapter.updateOrgs(new ArrayList<OrganizationModel>());
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<OrganizationModel>> call, Throwable throwable) {
                        //when failure
                        System.out.println("FAILED CALL");
                    }
                });
                */

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.location:
                System.out.println("Location Pressed");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
