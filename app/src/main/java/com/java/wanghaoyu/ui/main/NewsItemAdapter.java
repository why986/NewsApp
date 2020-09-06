package com.java.wanghaoyu.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.java.wanghaoyu.R;
import com.java.wanghaoyu.SimpleNews;

import java.util.List;

public class NewsItemAdapter extends ArrayAdapter<SimpleNews> {
    public NewsItemAdapter(Context context, int resource, List<SimpleNews> objects){
        super(context, resource, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        SimpleNews news = getItem(position);
        View oneNewsView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);

        TextView t1 = (TextView) oneNewsView.findViewById(R.id.textView_news_title);
        TextView t2 = (TextView) oneNewsView.findViewById(R.id.textView_news_source);
        TextView t3 = (TextView) oneNewsView.findViewById(R.id.textView_news_date);

        t1.setText(news.title);
        t2.setText(news.source);
        t3.setText(news.date);
        return oneNewsView;
    }
}
