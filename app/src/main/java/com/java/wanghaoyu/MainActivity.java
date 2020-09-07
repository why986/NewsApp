package com.java.wanghaoyu;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;


import com.google.android.material.tabs.TabLayout;
import com.java.wanghaoyu.ui.main.NewsItemAdapter;
import com.java.wanghaoyu.ui.main.NewsListFragment;
import com.java.wanghaoyu.ui.main.ViewPagerAdapter;

import org.json.JSONException;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        String type = "news";
        adapter.addFragment(new NewsListFragment(type), type);





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