package com.lambency.lambency_client.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lambency.lambency_client.Models.EventModel;
import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Utils.ImageHelper;
import com.lambency.lambency_client.Utils.TimeHelper;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventCreationActivity extends AppCompatActivity {

    @BindView(R.id.eventImage)
    ImageView eventImage;

    @BindView(R.id.nameOfEvent)
    EditText nameEdit;

    @BindView(R.id.dateOfEvent)
    Button dateButton;

    @BindView(R.id.startTimeButton)
    Button startTimeButton;

    @BindView(R.id.endTimeButton)
    Button endTimeButton;

    @BindView(R.id.addressOfEvent)
    EditText addressEdit;

    @BindView(R.id.descriptionOfEvent)
    EditText descriptionEdit;

    @BindView(R.id.contactForEvent)
    EditText contactEdit;

    private boolean editing = false;
    String eventName, dateOfEvent, addressOfEvent, description, contact;
    private Context context;

    private EventModel eventModel;

    Button date,startTime,endTime;

    private String imagePath = "";

    Timestamp startingTime,endingTime;

    Calendar myCalendar = Calendar.getInstance();


    //for date
    DatePickerDialog.OnDateSetListener dateD = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    //for start time
    TimePickerDialog.OnTimeSetListener time_listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            // store the data in one string and set it to text
            String time1 = String.valueOf(hour) + ":" + String.valueOf(minute);
            startTime.setText(time1);
            startingTime = new Timestamp(myCalendar.get(Calendar.YEAR)-1900,myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DATE),
                    hour,minute,0,0);
        }
    };

    //for end time
    TimePickerDialog.OnTimeSetListener time_listener2 = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            // store the data in one string and set it to text
            String time1 = String.valueOf(hour) + ":" + String.valueOf(minute);
            endTime.setText(time1);
            endingTime = new Timestamp(myCalendar.get(Calendar.YEAR)-1900,myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DATE),
                    hour,minute,0,0);
        }
    };


    private void updateLabel() {
            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

            date.setText(sdf.format(myCalendar.getTime()));
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);
        this.context = this;
        ButterKnife.bind(this);


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            //TODO error check
            int event_id = bundle.getInt("event_id");
            getEventInfo(event_id);
            editing = true;
        }


        //Saving details when button pressed
        final Button saveDetails = findViewById(R.id.saveDetailsButton);
        date = (Button) findViewById(R.id.dateOfEvent);
        startTime = (Button) findViewById(R.id.startTimeButton);
        endTime = (Button) findViewById(R.id.endTimeButton);

        date.setOnClickListener(new View.OnClickListener() {
            //on click of date button
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EventCreationActivity.this, dateD, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                new TimePickerDialog(EventCreationActivity.this,time_listener,hour,minute,false).show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                new TimePickerDialog(EventCreationActivity.this,time_listener2,hour,minute,false).show();
            }
        });



        saveDetails.setOnClickListener(new View.OnClickListener() {
            EditText eName = (EditText) findViewById(R.id.nameOfEvent);
            //EditText eDate = (EditText) findViewById(R.id.dateOfEvent);
            EditText eAddr = (EditText) findViewById(R.id.addressOfEvent);
            EditText eDescrip = (EditText) findViewById(R.id.descriptionOfEvent);
            EditText eContact = (EditText) findViewById(R.id.contactForEvent);


            @Override
            public void onClick(View v) {

                if (editing) {
                    Bitmap bm;
                    if (imagePath.equals("")) {
                        //Use default profile image
                        bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_default_avatar);
                    } else {
                        bm = BitmapFactory.decodeFile(imagePath);
                    }
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                        byte[] b = baos.toByteArray();
                        String encodedProfile = Base64.encodeToString(b, Base64.DEFAULT);

                        eventModel.setName(nameEdit.getText().toString());
                        eventModel.setImageFile(encodedProfile);
                        eventModel.setOrg_id(2);
                        eventModel.setStart(startingTime);
                        eventModel.setEnd(endingTime);
                        eventModel.setDescription(descriptionEdit.getText().toString());
                        eventModel.setLocation(addressEdit.getText().toString());

                        updateEvent(eventModel);

                        Intent intent = new Intent(context, EventDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("event_id", eventModel.getEvent_id());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                } else {

                    eventName = eName.getText().toString();
                    //dateOfEvent = eDate.getText().toString();
                    addressOfEvent = eAddr.getText().toString();
                    description = eDescrip.getText().toString();
                    contact = eContact.getText().toString();

                    //making image string....
                    Bitmap bm;
                    if (imagePath.equals("")) {
                        //Use default profile image
                        bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_default_avatar);
                    } else {
                        bm = BitmapFactory.decodeFile(imagePath);
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                    byte[] b = baos.toByteArray();
                    String encodedProfile = Base64.encodeToString(b, Base64.DEFAULT);
                    //encoded profile is the image string


                    if (eventName.matches("") || addressOfEvent.matches("") || description.matches("") || contact.matches("") || startingTime == null || endingTime == null) {
                        Toast.makeText(getApplicationContext(), "You did not enter everything", Toast.LENGTH_LONG).show();
                        //saveDetails.setVisibility(View.GONE);
                    }

                //Go back to main page now
                if (!(eventName.matches("") || addressOfEvent.matches("") || description.matches("") || contact.matches("")  || startingTime == null || endingTime == null)) {
                    //the EventModel object to send to server(use this evan)
                    eventModel = new EventModel(encodedProfile,eventName, UserModel.myUserModel.getMyOrgs().get(0),startingTime,endingTime,description,addressOfEvent);

                        LambencyAPIHelper.getInstance().createEvent(eventModel).enqueue(new Callback<EventModel>() {
                            @Override
                            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                                if (response.body() == null || response.code() != 200) {
                                    Toast.makeText(getApplicationContext(), "Server error!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                //when response is back
                                EventModel createdEvent = response.body();
                                System.out.println("Created Event: " + createdEvent);

                                if (createdEvent == null) {
                                    Toast.makeText(getApplicationContext(), "Event error!", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Status now contains event_id
                                int event_id = createdEvent.getEvent_id();

                                Toast.makeText(getApplicationContext(), "Success creating event!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<EventModel> call, Throwable throwable) {
                                Toast.makeText(getApplicationContext(), "Server error!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });

                        Intent myIntent = new Intent(EventCreationActivity.this,
                                MainActivity.class);
                        startActivity(myIntent);
                    }
                }
            }
        });

    }


    private void updateEvent(EventModel event){
        LambencyAPIHelper.getInstance().postUpdateEvent(event).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("ERROR!!!!!");
                    return;
                }
                //when response is back
                Integer ret = response.body();
                if(ret == 0){
                    System.out.println("successfully updated event");
                }
                else{
                    System.out.println("failed to update event");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable throwable) {
                //when failure
                System.out.println("FAILED CALL");
            }
        });
    }

    private void getEventInfo(final int event_id){


        LambencyAPIHelper.getInstance().getEventSearchByID(Integer.toString(event_id)).enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("ERROR!!!!!");
                }
                //when response is back
                EventModel event= response.body();
                if(event == null){
                    System.out.println("failed to event");
                }
                else{
                    System.out.println("Got event data!");

                    eventModel = event;

                    nameEdit.setText(eventModel.getName());
                    descriptionEdit.setText(eventModel.getDescription());
                    addressEdit.setText(eventModel.getLocation());

                    startingTime = eventModel.getStart();
                    endingTime = eventModel.getEnd();
                    startTimeButton.setText(TimeHelper.hourFromTimestamp(startingTime));
                    endTimeButton.setText(TimeHelper.hourFromTimestamp(endingTime));

                    dateButton.setText(TimeHelper.dateFromTimestamp(startingTime));

                    eventImage.setImageBitmap(ImageHelper.stringToBitmap(eventModel.getImageFile()));
                }
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable throwable) {
                //when failure
                System.out.println("FAILED CALL");

            }
        });
    }

    //Setting event image
    @OnClick(R.id.eventImage)
    public void setEventImage(){
        EasyImage.openChooserWithGallery(this, "Select Event Image", 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {
                //Handle the images

                Picasso.Builder builder = new Picasso.Builder(context);
                builder.listener(new Picasso.Listener()
                {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                    {
                        exception.printStackTrace();
                    }
                });

                imagePath = imagesFiles.get(0).getPath();

                builder.build()
                        .load(new File(imagesFiles.get(0).getPath()))
                        .error(R.drawable.ic_books)
                        .into(eventImage);

                /*
                Bitmap bitmap = BitmapFactory.decodeFile(imagesFiles.get(0).getPath(), null);
                profileImage.setImageBitmap(bitmap);
                */
            }
        });
    }



}






