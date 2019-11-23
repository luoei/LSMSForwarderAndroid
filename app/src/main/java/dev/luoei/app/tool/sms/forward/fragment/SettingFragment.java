package dev.luoei.app.tool.sms.forward.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import dev.luoei.app.tool.sms.forward.R;
import dev.luoei.app.tool.sms.forward.activity.BlackListActivity;
import dev.luoei.app.tool.sms.forward.activity.InboxActivity;
import dev.luoei.app.tool.sms.forward.activity.SettingAccountActivity;
import dev.luoei.app.tool.sms.forward.activity.SettingBackgroundTaskActivity;
import dev.luoei.app.tool.sms.forward.common.CommonParas;
import dev.luoei.app.tool.sms.forward.tools.DownloadUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by admin on 13-11-23.
 */
public class SettingFragment extends Fragment {

    private final String URI_UPDATE_API = "https://api.github.com/repos/luoei/LSMSForwarderAndroid/releases/latest";

    private Handler mHandlerT = new Handler();
    private List<String> dataSource;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settingview, null);
        view.setBackgroundColor(Color.LTGRAY);

        initData();

        initViewLayout();

        return view;
    }

    private void initData(){
        dataSource = new ArrayList<>();
        dataSource.add("账号设置");
        dataSource.add("其它设置");
        dataSource.add("收件箱");
        dataSource.add("黑名单");
        dataSource.add("当前版本");
        dataSource.add("版本更新");
    }

    private void initViewLayout(){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.setting_home_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(CommonParas.getMainContext()));
        recyclerView.setAdapter(new ListAdapter());
    }

    @Override
    public void onDestroyView() {
        Log.d("视图销毁-->",this.getClass().getName());
        super.onDestroyView();
    }


    class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder>{

        class MyViewHolder extends RecyclerView.ViewHolder
        {
            TextView title;

            public MyViewHolder(View view)
            {
                super(view);
                title = (TextView)  view.findViewById(R.id.setting_home_list_item_title);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position == 0){
                            Intent accountView = new Intent(CommonParas.getMainContext(), SettingAccountActivity.class);
                            startActivity(accountView);
                        }else if (position == 1){
                            Intent accountView = new Intent(CommonParas.getMainContext(), SettingBackgroundTaskActivity.class);
                            startActivity(accountView);
                        }else if (position == 2){
                            Intent accountView = new Intent(CommonParas.getMainContext(), InboxActivity.class);
                            startActivity(accountView);
                        }else if (position == 3){
                            Intent accountView = new Intent(CommonParas.getMainContext(), BlackListActivity.class);
                            startActivity(accountView);
                        }else if (position == 4){
                            PackageInfo packageInfo = null;
                            try {
                                packageInfo = CommonParas.getMainContext().getPackageManager().getPackageInfo(CommonParas.getMainContext().getPackageName(),0);
                                Toast.makeText(CommonParas.getMainContext(),"当前版本："+packageInfo.versionName +" ",Toast.LENGTH_LONG).show();
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                        }else if (position == 5){
                            Toast.makeText(CommonParas.getMainContext(),"版本检查中……",Toast.LENGTH_LONG).show();
                            versionUpdate();
                        }
                    }
                });
            }
        }


        private void versionUpdate(){

            new Thread(new Runnable() {
                @Override
                public void run() {


                    try {
                        String version = null;
                        String downloadUrl = null;
                        String message = null;
                        Response response = null;
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH){
                            List<CipherSuite> cipherSuites = ConnectionSpec.MODERN_TLS.cipherSuites();
                            if (!cipherSuites.contains(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)) {
                                cipherSuites = new ArrayList(cipherSuites);
                                cipherSuites.add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA);
                            }
                            final ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                                    .cipherSuites(cipherSuites.toArray(new CipherSuite[0]))
                                    .build();

                            OkHttpClient client = new OkHttpClient.Builder()
                                    .connectionSpecs(Collections.singletonList(spec))
                                    .build();
                            Request request = new Request.Builder()
                                    .url(URI_UPDATE_API)
                                    .build();
                            try {
                                response = client.newCall(request).execute();
                            }catch (Exception e){
                                e.printStackTrace();
                                mHandlerT.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(CommonParas.getMainContext(),"请升级系统到Android5以上",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                        }else {
                            OkHttpClient client = new OkHttpClient();

                            Request request = new Request.Builder()
                                    .url(URI_UPDATE_API)
                                    .build();
                            response = client.newCall(request).execute();
                        }
                        if(null == response)return;
                        String result = response.body().string();
                        if (null != result){
                            Map data = JSON.parseObject(result,Map.class);
                            if (null != data){
                                version = String.valueOf(data.get("tag_name"));
                                message = String.valueOf(data.get("name"));
                                String body = String.valueOf(data.get("body"));
                                message = message +" \n"+body;
                                Object assets = data.get("assets");
                                if (null != assets){
                                    List assetsList = (List)assets;
                                    if (assetsList.size() > 0 ){
                                        Map asset = (Map)assetsList.get(0);
                                        downloadUrl = String.valueOf(asset.get("browser_download_url"));
                                    }
                                }

                            }
                        }
                        if (null != version && null != downloadUrl){
                            String finalDownloadUrl = downloadUrl;
                            String finalVersion = version;
                            String finalMessage = message;
                            mHandlerT.post(new Runnable() {
                                @Override
                                public void run() {
                                    showVersion(finalVersion, finalMessage, finalDownloadUrl);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mHandlerT.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CommonParas.getMainContext(),"请求失败，"+e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }).start();
        }


        private void showVersion(String version,String message,String downloadUrl){

            String versionName = null;
            try {
                PackageInfo packageInfo = CommonParas.getMainContext().getPackageManager().getPackageInfo(CommonParas.getMainContext().getPackageName(),0);
                versionName = packageInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            boolean update = false;
            String[] serverVersions = version.split("\\.");
            String[] localVersions = versionName.split("\\.");
            if (serverVersions.length == localVersions.length){
                for (int idx = 0;idx<serverVersions.length;idx++){
                    int sv = Integer.parseInt(serverVersions[idx]);
                    int lv = Integer.parseInt(localVersions[idx]);
                    if (sv > lv){
                        update = true;
                        break;
                    }
                }
            }else if(serverVersions.length > localVersions.length){
                for (int idx = 0;idx<serverVersions.length;idx++){
                    int sv = Integer.parseInt(serverVersions[idx]);
                    int lv = 0;
                    if (idx < localVersions.length){
                        lv = Integer.parseInt(localVersions[idx]);
                    }
                    if (sv > lv){
                        update = true;
                        break;
                    }
                }
            }else {
                for (int idx = 0;idx<localVersions.length;idx++){
                    int lv = Integer.parseInt(localVersions[idx]);
                    int sv = 0;
                    if (idx < serverVersions.length){
                        sv = Integer.parseInt(serverVersions[idx]);
                    }
                    if (sv > lv){
                        update = true;
                        break;
                    }
                }
            }
            if (!update){
                Toast.makeText(CommonParas.getMainContext(),"无版本更新",Toast.LENGTH_LONG).show();
                return;
            }
            DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case Dialog.BUTTON_POSITIVE:
                            Toast.makeText(CommonParas.getMainContext(), "正在后台下载，下载完成后，自动打开安装界面...",
                                    Toast.LENGTH_SHORT).show();
                            downFile(downloadUrl);
                            break;
                        case Dialog.BUTTON_NEGATIVE:

                            break;
                        case Dialog.BUTTON_NEUTRAL:

                            break;
                    }
                }
            } ;
            AlertDialog.Builder builder = new AlertDialog.Builder(CommonParas.getMainContext(),3);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle("发现新版本");
            builder.setMessage(""+message);
            builder.setPositiveButton("升级", dialogOnclicListener);
            builder.setNegativeButton("取消", dialogOnclicListener);
            builder.create().show();
        }

        private void downFile(String url) {

            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            DownloadUtil.get().download(url, path, "smsforwarder.apk", new DownloadUtil.OnDownloadListener() {
                @Override
                public void onDownloadSuccess(File file) {
                    //下载完成进行相关逻辑操作
                    mHandlerT.post(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setDataAndType(
                                    Uri.fromFile(file),
                                    "application/vnd.android.package-archive");
                            Log.e("包路径---", file.getAbsolutePath());

                            startActivity(intent);//下载完后 启动应用。
                        }
                    });
                }

                @Override
                public void onDownloading(int progress) {
//                        progressDialog.setProgress(progress);
                    Log.e("包下载中", progress+"");
                }

                @Override
                public void onDownloadFailed(Exception e) {
                    //下载异常进行相关提示操作
                    mHandlerT.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CommonParas.getMainContext(),"下载失败，"+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }

        @NonNull
        @Override
        public ListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(CommonParas.getMainContext()).inflate(R.layout.activity_setting_home_list_item, viewGroup,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ListAdapter.MyViewHolder myViewHolder, int position) {
            myViewHolder.title.setText(dataSource.get(position));
        }

        @Override
        public int getItemCount() {
            return dataSource.size();
        }
    }


     class SettingFragmentListItemActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_setting_home_list_item);
        }
    }
}

