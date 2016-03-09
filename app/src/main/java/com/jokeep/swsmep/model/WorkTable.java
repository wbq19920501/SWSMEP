package com.jokeep.swsmep.model;

import java.io.Serializable;

/**
 * Created by wbq501 on 2016-3-8 09:10.
 * SWSMEP
 */
public class WorkTable implements Serializable{

    /**
     * F_ATTACHMENTID : 6FB27B4F963F442BBBAA5FEE4E2C0C01
     * F_DATAID : 302A79047C2E4419AAD7C4769204469C
     * F_FILENAME : IMAG0024.jpg
     * F_FILETYPE : jpg
     * F_FILESIZE: 1838725,
     * F_STORAGEPATH : UploadFiles/2016/03/07/201603071904364009.jpg
     */

    private String F_ATTACHMENTID;
    private String F_DATAID;
    private String F_FILENAME;
    private String F_FILETYPE;
    private int F_FILESIZE;
    private String F_STORAGEPATH;
    private boolean IsUp;

    public boolean isUp() {
        return IsUp;
    }

    public void setIsUp(boolean isUp) {
        IsUp = isUp;
    }

    public int getF_FILESIZE() {
        return F_FILESIZE;
    }

    public void setF_FILESIZE(int f_FILESIZE) {
        F_FILESIZE = f_FILESIZE;
    }
    public void setF_ATTACHMENTID(String F_ATTACHMENTID) {
        this.F_ATTACHMENTID = F_ATTACHMENTID;
    }

    public void setF_DATAID(String F_DATAID) {
        this.F_DATAID = F_DATAID;
    }

    public void setF_FILENAME(String F_FILENAME) {
        this.F_FILENAME = F_FILENAME;
    }

    public void setF_FILETYPE(String F_FILETYPE) {
        this.F_FILETYPE = F_FILETYPE;
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

    public String getF_FILETYPE() {
        return F_FILETYPE;
    }

    public String getF_STORAGEPATH() {
        return F_STORAGEPATH;
    }
}
