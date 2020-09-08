package com.java.wanghaoyu.ui.main;

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

import org.json.JSONException;

import java.util.List;
import java.util.Objects;

public class NewsListFragment extends Fragment {
    String type;

    View view;
    public NewsListFragment(String type){
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.new_list_fragment, container, false);

        final Manager manager = Manager.getInstance(view.getContext());
        manager.getSimpleNewsList(new Manager.SimpleNewsCallBack() {
            @Override
            public void onError(String data) {
                Log.d("manager.getSimpleNewsList", data);
            }

            @Override
            public void onSuccess(List<SimpleNews> news_list) {
                System.out.println("SIMPLE NEW SIZE:  "+news_list.size());

                NewsItemAdapter newsItemAdapter = new NewsItemAdapter(getActivity(), news_list);
                ListView listView = (ListView) view.findViewById(R.id.news_list_view);
                listView.setAdapter(newsItemAdapter);
                newsItemAdapter.notifyDataSetChanged();
                manager.insertSimpleNewsList(type, 1, news_list);
            }
        }, type, 1, 6);


/*
 */
        return view;
    }
}
