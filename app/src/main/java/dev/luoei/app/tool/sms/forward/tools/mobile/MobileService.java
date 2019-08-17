package dev.luoei.app.tool.sms.forward.tools.mobile;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by usb on 15-1-9.
 */
public class MobileService {
    public void setMobileNetwork(Context context, boolean flag){
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((flag&&networkInfo==null)||(!flag&&networkInfo!=null)){ //开启
            Class<?> cmClass=connectivityManager.getClass();
            Class<?>[] argClasses=new Class[1];
            argClasses[0]=boolean.class;
            try{
                Method method=cmClass.getMethod("setMobileDataEnabled",argClasses);
                method.invoke(connectivityManager,flag);
                Log.d("Mobile Network Manager -->", "Mobile Network " + (flag ? "open" : "close") + " done!");
            }catch (Exception e){
                e.printStackTrace();
                Log.e("Mobile Network Manager -->","管理异常，"+e.getMessage());
            }
        }
    }
}
