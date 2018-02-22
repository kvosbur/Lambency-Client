package com.lambency.lambency_client.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lshan on 2/20/2018.
 */

public class OrganizationAdapter extends RecyclerView.Adapter<OrganizationAdapter.AreaViewHolder>{

    List<OrganizationModel> orgs;
    private Context context;

    public OrganizationAdapter(Context context, List<OrganizationModel> orgs){
        this.context = context;
        this.orgs = orgs;
    }

    @Override
    public OrganizationAdapter.AreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_organization, parent, false);
        return new OrganizationAdapter.AreaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OrganizationAdapter.AreaViewHolder holder, int position) {
        OrganizationModel orgModel = orgs.get(position);

        if(orgModel.name != null) {
            holder.titleView.setText(orgModel.name);
        }

        if(orgModel.description != null) {
            holder.descriptionView.setText(orgModel.description);
        }

        if(orgModel.email != null) {
            holder.emailView.setText(orgModel.email);
        }

    }

    @Override
    public int getItemCount() {
        return orgs.size();
    }

    public class AreaViewHolder extends RecyclerView.ViewHolder {

        //Get references to layout and define onClick methods here

        @BindView(R.id.title)
        TextView titleView;

        @BindView(R.id.description)
        TextView descriptionView;

        @BindView(R.id.email)
        TextView emailView;

        public AreaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void updateOrgs(ArrayList<OrganizationModel> orgsList){
        orgs = orgsList;
        notifyDataSetChanged();
    }

}
