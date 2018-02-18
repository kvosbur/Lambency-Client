package com.lambency.lambency_client.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.lambency.lambency_client.Fragments.EventsMainFragment;
import com.lambency.lambency_client.Fragments.ProfileFragment;
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

import butterknife.BindView;


public class MainActivity extends AppCompatActivity implements EventsMainFragment.OnFragmentInteractionListener,ProfileFragment.OnFragmentInteractionListener{


//Key is 406595282653-cc9eb7143bvpgfe5da941r3jq174b4dq
//this goes in src/main/resources/client_secret.json

    private void launchActivity() {

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void onProfileFragmentInteraction(Uri uri)
    {

    }

    @Override
    public void onEventFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bar = findViewById(R.id.bottom_navigation);

        bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // handle desired action here
                switch (item.getItemId()) {
                    case R.id.feedBot:
                        switchToFragment1();
                        break;
                    case R.id.profileBot:
                        switchToFragment2();
                        break;

                    case R.id.lamBot:
                        switchToFragment3();
                        break;

                }

                // One possibility of action is to replace the contents above the nav bar
                // return true if you want the item to be displayed as the selected item
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_search){
            Intent intent = new Intent(this, SearchActivity.class);
            this.startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void switchToFragment1() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragContainer, new EventsMainFragment()).commit();
    }

    public void switchToFragment2() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragContainer, new ProfileFragment()).commit();
    }

    public void switchToFragment3() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragContainer, new EventsMainFragment()).commit();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
