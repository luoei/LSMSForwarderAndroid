package dev.luoei.app.tool.sms.forward.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import dev.luoei.app.tool.sms.forward.R;
import dev.luoei.app.tool.sms.forward.common.CommonParas;
import dev.luoei.app.tool.sms.forward.common.CommonVariables;

import static dev.luoei.app.tool.sms.forward.common.CommonVariables.CONFIG_BACKGROUND_SETTING_SMS_CHECKIN;
import static dev.luoei.app.tool.sms.forward.common.CommonVariables.CONFIG_SETTING_USB_SHARE_OPEN;

public class SettingOtherActivity extends AppCompatActivity {

    private  Switch smsSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_other);

        initAction();

        initData();
    }

    private void initAction(){

        final SharedPreferences sharedPreferences= CommonParas.getMainContext().getSharedPreferences(CommonVariables.CONFIG_BACKGROUND_SETTING, Activity.MODE_PRIVATE);
        boolean smsSelected = sharedPreferences.getBoolean(CONFIG_BACKGROUND_SETTING_SMS_CHECKIN,false);
        boolean usbSelected = sharedPreferences.getBoolean(CONFIG_SETTING_USB_SHARE_OPEN,false);

        final SettingOtherActivity self = this;

        Button backButton = (Button) findViewById(R.id.title_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.finish();
            }
        });

        smsSwitch = (Switch) findViewById(R.id.setting_sms_checkin_switch);
        smsSwitch.setChecked(smsSelected);
        smsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putBoolean(CONFIG_BACKGROUND_SETTING_SMS_CHECKIN,isChecked);
                editor.commit();
                Toast.makeText(self,"重启后生效",Toast.LENGTH_SHORT).show();
            }
        });

        Switch usbSwitch = (Switch) findViewById(R.id.setting_usb_share_switch);
        usbSwitch.setChecked(true);
        usbSwitch.setEnabled(false);
        usbSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putBoolean(CONFIG_SETTING_USB_SHARE_OPEN,isChecked);
                editor.commit();
                Toast.makeText(self,"该功能默认开启，暂无法修改",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initData(){

    }

}
