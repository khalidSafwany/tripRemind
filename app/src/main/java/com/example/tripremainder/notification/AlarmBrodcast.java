package com.example.tripremainder.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.example.tripremainder.R;

import java.util.Random;

public class AlarmBrodcast extends BroadcastReceiver {
    int notificationId = new Random().nextInt(); // just use a counter in some util class...
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    @Deprecated
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String text = bundle.getString("event");
        String endLoc = bundle.getString("end");
        int id =bundle.getInt("tripID");
        //Click on Notification

        Intent intent1 = new Intent(context, DialogNotification.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.putExtra("event",text);
        intent1.putExtra("end",endLoc);
        intent1.putExtra("tripID",id);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent1, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify_001");

        //Notification Builder


        mBuilder.setSmallIcon(R.drawable.ic_baseline_calendar_today_24);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(true);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.build().flags = Notification.FLAG_NO_CLEAR | Notification.PRIORITY_HIGH ;
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setContentTitle(text);
        mBuilder.setContentText(endLoc);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        Notification notification = mBuilder.build();
        notificationManager.notify(1, notification);


    }






}
