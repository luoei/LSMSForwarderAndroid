package dev.luoei.app.tool.sms.forward.tools;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import dev.luoei.app.tool.sms.forward.R;
import dev.luoei.app.tool.sms.forward.activity.MainActivity;
import dev.luoei.app.tool.sms.forward.common.CommonParas;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 消息通知工具
 */

public class NotificationUtil {

    private static final String TAG = "NotificationUtil";


    public void showLocalNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            showNotificationAction();
        else
            showNotificationActionPrevious();

    }

    private void showNotificationAction(){
        Map<Integer,NotifyObject> notifyObjects = new HashMap<>();
        long now = System.currentTimeMillis();
        long interval[] = {0};
        int count = 1;
        SimpleDateFormat smf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        for (long inter:interval) {
            Date date = new Date(now+inter);

            NotifyObject obj = new NotifyObject();
            obj.title = "短信转发";
            obj.content = "不要删除此通知";
            obj.type = count++;
            obj.firstTime = now+inter;
            obj.activityClass = MainActivity.class;
            /**
             type:count++,
             title:"标题",
             subText:"理论提醒时间:"+smf.format(date),
             content:"类型:"+(count-1)+","+inter,
             "",
             firstTime:now+inter,
             -1l,
             null
             //赋值就自己赋值啦~
             **/

            notifyObjects.put(obj.type,obj);
        }

        NotificationUtil.notifyByAlarm(CommonParas.getMainContext(),notifyObjects);

    }

    private void showNotificationActionPrevious() {
        NotificationManager mNotifyMgr =
                (NotificationManager) CommonParas.getMainContext().getSystemService(NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(
                CommonParas.getMainContext(), 0, new Intent(CommonParas.getMainContext(), MainActivity.class), 0);

        String NOTIFICATION_CHANNEL_ID = "sms_forward_v2_channel";

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(CommonParas.getMainContext(),NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle("短信转发")
                        .setContentText("请不要删除此通知")
                        .setContentIntent(contentIntent);

        //构建通知
        Notification notification = mBuilder.build();
        //显示通知
        mNotifyMgr.notify(0, notification);
        //启动为前台服务
//        startForeground(0, notification);

    }


    /**
     * 通过定时闹钟发送通知
     * @param context
     * @param notifyObjectMap
     */
    public static void  notifyByAlarm(Context context, Map<Integer,NotifyObject> notifyObjectMap){
        //将数据存储起来
        int count = 0;
        NotificationManager manager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);

        Set<Integer> keySet = notifyObjectMap.keySet();
        for (Integer key0: keySet) {
            if(!notifyObjectMap.containsKey(key0)){
                break;
            }

            NotifyObject obj = notifyObjectMap.get(key0);
            if(obj == null){
                break;
            }

            if(obj.times.size() <= 0){
                if(obj.firstTime > 0){
                    try {
                        Map<String, Serializable> map = new HashMap<>();
                        map.put("KEY_NOTIFY_ID",obj.type);
                        map.put("KEY_NOTIFY",NotifyObject.to(obj));
                        AlarmTimerUtil.setAlarmTimer(context,++count,obj.firstTime,"TIMER_ACTION",map);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                for (long time: obj.times) {
                    if(time > 0){
                        try {
                            Map<String,Serializable> map = new HashMap<>();
                            map.put("KEY_NOTIFY_ID",obj.type);
                            map.put("KEY_NOTIFY",NotifyObject.to(obj));
                            AlarmTimerUtil.setAlarmTimer(context,++count,time,"TIMER_ACTION",map);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        SharedPreferences mPreferences = context.getSharedPreferences("SHARE_PREFERENCE_NOTIFICATION",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putInt("KEY_MAX_ALARM_ID",count);
        edit.commit();
    }

    public static Notification  notifyByAlarmByReceiver(Context context,NotifyObject obj){
        if(context == null || obj== null)return null;
        NotificationManager manager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        return notifyMsg(context,obj,obj.type,System.currentTimeMillis(),manager);
    }

    /**
     * 消息通知
     * @param context
     * @param obj
     */
    private static Notification notifyMsg(Context context,NotifyObject obj,int nid,long time,NotificationManager mNotifyMgr){
        if(context == null || obj == null)return null;
        if(mNotifyMgr == null){
            mNotifyMgr =  (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        }

        if(time <= 0)return null;

        //准备intent
        Intent intent = new Intent(context,obj.activityClass);
        if(obj.param != null && obj.param.trim().length() > 0){
            intent.putExtra("param",obj.param);
        }

        //notification
        Notification notification = null;
        String contentText = obj.content;
        // 构建 PendingIntent
        PendingIntent pi = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //版本兼容

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//兼容Android8.0
            String id ="my_channel_01";
            int importance =NotificationManager.IMPORTANCE_LOW;
            CharSequence name = "notice";
            NotificationChannel mChannel =new NotificationChannel(id, name,importance);
            mChannel.enableLights(true);
            mChannel.setDescription("just show notice");
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.GREEN);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});
            mNotifyMgr.createNotificationChannel(mChannel);

            Notification.Builder builder = new Notification.Builder(context,id);
            builder.setAutoCancel(true)
                    .setContentIntent(pi)
                    .setContentTitle(obj.title)
                    .setContentText(obj.content)
                    .setOngoing(false)
                    .setSmallIcon(R.drawable.app_icon)
                    .setWhen(System.currentTimeMillis());
            if(obj.subText != null && obj.subText.trim().length() > 0){
                builder.setSubText(obj.subText);
            }
            notification =  builder.build();
        }else if (Build.VERSION.SDK_INT >= 23) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle(obj.title)
                    .setContentText(contentText)
                    .setSmallIcon(R.drawable.app_icon)
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .setOngoing(false)
                    .setWhen(System.currentTimeMillis());
            if(obj.subText != null && obj.subText.trim().length() > 0){
                builder.setSubText(obj.subText);
            }
            notification = builder.build();
        } else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Notification.Builder builder = new Notification.Builder(context);
            builder.setAutoCancel(true)
                    .setContentIntent(pi)
                    .setContentTitle(obj.title)
                    .setContentText(obj.content)
                    .setOngoing(false)
                    .setSmallIcon(R.drawable.app_icon)
                    .setWhen(System.currentTimeMillis());
            if(obj.subText != null && obj.subText.trim().length() > 0){
                builder.setSubText(obj.subText);
            }
            notification =  builder.build();
        }
        if(notification != null){
            mNotifyMgr.notify(nid, notification);

        }
        return notification;
    }

    /**
     * 取消所有通知 同时取消定时闹钟
     * @param context
     */
    public static void clearAllNotifyMsg(Context context){
        try{

            NotificationManager mNotifyMgr =
                    (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.cancelAll();

            SharedPreferences mPreferences = context.getSharedPreferences("SHARE_PREFERENCE_NOTIFICATION",Context.MODE_PRIVATE);
            int max_id = mPreferences.getInt("KEY_MAX_ALARM_ID",0);
            for (int i = 1;i <= max_id;i++){
                AlarmTimerUtil.cancelAlarmTimer(context,"TIMER_ACTION",i);
            }
            //清除数据
            mPreferences.edit().remove("KEY_MAX_ALARM_ID").commit();

        }catch (Exception e){
            Log.e(TAG,"取消通知失败",e);
        }
    }
}