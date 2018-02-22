package com.lambency.lambency_client.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.R;

import java.util.ArrayList;
import java.util.List;

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
        //Change card info here
    }

    @Override
    public int getItemCount() {
        return orgs.size();
    }

    public class AreaViewHolder extends RecyclerView.ViewHolder {

        //Get references to layout and define onClick methods here

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
