package dev.luoei.app.tool.sms.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.Logger;

import dev.luoei.app.tool.sms.tool.SmsContentObserver;


public class SmsObserverService extends Service {
    private final String TAG = "SmsObserver";

//    private static Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();

        LogService.initLogger();


        Logger.d("短信模块|短信服务|Service1|onStart| 短信启动");
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        initLogger();

//        //监听短信接受
        Handler handler = new Handler();
        SmsContentObserver sms=new SmsContentObserver(this,handler);
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"),true,sms);
        Logger.d("短信模块|短信服务|Service1|onStartCommand| 短信启动");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
