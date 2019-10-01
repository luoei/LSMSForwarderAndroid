package dev.luoei.app.tool.sms.forward.controller;

import dev.luoei.app.tool.sms.forward.common.CommonParas;
import dev.luoei.app.tool.router.dao.MailAccountDao;
import dev.luoei.app.tool.sms.dao.SMSDao;
import dev.luoei.app.tool.router.dao.impl.MailAccountDaoImpl;
import dev.luoei.app.tool.sms.dao.impl.SMSDaoImpl;


/**
 * Created by anseenly on 15/1/24.
 */
public class InitializeController {

    public InitializeController() {
        start();
    }

    public void start(){
        /// 数据库初始化
        SMSDao smsDao = new SMSDaoImpl(CommonParas.getMainContext());
        smsDao.create();
        MailAccountDao mailAccountDao = new MailAccountDaoImpl(CommonParas.getMainContext());
        mailAccountDao.create();

    }


}
