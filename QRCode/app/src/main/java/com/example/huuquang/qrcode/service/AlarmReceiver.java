package com.example.huuquang.qrcode.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.huuquang.qrcode.MainActivity;
import com.example.huuquang.qrcode.R;

public class AlarmReceiver extends BroadcastReceiver {
    private static final int REQUEST_CODE = 1;
    private static final int NOTIFICATION_ID = 6578;
    private static final String CHANNEL_ID = "QR";

    @Override
    public void onReceive(Context context, Intent intent) {
        String content = intent.getStringExtra("content");

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE,
                i, PendingIntent.FLAG_UPDATE_CURRENT );
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentText(content)
                .setContentTitle("Công việc sắp tới")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setTicker(content)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationBuilder.setPriority(NotificationManager.IMPORTANCE_MAX);
        }else{
            notificationBuilder.setPriority(Notification.PRIORITY_MAX);
        }
        notificationBuilder.setPriority(Notification.PRIORITY_MAX);

        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);
    }
}
