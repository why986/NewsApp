package com.java.wanghaoyu;

public class SimpleNews {
    public String id;
    public String title;
    public String time;
    public String type;
    public String source;
    public boolean hasRead = false;

    public SimpleNews()
    {

    }

    public SimpleNews(String id, String title, String time, String type, String source, String hasRead)
    {
        this.id = id; this.title = title; this.time = time; this.type = type; this.source = source;
        if(id.endsWith("'"))
            this.id = id.substring(1, id.length()-1);
        if(title.endsWith("'"))
            this.title = title.substring(1, title.length()-1);
        if(time.endsWith("'"))
            this.time = time.substring(1, time.length()-1);
        if(type.endsWith("'"))
            this.type = type.substring(1, type.length()-1);
        if(source.endsWith("'"))
            this.source = source.substring(1, source.length()-1);
        if(hasRead.equals("TRUE"))
            this.hasRead = true;
        else this.hasRead = false;
    }


}