package com.medicento.retailerappmedi.data;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.medicento.retailerappmedi.PlaceOrderActivity;
import com.medicento.retailerappmedi.R;

import java.util.Map;

public class MessagingService extends FirebaseMessagingService {
    public MessagingService() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData() != null) {
                sendNotification(remoteMessage);
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNotification(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String content = data.get("content");
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "Medicento";
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             @SuppressLint("WrongConstant")
             NotificationChannel nc = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "Medicento_Notification",
                     NotificationManager.IMPORTANCE_MAX );
             nc.setDescription("Medicento For Your Help");
             nc.enableLights(true);
             nc.setLightColor(Color.RED);
             nc.setVibrationPattern(new long[]{0,1000,500,1000});
            nc.enableVibration(true);
            nm.createNotificationChannel(nc);
         }
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
         nb.setSmallIcon(R.drawable.ic_user)
                 .setAutoCancel(true)
                 .setDefaults(Notification.DEFAULT_ALL)
                 .setWhen(System.currentTimeMillis())
                 .setTicker("Medi")
                 .setContentTitle(title)
                 .setContentText(content)
                 .setContentInfo("info");
         nm.notify(1, nb.build());
    }
}
