package com.lambency.lambency_client.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lambency.lambency_client.Models.EventModel;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Utils.TimeHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
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

        EventModel eventModel = events.get(position);

        if(eventModel.getName() != null){
            holder.titleView.setText(eventModel.getName());
        }

        if (eventModel.getDescription() != null){
            holder.descriptionView.setText(eventModel.getDescription());
        }

        if(eventModel.getStart() != null){
            String date = "Date: " + TimeHelper.dateFromTimestamp(eventModel.getStart());
            holder.dateView.setText(date);
        }

        if(eventModel.getStart() != null && eventModel.getEnd() != null){
            String time = "Time: " + TimeHelper.hourFromTimestamp(eventModel.getStart()) +
                    "-" + TimeHelper.hourFromTimestamp(eventModel.getEnd());
            holder.timeView.setText(time);
        }

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class AreaViewHolder extends RecyclerView.ViewHolder {

        //Get references to layout and define onClick methods here
        @BindView(R.id.title)
        TextView titleView;

        @BindView(R.id.descriptionOfEvent)
        TextView descriptionView;

        @BindView(R.id.dateOfEvent)
        TextView dateView;

        @BindView(R.id.timeOfEvent)
        TextView timeView;


        public AreaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void updateEvents(List<EventModel> eventList){
        events = eventList;
        notifyDataSetChanged();
    }

}
