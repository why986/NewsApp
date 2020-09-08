package com.java.wanghaoyu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

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

    private MyDBOpenHelper myDBOpenHelper;
    private SQLiteDatabase dataBase;
    private boolean hasInitCovidData;


    private Manager(Context context) throws IOException
    {
        this.myDBOpenHelper = new MyDBOpenHelper(context, "mydata.db", null, 1);
        hasInitCovidData = false;
    }

    public interface MyCallBack {
        void timeout();
        void error();
        void onSuccess(String data);
    }

    public interface SimpleNewsCallBack{
        void onError(String data);
        void onSuccess(List<SimpleNews> data);
    }

    private static class MyTask extends AsyncTask<Void, Void, MyTask.Result> {
        SimpleNewsCallBack myCallBack;
        String urlStr;
        String type;
        int page;
        MyTask(SimpleNewsCallBack myCallBack, String urlStr, String type, int page){
            this.myCallBack = myCallBack; this.urlStr = urlStr; this.type = type; this.page = page;
        }

        static class Result{
            public List<SimpleNews> data;
            public String errorData;
            public Result(List<SimpleNews> data){
                this.data = data;
            }
            public Result(String errorData) {this.errorData = errorData;}
        }

        @Override
        protected Result doInBackground(Void... voids) {
            try {

                URL url = new URL(urlStr);
                HttpsTrustManager.allowAllSSL();
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setAllowUserInteraction(false);
                connection.setInstanceFollowRedirects(true);
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
// 发起请求
                connection.connect();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e("HTTPS", "[NewsDetailActivity line 80] NOT OK");
                }
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                reader.close();
                connection.disconnect();
                Log.d("READ", builder.toString());
                String rawData = builder.toString();

                List<SimpleNews> newsList = new ArrayList<>();
                try {
                    Log.d("insertNewsList ", String.format(" type is %s ", type));
                    JSONObject data = new JSONObject(rawData);
                    JSONArray newsArray = data.getJSONArray("data");
                    //Log.d("insertNewsList", String.valueOf(newsArray.length()));
                    for (int i = 0; i < newsArray.length(); ++i) {
                        JSONObject newsJson = newsArray.getJSONObject(i);

                        newsList.add(new SimpleNews(newsJson.getString("_id"),
                                newsJson.getString("title"),
                                newsJson.getString("time"),
                                type,
                                newsJson.getString("source")));
                        /*
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("id", DatabaseUtils.sqlEscapeString(newsJson.getString("_id")));
                        contentValues.put("title", DatabaseUtils.sqlEscapeString(newsJson.getString("title")));
                        contentValues.put("time", DatabaseUtils.sqlEscapeString(newsJson.getString("time")));
                        contentValues.put("type", DatabaseUtils.sqlEscapeString(type));
                        contentValues.put("source", DatabaseUtils.sqlEscapeString(newsJson.getString("source")));
                        contentValues.put("page", page);

                         */
                        //Log.d("insertNewsList ", String.format(" id is %s ", newsJson.getString("_id")));
                        //Log.d("insertNewsList", String.format(" type is %s ", type));
                    }
                }catch (JSONException e)
                {
                    Log.d("insertNewsList", e.toString());
                }
                return new Result(newsList);
            }catch (Exception e){
                Log.d("READ", e.toString());
                return new Result(e.toString());
            }
        }

        @Override
        protected void onPostExecute(Result result) {
            if (result.data != null)
                myCallBack.onSuccess(result.data);
            else
                myCallBack.onError(result.errorData);
        }
    }

    private void connectToInterface(String url, final MyCallBack myCallBack)
    {
        Log.d("connectToInterface", url);
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("connectToInterface", " Failed");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    //Log.d("okHttp ", response.body().string());
                    myCallBack.onSuccess(response.body().string());
                }catch (Exception e)
                {
                    Log.d("connectToInterface ", e.toString());
                    e.printStackTrace();
                }
            }
        });

        Log.d("connectToInterface", url);
    }

    public void insertSimpleNewsList(String type, int page, List<SimpleNews> newsList)
    {
        dataBase = myDBOpenHelper.getWritableDatabase();
        for(SimpleNews simpleNews : newsList)
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", DatabaseUtils.sqlEscapeString(simpleNews.id));
            contentValues.put("title", DatabaseUtils.sqlEscapeString(simpleNews.title));
            contentValues.put("time", DatabaseUtils.sqlEscapeString(simpleNews.title));
            contentValues.put("type", DatabaseUtils.sqlEscapeString(type));
            contentValues.put("source", DatabaseUtils.sqlEscapeString(simpleNews.source));
            contentValues.put("page", page);
            dataBase.insert("news", null, contentValues);
        }
    }

    public void getSimpleNewsList(SimpleNewsCallBack simpleNewsCallBack, final String type, final int page, int size)
    {
        new MyTask(simpleNewsCallBack, "https://covid-dashboard.aminer.cn/api/events/list"
                + "?type=" + type + "&page=" + page + "&size=" + size, type, page).execute();
        /*
        dataBase = myDBOpenHelper.getReadableDatabase();
        List<SimpleNews> list = new ArrayList<>();
        Cursor cursor = dataBase.query("news", new String[]{"id", "title", "time", "type", "source"},
                "type=? AND page=?", new String[]{type, String.valueOf(page)}, null, null, null);
        while(cursor.moveToNext())
        {
            Log.d("getListSimpleNews ", String.format("(type, id, title, source) VALUES(%s, %s, %s, %s) type is %s and page is %s",
                    cursor.getString(cursor.getColumnIndex("type")),
                    cursor.getString(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("source")),
                    type,
                    String.valueOf(page)));


            //System.out.println(cursor.getColumnCount());
            //for(int j = 0; j < 5; ++j) System.out.println(cursor.getColumnName(j));
            System.out.println(cursor.getColumnIndex("id") + " " + cursor.getColumnIndex("title") + " "
                    + cursor.getColumnIndex("time") + " " + cursor.getColumnIndex("type") + " " + cursor.getColumnIndex("source"));
            list.add(new SimpleNews(cursor.getString(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("type")),
                    cursor.getString(cursor.getColumnIndex("source"))));
        }
        cursor.close();

         */
        return ;
    }

    /*

    List<SimpleNews> searchSimpleNews(final String type, final String keyWord){
        connectToInterface("https://covid-dashboard.aminer.cn/api/events/list"
                + "?type=" + type + "&page=" + 1 + "&size=" + 100, new CallBack() {
            @Override
            public void timeout() {
                Log.d("searchSimpleNews", "timeout");
            }

            @Override
            public void error() {
                Log.d("searchSimpleNews", "error");
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
                    Log.d("searchSimpleNews ", e.toString());
                }
            }
        });
        return dataBase.getListSimpleNews(type, 1001);
    }

    DetailedNews getDetailedNews(String id)
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

    public String getBeginTimeAndPointValues(List<PointValue> confirmedPointValues, List<PointValue> curedPointValues, List<PointValue> deadPointValues, String region)
    {
        if(hasInitCovidData == false)
        {
            hasInitCovidData = true;
            connectToInterface("https://covid-dashboard.aminer.cn/api/dist/epidemic.json", new CallBack() {
                @Override
                public void timeout()  {
                    Log.d("getSimpleNewsList", "timeout");
                }

                @Override
                public void error() {
                    Log.d("getSimpleNewsList", "error");
                }

                @Override
                public void onSuccess(String data) {
                    dataBase.initCovidData(data);
                }
            });
        }
        return dataBase.getBeginTimeAndPointValues(confirmedPointValues, curedPointValues, deadPointValues, region);
    }
     */
}

