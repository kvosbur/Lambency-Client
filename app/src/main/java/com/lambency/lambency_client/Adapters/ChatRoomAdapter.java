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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lambency.lambency_client.Activities.EventDetailsActivity;
import com.lambency.lambency_client.Activities.FilterActivity;
import com.lambency.lambency_client.Activities.MessageListActivity;
import com.lambency.lambency_client.Activities.smsActivity;
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
 * Created by lBrian 4.7.18
 */

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.AreaViewHolder>{

    List<String> rooms;
    Context context;

    public ChatRoomAdapter(Context context, List<String> rooms){
        this.rooms = rooms;
        this.context = context;
    }

    @Override
    public AreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card, parent, false);
        return new AreaViewHolder(v);
    }


    @Override
    public void onBindViewHolder(AreaViewHolder holder, int position) {
        //Change card info here

        holder.cardView.setTag(position);

        String s = rooms.get(position);

        if(s == null){
            return;
        }

        if(s != null){
            holder.titleView.setText(s);
            holder.lastSentMessage.setText(s);
        }



//        if(eventModel.getImageFile() != null){
//            //holder.eventImageView.setImageBitmap(ImageHelper.stringToBitmap(eventModel.getImageFile()));
//            ImageHelper.loadWithGlide(context,
//                    ImageHelper.saveImage(context, eventModel.getImageFile(), "eventImage" + eventModel.getEvent_id()),
//                    holder.eventImageView);
//        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent intent = new Intent(context, EventDetailsActivity.class);
                Bundle bundle = new Bundle();

                Integer taggedPosition = (Integer) view.getTag();
                bundle.putInt("event_id", events.get(taggedPosition).getEvent_id());
                intent.putExtras(bundle);
                context.startActivity(intent);
                */
                Toast.makeText(context, rooms.get((Integer)view.getTag()), Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, MessageListActivity.class);
                context.startActivity(i);
            }
        });



    }

    @Override
    public int getItemCount() {
        if(rooms == null){
            return 0;
        }
        return rooms.size();
    }

    public class AreaViewHolder extends RecyclerView.ViewHolder {

        //Get references to layout and define onClick methods here
        @BindView(R.id.title)
        TextView titleView;

        @BindView(R.id.lastSentMessage)
        TextView lastSentMessage;


        @BindView(R.id.pictureOfChatBuddy)
        ImageView pictureOfChatBuddy;

        @BindView(R.id.chat_card)
        CardView cardView;



        public AreaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

    public void updateEvents(List<String> roomsList){
        rooms = roomsList;
        notifyDataSetChanged();
    }

    public List<String> getRooms() {
        return rooms;
    }
}
