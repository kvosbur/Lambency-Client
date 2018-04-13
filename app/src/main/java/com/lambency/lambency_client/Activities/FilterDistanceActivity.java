package com.lambency.lambency_client.Activities;

import android.app.ActionBar;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.lambency.lambency_client.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Evan on 3/16/2018.
 */

public class FilterDistanceActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

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

    public FilterDistanceActivity() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_filter_distance);
        ButterKnife.bind(this);
        seekBar.setMax(100);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


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
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                String updateText = "10 miles";
                seekBarText.setText(updateText);

                seekBar.setProgress(10);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                String updateText = "25 miles";
                seekBarText.setText(updateText);

                seekBar.setProgress(25);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                String updateText = "50 miles";
                seekBarText.setText(updateText);

                seekBar.setProgress(50);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO swap back to search page
            }
        });


        //((SearchActivity) getActivity())
        //       .setActionBarTitle("FilterDistance");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                EventFilterModel.currentFilter.setTitle("");
                //Intent intent = new Intent(this, SearchActivity.class);
                //startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
