package com.java.wanghaoyu.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.java.wanghaoyu.R;
import com.java.wanghaoyu.SimpleNews;

import java.util.ArrayList;
import java.util.List;

public class NewsItemAdapter extends BaseAdapter {
    private List<SimpleNews> mnews_list;
    private Context mContext;


    public NewsItemAdapter(Context context, List<SimpleNews> objects){
        mnews_list = objects;
        mContext = context;
    }

    @Override
    public int getCount() {
//        System.out.println("NEWSITEMADAPTER COUNT:        " + mnews_list.size());
        return mnews_list.size();
    }

    @Override
    public Object getItem(int position) {
        return mnews_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        @SuppressLint("ViewHolder") View oneNewsView = LayoutInflater.from(mContext).inflate(R.id.news_item, parent, false);
        TextView t1 = (TextView) oneNewsView.findViewById(R.id.textView_news_title);
        TextView t2 = (TextView) oneNewsView.findViewById(R.id.textView_news_source);
        TextView t3 = (TextView) oneNewsView.findViewById(R.id.textView_news_date);

        SimpleNews news = mnews_list.get(position);
        t1.setText(news.title);
        t2.setText(news.source);
        t3.setText(news.time);
        if(news.hasRead){
            t1.setTextColor(0xFF888888);
        }
        return oneNewsView;
    }
}
