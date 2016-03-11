package com.jokeep.swsmep.model;

import java.io.Serializable;

/**
 * Created by wbq501 on 2016-3-11 09:25.
 * SWSMEP
 */
public class SuggestionsInfo implements Serializable{

    /**
     * F_OPINIONREPLYID : B9665A068FAD470282EC766349FFA480
     * F_OPINIONID : 40D28FDA88ED4C4F812B89B19B0C84C7
     * F_OPINION : 盛大的飒飒飒飒的
     * F_USERID : 8D2C041814114790A543EDB103330A97
     * F_USERNAME : 王宝全
     * F_REPLYTIME : 2016.03.10 19:04
     * F_REPLYTIMEFULL : 2016-03-10 19:04:49
     */

    private String F_OPINIONREPLYID;
    private String F_OPINIONID;
    private String F_OPINION;
    private String F_USERID;
    private String F_USERNAME;
    private String F_REPLYTIME;
    private String F_REPLYTIMEFULL;
    private String F_DEPARTMENTNAME;
    private String F_POSITIONNAME;

    public String getF_DEPARTMENTNAME() {
        return F_DEPARTMENTNAME;
    }

    public void setF_DEPARTMENTNAME(String f_DEPARTMENTNAME) {
        F_DEPARTMENTNAME = f_DEPARTMENTNAME;
    }

    public String getF_POSITIONNAME() {
        return F_POSITIONNAME;
    }

    public void setF_POSITIONNAME(String f_POSITIONNAME) {
        F_POSITIONNAME = f_POSITIONNAME;
    }

    public void setF_OPINIONREPLYID(String F_OPINIONREPLYID) {
        this.F_OPINIONREPLYID = F_OPINIONREPLYID;
    }

    public void setF_OPINIONID(String F_OPINIONID) {
        this.F_OPINIONID = F_OPINIONID;
    }

    public void setF_OPINION(String F_OPINION) {
        this.F_OPINION = F_OPINION;
    }

    public void setF_USERID(String F_USERID) {
        this.F_USERID = F_USERID;
    }

    public void setF_USERNAME(String F_USERNAME) {
        this.F_USERNAME = F_USERNAME;
    }

    public void setF_REPLYTIME(String F_REPLYTIME) {
        this.F_REPLYTIME = F_REPLYTIME;
    }

    public void setF_REPLYTIMEFULL(String F_REPLYTIMEFULL) {
        this.F_REPLYTIMEFULL = F_REPLYTIMEFULL;
    }

    public String getF_OPINIONREPLYID() {
        return F_OPINIONREPLYID;
    }

    public String getF_OPINIONID() {
        return F_OPINIONID;
    }

    public String getF_OPINION() {
        return F_OPINION;
    }

    public String getF_USERID() {
        return F_USERID;
    }

    public String getF_USERNAME() {
        return F_USERNAME;
    }

    public String getF_REPLYTIME() {
        return F_REPLYTIME;
    }

    public String getF_REPLYTIMEFULL() {
        return F_REPLYTIMEFULL;
    }
}
