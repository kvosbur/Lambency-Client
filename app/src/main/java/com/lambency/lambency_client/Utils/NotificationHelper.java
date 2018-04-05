package com.lambency.lambency_client.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.lambency.lambency_client.R;

import java.nio.channels.Channel;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by lshan on 4/5/2018.
 */

public class NotificationHelper {


    private static ChannelInfo joinRequestChannel = new ChannelInfo("lambency-join", "Join Requests", "Notifications for users requesting to join organizations will appear here.");

    public static void sendJoinRequestNotification(Context context, String user, String org){
        createNotificationChannel(context, joinRequestChannel);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, joinRequestChannel.id)
                .setSmallIcon(R.drawable.ic_notification_default)
                .setContentTitle("Join Request")
                .setContentText(user + " would like to join " + org + ".")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, mBuilder.build());
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
