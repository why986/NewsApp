package com.java.wanghaoyu.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.java.wanghaoyu.Manager;
import com.java.wanghaoyu.R;
import com.java.wanghaoyu.SimpleNews;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONException;

import java.util.List;
import java.util.Objects;


public class NewsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeLayout;
    RecyclerView recyclerView;
    RecyclerView.Adapter recycleViewAdapter;
    RecyclerView.LayoutManager layoutManager;
    String type;
    View view;
    Context mcontext;
    Integer page_count, SIZE;
    Manager manager;
    public NewsListFragment(String type){
        this.type = type;
    }
    List<SimpleNews> news_list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
/*
 */

        view =  inflater.inflate(R.layout.new_list_fragment, container, false);
        mcontext = view.getContext();
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_news);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(mcontext);
        recyclerView.setLayoutManager(layoutManager);

        manager = Manager.getInstance(view.getContext());

        manager.getSimpleNewsList(new Manager.SimpleNewsCallBack() {
            @Override
            public void onError(String data) {
                Log.d("manager.getSimpleNewsList", data);
            }

            @Override
            public void onSuccess(List<SimpleNews> data) {
                news_list = data;
                manager.insertSimpleNewsList(type, 1, news_list);
                recycleViewAdapter = new NewsRecycleViewAdapter(news_list, mcontext);
                recyclerView.setAdapter(recycleViewAdapter);
                recyclerView.addOnScrollListener(new onLoadMoreListener() {
                    @Override
                    protected void onLoading(int countItem,int lastItem) {
                        Manager manager = Manager.getInstance(mcontext);
                        page_count++;
                        manager.getSimpleNewsList(new Manager.SimpleNewsCallBack() {
                            @Override
                            public void onError(String data) {
                                Log.d("manager.getSimpleNewsList", data);
                            }

                            @Override
                            public void onSuccess(List<SimpleNews> data) {
                                news_list = data;
                            }
                        }, type, page_count, SIZE);
                        //List<SimpleNews> news_list = manager.getSimpleNewsList(type, page_count, SIZE);
                        ((NewsRecycleViewAdapter)recycleViewAdapter).addNews(news_list);
                        recycleViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        }, type, 1, 15);
        //List<SimpleNews> news_list = manager.getSimpleNewsList(type, page_count, SIZE);

        //Log.d("onCreateView", String.valueOf(news_list.size()));



        return view;

    }

    @Override
    public void onRefresh() {
        Manager manager = Manager.getInstance(mcontext);
        page_count = 1;manager.getSimpleNewsList(new Manager.SimpleNewsCallBack() {
            @Override
            public void onError(String data) {
                Log.d("manager.getSimpleNewsList", data);
            }

            @Override
            public void onSuccess(List<SimpleNews> data) {
                news_list = data;
                ((NewsRecycleViewAdapter)recycleViewAdapter).changeToNews(news_list);
                recycleViewAdapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
            }
        }, type, page_count, SIZE);
    }


}

//public class NewsListFragment extends Fragment {
//
//    String type;
//    List<SimpleNews> news_list;
//    public NewsListFragment(String type){
//        this.type = type;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view =  inflater.inflate(R.layout.new_list_fragment, container, false);
//        Manager manager = Manager.getInstance(view.getContext());
//        List<SimpleNews> news_list = manager.getSimpleNewsList(type, 1, 20);
//
//        System.out.println("SIMPLE NEW SIZE:  "+news_list.size());
//
//        NewsItemAdapter newsItemAdapter = new NewsItemAdapter(getActivity(), news_list);
//        ListView listView = (ListView) view.findViewById(R.id.news_list_view);
//        listView.setAdapter(newsItemAdapter);
//        newsItemAdapter.notifyDataSetChanged();
//
//        return view;
//    }
//
//
//
//}