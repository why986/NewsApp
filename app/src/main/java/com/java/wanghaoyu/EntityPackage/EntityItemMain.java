package com.java.wanghaoyu.EntityPackage;

public class EntityItemMain extends EntityItem {
    public String url;
    public String label;
    public String wiki;
    public String img;

    public EntityItemMain(Entity e){
        url = e.url;
        label = e.label;
        wiki = e.wiki;
        img = e.img;
    }
}
