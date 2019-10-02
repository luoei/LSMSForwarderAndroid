package dev.luoei.app.tool.router.tool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dev.luoei.app.tool.router.entity.Process;

public class UIClientMessengerUtil {

    public static Class UI_MESSENGER_SERVICE;

    public static String FINISHED_TAG = "UIClientMessengerUtil_Send_Finished";

    static {
        try {
            UI_MESSENGER_SERVICE = Class.forName("dev.luoei.app.tool.sms.forward.service" +
                                ".MessengerService");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private final static String TAG = "UIClientMessengerUtil";

    public final static int MSG_FROM_SERVICE = 30086;

    public final static int MSG_FROM_CLIENT = 30087;

    private Context context;

    /// 消息客户端
    private Messenger clientMessenger;

    /// 消息队列
    private List<String> senders = new ArrayList<>();

    /// UI进程是否存在
    private boolean isAliveUIProcess = false;

    public UIClientMessengerUtil(){ }

    public UIClientMessengerUtil(Context context){
        this.context = context;
        isAliveUIProcess = ProcessUtil.isAliveProcess(Process.MAIN.toString(),context);
        if (isAliveUIProcess)connect();

    }

    public  ServiceConnection messengerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v(TAG,"客户端【"+name.getClassName()+" - "+MSG_FROM_SERVICE+"】连接成功");

            //1、发送消息给服务端
            clientMessenger = new Messenger(service);
            send();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {

            Log.v(TAG,"客户端【"+name.getClassName()+" - "+MSG_FROM_SERVICE+"】断开连接");

        }
    };

    private void connect(){
        if (null == UIClientMessengerUtil.UI_MESSENGER_SERVICE){
            Log.v(TAG,"UI 进程通信失败，请先注册实体类");
            return;
        }
        Log.v(TAG,"UI 进程通信发现实体类，连接中...");

        try {
            //Messenger 进行通信
            Intent intent = new Intent(context, UIClientMessengerUtil.UI_MESSENGER_SERVICE);
            context.startService(intent);
            context.bindService(intent, messengerServiceConnection, Context.BIND_NOT_FOREGROUND);
        }catch (Exception e){
            e.printStackTrace();
            disconnect();
        }
    }

    private void disconnect(){
        Intent intent = new Intent(context, UI_MESSENGER_SERVICE);
        context.stopService(intent);
        context.unbindService(messengerServiceConnection);
    }

    public void send(String content){
        senders.add(content);
        send();
    }

    private void send(){
        if (null == clientMessenger)return;
        if (senders.size() == 0)return;
        String content = senders.remove(0);
        if (FINISHED_TAG.equals(content)){
            disconnect();
            return;
        }

        Message message = Message.obtain(null, MSG_FROM_CLIENT);
        Bundle bundle = new Bundle();
        bundle.putString("message", content);
        message.setData(bundle);
        //3、这句是服务端回复客户端使用
//            message.replyTo = getReplyMessenger;
        try {
            clientMessenger.send(message);
            send();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Log.v(TAG,"客户端【"+MSG_FROM_SERVICE+"】发送完成");

    }

//    private final Messenger getReplyMessenger = new Messenger(new MessengerHandler());
//    private class MessengerHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MSG_FROM_SERVICE:
//                    //5、服务端回复消息给客户端，客户端接送消息
//
////                    String key = msg.getData().getString("key");
////                    int status = msg.getData().getInt("status");
////                    String message = msg.getData().getString("message");
////
////                    Log.v(TAG,"接收到服务端回复:"+key+" - "+message);
//
//
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    }

}
