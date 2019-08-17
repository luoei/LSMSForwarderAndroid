package dev.luoei.app.tool.sms.forward.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import dev.luoei.app.tool.sms.forward.R;
import dev.luoei.app.tool.sms.forward.common.CommonVariables;
import dev.luoei.app.tool.sms.forward.tools.BasicTools;


public class HomeFragment extends Fragment {

    public Button _startStop_button = null;
    public EditText _root_text = null;
    public TextView noticeid = null;
    public TextView noticemsg=null;
    public View view;
    public static Handler handler;
    private static final String NOTICE_INFO="系统初始化";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mainview, null);
        init();

        return view;
    }

    @Override
    public void onDestroyView() {
        Log.d("Main View --> ","主视图被销毁...");
        super.onDestroyView();
    }

    private void updateNoticeUI(String info,String msg){
        if(info!=null)noticeid.setText(info);
        if(msg!=null){
            CharSequence text=noticemsg.getText();
            if(text!=null&&text!=""&&text.length()>0){
               if(text.length()>500){
                   text=((String)text).substring(0,500);
               }
                msg=msg+"\r\n"+ text;
            }
            noticemsg.setText(new BasicTools().getCurrentTime(null)+" : "+ msg);
        }
    }


    public void init() {
        noticeid=(TextView)view.findViewById(R.id.noticeid);
        noticemsg=(TextView)view.findViewById(R.id.noticemsg);

        handler= new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case CommonVariables.UPDATE_MAINVIEW_NOTICE:
                        if (msg.obj != null) {
                            String[] info = (String[]) msg.obj;
                            updateNoticeUI(info[0], info[1]);
                        }
                        break;
                    case CommonVariables.UPDATE_MAINVIEW_NOTICEINFO:
                        if (msg.obj != null) {
                            String[] info = (String[]) msg.obj;
                            updateNoticeUI(info[0], null);
                        }
                        break;
                    case CommonVariables.UPDATE_MAINVIEW_NOTICEMSG:
                        if (msg.obj != null) {
                            String[] info = (String[]) msg.obj;
                            updateNoticeUI(null, info[1]);
                        }
                        break;
                    default:
                        break;
                }
            }

        };

    }



}
