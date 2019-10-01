package dev.luoei.app.tool.router.tool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class UIClientMessengerUtil {

    public static Class uimessengerService;

    private final static String TAG = "UIClientMessengerUtil";

    public final static int MSG_FROM_SERVICE = 30086;

    public final static int MSG_FROM_CLIENT = 30087;

    private Context context;

    public String content;

    public UIClientMessengerUtil(){

    }

    public UIClientMessengerUtil(Context context){
        this.context = context;
    }

    private Messenger clientMessenger;
    public ServiceConnection messengerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v(TAG,"服务端【"+name+" - "+MSG_FROM_SERVICE+"】连接成功");

            //1、发送消息给服务端
            clientMessenger = new Messenger(service);
            Message message = Message.obtain(null, MSG_FROM_CLIENT);
            Bundle bundle = new Bundle();
            bundle.putString("message", content);
            message.setData(bundle);
            //3、这句是服务端回复客户端使用
//            message.replyTo = getReplyMessenger;
            try {
                clientMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            Log.v(TAG,"服务端【"+name+" - "+MSG_FROM_SERVICE+"】发送完成");

            Intent intent = new Intent(context, uimessengerService);
            context.stopService(intent);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {

            Log.v(TAG,"服务端【"+name+" - "+MSG_FROM_SERVICE+"】断开连接");

        }
    };
    private final Messenger getReplyMessenger = new Messenger(new MessengerHandler());
    private class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FROM_SERVICE:
                    //5、服务端回复消息给客户端，客户端接送消息

//                    String key = msg.getData().getString("key");
//                    int status = msg.getData().getInt("status");
//                    String message = msg.getData().getString("message");
//
//                    Log.v(TAG,"接收到服务端回复:"+key+" - "+message);


                    break;
            }
            super.handleMessage(msg);
        }
    }

}
