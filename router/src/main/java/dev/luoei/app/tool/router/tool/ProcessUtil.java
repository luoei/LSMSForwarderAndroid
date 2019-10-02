package dev.luoei.app.tool.router.tool;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/*
* 进程工具
* */
public class ProcessUtil {

    /// 包名
    public static String packageName = "dev.luoei.app.tool.sms.forward";

    /// 当前进程
    private static String _processName = null;

    /// 获取当前进程
    public static String getProcessName() {

        if (!TextUtils.isEmpty(_processName)){
            return _processName;
        }
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            _processName = processName;
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /// 判断是否是主进程
    public static boolean isMainProcess(){
        String name = packageName;
        return getProcessName().equals(name);
    }

    /// 获取所有进程
    public static List<String> getAllProcess(Context context){
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
        List<String> processNames = new ArrayList<>();
        for (ActivityManager.RunningAppProcessInfo info : infoList) {
            //进程名称
            String processName = info.processName;
            processNames.add(processName);
        }
        return processNames;
    }

    /// 进程存活检查
    public static boolean isAliveProcess(String processName, Context context){
        List<String> prcessNames = getAllProcess(context);
        for (String prcessName : prcessNames){
            if (prcessName.equals(processName)){
                return true;
            }
        }
        return false;
    }

}
