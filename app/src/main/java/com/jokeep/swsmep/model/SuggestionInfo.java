package com.jokeep.swsmep.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wbq501 on 2016-3-10 18:34.
 * SWSMEP
 */
public class SuggestionInfo implements Serializable{

    /**
     * F_CONCLUSION : 同意
     * F_HANDLETIME : 2016.03.10 19:04
     * F_NODEID : BDB1006D7FA847C8BF2BCA1E649146E4
     * OPINIONREPLY : [
     {
     "F_OPINIONREPLYID": "B9665A068FAD470282EC766349FFA480",
     "F_OPINIONID": "40D28FDA88ED4C4F812B89B19B0C84C7",
     "F_OPINION": "盛大的飒飒飒飒的",
     "F_USERID": "8D2C041814114790A543EDB103330A97",
     "F_USERNAME": "王宝全",
     "F_REPLYTIME": "2016.03.10 19:04",
     "F_REPLYTIMEFULL": "2016-03-10 19:04:49"
     },
     {
     "F_OPINIONREPLYID": "1EBE52E093F94A6D9B8BF62A824B4FDB",
     "F_OPINIONID": "40D28FDA88ED4C4F812B89B19B0C84C7",
     "F_OPINION": "按时打算打打",
     "F_USERID": "8D2C041814114790A543EDB103330A97",
     "F_USERNAME": "王宝全",
     "F_REPLYTIME": "2016.03.10 19:04",
     "F_REPLYTIMEFULL": "2016-03-10 19:04:53"
     }
     ]
     * F_STATE : 1
     * F_OPINION : 飒飒飒飒撒打算萨达
     * F_OPINIONID : 40D28FDA88ED4C4F812B89B19B0C84C7
     * F_HANDLEID : 1FB7A7067C5D47B996FD99332E90746D
     * F_EXECUTMAINID : 0ADDD65E7D75498D81AFE68F04519CCA
     * ATTACHMENT :
     *    [{
         "F_ATTACHMENTID": "BEFA1A91FC0F4F39AE3B7AEA300C5160",
         "F_DATAID": "40D28FDA88ED4C4F812B89B19B0C84C7",
         "F_FILENAME": "btn_search_normal.png",
         "F_STORAGEPATH": "UploadFiles/CooperativeWork/201603/10/20160310190347_2805.png"
         }
         ]
     * F_HANDLENAME : 蔡涛
     */

    private String F_CONCLUSION;
    private String F_HANDLETIME;
    private String F_NODEID;
    private String OPINIONREPLY;
    private int F_STATE;
    private String F_OPINION;
    private String F_OPINIONID;
    private String F_HANDLEID;
    private String F_EXECUTMAINID;
    private String ATTACHMENT;
    private String F_HANDLENAME;
    private List<SuggestionFilesInfo> suggestionFilesInfos;
    private List<SuggestionsInfo> suggestionsInfos;
    private String F_DEPARTMENTNAME;
    private String F_POSITIONNAME;
    private String F_USERHEADURI;

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

    public String getF_USERHEADURI() {
        return F_USERHEADURI;
    }

    public void setF_USERHEADURI(String f_USERHEADURI) {
        F_USERHEADURI = f_USERHEADURI;
    }

    public List<SuggestionFilesInfo> getSuggestionFilesInfos() {
        return suggestionFilesInfos;
    }

    public void setSuggestionFilesInfos(List<SuggestionFilesInfo> suggestionFilesInfos) {
        this.suggestionFilesInfos = suggestionFilesInfos;
    }

    public List<SuggestionsInfo> getSuggestionsInfos() {
        return suggestionsInfos;
    }

    public void setSuggestionsInfos(List<SuggestionsInfo> suggestionsInfos) {
        this.suggestionsInfos = suggestionsInfos;
    }

    public void setF_CONCLUSION(String F_CONCLUSION) {
        this.F_CONCLUSION = F_CONCLUSION;
    }

    public void setF_HANDLETIME(String F_HANDLETIME) {
        this.F_HANDLETIME = F_HANDLETIME;
    }

    public void setF_NODEID(String F_NODEID) {
        this.F_NODEID = F_NODEID;
    }

    public void setOPINIONREPLY(String OPINIONREPLY) {
        this.OPINIONREPLY = OPINIONREPLY;
    }

    public void setF_STATE(int F_STATE) {
        this.F_STATE = F_STATE;
    }

    public void setF_OPINION(String F_OPINION) {
        this.F_OPINION = F_OPINION;
    }

    public void setF_OPINIONID(String F_OPINIONID) {
        this.F_OPINIONID = F_OPINIONID;
    }

    public void setF_HANDLEID(String F_HANDLEID) {
        this.F_HANDLEID = F_HANDLEID;
    }

    public void setF_EXECUTMAINID(String F_EXECUTMAINID) {
        this.F_EXECUTMAINID = F_EXECUTMAINID;
    }

    public void setATTACHMENT(String ATTACHMENT) {
        this.ATTACHMENT = ATTACHMENT;
    }

    public void setF_HANDLENAME(String F_HANDLENAME) {
        this.F_HANDLENAME = F_HANDLENAME;
    }

    public String getF_CONCLUSION() {
        return F_CONCLUSION;
    }

    public String getF_HANDLETIME() {
        return F_HANDLETIME;
    }

    public String getF_NODEID() {
        return F_NODEID;
    }

    public String getOPINIONREPLY() {
        return OPINIONREPLY;
    }

    public int getF_STATE() {
        return F_STATE;
    }

    public String getF_OPINION() {
        return F_OPINION;
    }

    public String getF_OPINIONID() {
        return F_OPINIONID;
    }

    public String getF_HANDLEID() {
        return F_HANDLEID;
    }

    public String getF_EXECUTMAINID() {
        return F_EXECUTMAINID;
    }

    public String getATTACHMENT() {
        return ATTACHMENT;
    }

    public String getF_HANDLENAME() {
        return F_HANDLENAME;
    }
}
