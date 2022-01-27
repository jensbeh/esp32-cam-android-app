package com.esp32camera.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.esp32camera.MainActivity;
import com.esp32camera.R;
import com.esp32camera.model.EspCamera;

import java.text.SimpleDateFormat;

// create notifications - https://www.youtube.com/watch?v=tTbd1Mfi-Sk&list=PLrnPJCHvNZuCN52QwGu7YTSLIMrjCF0gM

public class NotificationHandler {
    public static final String CHANNEL_NOTIFICATION_ID = "notificationChannel";
    private MainActivity mainActivity;
    private final NotificationManagerCompat notificationManager;

    public NotificationHandler(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        createNotificationChannels();

        notificationManager = NotificationManagerCompat.from(mainActivity);
    }

    /**
     * method to create notification channel to display notification from espCamera motion detected
     */
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_NOTIFICATION_ID,
                    "Motion Detection Notification",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Channel where motion detection notifications are send!");

            NotificationManager manager = mainActivity.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }

    /**
     * method to display/notify notification from espCamera motion detected with id from espCameraIpAddress
     */
    public void notifyFrom(EspCamera espCamera, com.esp32camera.model.Notification espNotification) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy '-' HH:mm");
        String notificationDate = sdf.format(espNotification.getTimeStamp());

        Intent notificationIntent = new Intent(mainActivity, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.putExtra("FRAGMENT", "NOTIFICATION");

        PendingIntent pendingIntent = PendingIntent.getActivity(mainActivity, (int) System.currentTimeMillis(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(mainActivity, CHANNEL_NOTIFICATION_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Motion detected!")
                .setContentText(espCamera.getName() + " at " + notificationDate)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        String espIpEndStr = espCamera.getIpAddress().substring(espCamera.getIpAddress().lastIndexOf(".") + 1);
        int espIpEndInt = Integer.parseInt(espIpEndStr);

        notificationManager.notify(espIpEndInt, notification);
    }
}
