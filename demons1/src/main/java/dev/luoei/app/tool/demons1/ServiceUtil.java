package dev.luoei.app.tool.demons1;

import android.content.Context;
import android.content.Intent;

//import dev.luoei.app.tool.router.service.DemonsRouterService;
//import dev.luoei.app.tool.sms.service.DemonsSmsObserverService;

public class ServiceUtil {

    /// 启动服务
    public static void start(Context context){

        /// 保活进程
        startDemon(context);

    }

    /// 保活进程
    public static void startDemon(Context context){
//        Intent demonsIntent=new Intent(context, DemonsService.class);
//        context.startService(demonsIntent);
    }


}
