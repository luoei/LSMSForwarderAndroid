package dev.luoei.app.tool.sms.forward.tools.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.luoei.app.tool.sms.forward.common.CommonConstant;
import dev.luoei.app.tool.sms.forward.common.CommonParas;

import static android.database.Cursor.FIELD_TYPE_BLOB;
import static android.database.Cursor.FIELD_TYPE_FLOAT;
import static android.database.Cursor.FIELD_TYPE_INTEGER;
import static android.database.Cursor.FIELD_TYPE_STRING;


/**
 * Created by anseenly on 15/1/24.
 */
public class DBHelper {

    private SQLiteDatabase db=null;

    public void open(){
        if( null == db )db= CommonParas.getMainContext().openOrCreateDatabase(CommonConstant.DB_NAME,
                CommonParas.getMainContext().MODE_PRIVATE,null);
//        Log.v("基础库|DB","地址:"+db.getPath());
    }

    private void open(Context context){
        db=context.openOrCreateDatabase(CommonConstant.DB_NAME,context.MODE_PRIVATE,null);
    }

    public void execute(String sql,Object[] paras){
        if( null == db )open();
        if(null != paras){
            db.execSQL(sql,paras);
        }else {
            db.execSQL(sql);
        }
    }

    public Cursor queryToCursor(String sql,String[] paras) {
        if( null == db )open();
        return db.rawQuery(sql,paras);
    }

    public <T> List<T> query(String sql, String[] paras, Class<T> obj) {
        List list = query(sql, paras);
        if (null != obj){
            if (null != list){
                return JSON.parseArray(JSON.toJSONString(list), obj);
            }
        }
        return list;
    }

    public List<Map> query(String sql, String[] paras) {
        if( null == db )open();
        Cursor cursor = db.rawQuery(sql,paras);
        List result = new ArrayList();
        while (cursor.moveToNext()){
            Map map = new HashMap();
            int length = cursor.getColumnCount();
            for (int i =0; i< length ; i++){
                String columnName = cursor.getColumnName(i);
                int dataType = cursor.getType(i);
                if(dataType == FIELD_TYPE_INTEGER){
                    map.put(columnName, cursor.getInt(i));
                }else if(dataType == FIELD_TYPE_STRING){
                    map.put(columnName, cursor.getString(i));
                }else if(dataType == FIELD_TYPE_FLOAT){
                    map.put(columnName, cursor.getDouble(i));
                }else if(dataType == FIELD_TYPE_BLOB){
                    map.put(columnName, cursor.getBlob(i));
                }
            }
            result.add(map);
        }
        return result.size() > 0 ? result : null;
    }

    public void close(){
        if( null != db){
            if(db.isOpen())db.close();
            db=null;
        }
    }

}
