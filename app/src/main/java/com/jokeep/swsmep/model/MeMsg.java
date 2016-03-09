package com.jokeep.swsmep.model;

import java.io.Serializable;

/**
 * Created by wbq501 on 2016-3-9 15:40.
 * SWSMEP
 */
public class MeMsg implements Serializable{

    /**
     * F_CONTENT : 金牛
     * F_USERID : 8D2C041814114790A543EDB103330A97
     * F_USERPHRASEBOOKID : E56B4ACC41054945978ECD8E1A7C05C0
     */

    private String F_CONTENT;
    private String F_USERID;
    private String F_USERPHRASEBOOKID;
    public boolean checked;
    public boolean type;

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setF_CONTENT(String F_CONTENT) {
        this.F_CONTENT = F_CONTENT;
    }

    public void setF_USERID(String F_USERID) {
        this.F_USERID = F_USERID;
    }

    public void setF_USERPHRASEBOOKID(String F_USERPHRASEBOOKID) {
        this.F_USERPHRASEBOOKID = F_USERPHRASEBOOKID;
    }

    public String getF_CONTENT() {
        return F_CONTENT;
    }

    public String getF_USERID() {
        return F_USERID;
    }

    public String getF_USERPHRASEBOOKID() {
        return F_USERPHRASEBOOKID;
    }
}
