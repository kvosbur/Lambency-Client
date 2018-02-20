package com.lambency.lambency_client.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lambency.lambency_client.Activities.SearchActivity;
import com.lambency.lambency_client.Fragments.EventSearchResultFragment;
import com.lambency.lambency_client.Fragments.OrgSearchResultFragment;

/**
 * Created by lshan on 2/19/2018.
 */

public class SearchTabsAdapter extends FragmentStatePagerAdapter {

    int numTabs;

    public SearchTabsAdapter(FragmentManager fm, int numTabs){
        super(fm);
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                EventSearchResultFragment tab1 = new EventSearchResultFragment();
                return tab1;
            case 1:
                OrgSearchResultFragment tab2 = new OrgSearchResultFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
