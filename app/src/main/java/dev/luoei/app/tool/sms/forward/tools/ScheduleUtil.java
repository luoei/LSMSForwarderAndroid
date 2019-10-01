package dev.luoei.app.tool.sms.forward.tools;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import dev.luoei.app.tool.router.dao.MailAccountDao;
import dev.luoei.app.tool.router.dao.impl.MailAccountDaoImpl;
import dev.luoei.app.tool.router.entity.MailAccount;
import dev.luoei.app.tool.sms.forward.common.CommonParas;
import dev.luoei.app.tool.sms.forward.common.CommonVariables;
import dev.luoei.app.tool.router.tool.SMSService;

import static dev.luoei.app.tool.sms.forward.common.CommonVariables.CONFIG_BACKGROUND_SETTING_SMS_CHECKIN;
import static dev.luoei.app.tool.sms.forward.common.CommonVariables.CONFIG_BACKGROUND_SETTING_SMS_PREVIOUS_SENDER_TIME;

public class ScheduleUtil {

    public static void smsCheckinSchedule(){

        final SharedPreferences sharedPreferences= CommonParas.getMainContext().getSharedPreferences(CommonVariables.CONFIG_BACKGROUND_SETTING, Activity.MODE_PRIVATE);
        if(sharedPreferences.getBoolean(CONFIG_BACKGROUND_SETTING_SMS_CHECKIN,false)){
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    //要推迟执行的方法
//                    Log.d("定时器","当前时间"+new Date());
                    MailAccountDao mailAccountDao = new MailAccountDaoImpl(CommonParas.getMainContext());
                    MailAccount mailAccount = mailAccountDao.getFromCache();
                    long lastTime = sharedPreferences.getLong(CONFIG_BACKGROUND_SETTING_SMS_PREVIOUS_SENDER_TIME,0);
                    if (null != mailAccount.getPhone() && (System.currentTimeMillis() - lastTime)/1000 > 20*24*3600){
                        SharedPreferences sharedPreferences= CommonParas.getMainContext().getSharedPreferences(CommonVariables.CONFIG_BACKGROUND_SETTING, Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor= sharedPreferences.edit();
                        editor.putLong(CONFIG_BACKGROUND_SETTING_SMS_PREVIOUS_SENDER_TIME,System.currentTimeMillis());
                        editor.commit();
                        new SMSService(CommonParas.getMainContext()).sender( mailAccount.getPhone(),"短信签到，时间："+new Date());
                        Log.d("定时器","当前时间"+new Date()+" 短信发送成功");
                    }
                }
            };
            timer.schedule(task,1000,10*60*1000);
            Toast.makeText(CommonParas.getMainContext(),"短信签到定时器加载完成",Toast.LENGTH_SHORT).show();
        }

    }

}
