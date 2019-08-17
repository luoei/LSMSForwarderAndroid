package dev.luoei.app.tool.sms.forward.common;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

import dev.luoei.app.tool.sms.forward.entity.MailAccount;

/**
 * Created by usb on 15-1-15.
 */
public class CommonParas {
    public static SharedPreferences sharedPreferences=null;

    public static boolean app_is_first=true;

    public static Map saveView=new HashMap();

    private static Context mainContext=null;
    // 邮件账号
    private static MailAccount mailAccount = null;

    public static long lastsendtime=0;

    public static int lastsendbatterycount=0;


    public static Context getMainContext() {
        return mainContext;
    }

    public static void setMainContext(Context mainContext) {
        CommonParas.mainContext = mainContext;
    }

    public static MailAccount getMailAccount() {
        return mailAccount;
    }

    public static void setMailAccount(MailAccount mailAccount) {
        CommonParas.mailAccount = mailAccount;
    }




}
