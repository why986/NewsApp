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
import java.util.List;

public class MyDataBase {
    private SQLiteDatabase database;
    String databasePath;

    MyDataBase(Context context) throws IOException{
        databasePath = context.getFilesDir().getPath() + "/data.db";
        database = SQLiteDatabase.openOrCreateDatabase(databasePath, null);

        database.execSQL(
                "CREATE TABLE IF NOT EXISTS news(id text primary key, type text, page integer," +
                        " title text, data text) "
        );
    }

    void insertSimpleNews(String type, int page, JSONObject data) throws JSONException
    {
        database.execSQL(
                        "INSERT OR REPLACE INTO news (id, type, page, title, data) VALUES(?, ?, ?, ?, ?)",
                        new String[]{
                                DatabaseUtils.sqlEscapeString(data.getString("_id")),
                                type,
                                String.valueOf(page),
                                DatabaseUtils.sqlEscapeString(data.getString("title")),
                                DatabaseUtils.sqlEscapeString(data.toString())
                        });
    }

    void insertNewsList(String type, int page,  String rawData)
    {
        try {
            Log.d("insertNewsList ", String.format(" type is %s ", type));
            JSONObject data = new JSONObject(rawData);
            JSONArray newsArray = data.getJSONArray("data");
            Log.d("insertNewsList", String.valueOf(newsArray.length()));
            for (int i = 0; i < newsArray.length(); ++i) {
                JSONObject newsJson = newsArray.getJSONObject(i);
                database.execSQL(
                                "INSERT OR REPLACE INTO news (id, type, page, title, data) VALUES(?, ?, ?, ?, ?)",
                                new String[]{
                                        DatabaseUtils.sqlEscapeString(newsJson.getString("_id")),
                                        type,
                                        String.valueOf(page),
                                        DatabaseUtils.sqlEscapeString(newsJson.getString("title")),
                                        DatabaseUtils.sqlEscapeString(newsJson.toString())
                                });
                Log.d("insertNewsList ", String.format(" id is %s ", newsJson.getString("_id")));
                Log.d("insertNewsList", String.format(" type is %s ", type));
            }
        }catch (JSONException e)
        {
            Log.d("insertNewsList", e.toString());
        }
    }


    List<SimpleNews> getListSimpleNews(String type, int page)
    {
        List<SimpleNews> list = new ArrayList<>();
        try {
            Cursor cursor = database.rawQuery(//"SELECT * FROM news",
                    "SELECT * FROM news WHERE type=? AND page=?",
                            new String[]{
                            type, String.valueOf(page)
            });
            while (cursor.moveToNext()) {
                Log.d("getListSimpleNews ", String.format("(type, id, page, title, data) VALUES(%s, %s, %s, %s, %s)",
                        cursor.getString(cursor.getColumnIndex("type")),
                        cursor.getString(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("page")),
                        cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getString(cursor.getColumnIndex("data"))));
                list.add(new SimpleNews(new JSONObject(cursor.getString(cursor.getColumnIndex("data")))));
            }
            cursor.close();
        }catch (Exception e)
        {
            Log.d("getListSimpleNews ", e.toString());
            e.printStackTrace();
        }
        return list;
    }

    DetailedNews getDetailedNews(String id) throws JSONException
    {
        Cursor cursor = database.rawQuery(
                "SELECT * FROM news WHERE id=?", new String[]{id});
        DetailedNews detailedNews = new DetailedNews(new JSONObject(cursor.getString(cursor.getColumnIndex("data"))));
        cursor.close();
        return  detailedNews;
    }
}
