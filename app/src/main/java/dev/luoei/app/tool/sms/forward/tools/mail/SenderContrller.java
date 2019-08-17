package dev.luoei.app.tool.sms.forward.tools.mail;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.Method;

import dev.luoei.app.tool.sms.forward.common.CommonParas;
import dev.luoei.app.tool.sms.forward.common.CommonVariables;
import dev.luoei.app.tool.sms.forward.dao.SMSDao;
import dev.luoei.app.tool.sms.forward.dao.impl.SMSDaoImpl;
import dev.luoei.app.tool.sms.forward.entity.MailAccount;
import dev.luoei.app.tool.sms.forward.tools.MessageTool;
import dev.luoei.app.tool.sms.forward.tools.NetWorkUtils;
import dev.luoei.app.tool.sms.forward.tools.mobile.SMSService;

/**
 * Created by anseenly on 15/1/8.
 */
public class SenderContrller {

    private  ConnectivityManager connectivityManager;
    private Context context;
    private int networkRestore=-1;

    //网络检测
    public  boolean networkTimeOutCheck(){
        return networkTimeoutValidate();
//        boolean flag=true;
//        //获取网络类型
//        int networkType=getAPNType();
//        if(networkType==-1){
//            //开启移动网络
//            setMobileNetwork(true);
//            try{
//                Log.e("网络超时测试 -->","开始打开移动网络,等待时间5秒");
//                Thread.sleep(5000);
//            }catch (Exception e){
//                Log.e("网络管理 -->","发送异常，"+e.getMessage());
//            }
//            networkRestore=0;
//            networkType=0;
//        }
//        //检测是否超时，超时则开启移动网络发送，如果当前已经是移动网络，则直接发送短信
//        if(networkTimeoutValidate())return true;
//        if(networkType==0){
//            Log.e("网络超时测试 -->","移动网络发送超时，开始调用短信发送...");
//            return false;
//        }
//        Log.e("网络超时测试 -->","WIFI 网络发送超时，开始调用移动网络发送...");
//        //如果没有网络则直接开启移动网络发送短信
//        //WIFI 开着，则需要关闭
//        if(networkType==1){
//            //关闭WIFI
//            setWifiManager(false);
//            networkRestore=1;
//        }
//        //开启移动网络
//        setMobileNetwork(true);
//        try{
//            Log.e("网络超时测试 -->","开始网络切换[WIFI-->移动网络],等待时间5秒");
//            Thread.sleep(5000);
//        }catch (Exception e){
//            Log.e("网络管理 -->","发送异常，"+e.getMessage());
//        }
//        //超时检查
//        if(networkTimeoutValidate())return true;
//        Log.e("网络超时测试 -->","WIFI--移动网络发送超时，开始调用短信发送...");
//        flag=false;
//        return flag;
    }


    private void setWifiManager(boolean flag){
        WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if(flag!=wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(flag);
        }
    }

    private void setMobileNetwork(boolean flag){
        NetworkInfo networkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((flag&&networkInfo==null)||(!flag&&networkInfo!=null)){ //开启
            Class<?> cmClass=connectivityManager.getClass();
            Class<?>[] argClasses=new Class[1];
            argClasses[0]=boolean.class;
            try{
                Method method=cmClass.getMethod("setMobileDataEnabled",argClasses);
                method.invoke(connectivityManager,flag);
                Log.d("Mobile Network Manager -->","Mobile Network "+(flag?"open":"close")+" done!");
            }catch (Exception e){
                e.printStackTrace();
                Log.e("Mobile Network Manager -->","管理异常，"+e.getMessage());
            }
        }
    }


    public  boolean networkTimeoutValidate(){

        return NetWorkUtils.isNetworkConnected(context);

//        boolean flag=true;
//        try{
//            String seturl=MailAccount.getStmpServerUrl();
//            if(seturl==null||seturl=="")return false;
//            seturl=seturl.substring(seturl.indexOf(".")+1);
//            URL url=new URL("http://xdf.cn");
//            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
//            connection.setConnectTimeout(Integer.parseInt(MailAccount.getTimeout())*1000);
//            connection.setReadTimeout(Integer.parseInt(MailAccount.getTimeout())*1000);
//            connection.connect();
//            connection.disconnect();
//        }catch(MalformedURLException e){
//            e.printStackTrace();
//            Log.e("Network TEST -->","URL 解析异常"+e.getMessage());
//            flag=false;
//        }catch(IOException e){
//            e.printStackTrace();
//            Log.e("Network TEST -->","URL IO 异常"+e.getMessage());
//            flag=false;
//        }
//        return flag;
    }

