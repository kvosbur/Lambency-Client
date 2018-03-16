package com.lambency.lambency_client.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lambency.lambency_client.Activities.BottomBarActivity;

/**
 * Created by Evan on 3/16/2018.
 */

public class FilterDistanceFragment extends Fragment {

    public FilterDistanceFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BottomBarActivity) getActivity())
                .setActionBarTitle("FilterDistance");
        setHasOptionsMenu(true);

    }
}
