package com.example.rctmp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

        Intent intent1 = new Intent(context,IssuedBooksActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,alarmId,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Book Due !!!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(book_name + "is due on " + book_date + "\n" + "Ignore If Returned"))
                .setContentText(book_name + "is due on" + book_date)
                .setContentIntent(pendingIntent)
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

