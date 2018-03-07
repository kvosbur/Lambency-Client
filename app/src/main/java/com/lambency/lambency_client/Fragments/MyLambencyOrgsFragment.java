package com.lambency.lambency_client.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.lambency.lambency_client.Adapters.OrganizationAdapter;
import com.lambency.lambency_client.Models.MyLambencyModel;
import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Utils.CustomLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyLambencyOrgsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyLambencyOrgsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyLambencyOrgsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private OrganizationAdapter memberOrgAdapter;
    private OrganizationAdapter organizerOrgAdapter;


    @BindView(R.id.orgsProgress)
    ProgressBar orgsProgress;

    @BindView(R.id.orgsScroll)
    ScrollView orgsScroll;

    @BindView(R.id.memberOrgsRecyclerView)
    RecyclerView memberOrgsRecyclerView;

    @BindView(R.id.organizerOrgsRecyclerView)
    RecyclerView organizerOrgsRecyclerView;

    @BindView(R.id.memberOrgsArrow)
    ImageView memberOrgsArrow;

    @BindView(R.id.organizerOrgsArrow)
    ImageView organizerOrgsArrow;

    public MyLambencyOrgsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyLambencyOrgsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyLambencyOrgsFragment newInstance(String param1, String param2) {
        MyLambencyOrgsFragment fragment = new MyLambencyOrgsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_lambency_orgs, container, false);

        ButterKnife.bind(this ,view);

        List<OrganizationModel> orgs = new ArrayList<OrganizationModel>();
        for (int i = 0; i < 10; i++) {
            orgs.add(new OrganizationModel());
        }

        memberOrgAdapter = new OrganizationAdapter(getContext(), orgs);
        memberOrgsRecyclerView.setLayoutManager(new CustomLinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        });
        memberOrgsRecyclerView.setAdapter(memberOrgAdapter);

        organizerOrgAdapter = new OrganizationAdapter(getContext(), orgs);
        organizerOrgsRecyclerView.setLayoutManager(new CustomLinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        });
        organizerOrgsRecyclerView.setAdapter(organizerOrgAdapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @OnClick(R.id.memberOrgsTitleLayout)
    public void handleRegisteredEventsTitleClick(){
        if(memberOrgsRecyclerView.getVisibility() == View.VISIBLE){
            memberOrgsArrow.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
            memberOrgsRecyclerView.setVisibility(View.GONE);
        }else{
            memberOrgsRecyclerView.setVisibility(View.VISIBLE);
            memberOrgsArrow.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
        }
    }

    @OnClick(R.id.organizerOrgsTitleLayout)
    public void handleMyEventsTitleClick(){
        if(organizerOrgsRecyclerView.getVisibility() == View.VISIBLE){
            organizerOrgsArrow.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
            organizerOrgsRecyclerView.setVisibility(View.GONE);
        }else{
            organizerOrgsArrow.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
            organizerOrgsRecyclerView.setVisibility(View.VISIBLE);
        }
    }


    public void setOrgs(MyLambencyModel myLambencyModel){
        ArrayList<OrganizationModel> memberOrgs = new ArrayList<>(myLambencyModel.getJoinedOrgs());
        memberOrgAdapter.updateOrgs(memberOrgs);

        ArrayList<OrganizationModel> organizerOrgs = new ArrayList<>(myLambencyModel.getMyOrgs());
        organizerOrgAdapter.updateOrgs(organizerOrgs);
    }

    public void showProgressBar(boolean flag){
        if(flag){
            orgsScroll.setVisibility(View.GONE);
            orgsProgress.setVisibility(View.VISIBLE);
        }else{
            orgsScroll.setVisibility(View.VISIBLE);
            orgsProgress.setVisibility(View.GONE);
        }
    }
}