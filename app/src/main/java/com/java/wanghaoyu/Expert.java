package com.java.wanghaoyu;

import org.json.JSONObject;

public class Expert {
    public String avatar;
    public String id;
    public String name;
    public String name_zh;
    public JSONObject indices;
    public boolean is_passedaway;
    public JSONObject profile;

    public Expert(String avatar, String id, String name, String name_zh, JSONObject indices, boolean is_passedaway, JSONObject profile)
    {
        this.avatar = avatar; this.id = id; this.name = name; this.name_zh = name_zh;
        this.indices = indices; this.is_passedaway = is_passedaway; this.profile = profile;
    }
}
