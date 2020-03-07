package dev.luoei.app.tool.usb.share;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.Nullable;

import com.orhanobut.logger.Logger;

public class UsbShareService extends Service {

    String TAG = "Demons";

    @Override
    public void onCreate() {
        super.onCreate();

        LogService.initLogger();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final UsbShareService self = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Logger.v("USB共享|开启|start...");
                        UsbShare.setOpen(true);
                        Logger.v("USB共享|开启|start finished!");
                    }
                }).start();
            }
        }).start();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        int anhour=60*1000;
        long triggerAtMillis = SystemClock.elapsedRealtime()+anhour;

        Intent alarmIntent = new Intent(this,UsbShareService.class);

        PendingIntent pendingIntent = PendingIntent.getService(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//  4.4
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtMillis, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtMillis, pendingIntent);
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
