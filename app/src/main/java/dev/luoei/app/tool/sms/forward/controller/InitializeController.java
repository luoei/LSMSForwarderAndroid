package dev.luoei.app.tool.sms.forward.controller;

import dev.luoei.app.tool.sms.forward.common.CommonParas;
import dev.luoei.app.tool.sms.forward.dao.MailAccountDao;
import dev.luoei.app.tool.sms.forward.dao.SMSDao;
import dev.luoei.app.tool.sms.forward.dao.impl.MailAccountDaoImpl;
import dev.luoei.app.tool.sms.forward.dao.impl.SMSDaoImpl;
import dev.luoei.app.tool.sms.forward.entity.MailAccount;


/**
 * Created by anseenly on 15/1/24.
 */
public class InitializeController {

    public InitializeController() {
        start();
    }

    public void start(){
        /// 数据库初始化
        SMSDao smsDao = new SMSDaoImpl();
        smsDao.create();
        MailAccountDao mailAccountDao = new MailAccountDaoImpl();
        mailAccountDao.create();
        /// 初始化数据获取
        MailAccount mailAccount = mailAccountDao.getFromCache();
        if(null != mailAccount){
            CommonParas.setMailAccount(mailAccount);
        }
    }


}
