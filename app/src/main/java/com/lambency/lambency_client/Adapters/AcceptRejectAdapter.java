package com.lambency.lambency_client.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AcceptRejectAdapter extends RecyclerView.Adapter<AcceptRejectAdapter.ViewHolder> {
    public AcceptRejectAdapter(List<UserModel> users)
    {
        this.users = users;
    }

    List<UserModel> users;

    @Override
    public AcceptRejectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_accept_reject_user, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AcceptRejectAdapter.ViewHolder holder, int position) {

        UserModel userModel = users.get(position);

        String name = userModel.getFirstName() + " " + userModel.getLastName();
        holder.nameView.setText(name);

        holder.emailView.setText(userModel.getEmail());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.acceptRejectName)
        TextView nameView;

        @BindView(R.id.acceptRejectEmail)
        TextView emailView;

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

