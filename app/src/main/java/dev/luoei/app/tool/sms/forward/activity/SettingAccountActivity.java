package dev.luoei.app.tool.sms.forward.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import dev.luoei.app.tool.router.controller.MailController;
import dev.luoei.app.tool.router.entity.MailAccount;
import dev.luoei.app.tool.sms.forward.R;
import dev.luoei.app.tool.sms.forward.common.CommonParas;
import dev.luoei.app.tool.router.dao.MailAccountDao;
import dev.luoei.app.tool.router.dao.impl.MailAccountDaoImpl;


import static android.view.View.INVISIBLE;

public class SettingAccountActivity extends AppCompatActivity {

    // 保存后是否需要返回
    boolean isActivityFinishedGoBack = true;
    View  view;
    private EditText addresses;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account);
        view = findViewById(R.id.activity_setting_account);
        context = this;

        initAction();
        initViewData();

    }

    public void initViewData(){
        if (null != getIntent().getExtras() && getIntent().getExtras().containsKey("isActivityFinishedGoBack")){
            isActivityFinishedGoBack = getIntent().getExtras().getBoolean("isActivityFinishedGoBack");
        }
        MailAccountDao mailAccountDao = new MailAccountDaoImpl(this);
        MailAccount mailAccount = mailAccountDao.getFromCache();
        if(null != mailAccount){
            ((EditText)view.findViewById(R.id.dsusername)).setText(mailAccount.getUsername());
            ((EditText)view.findViewById(R.id.dspassword)).setText(mailAccount.getPassword());
            ((EditText)view.findViewById(R.id.dsserver)).setText(mailAccount.getStmpServerUrl());
            ((EditText)view.findViewById(R.id.dsserverport)).setText(mailAccount.getPort());
            ((EditText)view.findViewById(R.id.dsphone)).setText(mailAccount.getPhone());
            ((EditText)view.findViewById(R.id.dsreceiver)).setText(mailAccount.getReceiver());
        }

        Button loadConfigButton = view.findViewById(R.id.loadConfigButton);
        if (!isActivityFinishedGoBack){
            Button gobackButton = view.findViewById(R.id.setting_navigtion_go_back);
            gobackButton.setVisibility(INVISIBLE);
            loadConfigButton.setVisibility(View.VISIBLE);
        }else {
            loadConfigButton.setVisibility(INVISIBLE);
        }

        requestPermissions();
    }

    private void initAction(){

        Button navGoBackButton = view.findViewById(R.id.setting_navigtion_go_back);
        navGoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button save = view.findViewById(R.id.setting_save);
        save.setOnClickListener(new DoneOnClickListener());

        Button connectButton = findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new ConnectOnClickListener());

        Button loadConfigButton = findViewById(R.id.loadConfigButton);
        loadConfigButton.setOnClickListener(new LoadConfigOnClickListener());
    }

    class ConnectOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Button connectButton = findViewById(R.id.connectButton);
            connectButton.setText("连接中……");
            connectButton.setEnabled(false);
            new ConnectButtonTask().execute();
        }
    }

    class LoadConfigOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Properties properties = new Properties();
            File file = new File(Environment.getExternalStorageDirectory(),"sms.pro");
            try {
                properties.load(new FileReader(file));
                String account = properties.getProperty("account");
                String password = properties.getProperty("password");
                String serverUrl = properties.getProperty("serverUrl");
                String serverPort = properties.getProperty("serverPort");
                String receiverMobile = properties.getProperty("receiverMobile");
                String receiverEmail = properties.getProperty("receiverEmail");
                ((EditText)view.findViewById(R.id.dsusername)).setText(account);
                ((EditText)view.findViewById(R.id.dspassword)).setText(password);
                ((EditText)view.findViewById(R.id.dsserver)).setText(serverUrl);
                ((EditText)view.findViewById(R.id.dsserverport)).setText(serverPort);
                ((EditText)view.findViewById(R.id.dsphone)).setText(receiverMobile);
                ((EditText)view.findViewById(R.id.dsreceiver)).setText(receiverEmail);
                Toast.makeText(CommonParas.getMainContext(),"加载成功",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(CommonParas.getMainContext(),"请把sms.pro文件放在内存卡根目录",Toast.LENGTH_SHORT).show();
//                Toast.makeText(CommonParas.getMainContext(),"加载失败，"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class ConnectButtonTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String username = ((EditText)view.findViewById(R.id.dsusername)).getText().toString();
            if (null ==  username || username.length() == 0)return null;
            String password = ((EditText)view.findViewById(R.id.dspassword)).getText().toString();
            String serverUrl = ((EditText)view.findViewById(R.id.dsserver)).getText().toString();
            String port = ((EditText)view.findViewById(R.id.dsserverport)).getText().toString();
            if(null == port || port.length() == 0){
                port = "465";
            }
            MailController mailController = new MailController();
            boolean success = mailController.login(username,password,serverUrl,port);
            return success ? "1" : "0";
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("1")){
                Toast.makeText(CommonParas.getMainContext(), "连接成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(CommonParas.getMainContext(), "账号或密码错误", Toast.LENGTH_SHORT).show();
            }
            Button connectButton = findViewById(R.id.connectButton);
            connectButton.setText("连接测试");
            connectButton.setEnabled(true);
        }
    }

    class DoneOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            InputMethodManager imm = (InputMethodManager) CommonParas.getMainContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            //保存设置
            Log.d("Setting Save -->","正在保存设置");
            String username = ((EditText)view.findViewById(R.id.dsusername)).getText().toString();
            if (null ==  username || username.length() == 0)return;
            String password = ((EditText)view.findViewById(R.id.dspassword)).getText().toString();
            String serverUrl = ((EditText)view.findViewById(R.id.dsserver)).getText().toString();
            String phone = ((EditText)view.findViewById(R.id.dsphone)).getText().toString();
            String receiver = ((EditText)view.findViewById(R.id.dsreceiver)).getText().toString();

            String port = ((EditText)view.findViewById(R.id.dsserverport)).getText().toString();
            if(null == port || port.length() == 0){
                port = "465";
            }
            MailAccountDao mailAccountDao = new MailAccountDaoImpl(CommonParas.getMainContext());
            MailAccount mailAccount = mailAccountDao.getFromCache();
            if(null == mailAccount){
                mailAccount = new MailAccount();
            }

            mailAccount.setUsername(username);
            mailAccount.setPassword(password);
            mailAccount.setStmpServerUrl(serverUrl);
            mailAccount.setPort(port);
            mailAccount.setPhone(phone);
            mailAccount.setReceiver(receiver);
            mailAccount.setSender(mailAccount.getUsername());
            mailAccount.setTimeout("60");

            mailAccountDao.save(mailAccount);

            if (!isActivityFinishedGoBack){
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            finish();
        }
    }

    private void requestPermissions(){
        boolean mShowRequestPermission = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String[] permissions = new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_PHONE_NUMBERS,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.CHANGE_NETWORK_STATE,Manifest.permission.RECEIVE_BOOT_COMPLETED,Manifest.permission.FOREGROUND_SERVICE,Manifest.permission.READ_EXTERNAL_STORAGE};
//            String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
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

            }
        }else {

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

                }
                break;
        }
    }

}
