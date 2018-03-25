package com.lambency.lambency_client.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lambency.lambency_client.Activities.EventDetailsActivity;
import com.lambency.lambency_client.Models.EventModel;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Utils.ImageHelper;
import com.lambency.lambency_client.Utils.TimeHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

        holder.cardView.setTag(position);

        EventModel eventModel = events.get(position);

        if(eventModel == null){
            return;
        }

        if(eventModel.getName() != null){
            holder.titleView.setText(eventModel.getName());
        }

        if (eventModel.getDescription() != null){
            holder.descriptionView.setText(eventModel.getDescription());
        }

        if(eventModel.getStart() != null){
            String date = TimeHelper.dateFromTimestamp(eventModel.getStart());
            holder.dateView.setText(date);
        }

        if(eventModel.getStart() != null && eventModel.getEnd() != null){
            String time = TimeHelper.hourFromTimestamp(eventModel.getStart()) +
                    "-" + TimeHelper.hourFromTimestamp(eventModel.getEnd());
            holder.timeView.setText(time);
        }

        if(eventModel.getImageFile() != null){
            //holder.eventImageView.setImageBitmap(ImageHelper.stringToBitmap(eventModel.getImageFile()));

            ImageHelper.loadWithGlide(context,
                    ImageHelper.saveImage(context, eventModel.getImageFile(), "eventImage" + eventModel.getEvent_id()),
                    holder.eventImageView);
        }

        holder.orgTitleView.setText(eventModel.getOrgName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventDetailsActivity.class);
                Bundle bundle = new Bundle();

                Integer taggedPosition = (Integer) view.getTag();
                bundle.putInt("event_id", events.get(taggedPosition).getEvent_id());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        if(events == null){
            return 0;
        }
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

        @BindView(R.id.card_event)
        CardView cardView;

        @BindView(R.id.pictureOfEvent)
        ImageView eventImageView;

        @BindView(R.id.orgTitle)
        TextView orgTitleView;

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
