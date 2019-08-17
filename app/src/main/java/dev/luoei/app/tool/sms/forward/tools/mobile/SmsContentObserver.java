package dev.luoei.app.tool.sms.forward.tools.mobile;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dev.luoei.app.tool.sms.forward.common.CommonParas;
import dev.luoei.app.tool.sms.forward.common.CommonVariables;
import dev.luoei.app.tool.sms.forward.dao.SMSDao;
import dev.luoei.app.tool.sms.forward.dao.impl.SMSDaoImpl;
import dev.luoei.app.tool.sms.forward.entity.SMS;
import dev.luoei.app.tool.sms.forward.tools.BasicTools;
import dev.luoei.app.tool.sms.forward.tools.mail.SenderContrller;

/**
 * Created by Luoei on 2017/5/7.
 */

public class SmsContentObserver extends ContentObserver {

    private  Context context;

    private  Handler handler;

   public SmsContentObserver(Context context, Handler handle){
       super(handle);
       this.context=context;
       this.handler=handle;
   }

    @Override
    public void onChange(boolean selfChange) {
        Log.d("短信监听","内容变化");

        start1();
    }

    protected void start1() {
        //获取本地短信
        List<Map<String,String>> list=new SmsRead().getSmsFromPhone();
        if(null == list || list.size() == 0)return;
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
            sms.setDataStatus(CommonVariables.SMS_SEND_INIT_CODE);
            sms.setDataStatusName(CommonVariables.SMS_SEND_INIT_MSG);
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
        if(smses.size() > 0){
            for (SMS sms : smses){
                String title=String.valueOf(sms.getDataMsg());
                if (title.length()>50){
                    title=title.substring(0,50);
                }
                String content="发件人："+ sms.getDataPhone()+"  接受时间："+ sms.getReceiveDate()+"\r\n"+ sms.getDataMsg();
                new SenderContrller().sender(sms.getDataId(),title, content, CommonParas.getMainContext());
                }
            }
            smsDao.adds(smses);
            Log.d("短信","发送短信");
        }

}
