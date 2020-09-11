package com.java.wanghaoyu.ExpertPackage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.java.wanghaoyu.R;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ExpertListViewAdapter extends BaseAdapter {
    private List<Expert> expertList;
    private Context context;
    private HashSet<Integer> isVisible = new HashSet<Integer>();
    private ListView listView;


    @Override
    public int getCount() {
        return expertList.size();
    }

    public ExpertListViewAdapter(Context context, ListView listView){
        expertList = new ArrayList<Expert>();
        this.context = context;
        this.listView = listView;
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
        ViewHolder mholder = null;
        final String tag = Integer.toString(position);

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expert,null);
            mholder = new ViewHolder();
            //name, title, hascp, school, work, indices, bio, edu, contact
            mholder.imageView = (ImageView) convertView.findViewById(R.id.imageView_avator);
            mholder.imageView.setTag(tag);
            mholder.linearLayoutAddition = (ConstraintLayout)convertView.findViewById(R.id.linearLayout_addition);
            mholder.layout1 = (ConstraintLayout)convertView.findViewById(R.id.linearLayoutEx1);
            mholder.name = (TextView) convertView.findViewById(R.id.textView_name);
            mholder.title = (TextView) convertView.findViewById(R.id.textView_title);
            mholder.hascp = (TextView) convertView.findViewById(R.id.textView_hascp);
            mholder.school = (TextView) convertView.findViewById(R.id.textView_school);
            mholder.work = (TextView) convertView.findViewById(R.id.textView_work);
            mholder.indices = (TextView) convertView.findViewById(R.id.textView_indices);
            mholder.bio = (TextView) convertView.findViewById(R.id.textView_bio);
            mholder.edu = (TextView) convertView.findViewById(R.id.textView_edu);
            mholder.contact = (TextView) convertView.findViewById(R.id.textView_contact);
            convertView.setTag(mholder);
        }else{
            mholder = (ViewHolder)convertView.getTag();
            mholder.imageView.setTag(tag);
        }

        final ViewHolder holder = mholder;
        // 解析字符串
        String name, title, hascp, school, work, indices, bio, edu, contact;
        final Expert e = expertList.get(position);
        if(e.name_zh.equals("")){
            name = e.name;
        }else {
            name = e.name_zh + " " + e.name;
        }
        try {
            try {
                title = e.profile.getString("position");
            } catch (JSONException in){
                title = "";
            }
            try {
                school = e.profile.getString("affiliation");
            } catch (JSONException in){
                school = "";
            }
            try {
                work = e.profile.getString("work");
            } catch (JSONException in){
                work = " ";
            }
            try {
                bio = e.profile.getString("bio").replace("<br>", "\n");
            } catch (JSONException in){
                bio = " ";
            }
            try {
                edu = e.profile.getString("edu");
            } catch (JSONException in){
                edu = " ";
            }
            try {
                contact = "主页：" + e.profile.getString("homepage");
            } catch (JSONException in){
                contact = " ";
            }
            indices = "Papers: " + e.indices.getString("pubs")
                    + "\nCitation: " + e.indices.getString("citations")
                    + "\nH-Index: " + e.indices.getString("hindex")
                    + "\nG-Index: " + e.indices.getString("gindex")
                    + "\nSociability: " + e.indices.getString("sociability")
                    + "\nDiversity: " + e.indices.getString("diversity")
                    + "\nActivity: " + e.indices.getString("activity");

            hascp = "H|" + e.indices.getString("hindex")
                    + "  A|" + e.indices.getString("activity")
                    + "  S|" + e.indices.getString("sociability")
                    + "  C|" + e.indices.getString("citations")
                    + "  P|" + e.indices.getString("pubs");

            // Image
            holder.imageView.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.drawable_seach));
            if(e.avatar.length() < 10){
                holder.imageView.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.drawable_seach));
            }else {
                notifyImage(e.avatar, tag);
            }

            holder.name.setText(name);
            holder.title.setText(title);
            holder.hascp.setText(hascp);
            holder.school.setText(school);
            holder.work.setText(work);
            holder.indices.setText(indices);
            holder.bio.setText(bio);
            holder.edu.setText(edu);
            holder.contact.setText(contact);
        }catch (JSONException ee){
            Log.d("JSON", ee.toString());
        }



        // 点击事件
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
        TextView name, title, hascp, school, work, indices, bio, edu, contact;
        ConstraintLayout linearLayoutAddition;
        ConstraintLayout layout1;
        ImageView imageView;
    }

    public void notifyImage(String url, String tag){
        if(!url.equals("null")) {
            BitmapWorkerTask task = new BitmapWorkerTask();
            task.execute(url, tag);
        }else{

        }
    }


    class BitmapWorkerTask extends AsyncTask<String, Void, Void> {
        String imageUrl, tag;
        Bitmap bitmap;

        @Override
        protected Void doInBackground(String... params) {
            imageUrl = params[0];
            tag = params[1];
            // 在后台开始下载图片
            try {
                InputStream inputStream = getImageViewInputStream(imageUrl);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException ignored) { }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            final ImageView imageView = (ImageView) listView.findViewWithTag(tag);
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
                System.out.println("Set Image Success "+tag);
            } else{
                System.out.println("Set Image Fail "+tag);
            }
        }


        InputStream getImageViewInputStream(String path) throws IOException {
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

}