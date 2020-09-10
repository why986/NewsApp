package com.java.wanghaoyu;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class Entity {
    public String url = "url";
    public String label = "label";
    public String wiki = "wiki";
    public String type = "type";
    public String img = "img";
    public JSONObject properties;
    public List<Relation> relations;

    public Entity(){}

}


