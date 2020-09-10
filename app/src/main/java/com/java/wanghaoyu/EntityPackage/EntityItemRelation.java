package com.java.wanghaoyu.EntityPackage;

import com.java.wanghaoyu.Relation;

public class EntityItemRelation extends EntityItem {
    public String relation;
    public String url;
    public String label;
    public boolean forward;

    public EntityItemRelation(Relation re){
        relation = re.relation;
        url = re.url;
        label = re.label;
        forward = re.forward;
    }
}
