package com.java.wanghaoyu;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

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
                "CREATE TABLE IF NOT EXISTS `news`(type string, id string, page integer, data text, PRIMARY KEY(type, page)) "
        );
    }

    void insertNewsList(String type, int page,  String rawData) throws JSONException
    {
        JSONObject data = new JSONObject(rawData);
        JSONArray newsArray = data.getJSONArray("data");
        for(int i = 0; i < newsArray.length(); ++i)
        {
            JSONObject newsJson = newsArray.getJSONObject(i);
            database.execSQL(
                    String.format(
                            "INSERT OR REPLACE INTO `news` (type, id, page, data) VALUES(%s, %s, %s, %s)",
                            DatabaseUtils.sqlEscapeString(type),
                            DatabaseUtils.sqlEscapeString(newsJson.getString("_id")),
                            String.valueOf(page),
                            DatabaseUtils.sqlEscapeString(newsJson.toString())
                    )
            );
        }

    }

    List<SimpleNews> getListSimpleNews(String type, int page) throws JSONException
    {
        Cursor cursor = database.rawQuery(
                String.format("SELECT * FROM `news` WHERE type=%s AND page=%s",
                        type, String.valueOf(page)),
                null);
        List<SimpleNews> list = new ArrayList<>();
        while(cursor.moveToNext())
            list.add(new SimpleNews(new JSONObject(cursor.getString(cursor.getColumnIndex("data")))));
        cursor.close();
        return list;
    }

    DetailedNews getDetailedNews(String id) throws JSONException
    {
        Cursor cursor = database.rawQuery(
                String.format("SELECT * FROM `news` WHERE id=%s", id),
                null);
        DetailedNews detailedNews = new DetailedNews(new JSONObject(cursor.getString(cursor.getColumnIndex("data"))));
        cursor.close();
        return  detailedNews;
    }
}
