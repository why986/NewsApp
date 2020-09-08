package com.java.wanghaoyu;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailedNews {
    String id;
    String title;
    String time;
    String content;
    String source;

    public DetailedNews() {}

    public DetailedNews(String id, String title, String time, String content, String source){
        this.id = id; this.title = title; this.time = time; this.content = content; this.source = source;
    }

    public DetailedNews(JSONObject data) throws JSONException {
        this.id = data.getString("_id");
        this.title = data.getString("title");
        this.time = data.getString("time");
        this.content = data.getString("content");
        this.source = data.getString("source");
    }
}
