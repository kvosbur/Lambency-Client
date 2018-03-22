package com.lambency.lambency_client.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lambency.lambency_client.Activities.SearchActivity;
import com.lambency.lambency_client.Fragments.EventSearchResultFragment;
import com.lambency.lambency_client.Fragments.FilterDistanceFragment;
import com.lambency.lambency_client.Fragments.OrgSearchResultFragment;
import com.lambency.lambency_client.Models.EventModel;
import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lshan on 2/19/2018.
 */

public class FilterTabsAdapter extends FragmentStatePagerAdapter {

    int numTabs;
    Context context;

    FilterDistanceFragment eventFragment;
    //OrgSearchResultFragment orgFragment;

    public FilterTabsAdapter(FragmentManager fm, int numTabs, Context context){
        super(fm);
        this.numTabs = numTabs;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        FilterDistanceFragment tab1 = new FilterDistanceFragment();
        this.eventFragment  = tab1;
        return tab1;
        /*
        switch (position) {
            case 0:

            //case 1:
            //    OrgSearchResultFragment tab2 = new OrgSearchResultFragment();
            //    this.orgFragment = tab2;
            //    return tab2;
            default:
                return null;
        }
        */
    }

    /*
    //public void updateOrgs(ArrayList<OrganizationModel> orgList){
    //    orgFragment.updateOrgs(orgList);
    //}

    //public void setOrgVisiblity(int progressBarVisiblity, int recyclerViewVisiblity){
     //   orgFragment.setVisiblity(progressBarVisiblity, recyclerViewVisiblity);
    //}

    public void setEventVisiblity(int progressBarVisiblity, int recyclerViewVisibility){
        if(eventFragment != null) {
            eventFragment.setVisibility(progressBarVisiblity, recyclerViewVisibility);
        }
    }

    public void updateEvents(List<EventModel> eventList){
        eventFragment.updateEvents(eventList);
    }
*/

    @Override
    public int getCount() {
        return numTabs;
    }
}
