package com.example.notebox;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BildirimYakalayici extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager bildirimYoneticisi =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification bildirim = intent.getParcelableExtra("nesne");
        int id = intent.getExtras().getInt("id");
        bildirimYoneticisi.notify(id,bildirim);
        Log.e("id", ""+id);
    }
}
