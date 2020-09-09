package com.java.wanghaoyu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.java.wanghaoyu.ui.main.NewsRecycleViewAdapter;
import com.java.wanghaoyu.ui.main.onLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public class SearchableActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<SimpleNews> news_list;
    NewsRecycleViewAdapter recycleViewAdapter;
    RecyclerView.LayoutManager layoutManager;
    Context mcontext;
    int page_count=1;
    final int SIZE=15;
    String type = "all";
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        mcontext = this;
        recyclerView = (RecyclerView) findViewById(R.id.searchRecycleView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recycleViewAdapter = new NewsRecycleViewAdapter(this);
        recyclerView.setAdapter(recycleViewAdapter);
        Intent intent = getIntent();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "搜索："+query, Toast.LENGTH_LONG).show();
            // Search with query, return in newsList;
            //Test
            final Manager manager = Manager.getInstance(this);

            manager.searchSimpleNews(new Manager.SimpleNewsCallBack() {
                @Override
                public void onError(String data) {
                    Log.d("manager.getSimpleNewsList", data);
                }

                @Override
                public void onSuccess(List<SimpleNews> data) {
                    news_list = data;
                    recycleViewAdapter.changeToNews(news_list);
                    recycleViewAdapter.notifyDataSetChanged();
                }
            }, type, query);

            // End Search
            databaseHelper = DatabaseHelper.getHelper(this);
            Word word0 = new Word(query);
            databaseHelper.insertWord(word0);
        }



        recycleViewAdapter.notifyDataSetChanged();
        recycleViewAdapter.setOnItemClickListener(new NewsRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                recycleViewAdapter.notifyDataSetChanged();
                if(position >= 0 && position < news_list.size()) {
                    Intent intent = new Intent(mcontext, NewsContentActivity.class);
                    intent.putExtra("ID", news_list.get(position).id);
                    startActivity(intent);
                }
            }
        });

    }
}