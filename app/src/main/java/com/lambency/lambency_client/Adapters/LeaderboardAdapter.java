package com.lambency.lambency_client.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.lambency.lambency_client.Activities.LeaderboardActivity;
import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 2/12/2018.
 */

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
    public LeaderboardAdapter(List<UserModel> users)
    {
        this.users = users;
    }

    List<UserModel> users;

    @Override
    public LeaderboardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_leaderboard, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LeaderboardAdapter.ViewHolder holder, int position) {
        UserModel organizationModel = users.get(position);

        String title = organizationModel.getFirstName() + organizationModel.getLastName();
        holder.leaderboardName.setText(title);

        int desc = organizationModel.getHoursWorked();
        holder.leaderboardHours.setText(desc + " hours");
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.leaderboard_card)
        public void onClickCard() {
            if(seeMore.getVisibility() == View.GONE) {
                //TODO Transition to user page
            }
            else {
                //TODO Retrofit call here for adding more users
            }
        }
    }

    public void updateUserList(ArrayList<UserModel> users){
        this.users = (List) users;
        notifyDataSetChanged();
    }
}

