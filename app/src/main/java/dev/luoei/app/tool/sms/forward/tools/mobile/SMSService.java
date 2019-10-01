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

import dev.luoei.app.tool.sms.dao.SMSDao;
import dev.luoei.app.tool.sms.dao.impl.SMSDaoImpl;
import dev.luoei.app.tool.sms.entity.SMS;
import dev.luoei.app.tool.sms.forward.common.CommonVariables;
import dev.luoei.app.tool.sms.forward.tools.BasicTools;
import dev.luoei.app.tool.sms.tool.SmsRead;

/**
 * Created by usb on 15-1-9.
 */
public class SMSService {

    private Context context;

    public SMSService(Context context){
        this.context = context;
    }

    public void sender(String address,String msg){
        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage(address,null,msg,null,null);
        Log.d("SMS -->","信息已发，收件人:"+address+" 内容为：\r\n"+msg);

    }

    public void loadLocalSms(){
        new LoadLocalSmsTask(this.context).execute();
    }

    private class LoadLocalSmsTask extends AsyncTask {

        private Context context;

        public LoadLocalSmsTask(Context context){
            this.context = context;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            //获取本地短信
            List<Map<String,String>> list=new SmsRead().getSmsFromPhone(this.context);
            if(null == list || list.size() == 0)return null;
            SMSDao smsDao =new SMSDaoImpl(context);
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
