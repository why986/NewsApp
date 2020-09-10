package com.java.wanghaoyu.ui.main;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import com.java.wanghaoyu.R;
import com.java.wanghaoyu.SimpleNews;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class NewsRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int TYPE_CONTENT=0;//正常内容
    private final static int TYPE_FOOTER=1;//下拉刷新
    List<SimpleNews> news; // 数据源
    Context context;    // 上下文Context
    ViewGroup parent;
    private HashSet<Integer> Clicked = new HashSet<Integer>();

    public void clearClicked(){
        Clicked.clear();
    }


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

    public NewsRecycleViewAdapter(Context c) {
        news = new ArrayList<SimpleNews>();
        context = c;
    }


    public void changeToNews(List<SimpleNews> n){
        news = n;
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
                    .inflate(R.layout.item_news_footview, parent, false);
            this.parent = parent;
            return new FootViewHolder(v);
        }
        else {
            View v = (View) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_news_list, parent, false);
            this.parent = parent;
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position)==TYPE_FOOTER){

        }
        else {
            if (holder instanceof MyViewHolder) {
                final SimpleNews n = news.get(position);
                View oneNewsView = ((MyViewHolder) holder).oneNewsView;
                final TextView t1 = (TextView) oneNewsView.findViewById(R.id.textView_news_title);
                TextView t2 = (TextView) oneNewsView.findViewById(R.id.textView_news_source);
                TextView t3 = (TextView) oneNewsView.findViewById(R.id.textView_news_date);
                t1.setText(n.title);
                t2.setText(n.source);
                t3.setText(n.time);
                if (Clicked.contains(position))
                    t1.setTextColor(0xFF888888);

                // item click
                if (mOnItemClickListener != null) {
                    ((MyViewHolder) holder).oneNewsView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(position >= 0 && position < news.size()) {
                                mOnItemClickListener.onItemClick(view, position);
                                Clicked.add(position);
                                notifyDataSetChanged();
                            }
                        }
                    });
                }
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



    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }


}