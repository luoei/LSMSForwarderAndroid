package dev.luoei.app.tool.sms.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;

import dev.luoei.app.tool.sms.tool.SmsContentObserver;


public class DemonsSmsObserverService extends Service {
    private final String TAG = "SmsObserver";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //监听短信接受
        Handler handler = new Handler();
        SmsContentObserver sms=new SmsContentObserver(this,handler);
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"),true,sms);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
