package dev.luoei.app.tool.sms.forward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import dev.luoei.app.tool.sms.forward.R;
import dev.luoei.app.tool.sms.forward.common.CommonParas;
import dev.luoei.app.tool.sms.forward.controller.InitializeController;
import dev.luoei.app.tool.sms.forward.entity.MailAccount;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initEvents();
    }

    private void initData(){ CommonParas.setMainContext(this.getApplication()); }

    private void initEvents(){
        /// 初始化
        new InitializeController();
        Intent intent = null;
        MailAccount mailAccount = CommonParas.getMailAccount();
        if (null != mailAccount && mailAccount.getUsername() != null && mailAccount.getUsername().length() > 0){
            intent = new Intent(this, MainActivity.class);
        }else {
            intent = new Intent(this,SettingAccountActivity.class);
            intent.putExtra("isActivityFinishedGoBack",false);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.overridePendingTransition(0,0);
    }
}
