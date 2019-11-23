package dev.luoei.app.tool.sms.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.orhanobut.logger.Logger;

public class SmsObserverReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Logger.d("短信模块|短信服务|Receive1|onReceive| 短信启动");
    }
}
