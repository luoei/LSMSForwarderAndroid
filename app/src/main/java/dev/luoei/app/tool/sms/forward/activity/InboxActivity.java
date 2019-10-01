package dev.luoei.app.tool.sms.forward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.luoei.app.tool.sms.forward.R;
import dev.luoei.app.tool.sms.forward.common.CommonParas;
import dev.luoei.app.tool.sms.dao.SMSDao;
import dev.luoei.app.tool.sms.dao.impl.SMSDaoImpl;
import dev.luoei.app.tool.sms.entity.SMS;

/**
 * Created by admin on 13-11-23.
 */
public class InboxActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_inboxview);
        this.initData();
    }

    private void initData(){
        //绑定XML中的ListView，作为Item的容器
        listView = (ListView) findViewById(R.id.listView);

        //生成动态数组，并且转载数据
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        SMSDao smsDao =new SMSDaoImpl(this);
        List<SMS> list= smsDao.queryAllData();
        if (null != list && list.size() > 0){
            for(SMS sms : list) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("phone", sms.getDataPhone());
                map.put("status", sms.getDataStatusName());
                map.put("message", sms.getDataMsg());
                mylist.add(map);
            }
        }
        //生成适配器，数组===》ListItem
        SimpleAdapter mSchedule = new SimpleAdapter(CommonParas.getMainContext(),
                mylist,
                R.layout.listview_item,
                new String[] {"phone", "status","message"},
                new int[] {R.id.phone,R.id.status,R.id.message});
        //添加并且显示
        listView.setAdapter(mSchedule);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("视图销毁-->", this.getClass().getName());
    }

}
