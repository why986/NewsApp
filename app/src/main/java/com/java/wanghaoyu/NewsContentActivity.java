package com.java.wanghaoyu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class NewsContentActivity extends AppCompatActivity {
    public TextView view_title;
    public TextView view_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_cotent);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        final String id = bundle.getString("ID");
        final Manager manager = Manager.getInstance(this);
//        Log.d("NewsContentActivity", "ok");
        manager.getDetailedNews(new Manager.DetailedNewsCallBack() {
            @Override
            public void onError(String data) {
                if(data.equals("TIMEOUT")){
                    DetailedNews detailedNews = manager.getDetailedNewsFromDatabase(id);
                    view_title = (TextView) findViewById(R.id.textView_Title);
                    view_content = (TextView) findViewById(R.id.textView_Content);
                    view_title.setText(detailedNews.title);
                    view_content.setText(detailedNews.content);
                }
            }

            @Override
            public void onSuccess(DetailedNews detailedNews) {
                manager.insertDetailedNews(detailedNews);
                view_title = (TextView) findViewById(R.id.textView_Title);
                view_content = (TextView) findViewById(R.id.textView_Content);
                view_title.setText(detailedNews.title);
                view_content.setText(detailedNews.content);
            }
        }, id);
        view_content = (TextView) findViewById(R.id.textView_Content);
        view_content.setMovementMethod(ScrollingMovementMethod.getInstance());

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_button:
                Manager manager = Manager.getInstance(this);
                String title, content;
                NewsContentActivity a = (NewsContentActivity) view.getContext();
                title = a.view_title.getText().toString();
                content = a.view_content.getText().toString();
                Toast.makeText(this, "分享新闻："+title, Toast.LENGTH_LONG).show();
                manager.shareNews(this, title, content);
                break;
        }
    }





}