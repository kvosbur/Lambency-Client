package com.lambency.lambency_client.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lambency.lambency_client.Adapters.OrganizationAdapter;
import com.lambency.lambency_client.Models.EventModel;
import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lshan on 2/19/2018.
 */

public class OrgSearchResultFragment extends Fragment {

    private OrganizationAdapter orgAdapter;

    @BindView(R.id.orgsRecyclerView)
    RecyclerView orgsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_org_search_result, container, false);
        ButterKnife.bind(this, view);


        ArrayList<OrganizationModel> orgs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            orgs.add(new OrganizationModel());
        }
        startAdapter(orgs);

        return view;
    }

    public void startAdapter(List<OrganizationModel> orgs){
        if(orgAdapter == null){
            orgAdapter = new OrganizationAdapter(getContext(), orgs);
        }

        orgsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orgsRecyclerView.setAdapter(orgAdapter);
    }
}
