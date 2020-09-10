package com.java.wanghaoyu;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class Entity {
    public String url;
    public String label;
    public String wiki;
    public String img;
    public JSONObject properties;
    public List<Relation> relations;

    public Entity(String url, String label, String wiki,
                  String img, JSONObject properties, List<Relation> relations)
    {
        this.url = url; this.label = label; this.wiki = wiki;
        this.img = img; this.properties = properties; this.relations = relations;
    }
}


