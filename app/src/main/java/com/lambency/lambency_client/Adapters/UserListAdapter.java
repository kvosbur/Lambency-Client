package com.lambency.lambency_client.Adapters;

import android.content.Context;
import android.support.v7.util.SortedList;
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

    private Context context;
    private final SortedList<UserModel> users = new SortedList<UserModel>(UserModel.class, new SortedList.Callback<UserModel>() {
        @Override
        public int compare(UserModel o1, UserModel o2) {
            return o1.getFirstName().compareTo(o2.getFirstName());
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(UserModel oldItem, UserModel newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(UserModel item1, UserModel item2) {
            return item1.getUserId() == item2.getUserId();
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }
    });

    public UserListAdapter(Context context, List<UserModel> users)
    {
        this.context = context;
        add(users);
    }



    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_user, parent, false);
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
        add(users);
        notifyDataSetChanged();
    }


    public void add(UserModel user){
        this.users.add(user);
    }

    public void add(List<UserModel> users){
        this.users.addAll(users);
    }

    public void remove(UserModel user){
        this.users.remove(user);
    }

    public void remove(List<UserModel> users){
        this.users.beginBatchedUpdates();
        for(UserModel user: users){
            this.users.remove(user);
        }
        this.users.endBatchedUpdates();
    }


    public void replaceAll(List<UserModel> users){
        if(users == null || users.size() == 0){
            for (int i = 0; i < this.users.size(); i++) {
                remove(this.users.get(i));
            }

            return;
        }

        this.users.beginBatchedUpdates();

        for (int i = users.size() - 1; i >= 0; i--) {
            UserModel user = users.get(i);
            if(!users.contains(user)) {
                this.users.remove(user);
            }
        }

        this.users.addAll(users);
        this.users.endBatchedUpdates();
    }

}

