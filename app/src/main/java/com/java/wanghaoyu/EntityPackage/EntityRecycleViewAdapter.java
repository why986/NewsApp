package com.java.wanghaoyu.EntityPackage;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.java.wanghaoyu.Entity;
import com.java.wanghaoyu.R;
import com.java.wanghaoyu.ui.main.NewsRecycleViewAdapter;

import java.util.List;

public class EntityRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    List<Entity> entityList;

    public EntityRecycleViewAdapter(List<Entity> list){entityList = list;}

    public EntityRecycleViewAdapter(){entityList = null;}

    public void setData(List<Entity> list){
        entityList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_entity_list, viewGroup, false);
        RecyclerView.ViewHolder holder = new MyViewHolder(item);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MyViewHolder holder = (MyViewHolder) viewHolder;
        Entity entity = entityList.get(i);
        holder.textView_label.setText(entity.label);
        holder.textView_url.setText("url:" + entity.url);
    }


    @Override
    public int getItemCount() {
        return entityList == null ? 0 : entityList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_label;
        TextView textView_url;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_label = itemView.findViewById(R.id.textView_label);
            textView_url = itemView.findViewById(R.id.textView_url);
        }
    }

    private EntityRecycleViewAdapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(EntityRecycleViewAdapter.OnItemClickListener a){
        this.mOnItemClickListener = a;
    }


}
