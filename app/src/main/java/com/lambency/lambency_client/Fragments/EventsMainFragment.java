package com.lambency.lambency_client.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.lambency.lambency_client.Activities.BottomBarActivity;
import com.lambency.lambency_client.Activities.SearchActivity;
import com.lambency.lambency_client.Adapters.EventsAdapter;
import com.lambency.lambency_client.Models.EventModel;
import com.lambency.lambency_client.Models.UserAuthenticatorModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventsMainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventsMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsMainFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, OnConnectionFailedListener,LocationListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private EventsAdapter eventsMainAdapter;

    @BindView(R.id.eventsMainRecyclerView)
    RecyclerView eventsMainRecyclerView;

    @BindView(R.id.eventMainProgress_bar)
    ProgressBar eventMainProgress_bar;

    private GoogleApiClient mGoogleApiClient;
    private double currentLatitude;
    private double currentLongitude;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


    public EventsMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventsMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsMainFragment newInstance(String param1, String param2) {
        EventsMainFragment fragment = new EventsMainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        ((BottomBarActivity) getActivity())
                .setActionBarTitle("Feed");
        setHasOptionsMenu(true);

    }

    private void startAdapter(List<EventModel> events){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        eventsMainRecyclerView.setLayoutManager(linearLayoutManager);
        eventsMainAdapter = new EventsAdapter(getContext(), events);
        eventsMainRecyclerView.setAdapter(eventsMainAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                this.startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events_main, container, false);
        ButterKnife.bind(this, view);

        //for getting current address
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        loadingEvents();
        callRetrofit();

        ((BottomBarActivity) getActivity()).getSupportActionBar().setElevation(15);

        return view;
    }

    public void callRetrofit(){
        LambencyAPIHelper.getInstance().getEventsFeed(UserModel.myUserModel.getOauthToken(),currentLatitude+"",currentLongitude+"").enqueue(new Callback<List<EventModel>>() {
            @Override
            public void onResponse(Call<List<EventModel>> call, Response<List<EventModel>> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("ERROR!!!!!");
                    return;
                }
                //when response is back
                List<EventModel> myEvents = response.body();
                if(response.body() == null) {
                    System.out.println("ERROR NULLED!!!!");
                    Toast.makeText(getApplicationContext(), "Events NULL", Toast.LENGTH_LONG).show();
                    myEvents = new ArrayList<>();

                }
                else if(myEvents.size() == 0){
                    System.out.println("No events founud");
                }
                else{
                    Toast.makeText(getApplicationContext(), "got events", Toast.LENGTH_LONG).show();
                    System.out.println("got events");

                    //System.out.println("SUCCESS");
                    startAdapter(myEvents);
                    eventsLoaded();
                    System.out.println("success");
                }
                /*if (response.body() == null || response.code() != 200) {
                    System.out.println("ERROR!!!!!");
                    return;
                }
                //when response is back
                List<EventModel> myEvents = response.body();
                if(response.body() == null) {
                    System.out.println("ERROR NULLED!!!!");
                    Toast.makeText(getApplicationContext(), "Events NULL", Toast.LENGTH_LONG).show();
                    myEvents = new ArrayList<>();

                }
                Toast.makeText(getApplicationContext(), "got events", Toast.LENGTH_LONG).show();
                System.out.println("got events");

                //System.out.println("SUCCESS");
                startAdapter(myEvents);
                eventsLoaded();*/
            }

            @Override
            public void onFailure(Call<List<EventModel>> call, Throwable throwable) {
                //when failure
                /*System.out.println("FAILED CALL");*/
                Toast.makeText(getApplicationContext(), "Something went wrong please try again", Toast.LENGTH_LONG).show();
                System.out.println("FAILED CALL");
            }
        });
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onEventFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void loadingEvents(){

        eventsMainRecyclerView.setVisibility(View.GONE);
        eventMainProgress_bar.setVisibility(View.VISIBLE);
    }

    public void eventsLoaded(){

        eventsMainRecyclerView.setVisibility(View.VISIBLE);
        eventMainProgress_bar.setVisibility(View.GONE);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        @SuppressLint("MissingPermission") Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Toast.makeText(getActivity(), currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }

    //start methods for getting current location
    @Override
    public void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Toast.makeText(getActivity(), currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onEventFragmentInteraction(Uri uri);
    }
}
