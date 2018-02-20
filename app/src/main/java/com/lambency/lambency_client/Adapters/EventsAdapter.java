package com.lambency.lambency_client.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lambency.lambency_client.Models.EventModel;
import com.lambency.lambency_client.R;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by lshan on 2/17/2018.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.AreaViewHolder>{

    List<EventModel> events;
    private Context context;

    public EventsAdapter(Context context, List<EventModel> events){
        this.context = context;
        this.events = events;
    }

    @Override
    public AreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event, parent, false);
        return new AreaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AreaViewHolder holder, int position) {
        //Change card info here
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class AreaViewHolder extends RecyclerView.ViewHolder {

        //Get references to layout and define onClick methods here

        public AreaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
