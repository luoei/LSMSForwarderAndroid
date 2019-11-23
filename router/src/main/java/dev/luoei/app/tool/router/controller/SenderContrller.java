package dev.luoei.app.tool.router.controller;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;

import dev.luoei.app.tool.router.dao.MailAccountDao;
import dev.luoei.app.tool.router.dao.impl.MailAccountDaoImpl;
import dev.luoei.app.tool.router.define.Define;
import dev.luoei.app.tool.router.entity.MailAccount;
import dev.luoei.app.tool.router.entity.MailSenderInfo;
import dev.luoei.app.tool.router.service.LogService;
import dev.luoei.app.tool.router.tool.NetWorkUtils;
import dev.luoei.app.tool.router.tool.SMSUtil;

/**
 * Created by anseenly on 15/1/8.
 */
public class SenderContrller {

    private final static String TAG = "SenderContrller";

    private Context context;

//    private UIClientMessengerUtil uiClientMessengerUtil;

    //网络检测
    public  boolean networkTimeOutCheck(){
        return NetWorkUtils.isNetworkConnected(context);
    }


    /**
     * 短信转发
     * @param title 标题
     * @param content 内容
     * @param context
     */
    public  void sender(String key,String title,String content,Context context,SenderContrllerStatus status){
        this.context=context;
        LogService.initLogger();

        Logger.d("路由模块|发送|准备发送|"+content);

//        uiClientMessengerUtil = new UIClientMessengerUtil(context);

        /// 初始化数据获取
        MailAccountDao mailAccountDao = new MailAccountDaoImpl(context);
        MailAccount mailAccount = mailAccountDao.getFromCache();

        if (null == mailAccount || TextUtils.isEmpty(mailAccount.getUsername())){
//            Log.e(TAG,"请先完善账号信息为空，本次发送取消。");
            Logger.e("路由模块|发送|准备发送|请先完善账号信息为空，本次发送取消。");
            return;
        }

        //更新界面 UI
        sendMessage("正在检测E-MAIL网络");
        status.onChanged(key,Define.SMS_SEND_PROCESS_CODE, Define.SMS_SEND_PROCESS_MSG);

        boolean checkResult=networkTimeOutCheck();
        Log.d(TAG,("Network TEST -->"+checkResult));

        //通过网络发送
        if(checkResult){
            Log.d(TAG,"信息发送,网络超时验证通过，将通过网络发送...");
            sendMessage("已验证通过，通过EMAIL网络发送消息");
            try {
                mailSender(title, content,mailAccount);
                sendMessage("EMAIL-发送完成");
                status.onChanged(key, Define.SMS_SEND_SUCCESS_EMAIL_CODE, Define.SMS_SEND_SUCCESS_EMAIL_MSG);
            } catch (Exception e) {
                e.printStackTrace();
//                sendMessage("EMAIL网络发送失败,将通过SMS 通道发送，失败原因:"+e.getMessage());
                Logger.e("路由模块|发送|失败|EMAIL网络发送失败,将通过SMS 通道发送，失败原因:"+e.getMessage());
                try{
                    new SMSUtil(context).sender(mailAccount.getPhone(),content);
                    sendMessage("SMS-发送完成");
                    status.onChanged(key, Define.SMS_SEND_SUCCESS_SMS_CODE, Define.SMS_SEND_SUCCESS_SMS_MSG);
                }catch (Exception ex){
                    Logger.e("路由模块|发送|失败|"+e.getMessage());
//                    sendMessage("SMS" +
//                            "-失败，原因："+ex.getMessage());
                    status.onChanged(key, Define.SMS_SEND_FAILURE_CODE, Define.SMS_SEND_FAILURE_MSG);
                }
            }
        }else{
            //通过短信发送
            Log.d(TAG,"信息发送,网络超时验证不通过，将通过短信发送...");
            sendMessage("验证未通过，通过SMS网络发送消息");
            try{
                new SMSUtil(context).sender(mailAccount.getPhone(),content);
                sendMessage("SMS-发送完成");
                status.onChanged(key, Define.SMS_SEND_SUCCESS_SMS_CODE, Define.SMS_SEND_SUCCESS_SMS_MSG);
            }catch (Exception e){
//                sendMessage("SMS-失败，原因："+e.getMessage());
                status.onChanged(key, Define.SMS_SEND_FAILURE_CODE, Define.SMS_SEND_FAILURE_MSG);
                Logger.e("路由模块|发送|失败|"+e.getMessage());
            }
        }

        status.onChanged(key, Define.SMS_SEND_FINISHED_CODE, Define.SMS_SEND_FINISHED_MSG);
    }

    public void sendMessage(final String message){
        Logger.d("路由模块|发送|"+message);
        // 发送
//        uiClientMessengerUtil.send(message);
    }


    //邮件发送
    public  void mailSender(String title,String content, MailAccount mailAccount) throws Exception {
        //这个类主要是设置邮件
        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost(mailAccount.getStmpServerUrl());
        mailInfo.setMailServerPort(mailAccount.getPort());
        mailInfo.setValidate(true);
        mailInfo.setUserName(mailAccount.getUsername());
        mailInfo.setPassword(mailAccount.getPassword());//您的邮箱密码
        mailInfo.setFromAddress(mailAccount.getSender());
        mailInfo.setSubject(title);
        mailInfo.setContent(content);
        mailInfo.setTimeout(mailAccount.getTimeout());
        mailInfo.setToAddress(mailAccount.getReceiver());

        //发送邮件
        MailController sms = new MailController();
        sms.sendTextMail(mailInfo);//发送文体格式

    }





















}
