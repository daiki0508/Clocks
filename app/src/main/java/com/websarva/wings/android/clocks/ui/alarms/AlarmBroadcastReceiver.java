package com.websarva.wings.android.clocks.ui.alarms;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.websarva.wings.android.clocks.R;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(context.getString(R.string.notification_alarm_channel_name),context.getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(context.getString(R.string.notification_description_alarms));
            channel.enableVibration(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), null);
            channel.setShowBadge(true);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.notification_alarm_channel_name));
        builder.setSmallIcon(android.R.drawable.ic_dialog_info);
        builder.setContentText(context.getString(R.string.notification_description_alarms));
        builder.setAutoCancel(true);

        Notification notification = builder.build();

        manager.notify(1, notification);
    }
}
