package com.java.wanghaoyu.EntityPackage;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.java.wanghaoyu.Entity;
import com.java.wanghaoyu.R;
import com.java.wanghaoyu.Relation;
import com.java.wanghaoyu.ui.main.NewsRecycleViewAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_PROPERTY = 1;
    private static final int ITEM_VIEW_TYPE_RELATION = 2;

    List<Entity> entityList;
    List<EntityItem> entityItemList;

    public EntityRecycleViewAdapter(){entityList = null;}

    public void setData(List<Entity> list){
        entityList = list;
        entityItemList = new ArrayList<EntityItem>();
        for(Entity e : list){
            entityItemList.add(new EntityItemMain(e));
            try {
                Iterator iterator = e.properties.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    entityItemList.add(new EntityItemProperty(key, e.properties.getString(key)));
                }
            }catch (JSONException exc){ }
            for(Relation re: e.relations){
                entityItemList.add(new EntityItemRelation(re));
            }
        }
    }

    class MainViewHolder extends RecyclerView.ViewHolder {
        TextView textView_label;
        TextView textView_wiki;
        TextView textView_url;
        ImageView imageView;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_label = itemView.findViewById(R.id.textView_label);
            textView_wiki = itemView.findViewById(R.id.textView_wiki);
            textView_url = itemView.findViewById(R.id.textView_url);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    class PropertyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_key;
        TextView textView_val;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_key = itemView.findViewById(R.id.textView_pro_key);
            textView_val = itemView.findViewById(R.id.textView_pro_val);
        }
    }

    class RelationViewHolder extends RecyclerView.ViewHolder {
        TextView textView_re;
        TextView textView_fo;
        TextView textView_la;

        public RelationViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_re = itemView.findViewById(R.id.textView_re_relation);
            textView_fo = itemView.findViewById(R.id.textView_re_forward);
            textView_la = itemView.findViewById(R.id.textView_re_label);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(viewType == ITEM_VIEW_TYPE_HEADER){
            View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_entity_list, viewGroup, false);
            RecyclerView.ViewHolder holder =  new MainViewHolder(item);
            return holder;

        }else if(viewType == ITEM_VIEW_TYPE_PROPERTY){
            View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_entity_property, viewGroup, false);
            RecyclerView.ViewHolder holder =  new PropertyViewHolder(item);
            return holder;

        }else{
            View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_entity_relation, viewGroup, false);
            RecyclerView.ViewHolder holder =  new RelationViewHolder(item);
            return holder;
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof MainViewHolder){
            final MainViewHolder holder = (MainViewHolder) viewHolder;
            final EntityItemMain e = (EntityItemMain)entityItemList.get(position);
            holder.textView_label.setText(e.label);
            holder.textView_wiki.setText(e.wiki);
            holder.textView_url.setText("url:" + e.url);
            // Image
            if(!e.img.equals("null")) {
                new Thread(new Runnable() {
                    Bitmap bitmap;
                    @Override
                    public void run() {
                        try {
                            InputStream inputStream = getImageViewInputStream(e.img);
                            bitmap = BitmapFactory.decodeStream(inputStream);
                        } catch (IOException e) { }
                        holder.imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                holder.imageView.setImageBitmap(bitmap);
                            }
                        });
                    }
                }).start();
            }

        }else if(viewHolder instanceof PropertyViewHolder){
            PropertyViewHolder holder = (PropertyViewHolder) viewHolder;
            EntityItemProperty e = (EntityItemProperty)entityItemList.get(position);
            holder.textView_key.setText(e.key);
            holder.textView_val.setText(e.val);

        }else{
            RelationViewHolder holder = (RelationViewHolder) viewHolder;
            EntityItemRelation e = (EntityItemRelation)entityItemList.get(position);
            holder.textView_re.setText(e.relation);
            holder.textView_la.setText(e.label);
            if(e.forward){
                holder.textView_fo.setText(" → ");
                holder.textView_fo.setBackgroundColor(0xFF3399FF);
            }else{
                holder.textView_fo.setText(" ← ");
                holder.textView_fo.setBackgroundColor(0xFFFF6699);
            }
        }
    }


    @Override
    public int getItemCount() {
        return entityItemList == null ? 0 : entityItemList.size();
    }



    @Override
    public int getItemViewType(int position) {
        if(entityItemList.get(position) instanceof EntityItemMain){
            return ITEM_VIEW_TYPE_HEADER;
        } else if(entityItemList.get(position) instanceof EntityItemProperty){
            return ITEM_VIEW_TYPE_PROPERTY;
        } else if(entityItemList.get(position) instanceof EntityItemRelation) {
            return ITEM_VIEW_TYPE_RELATION;
        }
        return 0;
    }

    public static InputStream getImageViewInputStream(String path) throws IOException {
        InputStream inputStream = null;
        URL url = new URL(path);
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        httpURLConnection.setConnectTimeout(3000);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setDoInput(true);
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            inputStream = httpURLConnection.getInputStream();
        }
        return inputStream;
    }

}
