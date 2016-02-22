package com.jokeep.swsmep.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by wbq501 on 2016-2-17 17:28.
 * SWSMEP
 */
@Table(name = "info")
public class UserBook implements Serializable{
    /**
     * F_POSITIONNAME : 处长
     * F_CALLPHONETYPE : 0
     * F_USERID : 4DFE1ADD318B4AC392CFF5811F58BDB4
     * F_CALLPHONE : 65050051
     * F_USERNAME : 白平
     * F_DEPARTMENTNAME : 督查四处
     */

    public static final String ID = "_id";
    public static final String PHONENAME = "F_POSITIONNAME";
    public static final String USERTYPE = "F_CALLPHONETYPE";
    public static final String USERID = "F_USERID";
    public static final String CALLPHONE = "F_CALLPHONE";
    public static final String USERNAME = "F_USERNAME";
    public static final String USERTYPENAME = "F_DEPARTMENTNAME";

    @Column(name = "id",isId = true ,autoGen = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Column(name = "F_POSITIONNAME")
    private String F_POSITIONNAME;
    @Column(name = "F_CALLPHONETYPE")
    private int F_CALLPHONETYPE;
    @Column(name = "F_USERID")
    private String F_USERID;
    @Column(name = "F_CALLPHONE")
    private String F_CALLPHONE;
    @Column(name = "F_USERNAME")
    private String F_USERNAME;
    @Column(name = "F_DEPARTMENTNAME")
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

    @Override
    public String toString() {
        return "UserBook{" +
                "id=" + id +
                ", F_POSITIONNAME='" + F_POSITIONNAME + '\'' +
                ", F_CALLPHONETYPE=" + F_CALLPHONETYPE +
                ", F_USERID='" + F_USERID + '\'' +
                ", F_CALLPHONE='" + F_CALLPHONE + '\'' +
                ", F_USERNAME='" + F_USERNAME + '\'' +
                ", F_DEPARTMENTNAME='" + F_DEPARTMENTNAME + '\'' +
                '}';
    }
}
