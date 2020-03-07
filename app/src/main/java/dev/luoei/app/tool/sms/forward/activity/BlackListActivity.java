package dev.luoei.app.tool.sms.forward.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dev.luoei.app.tool.sms.forward.R;
import dev.luoei.app.tool.sms.forward.common.CommonParas;
import dev.luoei.app.tool.sms.forward.common.InitGlobalVar;

/**
 * Created by admin on 13-11-23.
 */
public class BlackListActivity extends AppCompatActivity {
    ListView listView;
    List<HashMap> dataSource;
    public static String StoreKey = "BlackFilterKey";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklistview);
        initViewEvents();
        loadData();
    }

    private void loadData(){
        //生成动态数组，并且转载数据
        Set<String> store = InitGlobalVar.getVar(CommonParas.getMainContext(),StoreKey);

        if (listView == null){
            //绑定XML中的ListView，作为Item的容器
            listView = (ListView) findViewById(R.id.black_listView);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i>=dataSource.size()){
                        return;
                    }
                    Map map = dataSource.get(i);
                    final String content = String.valueOf(map.get("content"));
                    if (content == null || content.length() == 0){
                        return;
                    }
                    AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(CommonParas.getMainContext());
                    alertDialogBuilder.setTitle("提示");
                    alertDialogBuilder.setNegativeButton("取消",null);
                    alertDialogBuilder.setPositiveButton("确定",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Set<String> store = InitGlobalVar.getVar(CommonParas.getMainContext(),StoreKey);
                            if (store == null || store.size() == 0 || !store.contains(content)){
                                Toast.makeText(CommonParas.getMainContext(),"删除失败",Toast.LENGTH_SHORT).show();
                            }
                            if (store.contains(content)){
                                store.remove(content);
                            }

                            boolean result = InitGlobalVar.setVar(CommonParas.getMainContext(),StoreKey,store);
                            if (result){
                                Toast.makeText(CommonParas.getMainContext(),"删除成功",Toast.LENGTH_SHORT).show();
                                loadData();
                            }else {
                                Toast.makeText(CommonParas.getMainContext(),"删除失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    alertDialogBuilder.setMessage(content + "\n确认删除?");
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });
        }

        List arrayList = new ArrayList();
        if (store != null && store.size() > 0) {
            for(String content : store)
            {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("content", content);
                arrayList.add(map);
            }
        }
        this.dataSource = arrayList;
        //生成适配器，数组===》ListItem
        SimpleAdapter mSchedule = new SimpleAdapter(CommonParas.getMainContext(),
                arrayList,
                R.layout.listview_black_item,
                new String[]{"content"},
                new int[] {R.id.black_content});
        //添加并且显示
        listView.setAdapter(mSchedule);
    }

    private void initViewEvents() {
        Button addButton = (Button) findViewById(R.id.black_add);

        addButton.setOnClickListener(new BlackClassAddFilterString());
    }

    class BlackClassAddFilterString implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            EditText editText = (EditText) findViewById(R.id.black_content);
            String content = editText.getText().toString();
            InputMethodManager imm = (InputMethodManager) CommonParas.getMainContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            if (content == null || content.length() == 0 || content.trim().length() == 0){
                Toast.makeText(CommonParas.getMainContext(),"不能为空",Toast.LENGTH_SHORT).show();
                return;
            }

            content = content.trim();

            Set store = InitGlobalVar.getVar(CommonParas.getMainContext(),StoreKey);
            if (store == null){
                store = new HashSet();
            }
            if (store.contains(content)){
                Toast.makeText(CommonParas.getMainContext(),"请勿重复添加",Toast.LENGTH_SHORT).show();
                return;
            }

            store.add(content);

            boolean result = InitGlobalVar.setVar(CommonParas.getMainContext(),StoreKey,store);
            if (result){
                Toast.makeText(CommonParas.getMainContext(),"添加成功",Toast.LENGTH_SHORT).show();
                loadData();
            }else {
                Toast.makeText(CommonParas.getMainContext(),"添加失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("视图销毁-->",this.getClass().getName());
    }

}
