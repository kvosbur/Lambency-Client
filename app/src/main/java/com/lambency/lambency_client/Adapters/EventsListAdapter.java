package com.lambency.lambency_client.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.lambency.lambency_client.Activities.EventDetailsActivity;
import com.lambency.lambency_client.Models.EventModel;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Utils.ImageHelper;
import com.lambency.lambency_client.Utils.TimeHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lshan on 3/1/2018.
 */

public class EventsListAdapter extends ArrayAdapter<EventModel> {

    private Context context;
    private ArrayList<EventModel> events;

    public EventsListAdapter(Context context, ArrayList<EventModel> events) {
        super(context, R.layout.card_event);
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;

        EventModel eventModel = getItem(position);
        ViewHolder viewHolder = new ViewHolder();

        if(v == null){
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.card_event, null);
        }

        viewHolder.titleView = convertView.findViewById(R.id.title);
        if(viewHolder.titleView != null){
            viewHolder.titleView.setText(eventModel.getName());
        }

        return v;
    }

    public class ViewHolder{
        TextView titleView;
        TextView descriptionView;
        TextView dateView;
        TextView timeView;
        CardView cardView;
        ImageView eventImageView;
    }

}
