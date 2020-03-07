package dev.luoei.app.tool.sms.forward.common;

/**
 * Created by usb on 15-1-12.
 */
public class CommonVariables {

    //----------------- Mail Server ---------------//
    public static final String MAIL_SERVER_USERNAME="mailServerUsername";
    public static  final String MAIL_SERVER_PASSWORD="mailServerPassword";
    public static final String MAIL_SERVER_STMP_ADDRESS="mailServerStmpAddress";
    public static final String  MAIL_SERVER_FROM_ADDRESS="mailServerFromAddress";
    public static final String MAIL_SERVER_TO_ADDRESS="mailServerToAddress";
    public static final String MAIL_SERVER_PORT="mailServerPort";
    public static final String MAIL_SERVER_TIMEOUT="mailServerTimeout";

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

    //----------------- UI ---------------//
    public static final int UPDATE_MAINVIEW_NOTICE=10000;
    public static final int UPDATE_MAINVIEW_NOTICEINFO=10001;
    public static final int UPDATE_MAINVIEW_NOTICEMSG=10002;


    //----------------- Global ---------------//
    public static final String CONFIG_SETTING="MY_PERE";
    public static final String CONFIG_IS_FIRST="my_pere_is_first";
    public static final String CONFIG_BLACK_SETTING="CONFIG_BLACK_SETTING";
    public static final String CONFIG_BACKGROUND_SETTING="CONFIG_BACKGROUND_SETTING";
    public static final String CONFIG_GLOBAL_SETTING="CONFIG_GLOBAL_SETTING";

    //----------------- CONFIG_OTHER_SETTING ---------------//
    public static final String CONFIG_BACKGROUND_SETTING_SMS_CHECKIN="CONFIG_BACKGROUND_SETTING_SMS_CHECKIN";
    public static final String CONFIG_BACKGROUND_SETTING_SMS_PREVIOUS_SENDER_TIME="CONFIG_BACKGROUND_SETTING_SMS_PREVIOUS_SENDER_TIME";
    public static final String CONFIG_SETTING_USB_SHARE_OPEN="CONFIG_SETTING_USB_SHARE_OPEN";

}
