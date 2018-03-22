package com.lambency.lambency_client.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.location.LocationServices;
import com.lambency.lambency_client.Adapters.FilterTabsAdapter;
import com.lambency.lambency_client.Adapters.SearchTabsAdapter;
import com.lambency.lambency_client.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Evan on 3/22/2018.
 */

public class FilterActivity extends AppCompatActivity {

    @BindView(R.id.toolbarFilter)
    Toolbar toolbar;

    @BindView(R.id.tabLayoutFilter)
    TabLayout tabLayout;

    @BindView(R.id.viewPagerFilter)
    ViewPager viewPager;

    Context context;

    FilterTabsAdapter searchTabsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        ButterKnife.bind(this);

        this.context = this;

        //setSupportActionBar(toolbar);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle("");
        //actionBar.setDisplayHomeAsUpEnabled(true);

        tabLayout.addTab(tabLayout.newTab().setText("Distance"));
        tabLayout.addTab(tabLayout.newTab().setText("Date"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        searchTabsAdapter = new FilterTabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), this);
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
}
