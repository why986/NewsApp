package com.java.wanghaoyu.ClusterPackage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.java.wanghaoyu.EntityPackage.Entity;
import com.java.wanghaoyu.EntityPackage.EntityItem;
import com.java.wanghaoyu.EntityPackage.EntityItemMain;
import com.java.wanghaoyu.EntityPackage.EntityItemProperty;
import com.java.wanghaoyu.EntityPackage.EntityItemRelation;
import com.java.wanghaoyu.EntityPackage.EntityRecycleViewAdapter;
import com.java.wanghaoyu.R;
import com.java.wanghaoyu.Relation;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClusterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Cluster> clusterList;
    Context context;

    public ClusterAdapter(Context c, List<Cluster> l){
        clusterList = l;
        context = c;
    }


    class MainViewHolder extends RecyclerView.ViewHolder {
        TextView txnum;
        TextView txword;
        TextView txnews;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            txnews = itemView.findViewById(R.id.textView_cluster_news);
            txnum = itemView.findViewById(R.id.textView_cluster_num);
            txword= itemView.findViewById(R.id.textView_word);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cluster_main, viewGroup, false);
        RecyclerView.ViewHolder holder =  new MainViewHolder(item);
        return holder;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final ClusterAdapter.MainViewHolder holder = (MainViewHolder) viewHolder;
        final Cluster e = clusterList.get(position);
        holder.txnum.setText(Integer.toString(position));
        holder.txword.setText(String.join(", ", e.keywords));
        holder.txnews.setText(String.join("\n\n", e.titles));

    }


    @Override
    public int getItemCount() {
        return clusterList == null ? 0 : clusterList.size();
    }






}
