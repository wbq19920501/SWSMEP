package com.jokeep.swsmep.model;

import java.io.Serializable;

/**
 * Created by wbq501 on 2016-3-3 10:28.
 * SWSMEP
 */
public class FileMsg implements Serializable{
    private String FileName;
    private String FilePath;
    private String FileType;
    private int FileSize;

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public String getFileType() {
        return FileType;
    }

    public void setFileType(String fileType) {
        FileType = fileType;
    }

    public int getFileSize() {
        return FileSize;
    }

    public void setFileSize(int fileSize) {
        FileSize = fileSize;
    }
}
