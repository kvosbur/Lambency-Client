package com.lambency.lambency_client.Fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lambency.lambency_client.Adapters.EventsMainAdapter;
import com.lambency.lambency_client.Models.EventModel;
import com.lambency.lambency_client.R;

import java.util.List;

/**
 * Created by Evan on 2/19/2018.
 */

public class MyLambencyFragment extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EventsMainFragment.OnFragmentInteractionListener mListener;
    private EventsMainAdapter eventsMainAdapter;


    public MyLambencyFragment()
    {

    }

    public static MyLambencyFragment newInstance(String param1, String param2) {
        MyLambencyFragment fragment = new MyLambencyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EventsMainFragment.OnFragmentInteractionListener) {
            mListener = (EventsMainFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onEventFragmentInteraction(uri);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_lambency, container, false);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onEventFragmentInteraction(Uri uri);
    }
}
