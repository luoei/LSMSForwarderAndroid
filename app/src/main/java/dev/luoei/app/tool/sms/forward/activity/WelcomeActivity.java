package dev.luoei.app.tool.sms.forward.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import dev.luoei.app.tool.usb.share.UsbShare;
import dev.luoei.app.tool.router.dao.MailAccountDao;
import dev.luoei.app.tool.router.dao.impl.MailAccountDaoImpl;
import dev.luoei.app.tool.router.entity.MailAccount;
import dev.luoei.app.tool.sms.forward.R;
import dev.luoei.app.tool.sms.forward.common.CommonParas;
import dev.luoei.app.tool.sms.forward.controller.InitializeController;
import dev.luoei.app.tool.sms.forward.service.LogService;
import dev.luoei.app.tool.sms.forward.tools.ServiceUtil;

public class WelcomeActivity extends AppCompatActivity {

    private final static String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        /// get root permission
        UsbShare.upgradeRootPermission(getPackageCodePath());

        initData();
    }

    private void initData(){
        LogService.initLogger();
        CommonParas.setMainContext(this.getApplication());
        initEvents();
    }

    private void initEvents(){
        /// 初始化
        new InitializeController();
        Intent intent = null;
        MailAccountDao mailAccountDao = new MailAccountDaoImpl(CommonParas.getMainContext());
        MailAccount mailAccount = mailAccountDao.getFromCache();
        if (null != mailAccount && mailAccount.getUsername() != null && mailAccount.getUsername().length() > 0){
            intent = new Intent(this, MainActivity.class);

            //启动服务进程
            ServiceUtil.start(CommonParas.getMainContext());
        }else {
            intent = new Intent(this,SettingAccountActivity.class);
            intent.putExtra("isActivityFinishedGoBack",false);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.overridePendingTransition(0,0);

    }


}
