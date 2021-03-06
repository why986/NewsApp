package com.java.wanghaoyu.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.java.wanghaoyu.MainActivity;
import com.java.wanghaoyu.Manager;
import com.java.wanghaoyu.NewsContentActivity;
import com.java.wanghaoyu.R;
import com.java.wanghaoyu.SimpleNews;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;


public class NewsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeLayout;
    RecyclerView recyclerView;
    NewsRecycleViewAdapter recycleViewAdapter;
    RecyclerView.LayoutManager layoutManager;
    String type;
    View view;
    Context mcontext;
    int page_count=1;
    final int SIZE=15;
    Manager manager;
    List<SimpleNews> news_list;


    public NewsListFragment(String type){
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {


        view =  inflater.inflate(R.layout.fragment_news_list, container, false);
        mcontext = view.getContext();
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_news);
        DividerItemDecoration divider = new DividerItemDecoration(mcontext, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(mcontext);
        recyclerView.setLayoutManager(layoutManager);
        recycleViewAdapter = new NewsRecycleViewAdapter(mcontext);
        recyclerView.setAdapter(recycleViewAdapter);



        manager = Manager.getInstance(view.getContext());

        manager.getSimpleNewsList(new Manager.SimpleNewsCallBack() {
            @Override
            public void onError(String data) {
                Log.d("manager.getSimpleNewsList", data);
                if(data.equals("TIMEOUT"))
                {
                    news_list = manager.getSimpleNewsListFromDatabase(type, page_count);
                    recycleViewAdapter.changeToNews(news_list);
                    recycleViewAdapter.notifyDataSetChanged();
                    recyclerView.addOnScrollListener(new onLoadMoreListener() {
                        @Override
                        protected void onLoading(int countItem,int lastItem) {
                            Manager manager = Manager.getInstance(mcontext);
                            page_count++;
                            manager.getSimpleNewsList(new Manager.SimpleNewsCallBack() {
                                @Override
                                public void onError(String data) {
                                    Log.d("recyclerView.addOnScrollListener", data);
                                    news_list.addAll(Manager.getInstance(view.getContext()).getSimpleNewsListFromDatabase(type, page_count));
                                    ((NewsRecycleViewAdapter)recycleViewAdapter).changeToNews(news_list);
                                    recycleViewAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onSuccess(List<SimpleNews> data) {
                                    news_list.addAll(data);
                                    ((NewsRecycleViewAdapter)recycleViewAdapter).changeToNews(news_list);
                                    recycleViewAdapter.notifyDataSetChanged();
                                }
                            }, type, page_count, SIZE);
                        }
                    });
                }
            }

            @Override
            public void onSuccess(List<SimpleNews> data) {
                news_list = data;
                manager.insertSimpleNewsList(type, page_count, news_list);
                recycleViewAdapter.changeToNews(news_list);
                recycleViewAdapter.notifyDataSetChanged();
                recyclerView.addOnScrollListener(new onLoadMoreListener() {
                    @Override
                    protected void onLoading(int countItem,int lastItem) {
                        Manager manager = Manager.getInstance(mcontext);
                        page_count++;
                        manager.getSimpleNewsList(new Manager.SimpleNewsCallBack() {
                            @Override
                            public void onError(String data) {
                                Log.d("recyclerView.addOnScrollListener", data);
                                news_list.addAll(Manager.getInstance(view.getContext()).getSimpleNewsListFromDatabase(type, page_count));
                                ((NewsRecycleViewAdapter)recycleViewAdapter).changeToNews(news_list);
                                recycleViewAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onSuccess(List<SimpleNews> data) {
                                news_list.addAll(data);
                                ((NewsRecycleViewAdapter)recycleViewAdapter).changeToNews(news_list);
                                recycleViewAdapter.notifyDataSetChanged();
                            }
                        }, type, page_count, SIZE);
                    }
                });
            }
        }, type, page_count, SIZE);

        recycleViewAdapter.setOnItemClickListener(new NewsRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                recycleViewAdapter.notifyDataSetChanged();
//                int size = news_list.size();
//                Toast.makeText(mcontext, "点击position:"+position+" size:"+size, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mcontext, NewsContentActivity.class);
                intent.putExtra("ID", news_list.get(position).id);
                startActivity(intent);

            }
        });
        return view;
    }

    @Override
    public void onRefresh() {
        Manager manager = Manager.getInstance(mcontext);
        page_count = 1;
        manager.getSimpleNewsList(new Manager.SimpleNewsCallBack() {
            @Override
            public void onError(String data) {
                Log.d("manager.getSimpleNewsList", data);
            }

            @Override
            public void onSuccess(List<SimpleNews> data) {
                news_list = data;
                recycleViewAdapter.changeToNews(news_list);
                recycleViewAdapter.clearClicked();
                recycleViewAdapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
                Toast.makeText(mcontext, "刷新成功", Toast.LENGTH_LONG).show();
            }
        }, type, page_count, SIZE);
    }
}


