package dev.luoei.app.tool.sms.tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dev.luoei.app.tool.router.controller.SenderContrller;
import dev.luoei.app.tool.router.controller.SenderContrllerStatus;

import dev.luoei.app.tool.sms.dao.SMSDao;
import dev.luoei.app.tool.sms.dao.impl.SMSDaoImpl;
import dev.luoei.app.tool.sms.define.Define;
import dev.luoei.app.tool.sms.entity.SMS;
import dev.luoei.app.tool.sms.service.LogService;


/**
 * Created by Luoei on 2017/5/7.
 */

public class SmsContentObserver extends ContentObserver {

    private final String TAG = "SmsContentObserver";

    private Context context;

    // 发送中
    private boolean sendering = false;

    public SmsContentObserver(Context context, Handler handle){
       super(handle);

       this.context = context;
   }

    @Override
    public void onChange(boolean selfChange) {
        LogService.initLogger();

        Log.d(TAG,"内容变化");
        Logger.d("短信模块|短信服务|短信变化");

        start1();
    }

    protected void start1() {
        if (sendering){
            Logger.d("短信模块|短信服务|有短信正在发送中，本次无效");
            return;
        }
        sendering = true;
        //获取本地短信
        List<Map<String,String>> list=new SmsRead().getSmsFromPhone(this.context);
        if(null == list || list.size() == 0)return;
        SMSDao smsDao =new SMSDaoImpl(context);
        List<SMS> smses = new ArrayList<>();
        Set<String> dataids = new HashSet<>();
        Set localDataIds = smsDao.getAllDataId();
        if (null !=localDataIds && localDataIds.size() > 0) {
            dataids.addAll(localDataIds);
        }
        for(int i=0,len=list.size();i<len;i++){
            String body=(list.get(i)).get(Define.SMS_BODY);
            SMS sms =new SMS();
            sms.setDataPhone((list.get(i)).get(Define.SMS_PHONE));
            sms.setDataMsg(body);
            sms.setDataStatus(Define.SMS_SEND_INIT_CODE);
            sms.setDataStatusName(Define.SMS_SEND_INIT_MSG);
            sms.setReceiveDate((list.get(i)).get(Define.SMS_SEND_DATE));
            sms.setLastupdatetime(TimeUtil.getCurrentTime("yyyyMMddHHmmdd"));
            sms.setDesc("");
            sms.setDataId(sms.generateDataId());
            String dataId = sms.getDataId();
            if (!dataids.contains(dataId)){
                dataids.add(dataId);
                smses.add(sms);
            }
        }
//        Log.d(TAG,"待发送短信条数："+smses.size());
        Logger.d("短信模块|短信服务| 待发送短信条数："+ smses.size());
        if(smses.size() > 0){
            for (SMS sms : smses){
                String title=String.valueOf(sms.getDataMsg());
                if (title.length()>50){
                    title=title.substring(0,50);
                }

                String sender = sms.getDataPhone();
                String senderContent = sms.getDataMsg();
                //黑名单过滤 , 验证码级别较高
                //判断是否验证码
                if (sender.startsWith("106") && !senderContent.contains("验证码")){
                    String filterString = "";
                    int startIndex = senderContent.indexOf("【");
                    if (startIndex > -1){
                        int endIndex = senderContent.indexOf("】");
                        if (endIndex > startIndex){
                            filterString = senderContent.substring(startIndex+1,endIndex);
                        }
                    }
                    if (filterString.length() > 0){
                        SharedPreferences sharedPreferences=context.getSharedPreferences(Define.CONFIG_BLACK_SETTING, Activity.MODE_PRIVATE);
                        Set<String> store = sharedPreferences.getStringSet("BlackFilterKey",new HashSet<String>());;
                        if (store!=null && store.size() > 0 ){
                            for (String string : store){
                                if (filterString.contains(string)){
                                    //更新界面 UI
//                                    new MessageTool().sendMessage(MessageTool.MAIN_VIEW, new String[]{"收到信息-黑名单过滤","黑名单-标记成功，关键字【"+string+"】"}, CommonVariables.UPDATE_MAINVIEW_NOTICE);
                                    title = "【垃圾短信】"+title;
                                    break;
                                }
                            }
                        }
                    }
                }

                String content="发件人："+ sms.getDataPhone()+"  接受时间："+ sms.getReceiveDate()+"\r\n"+ sms.getDataMsg();
                Logger.d("短信模块|短信服务|短信|开始发送|"+content);
                // Messenger 发送
                try {

                    new SenderContrller().sender(sms.getDataId(), title, content, context, new SenderContrllerStatus() {
                        @Override
                        public void onChanged(String key, int status, String message) {

                        }
                    });


//                    Log.d(TAG,"发送短信");
                    Logger.d("短信模块|短信服务|短信|发送完成");
                }catch (Exception e){
                    e.printStackTrace();
                    Logger.e("短信模块|短信服务|短信|发送失败|"+e.getMessage());
                }


            }
        }
        smsDao.adds(smses);
        sendering = false;
    }

}
