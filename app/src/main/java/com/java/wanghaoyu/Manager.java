package com.java.wanghaoyu;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PointValue;
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

    private MyDataBase dataBase;

    private static JSONObject covidData;

    private Manager(Context context) throws IOException
    {
        this.dataBase = new MyDataBase(context);
    }

    public interface CallBack{
        void timeout();
        void error();
        void onSuccess(String data);
    }

    private void CallNewsList(String type, int page, int size, final CallBack callBack)
    {
        String url = "https://covid-dashboard.aminer.cn/api/events/list"
                + "?type=" + type + "&page=" + page + "&size=" + size;
        Log.d("GetSimpleNewsList", url);
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("GetSimpleNewsList", " Failed");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    //Log.d("okHttp ", response.body().string());
                    callBack.onSuccess(response.body().string());
                }catch (Exception e)
                {
                    Log.d("okHttp ", e.toString());
                    e.printStackTrace();
                }
            }
        });

        Log.d("GetSimpleNewsList", url);
    }

    List<SimpleNews> getSimpleNewsList(final String type, final int page, int size)
    {
        CallNewsList(type, page, size, new CallBack() {
            @Override
            public void timeout() {
                Log.d("getSimpleNewsList", "timeout");
            }

            @Override
            public void error() {
                Log.d("getSimpleNewsList", "error");
            }

            @Override
            public void onSuccess(String data) {
                Log.d("callBack onSuccess ", data);
                dataBase.insertNewsList(type, page, data);
            }
        });
        return dataBase.getListSimpleNews(type, page);
    }

    List<SimpleNews> searchSimpleNews(final String type, final String keyWord)throws JSONException{
        CallNewsList(type, 1, 100, new CallBack() {
            @Override
            public void timeout() {

            }

            @Override
            public void error() {

            }

            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject rawData = new JSONObject(data);
                    JSONArray newsArray = rawData.getJSONArray("data");
                    List<SimpleNews> list = new ArrayList<>();
                    for (int i = 0; i < newsArray.length(); ++i) {
                        JSONObject newsData = newsArray.getJSONObject(i);
                        if (newsData.getString("title").contains(keyWord))
                            dataBase.insertSimpleNews(type, 1001, newsData);
                    }
                }catch (JSONException e){

                }
            }
        });
        return dataBase.getListSimpleNews(type, 1001);
    }

    DetailedNews getDetailedNews(String id) throws JSONException
    {
        return dataBase.getDetailedNews(id);
    }

    public void shareNews(Context context, String title, String content){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        if(content.length() > 50)
            intent.putExtra(Intent.EXTRA_TEXT, content.substring(0, 50) + "...");
        else
            intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "Share to:"));
    }

    public void getPointValues(List<PointValue> confirmedPointValues, List<PointValue> curedPointValues, List<PointValue> deadPointValues, String region)
    {
        if(covidData == null)
        {
            String url = "https://covid-dashboard.aminer.cn/api/dist/epidemic.json";
            OkHttpClient okHttpClient = new OkHttpClient();
            final Request request = new Request.Builder().url(url).get().build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("SearchSimpleNews", " Failed ");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        covidData = new JSONObject(response.body().string());
                    }catch (JSONException e)
                    {

                    }
                }
            });
        }
    }
}
