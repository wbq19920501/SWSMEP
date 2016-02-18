package com.jokeep.swsmep.model;

/**
 * Created by wbq501 on 2016-2-2 17:21.
 * SWSMEP
 */
public class SortModel {
    private String name;
    private String nametype;
    private String sortLetters;
    private String callphone;
    private int F_CALLPHONETYPE;
    private String F_USERID;

    public String getCallphone() {
        return callphone;
    }

    public void setCallphone(String callphone) {
        this.callphone = callphone;
    }

    public int getF_CALLPHONETYPE() {
        return F_CALLPHONETYPE;
    }

    public void setF_CALLPHONETYPE(int f_CALLPHONETYPE) {
        F_CALLPHONETYPE = f_CALLPHONETYPE;
    }

    public String getF_USERID() {
        return F_USERID;
    }

    public void setF_USERID(String f_USERID) {
        F_USERID = f_USERID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNametype() {
        return nametype;
    }

    public void setNametype(String nametype) {
        this.nametype = nametype;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
