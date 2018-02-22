package com.lambency.lambency_client.Activities;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.lambency.lambency_client.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;

public class EventDetailsActivity extends AppCompatActivity {


    //@BindView(R.id.createEventButton)
    //Button shareEventButton;

    @BindView(R.id.joinButtonText)
    TextView joinButText;

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

        LinearLayout linearLayout = findViewById(R.id.joinButton);
        Button listUser = findViewById(R.id.listUser);
        listUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EventDetailsActivity.this, ListUserActivity.class);
                startActivity(intent);
            }
        });

        boolean creator = true; // API CALL HERE
        if(creator) {
            listUser.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }

        final Button shareButton = findViewById(R.id.shareEvent);
        shareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // if statement for user will go here

                // Start smsActivity.class
                /*Intent myIntent = new Intent(EventDetailsActivity.this,
                        smsActivity.class);
                startActivity(myIntent);*/
                shareIt();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            System.out.println(bundle.getInt("event_id"));
        }
    }

    private void shareIt() {
    //sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Here is the share content body";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return true;
        }
    }

}
