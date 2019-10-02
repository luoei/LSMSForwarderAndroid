package dev.luoei.app.tool.router.tool;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by usb on 15-1-9.
 */
public class SMSUtil {

    private Context context;

    public SMSUtil(Context context){
        this.context = context;
    }

    public void sender(String address,String msg){
        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage(address,null,msg,null,null);
        Log.d("SMS -->","信息已发，收件人:"+address+" 内容为：\r\n"+msg);
    }

}
