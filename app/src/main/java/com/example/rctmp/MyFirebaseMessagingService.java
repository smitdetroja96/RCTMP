package com.example.rctmp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String  notification_title,notification_body;

    public MyFirebaseMessagingService(){}

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        Log.e("ttt:",remoteMessage.getFrom());
        String getTopic = remoteMessage.getFrom().substring(8);
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
//            Toast.makeText(this, remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();

            notification_title = remoteMessage.getNotification().getTitle();
            notification_body = remoteMessage.getNotification().getBody();

            Log.e("---------------------",remoteMessage.getNotification().getTitle());
            Log.e("++++++++++++++++++++++", remoteMessage.getNotification().getBody());
//            remoteMessage.getNotification().
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        int notificationId = 999999;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

//        Toast.makeText(this, notification_title, Toast.LENGTH_SHORT).show();
        if(!getTopic.equals("general")) {
            Intent notifyIntent = new Intent(this, ShowNotificationActivity.class);
            Map<String,String> data = remoteMessage.getData();
            String value = data.get("url").toString();
            notifyIntent.putExtra("url",value);
            Log.e("here:url",remoteMessage.getData().toString());
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                    this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
            );


            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(notification_title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(notification_body))
                    .setContentText(notification_body)
                    .setAutoCancel(true)
                    .setOngoing(false);

            mBuilder.setContentIntent(notifyPendingIntent);
            NotificationManager nc = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, importance);
                nc.createNotificationChannel(mChannel);
            }
            nc.notify(notificationId, mBuilder.build());
        }
        else{
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(notification_title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(notification_body))
                    .setContentText(notification_body)
                    .setAutoCancel(true)
                    .setOngoing(false);

            NotificationManager nc = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, importance);
                nc.createNotificationChannel(mChannel);
            }
            nc.notify(notificationId, mBuilder.build());
        }
    }

}
