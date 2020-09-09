package com.java.wanghaoyu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class NewsContentActivity extends AppCompatActivity {
    TextView view_title;
    TextView view_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_cotent);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        final String id = bundle.getString("ID");
        final Manager manager = Manager.getInstance(this);
        Log.d("NewsContentActivity", "ok");
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
                view_title = (TextView) findViewById(R.id.textView_Title);
                view_content = (TextView) findViewById(R.id.textView_Content);
                view_title.setText(detailedNews.title);
                view_content.setText(detailedNews.content);
            }
        }, id);


    }


}