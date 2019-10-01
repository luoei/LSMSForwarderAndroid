package dev.luoei.app.tool.sms.forward.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.TimerTask;

import dev.luoei.app.tool.router.tool.UIClientMessengerUtil;
import dev.luoei.app.tool.sms.forward.common.CommonVariables;
import dev.luoei.app.tool.sms.forward.tools.MessageTool;

public class MessengerService extends Service {

    private final static String TAG = "MessengerService";

    public final static int MSG_FROM_SERVICE = 30086;

    public final static int MSG_FROM_CLIENT = 30087;

    private final Messenger messenger = new Messenger(new MessengerHandler());
    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FROM_CLIENT:
                    //2、服务端接送消息

                    String message = msg.getData().getString("message");

                    new Thread(new TimerTask() {
                        @Override
                        public void run() {
                            new MessageTool().sendMessage(MessageTool.MAIN_VIEW, new String[]{null,message}, CommonVariables.UPDATE_MAINVIEW_NOTICEMSG);
                        }
                    }).start();

                    Log.v(TAG, "接受到消息：【"+message+"】");

                    //4、服务端回复消息给客户端
//                    Messenger serviceMessenger = msg.replyTo;
//                    Message replyMessage = Message.obtain(null, MSG_FROM_SERVICE);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("msg", "Hello from service.");
//                    replyMessage.setData(bundle);
//                    try {
//                        serviceMessenger.send(replyMessage);
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        UIClientMessengerUtil.uimessengerService = this.getClass();
        return messenger.getBinder();
    }

}
