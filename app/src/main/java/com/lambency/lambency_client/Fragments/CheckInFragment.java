package com.lambency.lambency_client.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.lambency.lambency_client.Activities.BottomBarActivity;
import com.lambency.lambency_client.Models.EventAttendanceModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

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
 * {@link CheckInFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckInFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.sendButton)
    Button sendButton;
    @BindView(R.id.enterCode)
    EditText code2Send;
    @BindView(R.id.textViewDateValue)
    TextView textViewdate;
    @BindView(R.id.checkInDisp)
    TextView checkInDisplay;
    @BindView(R.id.textTimer)
    TextView countUpTimer;

    @BindView(R.id.qrButton)
    Button qrButton;

    private String codeString;
    private long startTimeCounter = 0L;
    private Handler myHandler = new Handler();
    long timeInMillies = 0L;
    long timeSwap = 0L;
    long finalTime = 0L;

    private final int CAMERA_REQUEST = 0;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CheckInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckInFragment newInstance(String param1, String param2) {
        CheckInFragment fragment = new CheckInFragment();
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

    private Runnable updateTimerMethod = new Runnable() {

        public void run() {
            timeInMillies = SystemClock.uptimeMillis() - startTimeCounter;
            finalTime = timeSwap + timeInMillies;

            int seconds = (int) (finalTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (finalTime % 1000);
            countUpTimer.setText("" + minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliseconds));
            myHandler.postDelayed(this, 0);
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_check_in2, container, false);
        ButterKnife.bind(this, v);

        String oAuth = UserModel.myUserModel.getOauthToken();
        System.out.println(oAuth);

        /// For Show Date
        String currentDateString = DateFormat.getDateInstance().format(new Date());
        // textView is the TextView view that should display it
        textViewdate.setText(currentDateString);

        ((BottomBarActivity) getActivity())
                .setActionBarTitle("Check In");

        return v;
    }


    @OnClick(R.id.sendButton)
    public void handleSendClick() {
        codeString = code2Send.getText().toString();

        int time = (int) (System.currentTimeMillis());
        final Timestamp tsTemp = new Timestamp(time);


        if (codeString.matches("")) {
            Toast.makeText(getContext(), "Please enter the event start code", Toast.LENGTH_LONG).show();
        }

        EventAttendanceModel eventAttendanceModel = new EventAttendanceModel(UserModel.myUserModel.getUserId(),tsTemp,codeString);


        //Retrofits
        LambencyAPIHelper.getInstance().sendClockInCode(UserModel.myUserModel.getOauthToken(), eventAttendanceModel).enqueue(new retrofit2.Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("Error cause body returned null or bad response code in register response.");
                    Toast.makeText(getApplicationContext(), "Problem checkingInOut", Toast.LENGTH_LONG).show();
                }
                //when response is back
                Integer ret = response.body();
                if (ret == 0) {
                    System.out.println("successfully checked in for the event");
                    Toast.makeText(getApplicationContext(), "You have successfully checked in", Toast.LENGTH_LONG).show();
                    if (startTimeCounter<1) {
                        Toast.makeText(getApplicationContext(), "time sent was" + tsTemp.getHours()+":"
                                +tsTemp.getMinutes(), Toast.LENGTH_LONG).show();
                        startTimeCounter = SystemClock.uptimeMillis();
                        myHandler.postDelayed(updateTimerMethod, 0);
                    }
                    else{
                        timeSwap += timeInMillies;
                        myHandler.removeCallbacks(updateTimerMethod);
                    }
                }
                else if (ret ==1){
                    Toast.makeText(getApplicationContext(), "the oauth token is invalid", Toast.LENGTH_LONG).show();
                }
                else if (ret == 2) {
                    System.out.println("undetermined error");
                    Toast.makeText(getApplicationContext(), "Unknown Error", Toast.LENGTH_LONG).show();
                }
                else if (ret ==3){
                    Toast.makeText(getApplicationContext(), "You are not signed up for the event", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable throwable) {
                //when failure
                System.out.println("FAILED CALL");
                Toast.makeText(getApplicationContext(), "Failure code was not accepted", Toast.LENGTH_LONG).show();
            }
        });


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

    @OnClick(R.id.qrButton)
    public void handleQRClick(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST);
        }else {
            launchScanner();
        }
    }

    private void launchScanner(){
        IntentIntegrator.forSupportFragment(this)
                .setOrientationLocked(false)
                .setPrompt("Scan a Lambency QR Code")
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                .initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            // handle scan result
            String code = scanResult.getContents();
            System.out.println("From QR: " + code);
            code2Send.setText(code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    launchScanner();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
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