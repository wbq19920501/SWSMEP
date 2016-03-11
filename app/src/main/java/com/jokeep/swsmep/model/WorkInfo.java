package com.jokeep.swsmep.model;

import java.io.Serializable;

/**
 * Created by androidOne on 2016/3/10.
 */
public class WorkInfo implements Serializable{

    /**
     * F_ISATT : 0
     * F_TODOCOUNT : 1
     * F_TITLE : 123
     * F_SPONSUSER : 罗杨
     * F_BUSINESSCODE : 000001
     * F_SORTNAME : 2016.03.08
     * F_DATAGUID : 374171198FD54B648348C63C8A5F3FE7
     * F_CORLOR : #F75D8C
     */

    private int F_ISATT;
    private int F_TODOCOUNT;
    private String F_TITLE;
    private String F_SPONSUSER;
    private String F_BUSINESSCODE;
    private String F_SORTNAME;
    private String F_DATAGUID;
    private String F_CORLOR;

    public void setF_ISATT(int F_ISATT) {
        this.F_ISATT = F_ISATT;
    }

    public void setF_TODOCOUNT(int F_TODOCOUNT) {
        this.F_TODOCOUNT = F_TODOCOUNT;
    }

    public void setF_TITLE(String F_TITLE) {
        this.F_TITLE = F_TITLE;
    }

    public void setF_SPONSUSER(String F_SPONSUSER) {
        this.F_SPONSUSER = F_SPONSUSER;
    }

    public void setF_BUSINESSCODE(String F_BUSINESSCODE) {
        this.F_BUSINESSCODE = F_BUSINESSCODE;
    }

    public void setF_SORTNAME(String F_SORTNAME) {
        this.F_SORTNAME = F_SORTNAME;
    }

    public void setF_DATAGUID(String F_DATAGUID) {
        this.F_DATAGUID = F_DATAGUID;
    }

    public void setF_CORLOR(String F_CORLOR) {
        this.F_CORLOR = F_CORLOR;
    }

    public int getF_ISATT() {
        return F_ISATT;
    }

    public int getF_TODOCOUNT() {
        return F_TODOCOUNT;
    }

    public String getF_TITLE() {
        return F_TITLE;
    }

    public String getF_SPONSUSER() {
        return F_SPONSUSER;
    }

    public String getF_BUSINESSCODE() {
        return F_BUSINESSCODE;
    }

    public String getF_SORTNAME() {
        return F_SORTNAME;
    }

    public String getF_DATAGUID() {
        return F_DATAGUID;
    }

    public String getF_CORLOR() {
        return F_CORLOR;
    }
}
