package com.lambency.lambency_client.Activities;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.lambency.lambency_client.R;

public class EventDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        toolbar.setTitle("Event Title");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle("Event Title");
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        //For Button
        final Button button = findViewById(R.id.createEventButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // if statement for user will go here

                // Start smsActivity.class
                Intent myIntent = new Intent(EventDetailsActivity.this,
                        smsActivity.class);
                startActivity(myIntent);
            }
        });

    }
}
