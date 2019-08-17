package dev.luoei.app.tool.sms.forward.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by anseenly on 15/4/19.
 */
public class SMS {

    @JSONField(name = "data_id")
    private String dataId;

    @JSONField(name = "data_phone")
    private String dataPhone;

    @JSONField(name = "data_msg")
    private String dataMsg;

    @JSONField(name = "data_status")
    private int dataStatus;

    @JSONField(name = "data_status_name")
    private String dataStatusName;

    @JSONField(name = "receive_date")
    private String  receiveDate;

    private String lastupdatetime;

    private String desc;


    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getDataPhone() {
        return dataPhone;
    }

    public void setDataPhone(String dataPhone) {
        this.dataPhone = dataPhone;
    }

    public String getDataMsg() {
        return dataMsg;
    }

    public void setDataMsg(String dataMsg) {
        this.dataMsg = dataMsg;
    }

    public int getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(int dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getDataStatusName() {
        return dataStatusName;
    }

    public void setDataStatusName(String dataStatusName) {
        this.dataStatusName = dataStatusName;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getLastupdatetime() {
        return lastupdatetime;
    }

    public void setLastupdatetime(String lastupdatetime) {
        this.lastupdatetime = lastupdatetime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public String generateDataId(){
        String value = this.getDataPhone()+ this.getDataMsg()+ this.getReceiveDate();
        return MD5(value);
    }

    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
