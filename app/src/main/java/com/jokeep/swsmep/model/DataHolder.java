package com.jokeep.swsmep.model;

/**
 * Created by wbq501 on 2016-3-4 14:17.
 * SWSMEP
 */
public class DataHolder {
    public String title;
    public String context;
    public boolean checked;

    public DataHolder(String title,String context,boolean check){
        this.title = title;
        this.context = context;
        this.checked=check;
    }
    public DataHolder(String context,boolean check){
        this.context = context;
        this.checked=check;
    }
}
