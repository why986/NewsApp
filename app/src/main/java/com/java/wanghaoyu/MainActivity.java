package com.java.wanghaoyu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.java.wanghaoyu.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


import com.java.wanghaoyu.ui.main.NewsItemAdapter;
import com.java.wanghaoyu.ui.main.SectionsPagerAdapter;

import org.json.JSONException;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        manager = Manager.getInstance(this);
        try {
//            List<SimpleNews> news_list = manager.getSimpleNewsList("news", 1, 20);
            NewsItemAdapter newsItemAdapter = new NewsItemAdapter(this, R.layout.news_item, news_list);
            ListView listView = (ListView) findViewById(R.id.news_list_view);
            listView.setAdapter(newsItemAdapter);
        }catch (JSONException e) {}


    }

/*
    private static class Task extends AsyncTask<Void, Void, Task.Result> {
        String url;
        String type;
        int page;
        int size;

        Task(String url, String type, int page, int size)
        {
            this.url = url;
            this.type = type;
            this.page = page;
            this.size = size;
        }
        @Override
        protected Result doInBackground(Void... voids) {
            try {
                URL url = new URL(this.url + "?type=" + this.type + "&page=" + this.page + "size=" + this.size);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setReadTimeout(3000);
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder stringBuilder = new StringBuilder();
                String str;
                while((str = in.readLine()) != null){
                    stringBuilder.append(str);
                    stringBuilder.append("\n");
                }
                in.close();
                httpURLConnection.disconnect();

                return new Result(STATE.OK, stringBuilder.toString());
            }catch (SocketTimeoutException e) {
                return new Result(STATE.TIMEOUT, null);
            }catch (Exception e){
                return new Result(STATE.OTHERS, null);
            }
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            switch (result.state){
                case OK:
                    try {
                        manager.dataBase.insertNewsList(type, page, result.data);
                    }catch (JSONException e){

                    }
                    break;
                case TIMEOUT:
                    break;
                case OTHERS:
                    break;
            }
        }

        enum STATE{OK, TIMEOUT, OTHERS}

        class Result {
            STATE state;
            String data;

            Result(STATE state, String data)
            {
                this.state = state;
                this.data = data;
            }
        }
    }

 */
}