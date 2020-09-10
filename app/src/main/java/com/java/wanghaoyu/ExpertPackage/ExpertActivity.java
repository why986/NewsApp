package com.java.wanghaoyu.ExpertPackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.java.wanghaoyu.Entity;
import com.java.wanghaoyu.Expert;
import com.java.wanghaoyu.Manager;
import com.java.wanghaoyu.R;

import java.util.ArrayList;
import java.util.List;

public class ExpertActivity extends AppCompatActivity {
    private ListView listView;
    private List<Expert> expertList;
    private ExpertListViewAdapter mAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert);
        listView = (ListView)findViewById(R.id.listViewExpert);
        mAdapter = new ExpertListViewAdapter(this);
        listView.setAdapter(mAdapter);
        context = this;
        // Get Expert List
        final Manager manager = Manager.getInstance(this);

        manager.getExpertList(new Manager.ExpertCallBack() {
            @Override
            public void onError(String data) {
                Toast.makeText(context, "ERROR:"+data, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(List<Expert> data) {
                expertList = data;
                mAdapter.setData(expertList);
                mAdapter.notifyDataSetChanged();
                Toast.makeText(context, "知疫学者共"+expertList.size()+"人", Toast.LENGTH_LONG).show();
            }
        });
        // End
//        expertList = new ArrayList<Expert>();
//        for (int i=9;i>0;i--) expertList.add(new Expert());
//        mAdapter.setData(expertList);
//        mAdapter.notifyDataSetChanged();

    }
}