package com.lambency.lambency_client.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lambency.lambency_client.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.myFAB)
    FloatingActionButton myButton;

    @BindView(R.id.firstName)
    TextView firstNameText;

    @BindView(R.id.editFirstName)
    EditText editFirstName;

    @BindView(R.id.lastName)
    TextView lastNameText;

    @BindView(R.id.editLastName)
    EditText editLastName;

    @BindView(R.id.phoneNum)
    TextView phoneNum;

    @BindView(R.id.editPhoneNum)
    EditText editPhoneNum;

    @BindView(R.id.emailOfUser)
    TextView emailOfUser;

    @BindView(R.id.editEmail)
    EditText editEmail;

    boolean edit = false;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
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
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
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


    @OnClick(R.id.myFAB)
    public void clickSend() {

        if (!edit) {
            firstNameText.setVisibility(View.INVISIBLE);
            editFirstName.setText(firstNameText.getText());
            editFirstName.setVisibility(View.VISIBLE);

            lastNameText.setVisibility(View.INVISIBLE);
            editLastName.setText(lastNameText.getText());
            editLastName.setVisibility(View.VISIBLE);

            phoneNum.setVisibility(View.INVISIBLE);
            editPhoneNum.setText(phoneNum.getText());
            editPhoneNum.setVisibility(View.VISIBLE);

            emailOfUser.setVisibility(View.INVISIBLE);
            editEmail.setText(emailOfUser.getText());
            editEmail.setVisibility(View.VISIBLE);
            edit = true;

            myButton.setImageResource(R.drawable.ic_check_black_24dp);
        } else {
            editFirstName.setVisibility(View.INVISIBLE);
            firstNameText.setText(editFirstName.getText());
            firstNameText.setVisibility(View.VISIBLE);

            editLastName.setVisibility(View.INVISIBLE);
            lastNameText.setText(editLastName.getText());
            lastNameText.setVisibility(View.VISIBLE);

            editPhoneNum.setVisibility(View.INVISIBLE);
            phoneNum.setText(editPhoneNum.getText());
            phoneNum.setVisibility(View.VISIBLE);

            editEmail.setVisibility(View.INVISIBLE);
            emailOfUser.setText(editEmail.getText());
            emailOfUser.setVisibility(View.VISIBLE);
            edit = false;

            myButton.setImageResource(R.drawable.ic_mode_edit_black_24dp);
        }
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
}
