package dev.luoei.app.tool.sms.forward.tools;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dev.luoei.app.tool.sms.forward.activity.BlackListActivity;
import dev.luoei.app.tool.sms.forward.common.CommonParas;
import dev.luoei.app.tool.sms.forward.common.InitGlobalVar;

/**
 * Created by anseenly on 15/1/23.
 */
public class BasicTools {

    /**
     * 得到当前时间
     * @param  style 时间格式
     * @return 格式化之后的时间
     */
    public String getCurrentTime(String style){
        if(style==null)style="yyyy-MM-dd HH:mm:ss";
        return  new SimpleDateFormat(style).format(new Date());
    }

    /**
     * 数组转JSON对象
     * @param list 数组
     * @return JSON 对象
     */
    public String ObjToJSON(List<Map<String,String>> list){
        StringBuilder jsons=new StringBuilder();
        jsons.append("[");
        for(int i=0,len=list.size();i<len;i++){
            jsons.append("{");
            Map<String,String> map= list.get(i);
            for(String key : map.keySet()){
                jsons.append("\"".concat(key).concat("\":\"").concat(map.get(key)).concat("\","));
            }
            if(map.size()>0)jsons.deleteCharAt(jsons.length()-1);
            jsons.append("}");
        }
        jsons.append("]");
        return jsons.toString();
    }

    // 初始化黑名单
    public void initBlackFilter(){
        Set store = InitGlobalVar.getVar(CommonParas.getMainContext(), BlackListActivity.StoreKey);
        if (store == null){
            store = new HashSet();
        }
        Set<String> result = new HashSet<String>();
        result.addAll(blackFilterList());
        result.removeAll(store);
        store.addAll(result);
        Log.d("黑名单","新增关键字"+result);
        InitGlobalVar.setVar(CommonParas.getMainContext(), BlackListActivity.StoreKey,store);
    }

    private Set<String> blackFilterList(){
        Set<String> set = new HashSet<>();
        set.add("多点");
        set.add("什么值得买");
        set.add("当当");
        set.add("物美");
        set.add("阿里云");
        set.add("滴滴");
        set.add("七牛");
        set.add("脉脉");
        set.add("腾讯");
        set.add("百度");
        set.add("单车");
        set.add("电信");
        set.add("优酷");
        set.add("携程");
        set.add("网易");
        set.add("大众");
        set.add("美团");
        set.add("转转");
        set.add("开源中国");
        set.add("学而思");
        set.add("京东");
        set.add("中国移动");
        set.add("浦发");
        set.add("购物");
        set.add("大促");
        set.add("618");
        set.add("双11");
        set.add("拉卡拉");
        set.add("理财");
        set.add("咸鱼");
        set.add("兴业");
        set.add("魅族");
        set.add("优惠");
        return set;
    }

}
