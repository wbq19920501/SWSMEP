package com.jokeep.swsmep.model;

import java.io.Serializable;

/**
 * Created by wbq501 on 2016-2-17 17:26.
 * SWSMEP
 */
public class UnitInfo implements Serializable{
    /**
     * F_UNITNAME : 西南督察中心
     * F_UNITTYPE : 0
     * F_ORDERLEVEL : 0
     * F_OTHERUNITID : 0001
     */
    private String F_UNITNAME;
    private int F_UNITTYPE;
    private int F_ORDERLEVEL;
    private String F_OTHERUNITID;

    public void setF_UNITNAME(String F_UNITNAME) {
        this.F_UNITNAME = F_UNITNAME;
    }

    public void setF_UNITTYPE(int F_UNITTYPE) {
        this.F_UNITTYPE = F_UNITTYPE;
    }

    public void setF_ORDERLEVEL(int F_ORDERLEVEL) {
        this.F_ORDERLEVEL = F_ORDERLEVEL;
    }

    public void setF_OTHERUNITID(String F_OTHERUNITID) {
        this.F_OTHERUNITID = F_OTHERUNITID;
    }

    public String getF_UNITNAME() {
        return F_UNITNAME;
    }

    public int getF_UNITTYPE() {
        return F_UNITTYPE;
    }

    public int getF_ORDERLEVEL() {
        return F_ORDERLEVEL;
    }

    public String getF_OTHERUNITID() {
        return F_OTHERUNITID;
    }
}
