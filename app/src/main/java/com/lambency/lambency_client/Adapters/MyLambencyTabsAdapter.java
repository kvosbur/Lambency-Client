package com.lambency.lambency_client.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lambency.lambency_client.Fragments.EventSearchResultFragment;
import com.lambency.lambency_client.Fragments.MyLambencyEventsFragment;
import com.lambency.lambency_client.Fragments.MyLambencyOrgsFragment;
import com.lambency.lambency_client.Fragments.OrgSearchResultFragment;

/**
 * Created by lshan on 3/3/2018.
 */

public class MyLambencyTabsAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private int numTabs;

    private MyLambencyEventsFragment eventsFragment;
    private MyLambencyOrgsFragment orgsFragment;


    public MyLambencyTabsAdapter(FragmentManager fm, int numTabs, Context context) {
        super(fm);
        this.context = context;
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MyLambencyEventsFragment tab1 = new MyLambencyEventsFragment();
                this.eventsFragment  = tab1;
                return tab1;
            case 1:
                MyLambencyOrgsFragment tab2 = new MyLambencyOrgsFragment();
                this.orgsFragment = tab2;
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }

    public MyLambencyEventsFragment getEventsFragment(){
        return eventsFragment;
    }

    public MyLambencyOrgsFragment getOrgsFragment(){
        return orgsFragment;
    }

}
