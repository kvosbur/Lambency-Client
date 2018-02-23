package com.lambency.lambency_client.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lambency.lambency_client.Adapters.OrganizationAdapter;
import com.lambency.lambency_client.Models.EventModel;
import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.Models.UserModel;
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

    @BindView(R.id.nothingText)
    TextView nothingText;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_org_search_result, container, false);
        ButterKnife.bind(this, view);


        //I am going to get my events first
        List<Integer> orgsAttending = UserModel.myUserModel.getMyOrgs();
        final ArrayList<OrganizationModel> orgs = new ArrayList<>();
        if(orgsAttending.size() == 0){
            for (int i = 0; i < 10; i++) {
                orgs.add(new OrganizationModel());
            }
            startAdapter(orgs);
        }
        /*else{
            for(Integer ev_id:orgsAttending){
                LambencyAPIHelper.getInstance().getOrgSearchByID(Integer.toString(ev_id)).enqueue(new Callback<OrganizationModel>() {
                    @Override
                    public void onResponse(Call<OrganizationModel> call, Response<OrganizationModel> response) {
                        if (response.body() == null || response.code() != 200) {
                            System.out.println("ERROR!!!!!");
                        }
                        //when response is back
                        OrganizationModel organization= response.body();
                        if(organization == null){
                            System.out.println("failed to find organization");
                        }
                        else{
                            orgs.add(organization);
                            updateOrgs(orgs);
                            System.out.println("organization description: " + organization.getDescription());
                        }
                    }

                    @Override
                    public void onFailure(Call<OrganizationModel> call, Throwable throwable) {
                        //when failure
                        System.out.println("FAILED CALL");
                    }
                });
            }
        }

        */

        return view;
    }

    public void startAdapter(List<OrganizationModel> orgs){
        if(orgAdapter == null){
            orgAdapter = new OrganizationAdapter(getContext(), orgs);
        }

        orgsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orgsRecyclerView.setAdapter(orgAdapter);
    }

    public void updateOrgs(ArrayList<OrganizationModel> orgList){
        if(orgList.size() == 0){
            //No search results
            nothingText.setVisibility(View.VISIBLE);
            orgsRecyclerView.setVisibility(View.GONE);
        }else {
            nothingText.setVisibility(View.GONE);
            orgsRecyclerView.setVisibility(View.VISIBLE);
            orgAdapter.updateOrgs(orgList);
        }
    }

    public void setVisiblity(int progressBarVisiblity, int recyclerViewVisibility){
        orgsRecyclerView.setVisibility(recyclerViewVisibility);
        progressBar.setVisibility(progressBarVisiblity);
    }


}
