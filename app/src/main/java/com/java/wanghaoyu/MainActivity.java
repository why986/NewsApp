package com.java.wanghaoyu;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabLayout;
import com.java.wanghaoyu.ClusterPackage.ClusterActivity;
import com.java.wanghaoyu.ClusterPackage.ClusterAdapter;
import com.java.wanghaoyu.EntityPackage.EntityActivity;
import com.java.wanghaoyu.ExpertPackage.ExpertActivity;
import com.java.wanghaoyu.ui.main.NewsListFragment;
import com.java.wanghaoyu.ui.main.ViewPagerAdapter;


public class MainActivity extends AppCompatActivity {

    private static Manager manager;
    private Toolbar mtbar;
    private ViewPagerAdapter adapter;
    private ViewPager viewpager;
    private TabLayout tabLayout;
    DatabaseHelper databaseHelper = null;
    private Button b1, b2, b3, b4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getApplicationContext(), SearchableActivity.class)));
        searchView.setIconifiedByDefault(false);
        // 显示“开始搜索”的按钮
        searchView.setSubmitButtonEnabled(true);
        // 提示内容右边提供一个将提示内容放到搜索框的按钮
        searchView.setQueryRefinementEnabled(true);
        return true;

    }

    public void onClick(View view){
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.button1:
                intent.setClass(MainActivity.this, LineChartActivity.class);
                startActivity(intent);
                break;

            case R.id.button2:
                intent.setClass(MainActivity.this, EntityActivity.class);
                startActivity(intent);
                break;

            case R.id.button3:
                intent.setClass(MainActivity.this, ClusterActivity.class);
                startActivity(intent);
                break;

            case R.id.button4:
                intent.setClass(MainActivity.this, ExpertActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String type;
        switch (item.getItemId()) {
            case R.id.menu_item_minus:
                Toast.makeText(MainActivity.this, "删除种类页面", Toast.LENGTH_LONG).show();
                adapter.delFragment();
                break;
            case R.id.menu_item_add_paper:
                Toast.makeText(MainActivity.this, "添加种类Paper，爱学术", Toast.LENGTH_LONG).show();
                type = "paper";
                adapter.addFragment(new NewsListFragment(type), type);
                break;
            case R.id.menu_item_add_all:
                Toast.makeText(MainActivity.this, "添加页面All，所有信息全部知晓", Toast.LENGTH_LONG).show();
                type = "all";
                adapter.addFragment(new NewsListFragment(type), type);
                break;
            case R.id.menu_item_add_points:
                Toast.makeText(MainActivity.this, "添加种类Points，但Points似乎什么都没有", Toast.LENGTH_LONG).show();
                type = "points";
                adapter.addFragment(new NewsListFragment(type), type);
                break;
            case R.id.menu_item_add_event:
                Toast.makeText(MainActivity.this, "添加种类Event，关注大事件", Toast.LENGTH_LONG).show();
                type = "events";
                adapter.addFragment(new NewsListFragment(type), type);
                break;
            case R.id.menu_item_add_news:
                Toast.makeText(MainActivity.this, "添加种类News，时事一手掌握", Toast.LENGTH_LONG).show();
                type = "news";
                adapter.addFragment(new NewsListFragment(type), type);
                break;
        }
        return true;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        initData();
    }

    private void initData(){
        databaseHelper = DatabaseHelper.getHelper(this);

        if (databaseHelper.isWordTableEmpty()){
            Word word0 = new Word("abolish");
            Word word1 = new Word("accuse");
            Word word2 = new Word("account");

            databaseHelper.insertWord(word0);
            databaseHelper.insertWord(word1);
            databaseHelper.insertWord(word2);
        }
    }




}