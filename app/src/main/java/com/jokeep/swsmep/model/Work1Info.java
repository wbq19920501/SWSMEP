package com.jokeep.swsmep.model;

import java.io.Serializable;

/**
 * Created by wbq501 on 2016-2-26 11:39.
 * SWSMEP
 */
public class Work1Info implements Serializable{

    /**
     * F_BUSINESSCODE : 000003
     * F_DATAGUID : 844708F9EECE4FDFB33942E627B1FB3C   协同id
     * F_EXECUTMAINID : DF75CB6E1D634BB280C0F462A3FF97E4
     * F_ISATT : 0  0无附件 1有附件
     * F_JOINTTYPE : 0
     * F_LINKURL : Office/CooperativeWork/Cooperative_ToDo_Manage.aspx?ExecutMainID=DF75CB6E1D634BB280C0F462A3FF97E4&NodeID=69E6A889D90949C18827F0C29AE133A2&NodeHandlerID=5FAA5F0CC6FA4FD39C6B242423DB6D1C&OriginalID=8D2C041814114790A543EDB103330A97&DataGuid=844708F9EECE4FDFB33942E627B1FB3C&ToDoID=F2454732E2A5452EBE18CD37776F96C6
     * F_PAGE_XH : 1
     * F_SENDDATE : 2016-02-26 11:31:29
     * F_SPONSTIME : 今日11:31
     * F_SPONSUSER : wbq
     * F_STATE : 0
     * F_TITLE : 测试11111111111
     * F_TODOID : F2454732E2A5452EBE18CD37776F96C6
     * F_TODOUSERID : 8D2C041814114790A543EDB103330A97
     */

    private String F_BUSINESSCODE;
    private String F_DATAGUID;
    private String F_EXECUTMAINID;
    private int F_ISATT;
    private int F_JOINTTYPE;
    private String F_LINKURL;
    private int F_PAGE_XH;
    private String F_SENDDATE;
    private String F_SPONSTIME;
    private String F_SPONSUSER;
    private int F_STATE;
    private String F_TITLE;
    private String F_TODOID;
    private String F_TODOUSERID;
    private int type;// 1 判读显示状态
    /**
     * F_CREATEUSERID : 8D2C041814114790A543EDB103330A97
     * F_FILEPATH :
     * F_JOINTID : 6D818E25AA35432A8FD5AAB47CBFA2D4
     * F_SORTDATE : 2016-02-26 14:55:44
     * F_STATENAME : 待发送
     */

    private String F_CREATEUSERID;
    private String F_FILEPATH;
    private String F_JOINTID;
    private String F_SORTDATE;
    private String F_STATENAME;

    private String F_COLOR;

    private int F_isView;

    public int getF_isView() {
        return F_isView;
    }

    public void setF_isView(int f_isView) {
        F_isView = f_isView;
    }

    public String getF_COLOR() {
        return F_COLOR;
    }

    public void setF_COLOR(String f_COLOR) {
        F_COLOR = f_COLOR;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    private int typename;

    public int getTypename() {
        return typename;
    }

    public void setTypename(int typename) {
        this.typename = typename;
    }

    public void setF_BUSINESSCODE(String F_BUSINESSCODE) {
        this.F_BUSINESSCODE = F_BUSINESSCODE;
    }

    public void setF_DATAGUID(String F_DATAGUID) {
        this.F_DATAGUID = F_DATAGUID;
    }

    public void setF_EXECUTMAINID(String F_EXECUTMAINID) {
        this.F_EXECUTMAINID = F_EXECUTMAINID;
    }

    public void setF_ISATT(int F_ISATT) {
        this.F_ISATT = F_ISATT;
    }

    public void setF_JOINTTYPE(int F_JOINTTYPE) {
        this.F_JOINTTYPE = F_JOINTTYPE;
    }

    public void setF_LINKURL(String F_LINKURL) {
        this.F_LINKURL = F_LINKURL;
    }

    public void setF_PAGE_XH(int F_PAGE_XH) {
        this.F_PAGE_XH = F_PAGE_XH;
    }

    public void setF_SENDDATE(String F_SENDDATE) {
        this.F_SENDDATE = F_SENDDATE;
    }

    public void setF_SPONSTIME(String F_SPONSTIME) {
        this.F_SPONSTIME = F_SPONSTIME;
    }

    public void setF_SPONSUSER(String F_SPONSUSER) {
        this.F_SPONSUSER = F_SPONSUSER;
    }

    public void setF_STATE(int F_STATE) {
        this.F_STATE = F_STATE;
    }

    public void setF_TITLE(String F_TITLE) {
        this.F_TITLE = F_TITLE;
    }

    public void setF_TODOID(String F_TODOID) {
        this.F_TODOID = F_TODOID;
    }

    public void setF_TODOUSERID(String F_TODOUSERID) {
        this.F_TODOUSERID = F_TODOUSERID;
    }

    public String getF_BUSINESSCODE() {
        return F_BUSINESSCODE;
    }

    public String getF_DATAGUID() {
        return F_DATAGUID;
    }

    public String getF_EXECUTMAINID() {
        return F_EXECUTMAINID;
    }

    public int getF_ISATT() {
        return F_ISATT;
    }

    public int getF_JOINTTYPE() {
        return F_JOINTTYPE;
    }

    public String getF_LINKURL() {
        return F_LINKURL;
    }

    public int getF_PAGE_XH() {
        return F_PAGE_XH;
    }

    public String getF_SENDDATE() {
        return F_SENDDATE;
    }

    public String getF_SPONSTIME() {
        return F_SPONSTIME;
    }

    public String getF_SPONSUSER() {
        return F_SPONSUSER;
    }

    public int getF_STATE() {
        return F_STATE;
    }

    public String getF_TITLE() {
        return F_TITLE;
    }

    public String getF_TODOID() {
        return F_TODOID;
    }

    public String getF_TODOUSERID() {
        return F_TODOUSERID;
    }

    public void setF_CREATEUSERID(String F_CREATEUSERID) {
        this.F_CREATEUSERID = F_CREATEUSERID;
    }

    public void setF_FILEPATH(String F_FILEPATH) {
        this.F_FILEPATH = F_FILEPATH;
    }

    public void setF_JOINTID(String F_JOINTID) {
        this.F_JOINTID = F_JOINTID;
    }

    public void setF_SORTDATE(String F_SORTDATE) {
        this.F_SORTDATE = F_SORTDATE;
    }

    public void setF_STATENAME(String F_STATENAME) {
        this.F_STATENAME = F_STATENAME;
    }

    public String getF_CREATEUSERID() {
        return F_CREATEUSERID;
    }

    public String getF_FILEPATH() {
        return F_FILEPATH;
    }

    public String getF_JOINTID() {
        return F_JOINTID;
    }

    public String getF_SORTDATE() {
        return F_SORTDATE;
    }

    public String getF_STATENAME() {
        return F_STATENAME;
    }
}
