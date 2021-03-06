package com.lambency.lambency_client.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lambency.lambency_client.Activities.BottomBarActivity;
import com.lambency.lambency_client.Activities.SearchActivity;
import com.lambency.lambency_client.Models.EventFilterModel;
import com.lambency.lambency_client.Models.EventModel;
import com.lambency.lambency_client.Models.OrganizationFilterModel;
import com.lambency.lambency_client.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Evan on 3/16/2018.
 */

public class FilterDistanceFragment extends Fragment {


    @BindView(R.id.seekBar)
    SeekBar seekBar;

    @BindView(R.id.distanceText)
    TextView seekBarText;

    @BindView(R.id.tenMiles)
    Button b1;

    @BindView(R.id.twentyFiveMiles)
    Button b2;

    @BindView(R.id.fiftyMiles)
    Button b3;

    @BindView(R.id.newTextAddr)
    EditText diffAddr;

    @BindView(R.id.doneButton)
    Button doneButton;

    boolean isOrg = false;

    public FilterDistanceFragment() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter_distance, container, false);
        ButterKnife.bind(this, view);

        seekBar.setMax(100);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub

                String updateText = progress + " miles";
                seekBarText.setText(updateText);

                if (isOrg) {
                    OrganizationFilterModel.currentFilter.setDistanceMiles(progress);
                } else {
                    EventFilterModel.currentFilter.setDistanceMiles(progress);
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                String updateText = "10 miles";
                seekBarText.setText(updateText);
                if (isOrg) {
                    OrganizationFilterModel.currentFilter.setDistanceMiles(10);
                } else {
                    EventFilterModel.currentFilter.setDistanceMiles(10);
                }
                seekBar.setProgress(10);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                String updateText = "25 miles";
                seekBarText.setText(updateText);
                if (isOrg) {
                    OrganizationFilterModel.currentFilter.setDistanceMiles(25);
                } else {
                    EventFilterModel.currentFilter.setDistanceMiles(25);
                }
                seekBar.setProgress(25);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                String updateText = "50 miles";
                seekBarText.setText(updateText);
                if (isOrg) {
                    OrganizationFilterModel.currentFilter.setDistanceMiles(25);
                } else {
                    EventFilterModel.currentFilter.setDistanceMiles(25);
                }
                seekBar.setProgress(50);
            }
        });

        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ButterKnife.bind(this, getView());

        //((SearchActivity) getActivity())
         //       .setActionBarTitle("FilterDistance");
        setHasOptionsMenu(true);

        if(EventFilterModel.currentFilter.getTitle().compareTo("org") == 0)
        {
            isOrg = true;
        }


    }
}
