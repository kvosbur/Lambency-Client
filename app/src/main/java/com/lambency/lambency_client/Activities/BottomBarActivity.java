package com.lambency.lambency_client.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.MenuItem;
import android.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.FirebaseApiNotAvailableException;
//import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.lambency.lambency_client.Adapters.MyLambencyTabsAdapter;
import com.lambency.lambency_client.Fragments.ChatListFragment;
import com.lambency.lambency_client.Fragments.CheckInFragment;
import com.lambency.lambency_client.Fragments.EventsMainFragment;
import com.lambency.lambency_client.Fragments.FilterDistanceFragment;
import com.lambency.lambency_client.Fragments.MyLambencyEventsFragment;
import com.lambency.lambency_client.Fragments.MyLambencyFragment;
import com.lambency.lambency_client.Fragments.MyLambencyOrgsFragment;
import com.lambency.lambency_client.Fragments.ProfileFragment;
import com.lambency.lambency_client.Models.MyLambencyModel;
import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
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

import com.lambency.lambency_client.Utils.MyLifecycleHandler;

import com.lambency.lambency_client.Utils.NotificationHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BottomBarActivity extends BaseActivity implements EventsMainFragment.OnFragmentInteractionListener,ProfileFragment.OnFragmentInteractionListener, MyLambencyFragment.OnFragmentInteractionListener, MyLambencyEventsFragment.OnFragmentInteractionListener, MyLambencyOrgsFragment.OnFragmentInteractionListener, CheckInFragment.OnFragmentInteractionListener{

    int notifyAmount = 0;
    View badge;

    public static BottomNavigationView bottomNavigationView;

//Key is 406595282653-cc9eb7143bvpgfe5da941r3jq174b4dq
//this goes in src/main/resources/client_secret.json

    static boolean isInBottomBar;

    public static HashMap<String, Integer> notificationOfChat = new HashMap<>();

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

        ButterKnife.bind(this);

        final Handler handler = new Handler();
        final int delay = 10000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                if(MyLifecycleHandler.isApplicationVisible()) {
                    UserModel.myUserModel.setActiveForModelAndDatabase(true);
                } else {
                    UserModel.myUserModel.setActiveForModelAndDatabase(false);
                }
                handler.postDelayed(this, delay);
            }
        }, delay);


        // Get token
        String token = FirebaseInstanceId.getInstance().getToken();
        sendFirebaseToken(token);
        Log.d("Bottom Bar", "Sent firebase token to server: " + token);


        BottomNavigationView bar = findViewById(R.id.bottom_navigation);
        bar.setSelectedItemId(R.id.lamBot);
        switchToFragment3();

        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bar.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(1);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        /*
        badge = LayoutInflater.from(this)
                .inflate(R.layout.bottom_badge, bottomNavigationMenuView, false);

        itemView.addView(badge);
        */

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

                    case R.id.checkinBot:
                        switchToFragment4();
                        break;

                    case R.id.messagingBot:
                        switchToFragment5();
                        break;

                }

                // One possibility of action is to replace the contents above the nav bar
                // return true if you want the item to be displayed as the selected item
                return true;
            }
        });

    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        BottomNavigationView bar = findViewById(R.id.bottom_navigation);
        Bundle b = getIntent().getExtras();
        if(b != null)
        {
            if(b.getString("bottomView") != null && b.getString("bottomView").equals("feed"))
            {
                bar.setSelectedItemId(R.id.feedBot);
            }
        }
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
        manager.beginTransaction().replace(R.id.fragContainer, new MyLambencyFragment(), "MyLambencyFragment").commit();

        LambencyAPIHelper.getInstance().getMyLambencyModel(UserModel.myUserModel.getOauthToken()).enqueue(new Callback<MyLambencyModel>() {
            @Override
            public void onResponse(Call<MyLambencyModel> call, Response<MyLambencyModel> response) {
                MyLambencyModel myLambencyModel = response.body();
                System.out.println("Got myLambency Model");

                MyLambencyFragment myLambencyFragment =
                        (MyLambencyFragment)
                                getSupportFragmentManager().findFragmentByTag("MyLambencyFragment");


                if(myLambencyFragment == null){
                    Log.e("Fragment", "null MyLambency fragment");
                }else{

                    //Set events and hide progress bars
                    MyLambencyTabsAdapter myLambencyTabsAdapter = myLambencyFragment.getMyLambencyTabsAdapter();

                    myLambencyTabsAdapter.getEventsFragment().setEvents(myLambencyModel);
                    myLambencyTabsAdapter.getEventsFragment().showProgressBar(false);

                    myLambencyTabsAdapter.getOrgsFragment().setOrgs(myLambencyModel);
                    myLambencyTabsAdapter.getOrgsFragment().showProgressBar(false);


                }

            }

            @Override
            public void onFailure(Call<MyLambencyModel> call, Throwable t) {
                Log.e("Retrofit", "Error getting myLambency model");
            }
        });
    }

    public void switchToFragment4(){
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragContainer, new CheckInFragment()).commit();

    }

    public void switchToFragment5(){
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragContainer, new ChatListFragment()).commit();
        //Intent i = new Intent(this, MessageListActivity.class);
        //startActivity(i);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void sendFirebaseToken(String token){
        String authToken = UserModel.myUserModel.getOauthToken();
        LambencyAPIHelper.getInstance().setFirebaseCode(authToken, token).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("Firebase", "Error sending new firebase token to the server.");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        isInBottomBar = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isInBottomBar = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInBottomBar = true;
    }

}
