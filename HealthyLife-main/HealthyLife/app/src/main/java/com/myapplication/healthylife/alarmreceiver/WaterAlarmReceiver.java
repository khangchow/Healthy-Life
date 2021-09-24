package com.myapplication.healthylife.alarmreceiver;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.navigation.NavDeepLinkBuilder;
import androidx.navigation.Navigation;

import com.myapplication.healthylife.MainActivity;
import com.myapplication.healthylife.NotificationActivity3;
import com.myapplication.healthylife.R;

import java.util.Date;

public class WaterAlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "CHANNEL WATER";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, NotificationActivity3.class);

//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addParentStack(NotificationActivity3.class);
//        stackBuilder.addNextIntent(notificationIntent);
//
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        PendingIntent pendingIntent = new NavDeepLinkBuilder(context)
                .setComponentName(MainActivity.class)
                .setGraph(R.navigation.my_nav)
                .setDestination(R.id.drinkWaterNoti)
                .createPendingIntent();

        Notification.Builder builder = new Notification.Builder(context);

        Notification notification = builder.setContentTitle("Water Drinking Time!")
                .setContentText("Staying Hydrated is important to be Fit and Active")
                .setTicker("Water!")
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.water_noti))
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
                    "Drink Water Notification",
                    IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(getId(), notification);
    }

    private int getId() {
        return (int) new Date().getTime();
    }
}
