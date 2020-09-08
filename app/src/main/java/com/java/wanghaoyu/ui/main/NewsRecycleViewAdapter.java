package com.java.wanghaoyu.ui.main;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import com.java.wanghaoyu.R;
import com.java.wanghaoyu.SimpleNews;

import java.util.ArrayList;
import java.util.List;

public class NewsRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int TYPE_CONTENT=0;//正常内容
    private final static int TYPE_FOOTER=1;//下拉刷新
    List<SimpleNews> news; // 数据源
    Context context;    // 上下文Context
    ViewGroup parent;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View oneNewsView;
        public MyViewHolder(View v) {
            super(v);
            oneNewsView = v;
        }

    }

    public static class FootViewHolder extends RecyclerView.ViewHolder {
        private ContentLoadingProgressBar progressBar;
        public FootViewHolder(View itemView) {
            super(itemView);
            progressBar=itemView.findViewById(R.id.pb_progress);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewsRecycleViewAdapter(List<SimpleNews> n, Context c) {
        news = n;
        context = c;
    }

    public void changeToNews(List<SimpleNews> n){
        news.clear();
        news.addAll(n);
    }

    public void addNews(List<SimpleNews> n){
        news.addAll(n);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        if (viewType==TYPE_FOOTER){
            View v = (View) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.footview, parent, false);
            this.parent = parent;
            return new FootViewHolder(v);
        }
        else {
            View v = (View) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_item, parent, false);
            this.parent = parent;
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==TYPE_FOOTER){

        }
        else {
            if (holder instanceof MyViewHolder) {
                SimpleNews n = news.get(position);
                View oneNewsView = ((MyViewHolder) holder).oneNewsView;
                TextView t1 = (TextView) oneNewsView.findViewById(R.id.textView_news_title);
                TextView t2 = (TextView) oneNewsView.findViewById(R.id.textView_news_source);
                TextView t3 = (TextView) oneNewsView.findViewById(R.id.textView_news_date);
                t1.setText(n.title);
                t2.setText(n.source);
                t3.setText(n.time);
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return news.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==news.size()){
            return TYPE_FOOTER;
        }
        return TYPE_CONTENT;
    }
}