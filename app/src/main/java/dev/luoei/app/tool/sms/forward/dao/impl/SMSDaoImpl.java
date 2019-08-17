package dev.luoei.app.tool.sms.forward.dao.impl;

import android.database.Cursor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dev.luoei.app.tool.sms.forward.dao.SMSDao;
import dev.luoei.app.tool.sms.forward.entity.SMS;
import dev.luoei.app.tool.sms.forward.tools.db.DBHelper;

/**
 * Created by anseenly on 15/4/19.
 */
public class SMSDaoImpl implements SMSDao {

    private static DBHelper db=new DBHelper();

    public SMSDaoImpl() {
        db.open();
    }

    @Override
    public void create() {
        db.open();
        String sql="CREATE TABLE IF NOT EXISTS GW_SMSLIST_TB (data_id TEXT PRIMARY KEY ASC NOT NULL,data_phone TEXT,data_msg TEXT,data_status INTEGER,data_status_name TEXT,receive_date TEXT,lastupdatetime TEXT,desc TEXT)";
        db.execute(sql,null);
        db.close();
    }

    @Override
    public void add(SMS smsObj) {
        db.open();
        String sql="insert into gw_smslist_tb(data_id,data_phone,data_msg,data_status,data_status_name,receive_date,lastupdatetime,desc)values(" +
                "\""+smsObj.getDataId()+"\",\""+smsObj.getDataPhone()+"\",\""
                +smsObj.getDataMsg().replace("\"","'")+"\","+
                smsObj.getDataStatus()+",\""+
                smsObj.getDataStatusName()+"\",\""+
                smsObj.getReceiveDate()+"\",\""+
                smsObj.getLastupdatetime()+"\",\""+
                smsObj.getDesc()+"\""+
                ")";
        try {
            db.execute(sql,null);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
    }

    @Override
    public void adds(List<SMS> smsObjs) {
        db.open();
        for (SMS smsObj : smsObjs) {
            String sql="insert into gw_smslist_tb(data_id,data_phone,data_msg,data_status,data_status_name,receive_date,lastupdatetime,desc)values(" +
                    "\""+smsObj.getDataId()+"\",\""+smsObj.getDataPhone()+"\",\""
                    +smsObj.getDataMsg().replace("\"","'")+"\","+
                    smsObj.getDataStatus()+",\""+
                    smsObj.getDataStatusName()+"\",\""+
                    smsObj.getReceiveDate()+"\",\""+
                    smsObj.getLastupdatetime()+"\",\""+
                    smsObj.getDesc()+"\""+
                ")";
            try {
                db.execute(sql,null);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        db.close();
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void query(String sql) {

    }

    @Override
    public void update(String id, String obj) {

    }

    public void updateStatus(String key,int status,String status_name) {
        db.open();
        String sql="update gw_smslist_tb set data_status="+status+",data_status_name=\""+status_name+"\" where data_id=\""+key+"\"";
        db.execute(sql,null);
        db.close();
    }

    //查询所有内容
    public List<SMS> queryAllData(){
        db.open();
        String sql="select * from gw_smslist_tb order by receive_date desc";
        List result = db.query(sql,null,SMS.class);
        db.close();
        return result;
    }

    //查询所有消息内容
    public Set queryAllBody(){
        Set set=new HashSet();
        db.open();
        String sql="select data_msg from gw_smslist_tb";
        Cursor cursor= db.queryToCursor(sql, null);
        while (cursor.moveToNext()){
            set.add(cursor.getString(0));
        }
        db.close();
        return set;
    }

    @Override
    public Set getAllDataId() {
        Set set=new HashSet();
        db.open();
        String sql="select data_id from gw_smslist_tb";
        Cursor cursor= db.queryToCursor(sql, null);
        while (cursor.moveToNext()){
            set.add(cursor.getString(0));
        }
        db.close();
        return set;
    }
}
