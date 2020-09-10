package com.java.wanghaoyu;

public class Relation{
    public String relation;
    public String url;
    public String label;
    public boolean forward;

    public Relation(String relation, String url, String label, boolean forward)
    {
        this.relation = relation; this.url = url; this.label = label; this.forward = forward;
    }
}