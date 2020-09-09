package com.java.wanghaoyu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SearchableActivity extends AppCompatActivity {

    TextView mTvWord = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        mTvWord = (TextView) findViewById(R.id.tv_word);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mTvWord.append(intent.getStringExtra(SearchManager.QUERY));
        } else if (Intent.ACTION_VIEW.equals((intent.getAction()))){
            mTvWord.append(intent.getDataString());
        } else {
            mTvWord.setText("R.string.word_not_found");
        }
    }
}