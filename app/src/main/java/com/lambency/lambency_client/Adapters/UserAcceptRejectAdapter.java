package com.lambency.lambency_client.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Utils.ImageHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.lambency.lambency_client.Adapters.LeaderboardAdapter.context;

/**
 * Created by Evan on 2/12/2018.
 */

public class UserAcceptRejectAdapter extends RecyclerView.Adapter<UserAcceptRejectAdapter.ViewHolder> {

    public UserAcceptRejectAdapter(List<OrganizationModel> orgs, Context context)
    {
        this.orgs = orgs;
        this.context = context;
    }

    List<OrganizationModel> orgs;
    Context context;

    @Override
    public UserAcceptRejectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_organization, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserAcceptRejectAdapter.ViewHolder holder, int position) {

        OrganizationModel organizationModel = orgs.get(position);

        String title = organizationModel.getName();
        holder.titleOfOrg.setText(title);

        String desc = organizationModel.getDescription();
        holder.descriptionOfOrg.setText(desc);

        if(organizationModel.getImagePath() != null){
            ImageHelper.loadWithGlide(context,
                    organizationModel.getImagePath(),
                    holder.profileImage);
        }

        /*
        String name = organizationModel.getFirstName() + " " + organizationModel.getLastName();
        holder.nameView.setText(name);
        */

        holder.emailView.setText(organizationModel.getEmail());

    }

    @Override
    public int getItemCount() {
        return orgs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView titleOfOrg;

        @BindView(R.id.description)
        TextView descriptionOfOrg;

        @BindView(R.id.profileImage)
        ImageView profileImage;

        @BindView(R.id.email)
        TextView emailView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void updateUserList(ArrayList<OrganizationModel> orgs){
        this.orgs = (List) orgs;
        notifyDataSetChanged();
    }
}

