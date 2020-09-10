package com.java.wanghaoyu.ExpertPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.java.wanghaoyu.Expert;
import com.java.wanghaoyu.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ExpertListViewAdapter extends BaseAdapter {
    private List<Expert> expertList;
    private Context context;
    private HashSet<Integer> isVisible = new HashSet<Integer>();
    String getName(int i){
        Expert e = expertList.get(i);
        String name = e.name_zh + ", " + e.name;
        return name;
    }

    String getProfile(int i){
        Expert e = expertList.get(i);
        String name = e.name_zh + ", " + e.name;
        return name;
    }

    String getIndices(int i){
        Expert e = expertList.get(i);
        String name = e.name_zh + ", " + e.name;
        return name;
    }

    @Override
    public int getCount() {
        return expertList.size();
    }

    public ExpertListViewAdapter(Context context){
        expertList = new ArrayList<Expert>();
        this.context = context;
    }

    public void setData(List<Expert> e){
        this.expertList = e;
        this.isVisible.clear();
    }

    @Override
    public Object getItem(int position) {
        return expertList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expert,null);
            holder = new ViewHolder();
            holder.textView_name = (TextView)convertView.findViewById(R.id.textView_name);
            holder.textView_pro = (TextView)convertView.findViewById(R.id.textView_profile);
            holder.textView_ind = (TextView) convertView.findViewById(R.id.textView_indices);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView_avator);
            holder.linearLayoutAddition = (ConstraintLayout)convertView.findViewById(R.id.linearLayout_addition);
            holder.layout1 = (ConstraintLayout)convertView.findViewById(R.id.linearLayoutEx1);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.textView_name.setText(getName(position));
        holder.textView_pro.setText(getProfile(position));
        holder.textView_ind.setText(getIndices(position));

        /**
         *  判断currentItem与position是否相等，首次加载进来的时候，因为currentItem为-1，则不会加载。
         *  点击后刷新listview再次比对，相等后加载隐藏信息。
         */

        if(isVisible.contains(position)){
            holder.linearLayoutAddition.setVisibility(View.VISIBLE);
        }else {
            holder.linearLayoutAddition.setVisibility(View.GONE);
        }
        holder.layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position >= 0) {
                    if (isVisible.contains(position)) {
                        isVisible.remove(position);
                    } else {
                        isVisible.add(position);
                    }
                    notifyDataSetChanged();
                }
            }
        });
        return convertView;
    }

    public class ViewHolder{
        TextView textView_name;
        TextView textView_pro;
        TextView textView_ind;
        ConstraintLayout linearLayoutAddition;
        ConstraintLayout layout1;
        ImageView imageView;
    }
}