    /**
     * 网络类型判断
     * @return -1 无网络 0 移动网络 1 WIFI网络
     */
    public  int getAPNType(){
        int apnType=-1;
        connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null)return apnType;
        int nType=networkInfo.getType();
        Log.d("Network Check -->","类型 : "+nType);
        Log.d("Network Check -->","getExtraInfo: "+networkInfo.getExtraInfo()+" == "+networkInfo.getTypeName()+" -- "+networkInfo.getReason()
        +" == "+networkInfo.getSubtypeName()+" == "+networkInfo.getDetailedState()+" == "+networkInfo.getState());
        if(nType==ConnectivityManager.TYPE_MOBILE){
//            if(networkInfo.getExtraInfo())
            apnType=0;
        }
        if(nType==ConnectivityManager.TYPE_WIFI){
            apnType=1;
        }
        return apnType;
    }


    /**
     * 短信转发
     * @param title 标题
     * @param content 内容
     * @param context
     */
    public  void sender(String key,String title,String content,Context context){
        //网络检测
//更新界面 UI
        new MessageTool().sendMessage(MessageTool.MAIN_VIEW, new String[]{null,"正在检测E-MAIL网络"}, CommonVariables.UPDATE_MAINVIEW_NOTICEMSG);
//        getAPNType(context);
        this.context=context;
        SMSDao smsDao =new SMSDaoImpl();
        smsDao.updateStatus(key, CommonVariables.SMS_SEND_PROCESS_CODE, CommonVariables.SMS_SEND_PROCESS_MSG);
        boolean checkResult=networkTimeOutCheck();
        Log.d("Network TEST -->",("------------------------"+checkResult));
        //通过网络发送
        if(checkResult){
            Log.d("信息发送-->","网络超时验证通过，将通过网络发送...");
            new MessageTool().sendMessage(MessageTool.MAIN_VIEW, new String[]{null,"已验证通过，通过EMAIL网络发送消息"}, CommonVariables.UPDATE_MAINVIEW_NOTICEMSG);
            try {
                mailSender(title, content);
                new MessageTool().sendMessage(MessageTool.MAIN_VIEW, new String[]{null,"EMAIL-发送完成"}, CommonVariables.UPDATE_MAINVIEW_NOTICEMSG);
                smsDao.updateStatus(key, CommonVariables.SMS_SEND_SUCCESS_EMAIL_CODE, CommonVariables.SMS_SEND_SUCCESS_EMAIL_MSG);
            } catch (Exception e) {
//                e.printStackTrace();
                new MessageTool().sendMessage(MessageTool.MAIN_VIEW, new String[]{null,"EMAIL网络发送失败,将通过SMS 通道发送，失败原因:"+e.getMessage()}, CommonVariables.UPDATE_MAINVIEW_NOTICEMSG);
                try{
                    new SMSService().sender(context, CommonParas.getMailAccount().getPhone(),content);
                    new MessageTool().sendMessage(MessageTool.MAIN_VIEW, new String[]{null,
                            "SMS-发送完成"}, CommonVariables.UPDATE_MAINVIEW_NOTICEMSG);
                    smsDao.updateStatus(key, CommonVariables.SMS_SEND_SUCCESS_SMS_CODE, CommonVariables.SMS_SEND_SUCCESS_SMS_MSG);
                }catch (Exception ex){
                    new MessageTool().sendMessage(MessageTool.MAIN_VIEW, new String[]{null,"SMS" +
                            "-失败，原因："+ex.getMessage()}, CommonVariables.UPDATE_MAINVIEW_NOTICEMSG);
                    smsDao.updateStatus(key, CommonVariables.SMS_SEND_FAILURE_CODE, CommonVariables.SMS_SEND_FAILURE_MSG);
                }
            }


        }else{//通过短信发送
            //短信发送开启
            //短信发送失败，则加入排队机制中，等待下一次触发
            Log.d("信息发送-->","网络超时验证不通过，将通过短信发送...");
            new MessageTool().sendMessage(MessageTool.MAIN_VIEW, new String[]{null,"验证未通过，通过SMS网络发送消息"}, CommonVariables.UPDATE_MAINVIEW_NOTICEMSG);
            try{
                new SMSService().sender(context, CommonParas.getMailAccount().getPhone(),content);
                new MessageTool().sendMessage(MessageTool.MAIN_VIEW, new String[]{null,"SMS-发送完成"}, CommonVariables.UPDATE_MAINVIEW_NOTICEMSG);
                smsDao.updateStatus(key, CommonVariables.SMS_SEND_SUCCESS_SMS_CODE, CommonVariables.SMS_SEND_SUCCESS_SMS_MSG);
            }catch (Exception e){
                new MessageTool().sendMessage(MessageTool.MAIN_VIEW, new String[]{null,"SMS-失败，原因："+e.getMessage()}, CommonVariables.UPDATE_MAINVIEW_NOTICEMSG);
                smsDao.updateStatus(key, CommonVariables.SMS_SEND_FAILURE_CODE, CommonVariables.SMS_SEND_FAILURE_MSG);
            }
        }
    }

    //邮件发送
    public  void mailSender(String title,String content) throws Exception {
        MailAccount mailAccount = CommonParas.getMailAccount();
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
        //这个类主要来发送邮件
        MailController sms = new MailController();

        mailInfo.setToAddress(mailAccount.getReceiver());
        sms.sendTextMail(mailInfo);//发送文体格式

    }





















}
