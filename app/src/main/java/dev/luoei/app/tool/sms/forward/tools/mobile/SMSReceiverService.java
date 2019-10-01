package dev.luoei.app.tool.sms.forward.tools.mobile;


import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

import dev.luoei.app.tool.sms.forward.activity.MainActivity;
import dev.luoei.app.tool.sms.tool.SmsContentObserver;

/**
 * Created by Luoei on 2017/5/4.
 */

public class SMSReceiverService extends IntentService {

    public SMSReceiverService(){
        super("SMSReceiverService");
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SmsContentObserver sms=new SmsContentObserver(this, MainActivity.mHandler);

        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"),true,sms);
    }

}
