package dev.luoei.app.tool.sms.forward.controller;

import android.app.Fragment;

import dev.luoei.app.tool.sms.forward.R;
import dev.luoei.app.tool.sms.forward.common.CommonParas;
import dev.luoei.app.tool.sms.forward.fragment.HomeFragment;
import dev.luoei.app.tool.sms.forward.fragment.SettingFragment;

/**
 * Created by admin on 13-11-23.
 */
public class TabbarController {
    public static Fragment getInstanceByIndex(int index) {
        if(CommonParas.saveView.get(index)!=null)return (Fragment) CommonParas.saveView.get(index);
        Fragment fragment = null;
        switch (index) {
            case R.id.btn01:
                fragment = new HomeFragment();
                CommonParas.saveView.put(index,fragment);
                break;
//            case R.id.btn02:
//                fragment = new InboxActivity();
//                break;
//            case R.id.btn03:
//                fragment = new BlackListFragment();
//                break;
//            case R.id.btn04:
//                fragment = new ScheduleServiceView();
//                break;
            case R.id.btn05:
                fragment = new SettingFragment();
                break;
        }
        return fragment;
    }
}
