package com.lambency.lambency_client.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lambency.lambency_client.Activities.MainActivity;
import com.lambency.lambency_client.Adapters.EventsAdapter;
import com.lambency.lambency_client.Models.EventModel;
import com.lambency.lambency_client.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lshan on 2/19/2018.
 */

public class EventSearchResultFragment extends Fragment {

    private EventsAdapter eventsAdapter;

    @BindView(R.id.eventsRecyclerView)
    RecyclerView eventsRecyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_search_result, container, false);
        ButterKnife.bind(this, view);


        ArrayList<EventModel> events = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            events.add(new EventModel());
        }
        startAdapter(events);

        return view;
    }


    private void startAdapter(List<EventModel> events){
        if(eventsAdapter == null){
            eventsAdapter = new EventsAdapter(getContext(), events);
        }

        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventsRecyclerView.setAdapter(eventsAdapter);

        eventsAdapter.notifyDataSetChanged();
    }

    public void updateEvents(List<EventModel> eventList){
        eventsAdapter.updateEvents(eventList);
    }

    public void setVisibility(int progressBarVisiblity, int recyclerViewVisbility){
        progressBar.setVisibility(progressBarVisiblity);
        eventsRecyclerView.setVisibility(recyclerViewVisbility);
    }
}
