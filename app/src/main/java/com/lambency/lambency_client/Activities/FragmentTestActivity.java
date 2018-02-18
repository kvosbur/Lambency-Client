package com.lambency.lambency_client.Activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lambency.lambency_client.Fragments.EventsMainFragment;
import com.lambency.lambency_client.R;

public class FragmentTestActivity extends AppCompatActivity implements EventsMainFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
