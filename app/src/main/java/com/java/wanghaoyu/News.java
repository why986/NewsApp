package com.java.wanghaoyu;

import org.json.JSONObject;
import org.json.JSONException;

public class News{
    String id;
    String title;
    String time;
    String date;
    String content;
    String type;
    String source;

    public News()
    {

    }

    public News(JSONObject data) throws JSONException{
        this.id = data.getString("_id");
        this.title = data.getString("title");
        this.time = data.getString("time");
        this.date = data.getString("date");
        this.content = data.getString("content");
        this.type = data.getString("type");
    }
}