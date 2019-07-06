package com.example.rctmp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    Context con;
    String book_name,book_date;

    @Override
    public void onReceive(Context context, Intent intent){

        con = context;

        book_name = intent.getStringExtra("title");
        book_date = intent.getStringExtra("check_in_date");

        int alarmId = intent.getIntExtra("requestCode", 0);


        int notificationId = alarmId;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Book Due !!!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(book_name + "is due on " + book_date + "\n" + "Ignore If Returned"))
                .setContentText(book_name + "is due on" + book_date)
                .setAutoCancel(true)
                .setOngoing(false);

        NotificationManager nc = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            nc.createNotificationChannel(mChannel);
        }
        nc.notify(notificationId, mBuilder.build());
    }
}

