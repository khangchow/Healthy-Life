package com.myapplication.healthylife.alarmreceiver;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.navigation.NavDeepLinkBuilder;

import com.myapplication.healthylife.MainActivity;
import com.myapplication.healthylife.R;

import java.util.Date;

public class StandUpAlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "CHANNEL 2";
    private static final String CHANNEL_NAME = "STAND UP CHANNEL";
    private static final String BIGTEXT = "Stand up and move a little for one minute!";

    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent notificationIntent = new Intent(context, NotificationActivity.class);

//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addParentStack(NotificationActivity.class);
//        stackBuilder.addNextIntent(notificationIntent);
//
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent pendingIntent = new NavDeepLinkBuilder(context)
                .setComponentName(MainActivity.class)
                .setGraph(R.navigation.my_nav)
                .setDestination(R.id.standUpNoti)
                .createPendingIntent();

        Notification.Builder builder = new Notification.Builder(context);

        Notification notification = builder.setContentTitle("Time to stand!")
                .setStyle(new Notification.BigTextStyle().bigText(BIGTEXT))
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.standup_noti))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(getId(), notification);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(context, StandUpAlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 10000 , broadcast);
    }

    private int getId() {
        return (int) new Date().getTime();
    }
}
