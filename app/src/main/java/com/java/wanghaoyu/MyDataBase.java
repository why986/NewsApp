package com.java.wanghaoyu;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lecho.lib.hellocharts.model.PointValue;

public class MyDataBase {
    private SQLiteDatabase database;
    String databasePath;

    MyDataBase(Context context) throws IOException{
        databasePath = context.getFilesDir().getPath() + "/data.db";
        database = SQLiteDatabase.openOrCreateDatabase(databasePath, null);

        database.execSQL(
                "CREATE TABLE IF NOT EXISTS simpleNews(id text primary key, title text, " +
                        "time text, type text, source text, page integer)"
        );
        database.execSQL(
                "CREATE TABLE IF NOT EXISTS covid_data(region text primary key, begin_time text, data text)"
        );
    }

    void insertSimpleNews(String type, int page, JSONObject data) throws JSONException
    {
        database.execSQL(
                        "INSERT OR REPLACE INTO simpleNews (id, title, time, type, source, page) VALUES(?, ?, ?, ?, ?, ?)",
                        new String[]{
                                DatabaseUtils.sqlEscapeString(data.getString("_id")),
                                DatabaseUtils.sqlEscapeString(data.getString("title")),
                                DatabaseUtils.sqlEscapeString(data.getString("time")),
                                DatabaseUtils.sqlEscapeString(type),
                                DatabaseUtils.sqlEscapeString(data.getString("source")),
                                String.valueOf(page)
                        });
    }

    void insertNewsList(String type, int page,  String rawData)
    {
        try {
            Log.d("insertNewsList ", String.format(" type is %s ", type));
            JSONObject data = new JSONObject(rawData);
            JSONArray newsArray = data.getJSONArray("data");
            //Log.d("insertNewsList", String.valueOf(newsArray.length()));
            for (int i = 0; i < newsArray.length(); ++i) {
                JSONObject newsJson = newsArray.getJSONObject(i);
                insertSimpleNews(type, page, newsJson);
                //Log.d("insertNewsList ", String.format(" id is %s ", newsJson.getString("_id")));
                //Log.d("insertNewsList", String.format(" type is %s ", type));
            }
        }catch (JSONException e)
        {
            Log.d("insertNewsList", e.toString());
        }
    }


    List<SimpleNews> getListSimpleNews(String type, int page)
    {
        Log.d("getListSimpleNews ", "fuck off");
        List<SimpleNews> list = new ArrayList<>();
        try {
            Cursor cursor = database.rawQuery(
                    "SELECT * FROM simpleNews WHERE type = ?",
                            new String[]{type}
            );
            while (cursor.moveToNext()) {

                Log.d("getListSimpleNews ", String.format("(type, id, page, title, source) VALUES(%s, %s, %s, %s, %s) type is %s and page is %s",
                        cursor.getString(cursor.getColumnIndex("type")),
                        cursor.getString(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("page")),
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
        }catch (Exception e)
        {
            Log.d("getListSimpleNews ", e.toString());
            e.printStackTrace();
        }
        return list;
    }

    DetailedNews getDetailedNews(String id)
    {
        Cursor cursor = database.rawQuery(
                "SELECT * FROM news WHERE id=?", new String[]{id});
        DetailedNews detailedNews = null;
        try {
            detailedNews = new DetailedNews(new JSONObject(cursor.getString(cursor.getColumnIndex("data"))));
        }catch (JSONException e){
            Log.d("getDetailedNews ", e.toString());
        }
        cursor.close();
        return detailedNews;
    }

    void initCovidData(String rawData)
    {
        try{
            JSONObject data = new JSONObject(rawData);
            for (Iterator<String> it = data.keys(); it.hasNext(); ) {
                String region = it.next();
                JSONObject regionData = data.getJSONObject(region);
                database.execSQL(
                        "INSERT OR REPLACE INTO covid_data (region, begin_time, data) VALUES(?, ?, ?)",
                        new String[]{
                                DatabaseUtils.sqlEscapeString(region),
                                DatabaseUtils.sqlEscapeString(regionData.getString("begin")),
                                DatabaseUtils.sqlEscapeString(regionData.getString("data"))
                        });
            }
        }catch (JSONException e){
            Log.d("initCovidData ", e.toString());
        }
    }

    public String getBeginTimeAndPointValues(List<PointValue> confirmedPointValues, List<PointValue> curedPointValues, List<PointValue> deadPointValues, String region)
    {
        String begin_time = null;
        Cursor cursor = database.rawQuery(
                "SELECT * FROM covid_data WHERE region=?",
                new String[]{region});
        while (cursor.moveToNext()) {
            try {
                begin_time = cursor.getString(cursor.getColumnIndex("begin_time"));
                JSONArray data = new JSONArray(cursor.getString(cursor.getColumnIndex("data")));
                for(int i = 0; i < data.length(); ++i)
                {
                    JSONArray timeData = data.getJSONArray(i);
                    confirmedPointValues.add(new PointValue(i, Integer.parseInt(timeData.getString(0))));
                    curedPointValues.add(new PointValue(i, Integer.parseInt(timeData.getString(2))));
                    deadPointValues.add(new PointValue(i, Integer.parseInt(timeData.getString(3))));
                }
            }catch (JSONException e){
                Log.d("getPointValues", e.toString());
            }
        }
        return begin_time;
    }
}
