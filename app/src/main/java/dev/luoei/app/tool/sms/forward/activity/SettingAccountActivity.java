package dev.luoei.app.tool.sms.forward.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dev.luoei.app.tool.sms.forward.R;
import dev.luoei.app.tool.sms.forward.common.CommonParas;
import dev.luoei.app.tool.sms.forward.dao.MailAccountDao;
import dev.luoei.app.tool.sms.forward.dao.impl.MailAccountDaoImpl;
import dev.luoei.app.tool.sms.forward.entity.MailAccount;
import dev.luoei.app.tool.sms.forward.tools.mail.MailController;

import static android.view.View.INVISIBLE;

public class SettingAccountActivity extends AppCompatActivity {

    // 保存后是否需要返回
    boolean isActivityFinishedGoBack = true;
    View  view;
    private EditText addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account);
        view = findViewById(R.id.activity_setting_account);

        initAction();
        initViewData();
    }

    public void initViewData(){
        if (null != getIntent().getExtras() && getIntent().getExtras().containsKey("isActivityFinishedGoBack")){
            isActivityFinishedGoBack = getIntent().getExtras().getBoolean("isActivityFinishedGoBack");
        }
        MailAccount mailAccount = CommonParas.getMailAccount();
        if(null != CommonParas.getMailAccount()){
            ((EditText)view.findViewById(R.id.dsusername)).setText(mailAccount.getUsername());
            ((EditText)view.findViewById(R.id.dspassword)).setText(mailAccount.getPassword());
            ((EditText)view.findViewById(R.id.dsserver)).setText(mailAccount.getStmpServerUrl());
            ((EditText)view.findViewById(R.id.dsserverport)).setText(mailAccount.getPort());
            ((EditText)view.findViewById(R.id.dsphone)).setText(mailAccount.getPhone());
            ((EditText)view.findViewById(R.id.dsreceiver)).setText(mailAccount.getReceiver());
        }

        if (!isActivityFinishedGoBack){
            Button gobackButton = view.findViewById(R.id.setting_navigtion_go_back);
            gobackButton.setVisibility(INVISIBLE);
        }
    }

    private void initAction(){

        Button loadConfigButton = view.findViewById(R.id.setting_navigtion_go_back);
        loadConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button save = view.findViewById(R.id.setting_save);
        save.setOnClickListener(new DoneOnClickListener());

        Button connectButton = findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new ConnectOnClickListener());
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
            MailAccount mailAccount = CommonParas.getMailAccount();
            if(null == mailAccount){
                mailAccount = new MailAccount();
                CommonParas.setMailAccount(mailAccount);
            }

            mailAccount.setUsername(username);
            mailAccount.setPassword(password);
            mailAccount.setStmpServerUrl(serverUrl);
            mailAccount.setPort(port);
            mailAccount.setPhone(phone);
            mailAccount.setReceiver(receiver);
            mailAccount.setSender(mailAccount.getUsername());
            mailAccount.setTimeout("60");

            MailAccountDao mailAccountDao = new MailAccountDaoImpl();
            mailAccountDao.save(mailAccount);

            if (!isActivityFinishedGoBack){
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            finish();
        }
    }

}
