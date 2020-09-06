package com.java.wanghaoyu;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailedNews {
    String id;
    String title;
    String time;
    String date;
    String content;
    String source;
    String authors;

    public DetailedNews()
    {

    }

    public DetailedNews(JSONObject data) throws JSONException {
        this.id = data.getString("_id");
        this.title = data.getString("title");
        this.time = data.getString("time");
        this.date = data.getString("date");
        this.content = data.getString("content");
        this.source = data.getString("source");
        this.authors = data.getString("authors");
    }
}
