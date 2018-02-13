package com.lambency.lambency_client.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.R;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.lambency.lambency_client.R;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.lambency.lambency_client.R;


public class MainActivity extends AppCompatActivity {


//Key is 406595282653-cc9eb7143bvpgfe5da941r3jq174b4dq
//this goes in src/main/resources/client_secret.json


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //For Button
        final Button button = findViewById(R.id.createEventButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // if statement for user will go here

                // Start Eventcreationactivity.class
                Intent myIntent = new Intent(MainActivity.this,
                        EventCreationActivity.class);
                startActivity(myIntent);
            }
        });
    }

}
