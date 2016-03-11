package com.jokeep.swsmep.model;

import java.io.Serializable;

/**
 * Created by wbq501 on 2016-3-11 09:26.
 * SWSMEP
 */
public class SuggestionFilesInfo implements Serializable{

    /**
     * F_ATTACHMENTID : BEFA1A91FC0F4F39AE3B7AEA300C5160
     * F_DATAID : 40D28FDA88ED4C4F812B89B19B0C84C7
     * F_FILENAME : btn_search_normal.png
     * F_STORAGEPATH : UploadFiles/CooperativeWork/201603/10/20160310190347_2805.png
     */

    private String F_ATTACHMENTID;
    private String F_DATAID;
    private String F_FILENAME;
    private String F_STORAGEPATH;

    public void setF_ATTACHMENTID(String F_ATTACHMENTID) {
        this.F_ATTACHMENTID = F_ATTACHMENTID;
    }

    public void setF_DATAID(String F_DATAID) {
        this.F_DATAID = F_DATAID;
    }

    public void setF_FILENAME(String F_FILENAME) {
        this.F_FILENAME = F_FILENAME;
    }

    public void setF_STORAGEPATH(String F_STORAGEPATH) {
        this.F_STORAGEPATH = F_STORAGEPATH;
    }

    public String getF_ATTACHMENTID() {
        return F_ATTACHMENTID;
    }

    public String getF_DATAID() {
        return F_DATAID;
    }

    public String getF_FILENAME() {
        return F_FILENAME;
    }

    public String getF_STORAGEPATH() {
        return F_STORAGEPATH;
    }
}
