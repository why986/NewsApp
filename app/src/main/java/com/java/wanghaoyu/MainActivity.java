package com.java.wanghaoyu;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    private Toolbar mtbar;
    private ViewPagerAdapter adapter;
    private ViewPager viewpager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        initMain();
    }

    private void initMain(){
        String type = "all";
        adapter.addFragment(new NewsListFragment(type), type);
        type = "news";
        adapter.addFragment(new NewsListFragment(type), type);
        type = "paper";
        adapter.addFragment(new NewsListFragment(type), type);

        viewpager.setAdapter(adapter);
        mtbar = (Toolbar) findViewById(R.id.toolbar);
        initToolBar();

    }


    private void initToolBar(){
        setSupportActionBar(mtbar);
//        //设置NavigationIcon的点击事件,需要放在setSupportActionBar之后设置才会生效,
//        //因为setSupportActionBar里面也会setNavigationOnClickListener
//        mtbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View pView) {
//                // TODO: 2017/5/5 添加抽屉
//
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String type;
        switch (item.getItemId()) {
            case R.id.menu_item_minus:
                Toast.makeText(MainActivity.this, "种类--", Toast.LENGTH_LONG).show();
                adapter.delFragment();

                break;
            case R.id.menu_item_add_paper:
                Toast.makeText(MainActivity.this, "种类++", Toast.LENGTH_LONG).show();
                type = "paper";
                adapter.addFragment(new NewsListFragment(type), type);
                break;
            case R.id.menu_item_add_all:
                Toast.makeText(MainActivity.this, "种类++", Toast.LENGTH_LONG).show();
                type = "all";
                adapter.addFragment(new NewsListFragment(type), type);
                break;
            case R.id.menu_item_add_points:
                Toast.makeText(MainActivity.this, "种类++", Toast.LENGTH_LONG).show();
                type = "points";
                adapter.addFragment(new NewsListFragment(type), type);
                break;
            case R.id.menu_item_add_event:
                Toast.makeText(MainActivity.this, "种类++", Toast.LENGTH_LONG).show();
                type = "events";
                adapter.addFragment(new NewsListFragment(type), type);
                break;
            case R.id.menu_item_add_news:
                Toast.makeText(MainActivity.this, "种类++", Toast.LENGTH_LONG).show();
                type = "news";
                adapter.addFragment(new NewsListFragment(type), type);
                break;
        }
        return true;
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