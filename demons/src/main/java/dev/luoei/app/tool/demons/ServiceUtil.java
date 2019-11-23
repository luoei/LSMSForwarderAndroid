package dev.luoei.app.tool.demons;

import android.content.Context;
import android.content.Intent;

import dev.luoei.app.tool.sms.service.SmsObserverService;

public class ServiceUtil {

    /// 启动服务
    public static void start(Context context){

        /// 保活进程
        startDemon(context);

    }

    /// 保活进程
    public static void startDemon(Context context){
        Intent demonsIntent=new Intent(context, DemonsService.class);
        context.startService(demonsIntent);
    }

    /// 子保活进程
    public static void startDemonSubdemon(Context context){
        Intent demonsSubdemonIntent=new Intent(context, DemonsSubdemonService.class);
        context.startService(demonsSubdemonIntent);
    }

    /// 短信监听
    public static void startSmsObserverDemon(Context context){
        Intent smsObserverIntent=new Intent(context, SmsObserverService.class);
        context.startService(smsObserverIntent);
    }

}
