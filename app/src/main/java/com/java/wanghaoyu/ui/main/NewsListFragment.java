package com.java.wanghaoyu.ui.main;

import android.os.Bundle;
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
    List<SimpleNews> news_list;
    public NewsListFragment(String type){
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.new_list_fragment, container, false);
        Manager manager = Manager.getInstance(view.getContext());
        List<SimpleNews> news_list = manager.getSimpleNewsList(type, 1, 20);

        System.out.println("SIMPLE NEW SIZE:  "+news_list.size());

        NewsItemAdapter newsItemAdapter = new NewsItemAdapter(getActivity(), news_list);
        ListView listView = (ListView) view.findViewById(R.id.news_list_view);
        listView.setAdapter(newsItemAdapter);
        newsItemAdapter.notifyDataSetChanged();

        return view;
    }
}
