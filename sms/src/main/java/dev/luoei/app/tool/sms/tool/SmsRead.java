package dev.luoei.app.tool.sms.tool;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.luoei.app.tool.sms.define.Define;

/**
 * Created by anseenly on 15/4/18.
 */
public class SmsRead {
    private static final Uri SMS_INBOX = Uri.parse("content://sms/inbox");

    /**
     * 读取系统短信
     * @return 所有短信
     */
    public List<Map<String,String>> getSmsFromPhone(Context context) {
        Cursor cursor = context.getContentResolver().query(
                SMS_INBOX, null, null, null, null);
        List<Map<String,String>> list=new ArrayList<Map<String,String>>();
        Map<String,String> map;
        while (cursor.moveToNext()) {
            map=new HashMap<String,String>();
//            String[] names = cursor.getColumnNames();
//            for (String name : names){
//                Log.v("SMS-->", name+" : "+cursor.getString(cursor.getColumnIndex(name)));
//            }
            String number = cursor.getString(cursor.getColumnIndex("address"));//手机号
           // String name = cursor.getString(cursor.getColumnIndex("person"));//联系人姓名列表
            String body = cursor.getString(cursor.getColumnIndex("body"));
            long date = cursor.getLong(cursor.getColumnIndex("date_sent"));
            long date1 = cursor.getLong(cursor.getColumnIndex("date"));
            //日期格式化
            SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddHHmmss");
            java.util.Date dt = new Date(date > 0 ? date : date1);
            String sDateTime = sdf.format(dt);  //得到精确到秒的表示：
            map.put(Define.SMS_PHONE,number);
            map.put(Define.SMS_BODY,body);
            map.put(Define.SMS_SEND_DATE,sDateTime);
            list.add(map);
           // Log.d("SMSRead -->",number+" -- "+sDateTime+" -- "+body);
        }
        return list;
    }
}


//sms主要结构：
//　　
//　　_id：短信序号，如100
//　　
//　　thread_id：对话的序号，如100，与同一个手机号互发的短信，其序号是相同的
//　　
//　　address：发件人地址，即手机号，如+86138138000
//　　
//　　person：发件人，如果发件人在通讯录中则为具体姓名，陌生人为null
//　　
//　　date：日期，long型，如1346988516，可以对日期显示格式进行设置
//　　
//　　protocol：协议0SMS_RPOTO短信，1MMS_PROTO彩信
//　　
//　　read：是否阅读0未读，1已读
//　　
//　　status：短信状态-1接收，0complete,64pending,128failed
//　　
//　　type：短信类型1是接收到的，2是已发出
//　　
//　　body：短信具体内容
//　　
//　　service_center：短信服务中心号码编号，如+8613800755500