package dev.luoei.app.tool.sms.forward.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by usb on 15-1-12.
 */
public class MailAccount {

    private  String username;

    private  String password;

    @JSONField(name = "server_url")
    private  String stmpServerUrl;

    private  String sender;

    private  String receiver;

    private  String port;

    private  String timeout;

    private  String phone;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStmpServerUrl() {
        return stmpServerUrl;
    }

    public void setStmpServerUrl(String stmpServerUrl) {
        this.stmpServerUrl = stmpServerUrl;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
