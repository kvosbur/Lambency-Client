package com.lambency.lambency_client.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 2/12/2018.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    List<UserModel> users;
    private Context context;

    public UserListAdapter(Context context, List<UserModel> users)
    {
        this.context = context;
        this.users = users;
    }



    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserListAdapter.ViewHolder holder, int position) {

        UserModel userModel = users.get(position);

        String name = userModel.getFirstName() + " " + userModel.getLastName();
        holder.nameView.setText(name);

        holder.emailView.setText(userModel.getEmail());

        if(userModel.getOrgStatus() == UserModel.MEMBER){
            holder.permissionButton.setVisibility(View.VISIBLE);
            holder.permissionButton.setText("MEMBER");
            holder.permissionButton.setTextColor(context.getResources().getColor(R.color.androidGreen));
        }

        if(userModel.getOrgStatus() == UserModel.ORGANIZER){
            holder.permissionButton.setVisibility(View.VISIBLE);
            holder.permissionButton.setText("ORGANIZER");
            holder.permissionButton.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView nameView;

        @BindView(R.id.email)
        TextView emailView;

        @BindView(R.id.permissionButton)
        Button permissionButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void updateUserList(ArrayList<UserModel> users){
        this.users = (List) users;
        notifyDataSetChanged();
    }
}

