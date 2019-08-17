package dev.luoei.app.tool.sms.forward.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dev.luoei.app.tool.sms.forward.R;
import dev.luoei.app.tool.sms.forward.activity.BlackListActivity;
import dev.luoei.app.tool.sms.forward.activity.InboxActivity;
import dev.luoei.app.tool.sms.forward.activity.SettingAccountActivity;
import dev.luoei.app.tool.sms.forward.activity.SettingBackgroundTaskActivity;
import dev.luoei.app.tool.sms.forward.common.CommonParas;


/**
 * Created by admin on 13-11-23.
 */
public class SettingFragment extends Fragment {

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
    }

    private void initViewLayout(){
        RecyclerView recyclerView = view.findViewById(R.id.setting_home_list);
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
                title =  view.findViewById(R.id.setting_home_list_item_title);

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
                        }
                    }
                });
            }
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

