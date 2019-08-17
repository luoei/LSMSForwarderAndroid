package dev.luoei.app.tool.sms.forward.tools;

import android.app.AlertDialog;

import dev.luoei.app.tool.sms.forward.common.CommonParas;

public class AlertViewUtil {

    public static void alert(String message){
        alert("提示",message);
    }

    public static void alert(String title,String message){
        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(CommonParas.getMainContext());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setNegativeButton("确定",null);
        alertDialogBuilder.setMessage(message);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
