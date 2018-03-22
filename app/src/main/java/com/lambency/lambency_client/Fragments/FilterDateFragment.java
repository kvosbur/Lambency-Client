package com.lambency.lambency_client.Fragments;

import android.app.DatePickerDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lambency.lambency_client.Activities.BottomBarActivity;
import com.lambency.lambency_client.Models.EventModel;
import com.lambency.lambency_client.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Evan on 3/16/2018.
 */

public class FilterDateFragment extends Fragment
{

    @BindView(R.id.startDateButtonFilter)
    Button startTime;

    @BindView(R.id.endDateButtonFilter)
    Button endTime;


    String startDate = "";

    String endDate = "";

    Calendar myCalendar = Calendar.getInstance();

    private void updateLabel(Button date) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        date.setText(sdf.format(myCalendar.getTime()));
    }

    DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(startTime);
            startDate = startTime.toString();
        }
    };

    DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(endTime);
            endDate = endTime.toString();
        }
    };

    public FilterDateFragment() {
        // Required empty public constructor
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter_date, container, false);
        ButterKnife.bind(this, view);

        initObjects();

        return view;
    }



    public void initObjects() {
        //startTime = (Button) getView().findViewById(R.id.startDateButton);

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 new DatePickerDialog(getActivity(), dateStart, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                //Toast.makeText(getActivity(), "Yey", Toast.LENGTH_LONG).show();
            }
        });

        //endTime = getView().findViewById(R.id.endDateButton);

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (startDate.compareTo("") == 0) {
                    Toast.makeText(getActivity(), "Please select a start date first!", Toast.LENGTH_LONG).show();
                    return;
                }

                new DatePickerDialog(getActivity(), dateEnd, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                Toast.makeText(getActivity(), "Yey", Toast.LENGTH_LONG).show();
            }
        });


    }

    //public void onCreate(Bundle savedInstanceState) {
    //    super.onCreate(savedInstanceState);
    //
    //    setHasOptionsMenu(true);
    //}


}
