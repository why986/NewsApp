package com.java.wanghaoyu;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabLayout;
import com.java.wanghaoyu.ui.main.NewsListFragment;
import com.java.wanghaoyu.ui.main.ViewPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


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

}