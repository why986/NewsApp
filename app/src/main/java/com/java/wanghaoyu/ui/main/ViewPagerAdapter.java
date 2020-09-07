package com.java.wanghaoyu.ui.main;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.java.wanghaoyu.SimpleNews;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    List<String> newsListType;
    private final List<Fragment> mFragments = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    public void addFragment(Fragment fragment, String type) {
        this.newsListType.add(type);
        this.mFragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return newsListType.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return newsListType.get(position);
    }
}
