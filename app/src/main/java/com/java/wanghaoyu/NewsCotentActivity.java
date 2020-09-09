package com.java.wanghaoyu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class NewsCotentActivity extends AppCompatActivity {
    TextView view_title;
    TextView view_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_cotent);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        view_title = (TextView) findViewById(R.id.textView_Title);
        view_content = (TextView) findViewById(R.id.textView_Content);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String id = bundle.getString("ID");
        DetailedNews detailedNews;
        //GET Detailed News by id

        //END Detailed News
//        view_title.setText(detailedNews.title);
//        view_content.setText(detailedNews.content);
        view_title.setText("TITLE");
        view_content.setText("CONTENT "+id);

    }


}