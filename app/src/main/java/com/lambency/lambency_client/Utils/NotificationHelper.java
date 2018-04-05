package com.lambency.lambency_client.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.lambency.lambency_client.Activities.AcceptRejectActivity;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Services.MyNotificationServices;

import java.nio.channels.Channel;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by lshan on 4/5/2018.
 */

public class NotificationHelper {

    private static int id = 0;
    private static ChannelInfo joinRequestChannel = new ChannelInfo("lambency-join", "Join Requests", "Notifications for users requesting to join organizations will appear here.");

    public static void sendJoinRequestNotification(Context context, String user, String uid, String org, String org_id){
        createNotificationChannel(context, joinRequestChannel);

        Intent intent = new Intent(context, AcceptRejectActivity.class);
        intent.putExtra("org_id", org_id);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Accepting the user who wants to join
        Intent acceptIntent = new Intent(context, MyNotificationServices.class);
        acceptIntent.putExtra("approved", true);
        acceptIntent.putExtra("user_id", uid);
        acceptIntent.putExtra("org_id", org_id);
        acceptIntent.putExtra("notif_id", id);
        acceptIntent.setAction(MyNotificationServices.JOIN_REQUEST);
        PendingIntent acceptPendingIntent = PendingIntent.getService(context, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Denying the user who wants to join
        Intent denyIntent = new Intent(context, MyNotificationServices.class);
        denyIntent.putExtra("approved", false);
        denyIntent.putExtra("user_id", uid);
        denyIntent.putExtra("org_id", org_id);
        denyIntent.putExtra("notif_id", id);
        denyIntent.setAction(MyNotificationServices.JOIN_REQUEST);
        PendingIntent denyPendingIntent = PendingIntent.getService(context, 0, denyIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, joinRequestChannel.id)
                .setSmallIcon(R.drawable.ic_notification_lambency)
                .setContentTitle("Join Request")
                .setContentText(user + " would like to join " + org + ".")
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .addAction(new NotificationCompat.Action(0, "Approve", acceptPendingIntent))
                .addAction(new NotificationCompat.Action(0, "Deny", denyPendingIntent));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(id++, mBuilder.build());
    }

    private static void createNotificationChannel(Context context, ChannelInfo info){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            int importance = NotificationManagerCompat.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(info.id, info.name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(info.description);
            // Register the channel with the system
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public static class ChannelInfo{

        public String id, name, description;

        public ChannelInfo(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }
    }


}
