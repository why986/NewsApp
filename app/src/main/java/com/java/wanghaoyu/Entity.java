package com.java.wanghaoyu;

import java.util.HashMap;
import java.util.List;

public class Entity {
    public String url;
    public String label;
    public String wiki;
    public String type;
    public boolean forward;
    public String img;
    public HashMap<String, String> properties;
    public List<Relation> relations;

}

public class Relation{
    public String relation;
    public String url;
    public String label;
    public boolean forward;

}
