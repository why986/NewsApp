package com.java.wanghaoyu;

import org.json.JSONObject;
import org.json.JSONException;

public class SimpleNews {
    public String id;
    public String title;
    public String time;
    public String type;
    public String source;

    public SimpleNews()
    {

    }

    public SimpleNews(String id, String title, String time, String type, String source)
    {
        this.id = id; this.title = title; this.time = time; this.type = type; this.source = source;
    }

    public SimpleNews(JSONObject data) throws JSONException{
        this.id = data.getString("_id");
        this.title = data.getString("title");
        this.time = data.getString("time");
        this.type = data.getString("type");
        this.source = data.getString("source");
    }

}