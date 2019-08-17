package dev.luoei.app.tool.sms.forward.dao;

import java.util.List;
import java.util.Set;

import dev.luoei.app.tool.sms.forward.entity.SMS;

/**
 * Created by anseenly on 15/4/19.
 */
public interface SMSDao {
    // 创建表
    public void create();
    //增
    public void add(SMS smsObj);
    public void adds(List<SMS> smsObjs);
    //删除
    public void delete(String id);
    //查询
    public void query(String sql);
    //修改
    public void update(String id,String obj);
    //更新短信状态
    public void updateStatus(String key,int status,String status_name);
    //查询所有消息内容
    public Set queryAllBody();
    //获取所有数据
    public List<SMS> queryAllData();
    // 获取所有Id
    public Set getAllDataId();
}
