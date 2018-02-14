package com.lambency.lambency_client.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lambency.lambency_client.R;

public class EventCreationActivity extends AppCompatActivity {
    String eventName, dateOfEvent, addressOfEvent, description, contact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);

        //Saving details when button pressed
        final Button saveDetails = findViewById(R.id.saveDetailsButton);
        saveDetails.setOnClickListener(new View.OnClickListener() {
            EditText eName = (EditText) findViewById(R.id.nameOfEvent);
            EditText eDate = (EditText) findViewById(R.id.dateOfEvent);
            EditText eAddr = (EditText) findViewById(R.id.addressOfEvent);
            EditText eDescrip = (EditText) findViewById(R.id.descriptionOfEvent);
            EditText eContact = (EditText) findViewById(R.id.contactForEvent);

            @Override
            public void onClick(View v) {
                eventName = eName.getText().toString();
                dateOfEvent = eDate.getText().toString();
                addressOfEvent = eAddr.getText().toString();
                description = eDescrip.getText().toString();
                contact = eContact.getText().toString();

            }
        });
    }


}






