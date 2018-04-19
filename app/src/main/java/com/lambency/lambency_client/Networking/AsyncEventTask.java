package com.lambency.lambency_client.Networking;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lambency.lambency_client.Models.EventModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lshan on 4/8/2018.
 */

public class AsyncEventTask extends AsyncTask <Void, Void, Void>{

    public static final int CREATE_MODE = 0;
    public static final int EDIT_MODE = 1;

    private EventModel eventModel;
    private Context context;
    private String message;
    private int mode;

    public AsyncEventTask(Context context, EventModel eventModel, String message, int mode){
        this.eventModel = eventModel;
        this.context = context;
        this.mode = mode;
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        if(mode == CREATE_MODE) {
            createEvent(eventModel);
        }

        if(mode == EDIT_MODE){
            updateEvent(eventModel, message);
        }

        return null;
    }


    private void createEvent(EventModel event){
        LambencyAPIHelper.getInstance().createEvent(eventModel).enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                if (response.body() == null || response.code() != 200) {
                    Toast.makeText(context, "Server error!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //when response is back
                EventModel createdEvent = response.body();
                System.out.println("Created Event: " + createdEvent);
                System.out.println("location send was   !!! " + eventModel.getPrettyAddress());

                if (createdEvent == null) {
                    Toast.makeText(context, "Event error!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Status now contains event_id
                int event_id = createdEvent.getEvent_id();

                Toast.makeText(context, "Success creating event!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable throwable) {
                Toast.makeText(context, "Server error!", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private void updateEvent(EventModel event, String message){
        System.out.println("IT IS A WORKING");
        LambencyAPIHelper.getInstance().postUpdateEvent(event, message).enqueue(new Callback<Integer>() {
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
                System.out.println("FAILED CALL2");
            }
        });
    }
}
