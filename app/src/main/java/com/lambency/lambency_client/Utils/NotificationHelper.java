package com.lambency.lambency_client.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.lambency.lambency_client.Activities.AcceptRejectActivity;
import com.lambency.lambency_client.Activities.EventDetailsActivity;
import com.lambency.lambency_client.Activities.MessageListActivity;

import com.lambency.lambency_client.Activities.UserAcceptRejectActivity;

import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Services.MyNotificationServices;

import java.nio.channels.Channel;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by lshan on 4/5/2018.
 */

public class NotificationHelper {

    private static int id = 0;

    private static ChannelInfo joinRequestChannel = new ChannelInfo("lambency-join", "Join Requests", "Notifications for users requesting to join your organizations.");
    private static ChannelInfo chatMessageChannel = new ChannelInfo("lambency-chat", "Chat messaging", "Notifications about chat messaging.");
    private static ChannelInfo inviteChannel = new ChannelInfo("lambency-invite", "Organization Invites", "Notifications from organizations requesting you join their organizations.");
    private static ChannelInfo eventUpdateChannel = new ChannelInfo("lambency-event", "Event Updates", "Notifications about changes to events you're registered for.");

    public static void sendJoinRequestNotification(Context context, String user, String uid, String org, String org_id){
        int contentRequestCode = 0, acceptRequestCode = 1, denyRequestCode = 2;

        createNotificationChannel(context, joinRequestChannel);

        Intent intent = new Intent(context, AcceptRejectActivity.class);
        intent.putExtra("org_id", org_id);
        PendingIntent contentIntent = PendingIntent.getActivity(context, contentRequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Accepting the user who wants to join
        Intent acceptIntent = new Intent(context, MyNotificationServices.class);
        acceptIntent.putExtra("approved", true);
        acceptIntent.putExtra("user_id", uid);
        acceptIntent.putExtra("org_id", org_id);
        acceptIntent.putExtra("notif_id", id);
        acceptIntent.setAction(MyNotificationServices.JOIN_REQUEST);
        PendingIntent acceptPendingIntent = PendingIntent.getService(context, acceptRequestCode, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Denying the user who wants to join
        Intent denyIntent = new Intent(context, MyNotificationServices.class);
        denyIntent.putExtra("approved", false);
        denyIntent.putExtra("user_id", uid);
        denyIntent.putExtra("org_id", org_id);
        denyIntent.putExtra("notif_id", id);
        denyIntent.setAction(MyNotificationServices.JOIN_REQUEST);
        PendingIntent denyPendingIntent = PendingIntent.getService(context, denyRequestCode, denyIntent, PendingIntent.FLAG_UPDATE_CURRENT);


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


        Notification notification = mBuilder.build();
        // Cancel the notification after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(id++, notification);
    }


    public static void sendChatMessageNotification(Context context, String name, String msgId, String chatId)
    {
        int contentRequestCode = 0, acceptRequestCode = 1, denyRequestCode = 2;
        createNotificationChannel(context, chatMessageChannel);

        Intent intent = new Intent(context, MessageListActivity.class);
        intent.putExtra("msgId", msgId);
        intent.putExtra("chatId", chatId);
        PendingIntent contentIntent = PendingIntent.getActivity(context, contentRequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, joinRequestChannel.id)
                .setSmallIcon(R.drawable.ic_notification_lambency)
                .setContentTitle("Chat message")
                .setContentText(name + "sent you a message.")
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent);
  
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Notification notification = mBuilder.build();
        // Cancel the notification after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(id++, notification);
    }
  
    public static void sendInviteNotification(Context context, String org, String org_id){
        int contentRequestCode = 0, acceptRequestCode = 1, denyRequestCode = 2;

        createNotificationChannel(context, inviteChannel);

        Intent intent = new Intent(context, UserAcceptRejectActivity.class);
        intent.putExtra("org_id", org_id); //Not sure I need to pass this
        PendingIntent contentIntent = PendingIntent.getActivity(context, contentRequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Accepting the invite notification
        Intent joinIntent = new Intent(context, MyNotificationServices.class);
        joinIntent.putExtra("joined", true);
        joinIntent.putExtra("org_id", org_id);
        joinIntent.putExtra("notif_id", id);
        joinIntent.setAction(MyNotificationServices.ORG_INVITE);
        PendingIntent joinPendingIntent = PendingIntent.getService(context, acceptRequestCode, joinIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Ignoring the invite notification
        Intent ignoreIntent = new Intent(context, MyNotificationServices.class);
        ignoreIntent.putExtra("joined", false);
        ignoreIntent.putExtra("org_id", org_id);
        ignoreIntent.putExtra("notif_id", id);
        ignoreIntent.setAction(MyNotificationServices.ORG_INVITE);
        PendingIntent denyPendingIntent = PendingIntent.getService(context, denyRequestCode, ignoreIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, joinRequestChannel.id)
                .setSmallIcon(R.drawable.ic_notification_lambency)
                .setContentTitle("Organization Invite")
                .setContentText("You're invited to join " + org + ".")
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .addAction(new NotificationCompat.Action(0, "Join", joinPendingIntent))
                .addAction(new NotificationCompat.Action(0, "Ignore", denyPendingIntent));


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Notification notification = mBuilder.build();
        // Cancel the notification after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(id++, notification);
    }

    public static void sendEventUpdateNotification(Context context, String event_id, String eventName){
        int contentRequestCode = 0;

        createNotificationChannel(context, eventUpdateChannel);

        Intent intent = new Intent(context, EventDetailsActivity.class);
        intent.putExtra("event_id", Integer.parseInt(event_id));
        PendingIntent contentIntent = PendingIntent.getActivity(context, contentRequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, joinRequestChannel.id)
                .setSmallIcon(R.drawable.ic_notification_lambency)
                .setContentTitle("Event Update")
                .setContentText("Details for " + eventName + " have been changed.")
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Notification notification = mBuilder.build();
        // Cancel the notification after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(id++, notification);
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
