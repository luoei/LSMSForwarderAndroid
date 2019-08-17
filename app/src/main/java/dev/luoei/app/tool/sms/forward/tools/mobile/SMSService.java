package dev.luoei.app.tool.sms.forward.tools.mobile;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dev.luoei.app.tool.sms.forward.common.CommonVariables;
import dev.luoei.app.tool.sms.forward.dao.SMSDao;
import dev.luoei.app.tool.sms.forward.dao.impl.SMSDaoImpl;
import dev.luoei.app.tool.sms.forward.entity.SMS;
import dev.luoei.app.tool.sms.forward.tools.BasicTools;

/**
 * Created by usb on 15-1-9.
 */
public class SMSService {

    public void sender(Context context,String address,String msg){
//        String SENT_SMS_ACTION="com.paad.smssnippets.SENT_SMS_ACTION";
//        String DELIVERED_SMS_ACTION="com.paad.smssnippets.DELIVERED_SMS_ACTION";
//        context= CommonParas.getMainContext();
//        //create sentIntent parameter
//        Intent sentIntent=new Intent(SENT_SMS_ACTION);
//        PendingIntent sendPI=PendingIntent.getBroadcast(context.getApplicationContext(),0,sentIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//        //create deliveryIntent parameter
//        Intent deliveryIntent=new Intent(DELIVERED_SMS_ACTION);
//        PendingIntent deliveryPI=PendingIntent.getBroadcast(context.getApplicationContext(),0,deliveryIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//        //reg Broadcast Receiver
//        context.registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                String resultText = "UNKNOWN";
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        Log.d("SMSManager -->", "发送成功");
//                        resultText = "Send successful";
//                        break;
//                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        resultText = "Send failded";
//                        break;
//                    case SmsManager.RESULT_ERROR_RADIO_OFF:
//                        resultText = "Send failded:Radio is off";
//                        break;
//                    case SmsManager.RESULT_ERROR_NULL_PDU:
//                        resultText = "Send failded:No PUD specified";
//                        break;
//                    case SmsManager.RESULT_ERROR_NO_SERVICE:
//                        resultText = "Send failded:No service";
//                        break;
//                }
//                Toast.makeText(context, resultText, Toast.LENGTH_LONG).show();
//            }
//        }, new IntentFilter(SENT_SMS_ACTION));
//
//        context.registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Toast.makeText(context,"SMS Delivered",Toast.LENGTH_LONG).show();
//            }
//        },new IntentFilter(DELIVERED_SMS_ACTION));


        SmsManager smsManager=SmsManager.getDefault();
//        String sendTo="10001";
//        String msg="102";

//        ArrayList<String> msgArray=smsManager.divideMessage(msg);
//        ArrayList<PendingIntent> sentIntents=new ArrayList<PendingIntent>();
//        for(int i=0,len=msgArray.size();i<len;i++){
//            sentIntents.add(sendPI);
//        }
//        smsManager.sendTextMessage(sendTo,null,msg,sendPI,deliveryPI);
//        smsManager.sendMultipartTextMessage(address,null,msgArray,sentIntents,null);
        smsManager.sendTextMessage(address,null,msg,null,null);
        Log.d("SMS -->","信息已发，收件人:"+address+" 内容为：\r\n"+msg);

    }

    public void loadLocalSms(){
        new LoadLocalSmsTask().execute();
    }

    private class LoadLocalSmsTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            //获取本地短信
            List<Map<String,String>> list=new SmsRead().getSmsFromPhone();
            if(null == list || list.size() == 0)return null;
            SMSDao smsDao =new SMSDaoImpl();
            List<SMS> smses = new ArrayList<>();
            Set<String> dataids = new HashSet<>();
            Set localDataIds = smsDao.getAllDataId();
            if (null !=localDataIds && localDataIds.size() > 0) {
                dataids.addAll(localDataIds);
            }
            for(int i=0,len=list.size();i<len;i++){
                String body=(list.get(i)).get(CommonVariables.SMS_BODY);
                SMS sms =new SMS();
                sms.setDataPhone((list.get(i)).get(CommonVariables.SMS_PHONE));
                sms.setDataMsg(body);
                sms.setDataStatus(CommonVariables.SMS_SEND_HISTORY_CODE);
                sms.setDataStatusName(CommonVariables.SMS_SEND_HISTORY_MSG);
                sms.setReceiveDate((list.get(i)).get(CommonVariables.SMS_SEND_DATE));
                sms.setLastupdatetime(new BasicTools().getCurrentTime("yyyyMMddHHmmdd"));
                sms.setDesc("");
                sms.setDataId(sms.generateDataId());
                String dataId = sms.getDataId();
                if (!dataids.contains(dataId)){
                    dataids.add(dataId);
                    smses.add(sms);
                }
            }
            smsDao.adds(smses);
            Log.d("SMS -->","系统收件箱："+list.size()+"个，新增："+smses.size());
            return null;
        }
    }

}
