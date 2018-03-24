package com.lambency.lambency_client.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lambency.lambency_client.Activities.AcceptRejectActivity;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import com.lambency.lambency_client.Activities.BottomBarActivity;
import com.lambency.lambency_client.Activities.EventCreationActivity;
import com.lambency.lambency_client.Activities.LoginActivity;
import com.lambency.lambency_client.Activities.OrgCreationActivity;

import com.lambency.lambency_client.Adapters.AcceptRejectAdapter;

import com.lambency.lambency_client.Adapters.MyLambencyTabsAdapter;
import com.lambency.lambency_client.Adapters.SearchTabsAdapter;
import com.lambency.lambency_client.Models.MyLambencyModel;

import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Utils.SharedPrefsHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyLambencyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.fabMenu)
    FloatingActionsMenu floatingActionsMenu;

    @BindView(R.id.eventFab)
    FloatingActionButton eventFab;

    @BindView(R.id.orgFab)
    FloatingActionButton orgFab;


    private OnFragmentInteractionListener mListener;
    private MyLambencyTabsAdapter myLambencyTabsAdapter;
    private Context context;

    public MyLambencyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyLambencyFragment newInstance(String param1, String param2) {
        MyLambencyFragment fragment = new MyLambencyFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_lambency, container, false);
        ButterKnife.bind(this, view);

        tabLayout.addTab(tabLayout.newTab().setText("Events"));
        tabLayout.addTab(tabLayout.newTab().setText("Organizations"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        myLambencyTabsAdapter = new MyLambencyTabsAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount(), context);
        viewPager.setAdapter(myLambencyTabsAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Does not work... color still doesn't switch
                tabLayout.setScrollPosition(tab.getPosition(), 0f, true);

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /*
        if(UserModel.myUserModel.getMyOrgs().size() == 0)
        {
            createEventButton.setVisibility(View.GONE);
        }

        Button logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                SharedPreferences sharedPref = SharedPrefsHelper.getSharedPrefs(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove("myauth");
                editor.apply();
            }
        });
        */


        ((BottomBarActivity) getActivity())
                .setActionBarTitle("My Lambency");
        ((BottomBarActivity) getActivity()).getSupportActionBar().setElevation(0);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onProfileFragmentInteraction(uri);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_edit, menu);
    }


    @OnClick(R.id.eventFab)
    public void handleEventFabClick(){
        Intent intent = new Intent(getActivity(), EventCreationActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.orgFab)
    public void handleOrgFabClick(){
        Intent intent = new Intent(getActivity(), OrgCreationActivity.class);
        startActivity(intent);
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
        void onProfileFragmentInteraction(Uri uri);
    }


    public MyLambencyTabsAdapter getMyLambencyTabsAdapter(){
        return myLambencyTabsAdapter;
    }
}
