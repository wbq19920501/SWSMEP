package com.jokeep.swsmep.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by wbq501 on 2016-3-1 17:44.
 * SWSMEP
 */
@Table(name = "work2info")
public class Work2Info implements Serializable{

    /**
     * F_COMMONGROUPID : F5AABBA0DB6644D9984933DD53550168
     * F_COMMONGROUPUSERID : 8D2C041814114790A543EDB103330A97
     * F_DEPARTMENTNAME : 中心领导部门
     * F_DEPARTORDER : 0.0
     * F_MAINDEPARTID : C08C55FA2345489C9409F73D57B8C4B4
     * F_MAINPOSITIONID : A4DF68D8BB854151B058E6D3F1A897CF
     * F_MAINUNITID : DW001
     * F_POSITIONNAME : 主任
     * F_POSITIONORDER : 2.0
     * F_UNITFULLNAME : 环境保护部西南环境保护督察中心
     * F_UNITORDER : 1.0
     * F_UNITSORTNAME : 西南督察中心
     * F_USERID : 3EB6EA74B17F4DBC8D40FA3F0049AE91
     * F_USERNAME : 张迅
     * F_USERORDER : 999.0
     */
    @Column(name = "id",isId = true ,autoGen = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Column(name = "F_COMMONGROUPID")
    private String F_COMMONGROUPID;
    @Column(name = "F_COMMONGROUPUSERID")
    private String F_COMMONGROUPUSERID;
    @Column(name = "F_DEPARTMENTNAME")
    private String F_DEPARTMENTNAME;
    @Column(name = "F_DEPARTORDER")
    private double F_DEPARTORDER;
    @Column(name = "F_MAINDEPARTID")
    private String F_MAINDEPARTID;
    @Column(name = "F_MAINPOSITIONID")
    private String F_MAINPOSITIONID;
    @Column(name = "F_MAINUNITID")
    private String F_MAINUNITID;
    @Column(name = "F_POSITIONNAME")
    private String F_POSITIONNAME;
    @Column(name = "F_POSITIONORDER")
    private double F_POSITIONORDER;
    @Column(name = "F_UNITFULLNAME")
    private String F_UNITFULLNAME;
    @Column(name = "F_UNITORDER")
    private double F_UNITORDER;
    @Column(name = "F_UNITSORTNAME")
    private String F_UNITSORTNAME;
    @Column(name = "F_USERID")
    private String F_USERID;
    @Column(name = "F_USERNAME")
    private String F_USERNAME;
    @Column(name = "F_USERORDER")
    private double F_USERORDER;

    public void setF_COMMONGROUPID(String F_COMMONGROUPID) {
        this.F_COMMONGROUPID = F_COMMONGROUPID;
    }

    public void setF_COMMONGROUPUSERID(String F_COMMONGROUPUSERID) {
        this.F_COMMONGROUPUSERID = F_COMMONGROUPUSERID;
    }

    public void setF_DEPARTMENTNAME(String F_DEPARTMENTNAME) {
        this.F_DEPARTMENTNAME = F_DEPARTMENTNAME;
    }

    public void setF_DEPARTORDER(double F_DEPARTORDER) {
        this.F_DEPARTORDER = F_DEPARTORDER;
    }

    public void setF_MAINDEPARTID(String F_MAINDEPARTID) {
        this.F_MAINDEPARTID = F_MAINDEPARTID;
    }

    public void setF_MAINPOSITIONID(String F_MAINPOSITIONID) {
        this.F_MAINPOSITIONID = F_MAINPOSITIONID;
    }

    public void setF_MAINUNITID(String F_MAINUNITID) {
        this.F_MAINUNITID = F_MAINUNITID;
    }

    public void setF_POSITIONNAME(String F_POSITIONNAME) {
        this.F_POSITIONNAME = F_POSITIONNAME;
    }

    public void setF_POSITIONORDER(double F_POSITIONORDER) {
        this.F_POSITIONORDER = F_POSITIONORDER;
    }

    public void setF_UNITFULLNAME(String F_UNITFULLNAME) {
        this.F_UNITFULLNAME = F_UNITFULLNAME;
    }

    public void setF_UNITORDER(double F_UNITORDER) {
        this.F_UNITORDER = F_UNITORDER;
    }

    public void setF_UNITSORTNAME(String F_UNITSORTNAME) {
        this.F_UNITSORTNAME = F_UNITSORTNAME;
    }

    public void setF_USERID(String F_USERID) {
        this.F_USERID = F_USERID;
    }

    public void setF_USERNAME(String F_USERNAME) {
        this.F_USERNAME = F_USERNAME;
    }

    public void setF_USERORDER(double F_USERORDER) {
        this.F_USERORDER = F_USERORDER;
    }

    public String getF_COMMONGROUPID() {
        return F_COMMONGROUPID;
    }

    public String getF_COMMONGROUPUSERID() {
        return F_COMMONGROUPUSERID;
    }

    public String getF_DEPARTMENTNAME() {
        return F_DEPARTMENTNAME;
    }

    public double getF_DEPARTORDER() {
        return F_DEPARTORDER;
    }

    public String getF_MAINDEPARTID() {
        return F_MAINDEPARTID;
    }

    public String getF_MAINPOSITIONID() {
        return F_MAINPOSITIONID;
    }

    public String getF_MAINUNITID() {
        return F_MAINUNITID;
    }

    public String getF_POSITIONNAME() {
        return F_POSITIONNAME;
    }

    public double getF_POSITIONORDER() {
        return F_POSITIONORDER;
    }

    public String getF_UNITFULLNAME() {
        return F_UNITFULLNAME;
    }

    public double getF_UNITORDER() {
        return F_UNITORDER;
    }

    public String getF_UNITSORTNAME() {
        return F_UNITSORTNAME;
    }

    public String getF_USERID() {
        return F_USERID;
    }

    public String getF_USERNAME() {
        return F_USERNAME;
    }

    public double getF_USERORDER() {
        return F_USERORDER;
    }
}
