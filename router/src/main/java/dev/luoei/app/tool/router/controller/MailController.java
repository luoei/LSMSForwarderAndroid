package dev.luoei.app.tool.router.controller;

/**
 * Created by anseenly on 15/1/8.
 */

import android.os.Build;
import android.util.Log;

import java.util.Date;
import java.util.Properties;
import java.util.TimerTask;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import dev.luoei.app.tool.router.entity.MailSenderInfo;
import dev.luoei.app.tool.router.entity.MyAuthenticator;
import dev.luoei.app.tool.router.tool.TimeUtil;


/**
 * 简单邮件（不带附件的邮件）发送器
 */
public class MailController {

    private final static String TAG = "MailController";

    public boolean login(String username, String password, String serverUrl, String port) {

        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost(serverUrl);
        mailInfo.setMailServerPort(port);
        mailInfo.setValidate(true);
        mailInfo.setUserName(username);
        mailInfo.setPassword(password);//您的邮箱密码
        mailInfo.setTimeout("5");

        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        if (mailInfo.isValidate()) {
            // 如果需要身份认证，则创建一个密码验证器
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session session = Session.getInstance(pro,authenticator);
        try {
            Transport transport = session.getTransport();
            transport.connect();
            boolean success = transport.isConnected();
            transport.close();
            return success;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 以文本格式发送邮件
     * @param mailInfo 待发送的邮件的信息
     */
    public boolean sendTextMail(final MailSenderInfo mailInfo) throws Exception {
        try {
            Log.v(TAG,"邮件【"+mailInfo.getToAddress()+" - "+mailInfo.getContent()+"】发送中...");
            // 判断是否需要身份认证
            MyAuthenticator authenticator = null;
            Properties pro = mailInfo.getProperties();
            if (mailInfo.isValidate()) {
                // 如果需要身份认证，则创建一个密码验证器
                authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
            }
            // 根据邮件会话属性和密码验证器构造一个发送邮件的session
            Session sendMailSession = Session.getInstance(pro,authenticator);

            // 根据session创建一个邮件消息
            final Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO,to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // 设置邮件消息的主要内容
            String mailContent = mailInfo.getContent();
            mailContent=mailContent+ "\n\n---------------------------------\n Device Infomation: "
                    +Build.BRAND+" "
                    +Build.MODEL+" "
                    +Build.PRODUCT+"\n Sender Time: "
                    +TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:dd")+"\n";
            mailMessage.setText(mailContent);

            new Thread(new TimerTask() {
                @Override
                public void run() {
                    // 发送邮件
                    try {
                        Transport.send(mailMessage);
                        Log.v(TAG,"邮件【"+mailInfo.getToAddress()+"】发送完成");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();

            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }

    }

    /**
     * 以HTML格式发送邮件
     * @param mailInfo 待发送的邮件信息
     */
    public static boolean sendHtmlMail(MailSenderInfo mailInfo){
        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        //如果需要身份认证，则创建一个密码验证器
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getInstance(pro,authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailInfo.getToAddress());
            // Message.RecipientType.TO属性表示接收者的类型为TO
            mailMessage.setRecipient(Message.RecipientType.TO,to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();
            // 创建一个包含HTML内容的MimeBodyPart
            BodyPart html = new MimeBodyPart();
            // 设置HTML内容
            html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);
            // 将MiniMultipart对象设置为邮件内容
            mailMessage.setContent(mainPart);
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}