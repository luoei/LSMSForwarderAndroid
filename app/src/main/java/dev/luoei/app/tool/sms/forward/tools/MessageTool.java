package dev.luoei.app.tool.sms.forward.tools;

import android.os.Handler;
import android.util.Log;

import dev.luoei.app.tool.sms.forward.activity.MainActivity;
import dev.luoei.app.tool.sms.forward.fragment.HomeFragment;

/**
 * Created by usb on 15-1-29.
 */
public class MessageTool {
    public static final int MAIN_ACTIVITY=0;
    public static final int MAIN_VIEW=1;
    public static final int SCHEDULESERVICE_VIEW=4;

    public void sendMessage(int classid,Object obj,int messageid){
        android.os.Message msg =new android.os.Message();
        msg.obj=obj;
        msg.what= messageid;
        Handler handler;
        switch (classid){
            case MAIN_ACTIVITY:
                handler= MainActivity.mHandler;
               break;
            case MAIN_VIEW:
                handler= HomeFragment.handler;
                break;
            default:  handler=MainActivity.mHandler;
                break;
        }
        if(handler==null){
            Log.d("MessageTool -->","对象为空，取消本次发送。");
            return;
        }
        handler.sendMessage(msg);
    }

}
