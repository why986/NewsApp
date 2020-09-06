package com.java.wanghaoyu;

import org.json.JSONObject;
import org.json.JSONException;

public class SimpleNews {
    public String id;
    public String title;
    public String time;
    public String date;
    public String content;
    public String type;
    public String source;

    public SimpleNews()
    {

    }

    public SimpleNews(JSONObject data) throws JSONException{
        this.id = data.getString("_id");
        this.title = data.getString("title");
        this.time = data.getString("time");
        this.date = data.getString("date");
        this.content = data.getString("content");
        this.type = data.getString("type");
        this.source = data.getString("source");
    }
}