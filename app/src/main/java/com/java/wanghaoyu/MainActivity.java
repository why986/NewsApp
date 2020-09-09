package com.java.wanghaoyu;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabLayout;
import com.java.wanghaoyu.ui.main.NewsListFragment;
import com.java.wanghaoyu.ui.main.ViewPagerAdapter;


public class MainActivity extends AppCompatActivity {

    private static Manager manager;
    private Toolbar mtbar;
    private ViewPagerAdapter adapter;
    private ViewPager viewpager;
    private TabLayout tabLayout;
    DatabaseHelper databaseHelper = null;

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        // 显示“开始搜索”的按钮
        searchView.setSubmitButtonEnabled(true);
        // 提示内容右边提供一个将提示内容放到搜索框的按钮
        searchView.setQueryRefinementEnabled(true);
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