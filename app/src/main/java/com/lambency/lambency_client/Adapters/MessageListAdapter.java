package com.lambency.lambency_client.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Models.MessageModel;

import java.util.List;

/**
 * Created by Evan on 3/31/2018.
 */

public class MessageListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<MessageModel> mMessageModelList;

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

        void bind(MessageModel messageModel) {
            messageText.setText(messageModel.getMessageText());
            timeText.setText(messageModel.createdAt);

            // Format the stored timestamp into a readable String using method.
            //timeText.setText(Utils.formatDateTime(messageModel.getCreatedAt()));
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }

        void bind(MessageModel messageModel) {
            messageText.setText(messageModel.getMessageText());

            // Format the stored timestamp into a readable String using method.
            //timeText.setText(Utils.formatDateTime(messageModel.getCreatedAt()));
            nameText.setText(messageModel.getSender());
            timeText.setText(messageModel.createdAt);

            // Insert the profile image from the URL into the ImageView.
            //Utils.displayRoundImageFromUrl(mContext, messageModel.getSender().getProfileUrl(), profileImage);
        }
    }

    public MessageListAdapter(Context context, List<MessageModel> messageModelList) {
        mContext = context;
        mMessageModelList = messageModelList;
    }

    @Override
    public int getItemCount() {
        return mMessageModelList.size();
    }


    @Override
    public int getItemViewType(int position) {
        MessageModel messageModel = (MessageModel) mMessageModelList.get(position);
        String name = UserModel.myUserModel.getFirstName() + " " + UserModel.myUserModel.getLastName();

        if (messageModel.getSender().equals(name)) {
            // If the current user is the sender of the messageModel
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the messageModel
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = (MessageModel) mMessageModelList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(messageModel);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(messageModel);
        }
    }
}