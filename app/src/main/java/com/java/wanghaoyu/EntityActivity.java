package com.java.wanghaoyu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.java.wanghaoyu.EntityPackage.EntityRecycleViewAdapter;
import com.java.wanghaoyu.ui.main.NewsRecycleViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntityActivity extends AppCompatActivity {
    SearchView searchView;
    RecyclerView recyclerView;
    List<Entity> entities;
    Context context;
    EntityRecycleViewAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        searchView = (SearchView) findViewById(R.id.entitySearch);
        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.entityRecycle);
        adapter = new EntityRecycleViewAdapter();
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);


        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);


        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);

        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(context, "搜索Entity: "+query, Toast.LENGTH_LONG).show();
                // 更新entities by query
                entities = new ArrayList<Entity>();
                for(int i=0;i<30;i++) entities.add(newEntity());
                adapter.setData(entities);
                adapter.notifyDataSetChanged();
                // End
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {//搜索框内容变化时
                return true;
            }
        });

    }

    Entity newEntity(){
        return new Entity();
    }


}