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

public class AsyncEventCreationTask extends AsyncTask <Void, Void, Void>{

    private EventModel eventModel;
    private Context context;

    public AsyncEventCreationTask(Context context, EventModel eventModel){
        this.eventModel = eventModel;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {

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
                System.out.println("location send was   !!! "+ eventModel.getLocation());

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


        return null;
    }
}
