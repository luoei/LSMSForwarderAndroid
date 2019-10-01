package dev.luoei.app.tool.router.define;

public class Define {


    //----------------- SMS ---------------//
    public static final String SMS_ADDRESS="sms_address";
    public static final String SMS_MSG="sms_msg";
    public static final String SMS_PHONE = "data_phone";
    public static final String SMS_BODY = "data_msg";
    public static final String SMS_SEND_DATE = "send_date";

    //----------------- SMS Send Status---------------//
    public static final int SMS_SEND_SUCCESS_EMAIL_CODE=00;
    public static final String SMS_SEND_SUCCESS_EMAIL_MSG="E:已发送";
    public static final int SMS_SEND_SUCCESS_SMS_CODE=01;
    public static final String SMS_SEND_SUCCESS_SMS_MSG="S:已发送";
    public static final int SMS_SEND_FAILURE_CODE=02;
    public static final String SMS_SEND_FAILURE_MSG="发送失败";
    public static final int SMS_SEND_INIT_CODE=10;
    public static final String SMS_SEND_INIT_MSG="初始化";
    public static final int SMS_SEND_PROCESS_CODE=11;
    public static final String SMS_SEND_PROCESS_MSG="发送中";
    public static final int SMS_SEND_HISTORY_CODE=12;
    public static final String SMS_SEND_HISTORY_MSG="本地短信";

    public static final int SMS_SEND_FINISHED_CODE=99;
    public static final String SMS_SEND_FINISHED_MSG="发送完成";

}
