package com.java.wanghaoyu;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Manager {
    private static Manager managerInstance = null;

    public static synchronized Manager getInstance(Context context)
    {
        if(managerInstance != null) return managerInstance;
        try{
            managerInstance = new Manager(context);
        }  catch (IOException e){
            e.printStackTrace();
            throw new AssertionError();
        }
        return managerInstance;
    }

    public static MyDataBase dataBase;

    private Manager(Context context) throws IOException
    {
        this.dataBase = new MyDataBase(context);
    }

    List<SimpleNews> getSimpleNewsList(final String type, final int page, int size) throws JSONException
    {
        String url = "https://covid-dashboard.aminer.cn/api/events/list"
                + "?type=" + type + "&page=" + page + "size=" + size;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("GetSimpleNewsList", " Failed ");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    dataBase.insertNewsList(type, page, response.body().string());
                }catch (JSONException e)
                {

                }
            }
        });
        return dataBase.getListSimpleNews(type, page);
    }

    DetailedNews getDetailedNews(String id) throws JSONException
    {
        return dataBase.getDetailedNews(id);
    }
}
