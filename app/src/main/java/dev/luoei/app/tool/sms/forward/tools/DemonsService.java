package dev.luoei.app.tool.sms.forward.tools;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import dev.luoei.app.tool.sms.forward.entity.Process;


public class DemonsService extends Service {

    private final String TAG = "Demons";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        DemonsService self = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(ProcessUtil.isAliveProcess(Process.DEMON_SUBDEMON.toString(),self)){
                            Log.v(TAG,Process.DEMON_SUBDEMON.toString()+" is running!");
                        }else {
                            Log.v(TAG,Process.DEMON_SUBDEMON.toString()+" not running, and start...");
                            ServiceUtil.startDemonSubdemon(self);
                            Log.v(TAG,Process.DEMON_SUBDEMON.toString()+" start finished!");
                        }

                        if(ProcessUtil.isAliveProcess(Process.SMSOBSERVER.toString(),self)){
                            Log.v(TAG,Process.SMSOBSERVER.toString()+" is running!");
                        }else {
                            Log.v(TAG,Process.SMSOBSERVER.toString()+" not running, and start...");
                            ServiceUtil.startSmsObserverDemon(self);
                            Log.v(TAG,Process.SMSOBSERVER.toString()+" start finished!");
                        }

                        if(ProcessUtil.isAliveProcess(Process.ROUTER.toString(),self)){
                            Log.v(TAG,Process.ROUTER.toString()+" is running!");
                        }else {
                            Log.v(TAG,Process.ROUTER.toString()+" not running, and start...");
                            ServiceUtil.startRouterDemon(self);
                            Log.v(TAG,Process.ROUTER.toString()+" start finished!");
                        }
                    }
                }).start();
            }
        }).start();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        int anhour=6*1000;
        long triggerAtMillis = SystemClock.elapsedRealtime()+anhour;

        Intent alarmIntent = new Intent(this,DemonsService.class);

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
