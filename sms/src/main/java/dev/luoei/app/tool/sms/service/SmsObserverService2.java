package dev.luoei.app.tool.sms.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.orhanobut.logger.Logger;

public class SmsObserverService2 extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.d("短信模块|短信服务|Service2|onStart| 短信启动");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d("短信模块|短信服务|Service2|onStartCommand| 短信启动");
        return Service.START_NOT_STICKY;
    }
}
