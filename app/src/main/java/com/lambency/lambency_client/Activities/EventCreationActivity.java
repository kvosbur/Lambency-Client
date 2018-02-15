package com.lambency.lambency_client.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lambency.lambency_client.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class EventCreationActivity extends AppCompatActivity {
    String eventName, dateOfEvent, addressOfEvent, description, contact;
    ImageView eventImage;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);
        this.context = this;
        ButterKnife.bind(this);

        //Saving details when button pressed
        final Button saveDetails = findViewById(R.id.saveDetailsButton);
        saveDetails.setOnClickListener(new View.OnClickListener() {
            EditText eName = (EditText) findViewById(R.id.nameOfEvent);
            EditText eDate = (EditText) findViewById(R.id.dateOfEvent);
            EditText eAddr = (EditText) findViewById(R.id.addressOfEvent);
            EditText eDescrip = (EditText) findViewById(R.id.descriptionOfEvent);
            EditText eContact = (EditText) findViewById(R.id.contactForEvent);

            @Override
            public void onClick(View v) {
                eventName = eName.getText().toString();
                dateOfEvent = eDate.getText().toString();
                addressOfEvent = eAddr.getText().toString();
                description = eDescrip.getText().toString();
                contact = eContact.getText().toString();

                //Go back to main page now
                Intent myIntent = new Intent(EventCreationActivity.this,
                        MainActivity.class);
                startActivity(myIntent);
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






