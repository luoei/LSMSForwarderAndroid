package dev.luoei.app.tool.sms.forward.tools.mobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dev.luoei.app.tool.sms.forward.activity.BlackListActivity;
import dev.luoei.app.tool.sms.forward.common.CommonParas;
import dev.luoei.app.tool.sms.forward.common.CommonVariables;
import dev.luoei.app.tool.sms.forward.common.InitGlobalVar;
import dev.luoei.app.tool.sms.forward.dao.SMSDao;
import dev.luoei.app.tool.sms.forward.dao.impl.SMSDaoImpl;
import dev.luoei.app.tool.sms.forward.entity.SMS;
import dev.luoei.app.tool.sms.forward.tools.BasicTools;
import dev.luoei.app.tool.sms.forward.tools.MessageTool;
import dev.luoei.app.tool.sms.forward.tools.mail.SenderContrller;


/**
 * Created by usb on 15-1-8.
 */
public class SMSReceiver extends BroadcastReceiver {

    private String titles="";
    private String senderContent="";
    private Context context;
    private String data_id=null;
    @Override
    public void onReceive(Context context, Intent intent) {

        this.context=context;
        Bundle bundle = intent.getExtras();
        SmsMessage msg = null;
        if (null != bundle) {
            List<Map<String,String>> list=new ArrayList<Map<String,String>>();
            StringBuilder stringBuilder=new StringBuilder();
            Log.d("Monitor SMS -->","收到短信了.");
            Object[] smsObj = (Object[]) bundle.get("pdus");
            String lastAddress="";
            String receiveTime="";
            String who="";
            Map<String,String> map=null;
            for (Object object : smsObj) {
                msg = SmsMessage.createFromPdu((byte[]) object);
                who=msg.getOriginatingAddress();
                if(!lastAddress.equals(who)){
                    lastAddress=who;
                    Date date = new Date(msg.getTimestampMillis());//时间
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                    receiveTime = format.format(date);
                    map=new HashMap<String,String>();
                    map.put("WHO",who);
                    map.put("TIME",receiveTime);
                    list.add(map);
                    Log.d("Monitor SMS -->","接受时间："+date.getTime());
                }
                stringBuilder.append(msg.getDisplayMessageBody());
                map.put("MSG",stringBuilder.toString());
            }
//            titles="";
//            senderContent="";
            String sender = "";
            String smsMSG = "";
            for(int i=0,len=list.size();i<len;i++){
                map=list.get(i);
                String content="发件人："+map.get("WHO")+"  接受时间："+map.get("TIME")+"\r\n"+map.get("MSG");
                Log.d("Monitor SMS -->",content);
                senderContent+=content+"\r\n";
                sender = map.get("WHO");
                smsMSG = map.get("MSG");
                //更新界面 UI
                 new MessageTool().sendMessage(MessageTool.MAIN_VIEW, new String[]{"收到信息-"+map.get("WHO"),"初始化发送通道"}, CommonVariables.UPDATE_MAINVIEW_NOTICE);
                //更新到本地
                SMSDao smsDao =new SMSDaoImpl();
                SMS sms =new SMS();
                sms.setDataPhone(map.get("WHO"));
                sms.setDataMsg(map.get("MSG"));
                sms.setDataStatus(CommonVariables.SMS_SEND_INIT_CODE);
                sms.setDataStatusName(CommonVariables.SMS_SEND_INIT_MSG);
                sms.setReceiveDate(map.get("TIME"));
                sms.setLastupdatetime(new BasicTools().getCurrentTime("yyyyMMddHHssmm"));
                sms.setDesc("");
                sms.setDataId(sms.generateDataId());
                try{
                    smsDao.add(sms);
                    data_id = sms.getDataId();
                }catch (Exception e){
                    e.printStackTrace();
                }

                titles+=String.valueOf(sms.getDataMsg());
            }
            if(titles.length()>50)
            titles=titles.substring(0,50);
            //黑名单过滤 , 验证码级别较高
            //判断是否验证码
            if (sender.startsWith("106") && !senderContent.contains("验证码")){
                String filterString = "";
                int startIndex = smsMSG.indexOf("【");
                if (startIndex > -1){
                    int endIndex = smsMSG.indexOf("】");
                    if (endIndex > startIndex){
                        filterString = smsMSG.substring(startIndex+1,endIndex);
                    }
                }
                if (filterString.length() > 0){
                    Set<String> store = InitGlobalVar.getVar(CommonParas.getMainContext(), BlackListActivity.StoreKey);
                    if (store!=null && store.size() > 0 ){
                        for (String string : store){
                            if (filterString.contains(string)){
                                //更新界面 UI
                                new MessageTool().sendMessage(MessageTool.MAIN_VIEW, new String[]{"收到信息-黑名单过滤","黑名单-标记成功，关键字【"+string+"】"}, CommonVariables.UPDATE_MAINVIEW_NOTICE);
                                titles = "【垃圾短信】"+titles;
                                break;
                            }
                        }
                    }
                }
            }

            //调用email发送信息
            Log.d("Monitor SMS -->","正在发送邮件...");
            new Thread(runnable).start();
        }


    }


//    public   void newtorkTest(ConnectivityManager connectivityManager){
//        Log.d("Network Test -->","网络测试...");
//        this.connectivityManager=connectivityManager;
//        new Thread(runnable).start();
//    }

     Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("mylog","请求结果为-->" + val);
        }
    };

    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            new SenderContrller().sender(data_id,titles == "" ? "未知" : titles, senderContent,context);
        }
    };

}
