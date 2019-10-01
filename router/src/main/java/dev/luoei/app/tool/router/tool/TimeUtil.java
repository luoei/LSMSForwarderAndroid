package dev.luoei.app.tool.router.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    /**
     * 得到当前时间
     * @param  style 时间格式
     * @return 格式化之后的时间
     */
    public static String getCurrentTime(String style){
        if(style==null)style="yyyy-MM-dd HH:mm:ss";
        return  new SimpleDateFormat(style).format(new Date());
    }

}
