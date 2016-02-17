package com.jokeep.swsmep.model;

import java.io.Serializable;

/**
 * Created by wbq501 on 2016-2-17 17:28.
 * SWSMEP
 */
public class UserBook implements Serializable{
    /**
     * F_POSITIONNAME : 处长
     * F_CALLPHONETYPE : 0
     * F_USERID : 4DFE1ADD318B4AC392CFF5811F58BDB4
     * F_CALLPHONE : 65050051
     * F_USERNAME : 白平
     * F_DEPARTMENTNAME : 督查四处
     */

    private String F_POSITIONNAME;
    private int F_CALLPHONETYPE;
    private String F_USERID;
    private String F_CALLPHONE;
    private String F_USERNAME;
    private String F_DEPARTMENTNAME;

    public void setF_POSITIONNAME(String F_POSITIONNAME) {
        this.F_POSITIONNAME = F_POSITIONNAME;
    }

    public void setF_CALLPHONETYPE(int F_CALLPHONETYPE) {
        this.F_CALLPHONETYPE = F_CALLPHONETYPE;
    }

    public void setF_USERID(String F_USERID) {
        this.F_USERID = F_USERID;
    }

    public void setF_CALLPHONE(String F_CALLPHONE) {
        this.F_CALLPHONE = F_CALLPHONE;
    }

    public void setF_USERNAME(String F_USERNAME) {
        this.F_USERNAME = F_USERNAME;
    }

    public void setF_DEPARTMENTNAME(String F_DEPARTMENTNAME) {
        this.F_DEPARTMENTNAME = F_DEPARTMENTNAME;
    }

    public String getF_POSITIONNAME() {
        return F_POSITIONNAME;
    }

    public int getF_CALLPHONETYPE() {
        return F_CALLPHONETYPE;
    }

    public String getF_USERID() {
        return F_USERID;
    }

    public String getF_CALLPHONE() {
        return F_CALLPHONE;
    }

    public String getF_USERNAME() {
        return F_USERNAME;
    }

    public String getF_DEPARTMENTNAME() {
        return F_DEPARTMENTNAME;
    }
}
