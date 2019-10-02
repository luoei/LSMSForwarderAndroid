package dev.luoei.app.tool.router.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import dev.luoei.app.tool.router.controller.SenderContrller;
import dev.luoei.app.tool.router.controller.SenderContrllerStatus;

public class DemonsRouterService extends Service {
    private final static String TAG = "DemonsRouterService";

    public final static int MSG_FROM_SERVICE = 20086;
    public final static int MSG_FROM_CLIENT = 20087;

    private static Context context;
    private final Messenger messenger = new Messenger(new MessengerHandler());

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FROM_CLIENT:
                    //2、服务端接送消息
                    final String dataId = msg.getData().getString("dataId");
                    final String title = msg.getData().getString("title");
                    final String content = msg.getData().getString("content");

                    Log.d(TAG, "接受到消息： " + content);

                    //4、服务端回复消息给客户端
                    final Messenger serviceMessenger = msg.replyTo;
                    final Message replyMessage = Message.obtain(null, MSG_FROM_SERVICE);
                    final Bundle bundle = new Bundle();

                    new SenderContrller().sender(dataId, title, content, context, new SenderContrllerStatus() {
                        @Override
                        public void onChanged(String key, int status, String message) {
                            if (bundle.containsKey("key")) bundle.remove("key");
                            if (bundle.containsKey("status")) bundle.remove("status");
                            if (bundle.containsKey("message")) bundle.remove("message");
                            bundle.putString("key", key);
                            bundle.putInt("status",status);
                            bundle.putString("message", message);
                            replyMessage.setData(bundle);
                            try {
                                serviceMessenger.send(replyMessage);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    break;
            }
            super.handleMessage(msg);
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        context = this;
        return messenger.getBinder();
    }

}
