package com.lambency.lambency_client.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lambency.lambency_client.Fragments.MyLambencyEventsFragment;
import com.lambency.lambency_client.Fragments.MyLambencyOrgsFragment;
import com.lambency.lambency_client.Fragments.UserListFragment;

/**
 * Created by lshan on 3/8/2018.
 */

public class OrgUsersTabsAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private int numTabs;
    private String org_id;


    private UserListFragment memberListFragment;
    private UserListFragment organizerListFragment;


    public OrgUsersTabsAdapter(FragmentManager fm, int numTabs, Context context) {
        super(fm);
        this.context = context;
        this.numTabs = numTabs;
    }

    public OrgUsersTabsAdapter(FragmentManager fm, int numTabs, Context context, String org_id) {
        super(fm);
        this.context = context;
        this.numTabs = numTabs;
        this.org_id = org_id;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                UserListFragment tab1 = UserListFragment.newInstance(org_id);
                this.memberListFragment  = tab1;
                return tab1;

            case 1:
                UserListFragment tab2 = UserListFragment.newInstance(org_id);
                this.organizerListFragment = tab2;
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }

    public UserListFragment getMemberListFragment(){
        return memberListFragment;
    }

    public UserListFragment getOrganizerListFragment(){
        return organizerListFragment;
    }

}
