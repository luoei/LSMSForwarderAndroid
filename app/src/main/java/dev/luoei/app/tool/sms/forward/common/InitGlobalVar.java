package dev.luoei.app.tool.sms.forward.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by usb on 15-1-12.
 */
public class InitGlobalVar {

//    private  SharedPreferences preferences=null;

    public void initVar(Context context){
//        SharedPreferences sharedPreferences=getPreferences(context);
//        MailAccount.setSender(sharedPreferences.getString(CommonVariables.MAIL_SERVER_FROM_ADDRESS, ""));
//        MailAccount.setPassword(sharedPreferences.getString(CommonVariables.MAIL_SERVER_PASSWORD, ""));
//        MailAccount.setPort(sharedPreferences.getString(CommonVariables.MAIL_SERVER_PORT, ""));
//        MailAccount.setStmpServerUrl(sharedPreferences.getString(CommonVariables.MAIL_SERVER_STMP_ADDRESS, ""));
//        MailAccount.setTimeout(sharedPreferences.getString(CommonVariables.MAIL_SERVER_TIMEOUT, ""));
//        MailAccount.setReceiver(sharedPreferences.getStringSet(CommonVariables.MAIL_SERVER_TO_ADDRESS, null));
//        MailAccount.setUsername(sharedPreferences.getString(CommonVariables.MAIL_SERVER_USERNAME, ""));
//
//        SMSEntity.setAddress(sharedPreferences.getString(CommonVariables.SMS_ADDRESS, ""));
    }
    public boolean setGlobalVar(Context context,Map map){
        if(context==null||map==null||map.size()==0)return false;
        SharedPreferences sharedPreferences=getPreferences(context);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putStringSet(CommonVariables.MAIL_SERVER_TO_ADDRESS,(Set<String>)map.get(CommonVariables.MAIL_SERVER_TO_ADDRESS));
        editor.putString(CommonVariables.MAIL_SERVER_PASSWORD,String.valueOf(map.get(CommonVariables.MAIL_SERVER_PASSWORD)));
        editor.putString(CommonVariables.MAIL_SERVER_USERNAME,String.valueOf(map.get(CommonVariables.MAIL_SERVER_USERNAME)));
        editor.putString(CommonVariables.MAIL_SERVER_FROM_ADDRESS,String.valueOf(map.get(CommonVariables.MAIL_SERVER_FROM_ADDRESS)));
        editor.putString(CommonVariables.MAIL_SERVER_STMP_ADDRESS,String.valueOf(map.get(CommonVariables.MAIL_SERVER_STMP_ADDRESS)));
        editor.putString(CommonVariables.MAIL_SERVER_PORT,String.valueOf(map.get(CommonVariables.MAIL_SERVER_PORT)));
        editor.putString(CommonVariables.MAIL_SERVER_TIMEOUT,String.valueOf(map.get(CommonVariables.MAIL_SERVER_TIMEOUT)));
        editor.putString(CommonVariables.SMS_ADDRESS,String.valueOf(map.get(CommonVariables.SMS_ADDRESS)));
        if(editor.commit()){
            initVar(context);
            return true;
        }else return false;
    }

    public static Set getVar(Context context,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(CommonVariables.CONFIG_BLACK_SETTING, Activity.MODE_PRIVATE);;
        return sharedPreferences.getStringSet(key,new HashSet<String>());
    }
    public static boolean setVar(Context context,String key,Set set){
        if(key==null||key.length()==0)return false;
        SharedPreferences sharedPreferences=context.getSharedPreferences(CommonVariables.CONFIG_BLACK_SETTING, Activity.MODE_PRIVATE);;
        SharedPreferences.Editor editor= sharedPreferences.edit();
        if (set == null || set.size() == 0){
            editor.remove(key);
        }else {
            editor.clear();
            editor.putStringSet(key,set);
        }
        return editor.commit();
    }

    private  static  SharedPreferences preferences(Context context) {
        if(CommonParas.sharedPreferences==null)
            CommonParas.sharedPreferences=context.getSharedPreferences(CommonVariables.CONFIG_SETTING, Activity.MODE_PRIVATE);
        return CommonParas.sharedPreferences;
    }

    private  SharedPreferences getPreferences(Context context) {
        return InitGlobalVar.preferences(context);
    }



}
