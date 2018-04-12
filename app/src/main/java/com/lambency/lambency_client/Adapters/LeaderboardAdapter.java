package com.lambency.lambency_client.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 2/12/2018.
 */

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
    public LeaderboardAdapter(List<UserModel> users, Context context)
    {
        this.users = users;
        this.context = context;
    }

    List<UserModel> users;
    static Context context;

    @Override
    public LeaderboardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_leaderboard, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LeaderboardAdapter.ViewHolder holder, int position) {
        UserModel organizationModel = users.get(position);

        if(organizationModel.getFirstName().compareTo("...") == 0)
        {
            holder.seeMore.setVisibility(View.VISIBLE);
            holder.leaderboardHours.setVisibility(View.GONE);
            holder.leaderboardName.setVisibility(View.GONE);
            holder.leaderboardRank.setVisibility(View.GONE);
        }

        String title = organizationModel.getFirstName() + " "+ organizationModel.getLastName();
        holder.leaderboardName.setText(title);

        double desc = organizationModel.getHoursWorked();
        holder.leaderboardHours.setText(desc + " hours");

        holder.leaderboardRank.setText(position+1  + ".");
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nameOnLeaderboard)
        TextView leaderboardName;

        @BindView(R.id.hoursOnLeaderboard)
        TextView leaderboardHours;

        @BindView(R.id.leaderboard_card)
        CardView leaderboardCard;

        @BindView(R.id.seeMoreLeaderboard)
        TextView seeMore;

        @BindView(R.id.leaderBoardRank)
        TextView leaderboardRank;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.leaderboard_card)
        public void onClickCard() {
            if(seeMore.getVisibility() == View.GONE) {
                //TODO Transition to user page
                Toast.makeText(LeaderboardAdapter.context, "Go to user page", Toast.LENGTH_SHORT).show();
            }
            else {
                //TODO Retrofit call here for adding more users
                Toast.makeText(LeaderboardAdapter.context, "Add Retrofit", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updateUserList(ArrayList<UserModel> users){
        this.users = (List) users;
        notifyDataSetChanged();
    }
}

