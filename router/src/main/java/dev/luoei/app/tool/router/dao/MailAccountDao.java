package dev.luoei.app.tool.router.dao;


import dev.luoei.app.tool.router.entity.MailAccount;

public interface MailAccountDao {

    // 创建表
    public void create();

    // 从缓存读取配置信息
    public MailAccount getFromCache();

    // 保存到缓存
    public void save(MailAccount mailAccount);

}
