package com.example.huuquang.qrcode.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.huuquang.qrcode.MainActivity;
import com.example.huuquang.qrcode.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessageReceiver extends FirebaseMessagingService {
    private static final int REQUEST_CODE = 1;
    private static final int NOTIFICATION_ID = 6578;
    private static final String CHANNEL_ID = "QR";

    public MessageReceiver() {
        super();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        final String title = remoteMessage.getNotification().getTitle();
        final String message = remoteMessage.getNotification().getBody();

        showNotifications(title, message);
    }

    private void showNotifications(String title, String msg) {
        Intent i = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE,
                i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText(msg)
                .setContentTitle("Something")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setTicker(msg)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationBuilder.setPriority(NotificationManager.IMPORTANCE_MAX);
        }else{
            notificationBuilder.setPriority(Notification.PRIORITY_MAX);
        }

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }
}
