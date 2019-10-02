package dev.luoei.app.tool.demons.entity;

import dev.luoei.app.tool.demons.ProcessUtil;

/// 进程
public enum Process {
    MAIN,
    DEMON,
    DEMON_SUBDEMON,
    SMSOBSERVER,
    ROUTER;

    @Override
    public String toString() {
        switch (this){
            case MAIN:
                return ProcessUtil.packageName;
            case DEMON:
                return ProcessUtil.packageName+":demons";
            case ROUTER:
                return ProcessUtil.packageName+":router";
            case SMSOBSERVER:
                return ProcessUtil.packageName+":sms";
            case DEMON_SUBDEMON:
                return ProcessUtil.packageName+":demons1";
        }
        return super.toString();
    }
}
