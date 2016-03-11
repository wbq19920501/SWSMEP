package com.jokeep.swsmep.model;

import java.io.Serializable;

/**
 * Created by androidOne on 2016/3/10.
 */
public class WorkNumber implements Serializable{

    /**
     * F_TODOCOUNT : 1
     * F_MENUCODE : 0001
     */

    private int F_TODOCOUNT;
    private String F_MENUCODE;

    public void setF_TODOCOUNT(int F_TODOCOUNT) {
        this.F_TODOCOUNT = F_TODOCOUNT;
    }

    public void setF_MENUCODE(String F_MENUCODE) {
        this.F_MENUCODE = F_MENUCODE;
    }

    public int getF_TODOCOUNT() {
        return F_TODOCOUNT;
    }

    public String getF_MENUCODE() {
        return F_MENUCODE;
    }
}
