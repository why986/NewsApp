package com.java.wanghaoyu.ExpertPackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.java.wanghaoyu.Manager;
import com.java.wanghaoyu.R;

import java.util.List;
import java.util.Objects;

public class ExpertActivity extends AppCompatActivity {
    private ListView listView;
    private List<Expert> expertList;
    private ExpertListViewAdapter mAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        listView = (ListView)findViewById(R.id.listViewExpert);
        mAdapter = new ExpertListViewAdapter(this, listView);
        listView.setAdapter(mAdapter);
        context = this;
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
    }
}