package com.java.wanghaoyu;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

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


    public interface SimpleNewsCallBack{
        void onError(String data);
        void onSuccess(List<SimpleNews> data);
    }

    public interface DetailedNewsCallBack{
        void onError(String data);
        void onSuccess(DetailedNews data);
    }

    public interface CovidDataCallBack{
        void onError(String data);
        void onSuccess(JSONObject data);
    }

    public interface EntityCallBack{
        void onError(String data);
        void onSuccess(List<Entity> data);
    }

    private static class MyTask extends AsyncTask<Void, Void, MyTask.Result> {
        SimpleNewsCallBack simpleNewsCallBack;
        DetailedNewsCallBack detailedNewsCallBack;
        CovidDataCallBack covidDataCallBack;
        EntityCallBack entityCallBack;
        String urlStr;
        String type;
        int page;
        String keyword;
        String region;

        MyTask(SimpleNewsCallBack simpleNewsCallBack, String urlStr, String type, int page){
            this.simpleNewsCallBack = simpleNewsCallBack; this.urlStr = urlStr; this.type = type; this.page = page;
        }

        MyTask(DetailedNewsCallBack detailedNewsCallBack, String urlStr){
            this.detailedNewsCallBack = detailedNewsCallBack; this.urlStr = urlStr; this.type = "DetailedNews"; this.page = -1;
        }

        MyTask(SimpleNewsCallBack simpleNewsCallBack, String urlStr, String keyword){
            this.simpleNewsCallBack = simpleNewsCallBack; this.urlStr = urlStr; this.type = "Search"; this.keyword = keyword;
        }

        MyTask(CovidDataCallBack covidDataCallBack, String urlStr, String region){
            this.covidDataCallBack = covidDataCallBack; this.urlStr = urlStr; this.region = region; this.type = "CovidData";
        }

        MyTask(EntityCallBack entityCallBack, String urlStr, String keyword){
            this.entityCallBack = entityCallBack; this.urlStr = urlStr; this.keyword = keyword; this.type = "Entity";
        }

        static class Result{
            public List<SimpleNews> data;
            public String errorData;
            public DetailedNews detailedNewsData;
            public JSONObject covidData;
            public List<Entity> entityData;
            public Result(List<SimpleNews> data){
                this.data = data;
            }
            public Result(String errorData) {this.errorData = errorData;}
            public Result(DetailedNews detailedNewsData) {this.detailedNewsData = detailedNewsData;}
            public Result(JSONObject covidData) {this.covidData = covidData;}
            public Result(List<Entity> entityData, String errorData) { this.entityData = entityData; }
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
                //Log.d("READ", builder.toString());
                String rawData = builder.toString();

                if(this.type.equals("DetailedNews")) {
                    JSONObject newsJson = new JSONObject(rawData).getJSONObject("data");

                    return new Result(new DetailedNews(newsJson.getString("_id"),
                            newsJson.getString("title"),
                            newsJson.getString("time"),
                            newsJson.getString("content"),
                            newsJson.getString("source")));
                }
                else if(this.type.equals("CovidData"))
                {
                    return new Result(new JSONObject(rawData));//.getJSONObject(region));
                }
                else if(this.type.equals("Entity")){
                    JSONArray entityJsonArray = new JSONObject(rawData).getJSONArray("data");
                    List<Entity> entityList = new ArrayList<>();
                    for(int i = 0; i < entityJsonArray.length(); ++i){
                        JSONObject entityJson = entityJsonArray.getJSONObject(i);
                        JSONObject abstractInfo = entityJson.getJSONObject("abstractInfo");

                        String wiki = abstractInfo.getString("enwiki");
                        if("".equals(wiki))
                            wiki = abstractInfo.getString("baidu");
                        if("".equals(wiki))
                            wiki = abstractInfo.getString("zhwiki");

                        JSONObject covidInfo = abstractInfo.getJSONObject("COVID");
                        List<Relation> relations = new ArrayList<>();
                        JSONArray relationJsonArray = covidInfo.getJSONArray("relations");
                        for(int j = 0; j < relationJsonArray.length(); ++j)
                        {
                            JSONObject relationJson = relationJsonArray.getJSONObject(j);
                            relations.add(new Relation(relationJson.getString("relation"),
                                    relationJson.getString("url"),
                                    relationJson.getString("label"),
                                    relationJson.getBoolean("forward")));
                        }

                        entityList.add(new Entity(entityJson.getString("url"),
                                entityJson.getString("label"),
                                wiki,
                                entityJson.getString("img"),
                                covidInfo.getJSONObject("properties"),
                                relations));
                    }
                    return new Result(entityList, null);
                }
                else {
                        List<SimpleNews> newsList = new ArrayList<>();
                        try {
                            Log.d("insertNewsList ", String.format(" type is %s ", type));
                            JSONObject data = new JSONObject(rawData);
                            JSONArray newsArray = data.getJSONArray("data");
                            for (int i = 0; i < newsArray.length(); ++i) {
                                JSONObject newsJson = newsArray.getJSONObject(i);
                                if(this.type.equals("Search") && !newsJson.getString("title").contains(keyword))
                                    continue;
                                newsList.add(new SimpleNews(newsJson.getString("_id"),
                                        newsJson.getString("title"),
                                        newsJson.getString("time"),
                                        type,
                                        newsJson.getString("source"),
                                        "FALSE"));
                            }
                        } catch (JSONException e) {
                            Log.d("insertNewsList", e.toString());
                        }
                        return new Result(newsList);
                    }
            }catch (SocketTimeoutException e)
            {
                Log.d("READ", "TIMEOUT");
                return new Result("TIMEOUT");
            }
            catch (UnknownHostException e)
            {
                Log.d("READ", "TIMEOUT");
                return new Result("TIMEOUT");
            }
            catch (Exception e){
                Log.d("READ", e.toString());
                return new Result(e.toString());
            }
        }

        @Override
        protected void onPostExecute(Result result) {
            if(this.type.equals("DetailedNews")) {
                if (result.detailedNewsData != null)
                    detailedNewsCallBack.onSuccess(result.detailedNewsData);
                else
                    detailedNewsCallBack.onError(result.errorData);
            }
            else if(this.type.equals("CovidData")){
                if (result.covidData != null)
                    covidDataCallBack.onSuccess(result.covidData);
                else
                    covidDataCallBack.onError(result.errorData);
            }
            else if(this.type.equals("Entity")){
                if (result.entityData != null)
                    entityCallBack.onSuccess(result.entityData);
                else
                    entityCallBack.onError(result.errorData);
            }
            else{
                if (result.data != null)
                    simpleNewsCallBack.onSuccess(result.data);
                else
                    simpleNewsCallBack.onError(result.errorData);
            }
        }
    }


    public void insertSimpleNewsList(String type, int page, List<SimpleNews> newsList)
    {
        dataBase = myDBOpenHelper.getWritableDatabase();
        for(SimpleNews simpleNews : newsList)
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", DatabaseUtils.sqlEscapeString(simpleNews.id));
            contentValues.put("title", DatabaseUtils.sqlEscapeString(simpleNews.title));
            contentValues.put("time", DatabaseUtils.sqlEscapeString(simpleNews.time));
            contentValues.put("type", DatabaseUtils.sqlEscapeString(type));
            contentValues.put("source", DatabaseUtils.sqlEscapeString(simpleNews.source));
            contentValues.put("page", page);
            if(simpleNews.hasRead == true) contentValues.put("hasRead", "TRUE");
            else contentValues.put("hasRead", "FALSE");
            dataBase.replace("simpleNews", null, contentValues);
        }
    }

    public void setSimpleNewsRead(String id){
        dataBase = myDBOpenHelper.getWritableDatabase();
        Cursor cursor = dataBase.query("simpleNews", new String[]{"id", "title", "time", "type", "source", "page", "hasRead"},
                "id=?", new String[]{id}, null, null, null);
        while (cursor.moveToNext()){
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", cursor.getString(cursor.getColumnIndex("id")));
            contentValues.put("title", cursor.getString(cursor.getColumnIndex("title")));
            contentValues.put("time", cursor.getString(cursor.getColumnIndex("time")));
            contentValues.put("type", cursor.getString(cursor.getColumnIndex("type")));
            contentValues.put("source", cursor.getString(cursor.getColumnIndex("source")));
            contentValues.put("page", cursor.getString(cursor.getColumnIndex("page")));
            contentValues.put("hasRead", "TRUE");
            dataBase.replace("simpleNews", null, contentValues);
        }
    }


    public List<SimpleNews> getSimpleNewsListFromDatabase(String type, int page){
        dataBase = myDBOpenHelper.getReadableDatabase();
        List<SimpleNews> list = new ArrayList<>();
        Cursor cursor = dataBase.query("simpleNews", new String[]{"id", "title", "time", "type", "source", "hasRead"},
                "type=? AND page=?", new String[]{"'" + type + "'", String.valueOf(page)}, null, null, null);
        while(cursor.moveToNext())
        {
            list.add(new SimpleNews(cursor.getString(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("type")),
                    cursor.getString(cursor.getColumnIndex("source")),
                    cursor.getString((cursor.getColumnIndex("hasRead")))));
        }
        cursor.close();
        return list;
    }

    public void getSimpleNewsList(SimpleNewsCallBack simpleNewsCallBack, final String type, final int page, int size) {
        new MyTask(simpleNewsCallBack, "https://covid-dashboard.aminer.cn/api/events/list"
                + "?type=" + type + "&page=" + page + "&size=" + size, type, page).execute();
        return;
    }

    public void insertDetailedNews(DetailedNews detailedNews){
        dataBase = myDBOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", DatabaseUtils.sqlEscapeString(detailedNews.id));
        contentValues.put("title", DatabaseUtils.sqlEscapeString(detailedNews.title));
        contentValues.put("time", DatabaseUtils.sqlEscapeString(detailedNews.time));
        contentValues.put("content", DatabaseUtils.sqlEscapeString(detailedNews.content));
        contentValues.put("source", DatabaseUtils.sqlEscapeString(detailedNews.source));
        dataBase.replace("detailedNews", null, contentValues);
    }

    public void getDetailedNews(DetailedNewsCallBack detailedNewsCallBack, final String id)
    {
        new MyTask(detailedNewsCallBack, "https://covid-dashboard.aminer.cn/api/event/" + id).execute();
    }

    public DetailedNews getDetailedNewsFromDatabase(String id){

        Log.d("getDetailedNewsFromDatabase", id);
        dataBase = myDBOpenHelper.getReadableDatabase();
        List<SimpleNews> list = new ArrayList<>();
        Cursor cursor = dataBase.query("detailedNews", new String[]{"id", "title", "time", "content", "source"},
                //null, null,
                "id=?", new String[]{"'" + id + "'"},
                null, null, null);
        DetailedNews detailedNews = null;
        while(cursor.moveToNext())
        {
            detailedNews = new DetailedNews(cursor.getString(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("content")),
                    cursor.getString(cursor.getColumnIndex("source"))
            );
        }
        cursor.close();
        return detailedNews;
    }

    public void searchSimpleNews(SimpleNewsCallBack simpleNewsCallBack, final String type, final String keyWord){
        new MyTask(simpleNewsCallBack,
                "https://covid-dashboard.aminer.cn/api/events/list?type=all&page=1&size=500", keyWord).execute();
    }

    public void shareNews(Context context, String title, String content){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        if(content.length() > 50)
            intent.putExtra(Intent.EXTRA_TEXT, "标题：" + title +  "\n新闻摘要：" + content.substring(0, 50) + "...");
        else
            intent.putExtra(Intent.EXTRA_TEXT, "标题：" + title +  "\n新闻：" + content);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "Share to:"));
    }

    public void getCovidValues(CovidDataCallBack covidDataCallBack, String region)
    {
        new MyTask(covidDataCallBack, "https://covid-dashboard.aminer.cn/api/dist/epidemic.json", region).execute();
    }

    public void getEntities(EntityCallBack entityCallBack, String keyword)
    {
        new MyTask(entityCallBack,
                "https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity=" + keyword, keyword).execute();
    }
}

