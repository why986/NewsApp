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
        if(id.endsWith("'"))
            this.id = id.substring(1, id.length()-1);
        if(title.endsWith("'"))
            this.title = title.substring(1, title.length()-1);
        if(time.endsWith("'"))
            this.time = time.substring(1, time.length()-1);
        if(content.endsWith("'"))
            this.content = content.substring(1, content.length()-1);
        if(source.endsWith("'"))
            this.source = source.substring(1, source.length()-1);
    }

    public DetailedNews(JSONObject data) throws JSONException {
        this.id = data.getString("_id");
        this.title = data.getString("title");
        this.time = data.getString("time");
        this.content = data.getString("content");
        this.source = data.getString("source");
    }
}
