package dev.luoei.app.tool.sms.forward.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import dev.luoei.app.tool.sms.forward.R;
import dev.luoei.app.tool.sms.forward.common.CommonParas;
import dev.luoei.app.tool.sms.forward.common.CommonVariables;
import dev.luoei.app.tool.sms.forward.controller.TabbarController;
import dev.luoei.app.tool.sms.forward.tools.BasicTools;
import dev.luoei.app.tool.sms.forward.tools.ProcessUtil;
import dev.luoei.app.tool.sms.forward.tools.ScheduleUtil;
import dev.luoei.app.tool.sms.tool.SMSUtil;


public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    RadioGroup radioGroup;
    FragmentTransaction transaction;
    Fragment currFragment;
    int currFragmentId;
    public static Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarbar);

        initDockView();
        initGlobalConfig();

        // 黑名单配置
        BasicTools basicTools = new BasicTools();
        basicTools.initBlackFilter();

        requestPermissions();
    }

    private void requestPermissions(){
        boolean mShowRequestPermission = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String[] permissions = new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.CHANGE_NETWORK_STATE,Manifest.permission.RECEIVE_BOOT_COMPLETED,Manifest.permission.READ_EXTERNAL_STORAGE};
            List<String> mPermissionList = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(CommonParas.getMainContext(), permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);
                }
            }

            if (mPermissionList.isEmpty()) {// 全部允许
                mShowRequestPermission = true;
            } else {//存在未允许的权限
                String[] permissionsArr = mPermissionList.toArray(new String[mPermissionList.size()]);
                ActivityCompat.requestPermissions(this, permissionsArr, 101);
//                currFragment.requestPermissions(permissionsArr,101);
            }
            if (mShowRequestPermission){
                loadSmsService();
            }
        }else {
            loadSmsService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                int confim = 0;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                       confim++;
                    }
                }
                if (confim == grantResults.length){
                    loadSmsService();
                }
                break;
        }
    }

    private void loadSmsService(){
        new SMSUtil(this).loadLocalSms();
        ScheduleUtil.smsCheckinSchedule();
    }

    private void initGlobalConfig(){
        CommonParas.setMainContext(MainActivity.this);
        CommonParas.sharedPreferences=getSharedPreferences(CommonVariables.CONFIG_SETTING, Activity.MODE_PRIVATE);
        // 显示本地通知
//        new NotificationUtil().showLocalNotification();

//        Intent messengerService = new Intent(this, MessengerService.class);
//        startService(messengerService);

//        startService(new Intent(MainActivity.this, SmsObserverService.class));

        System.out.println("当前进程："+ ProcessUtil.getProcessName());
    }

    private void initDockView() {
        //初始化底部导航栏
        fragmentManager = getFragmentManager();
        radioGroup = (RadioGroup) findViewById(R.id.rg_tab);
        transaction = fragmentManager.beginTransaction();
        //首先加载主界面的第一个选项卡界面
        currFragmentId = R.id.btn01;
        currFragment = TabbarController.getInstanceByIndex(R.id.btn01);
        transaction.add(R.id.black_content,currFragment);
        transaction.commit();
        //为单选框按钮组添加选项改变事件
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedId = group.getCheckedRadioButtonId();
                //排除重复点击当前选项卡，造成资源浪费
                if(currFragmentId == checkedId) return;
                transaction = fragmentManager.beginTransaction();
                Fragment checkedFragment = TabbarController.getInstanceByIndex(checkedId);
                //只保留主视图在内存中，其他都删除
                if(checkedId==R.id.btn01||currFragmentId!=R.id.btn01) {
                    transaction.remove(currFragment);
                }
                //标签切换
                if(!checkedFragment.isAdded()){
                    transaction.hide(currFragment).add(R.id.black_content,checkedFragment);
                }else{
                    transaction.hide(currFragment).show(checkedFragment);
                }
                Log.d("Fragment changed -->",currFragment.getClass().getName()+" --> "+checkedFragment.getClass().getName());

                currFragmentId = checkedId;
                currFragment=checkedFragment;
                transaction.commit();
            }
        });


    }

}
