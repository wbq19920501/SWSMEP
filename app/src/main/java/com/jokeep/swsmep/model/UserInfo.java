package com.jokeep.swsmep.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wbq501 on 2016-1-27 11:19.
 * SWSMEP
 */
public class UserInfo implements Serializable{

    /**
     * ErrorCode : 0
     * ErrorMsg :
     * VersionMsg :
     * Result : {"UserInfo":[{"F_USERID":"0","F_USERNAME":"系统管理员","F_MAINDEPARTID":"C08C55FA2345489C9409F73D57B8C4B4","F_MAINUNITID":"DW001","F_POSITIONNAME":"处长","F_DEPARTMENTNAME":"中心领导","F_USERHEADURI":"http://192.168.1.212/SWSMEP/Web/UploadFiles/Platform/Users/2015/06/15/20150615004516823.png","F_INTERVAL":"60","F_PSDISPASSTIME":1}],"ToDoCount":[],"SystemSMS":[]}
     */

    private int ErrorCode;
    private String ErrorMsg;
    private String VersionMsg;
    private ResultEntity Result;

    public void setErrorCode(int ErrorCode) {
        this.ErrorCode = ErrorCode;
    }

    public void setErrorMsg(String ErrorMsg) {
        this.ErrorMsg = ErrorMsg;
    }

    public void setVersionMsg(String VersionMsg) {
        this.VersionMsg = VersionMsg;
    }

    public void setResult(ResultEntity Result) {
        this.Result = Result;
    }

    public int getErrorCode() {
        return ErrorCode;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public String getVersionMsg() {
        return VersionMsg;
    }

    public ResultEntity getResult() {
        return Result;
    }

    public static class ResultEntity {
        /**
         * F_USERID : 0
         * F_USERNAME : 系统管理员
         * F_MAINDEPARTID : C08C55FA2345489C9409F73D57B8C4B4
         * F_MAINUNITID : DW001
         * F_POSITIONNAME : 处长
         * F_DEPARTMENTNAME : 中心领导
         * F_USERHEADURI : http://192.168.1.212/SWSMEP/Web/UploadFiles/Platform/Users/2015/06/15/20150615004516823.png
         * F_INTERVAL : 60
         * F_PSDISPASSTIME : 1
         */

        private List<UserInfoEntity> UserInfo;
        private List<?> ToDoCount;
        private List<?> SystemSMS;

        public void setUserInfo(List<UserInfoEntity> UserInfo) {
            this.UserInfo = UserInfo;
        }

        public void setToDoCount(List<?> ToDoCount) {
            this.ToDoCount = ToDoCount;
        }

        public void setSystemSMS(List<?> SystemSMS) {
            this.SystemSMS = SystemSMS;
        }

        public List<UserInfoEntity> getUserInfo() {
            return UserInfo;
        }

        public List<?> getToDoCount() {
            return ToDoCount;
        }

        public List<?> getSystemSMS() {
            return SystemSMS;
        }

        public static class UserInfoEntity implements Serializable{
            private String F_USERID;
            private String F_USERNAME;
            private String F_MAINDEPARTID;
            private String F_MAINUNITID;
            private String F_POSITIONNAME;
            private String F_DEPARTMENTNAME;
            private String F_USERHEADURI;
            private String F_INTERVAL;
            private int F_PSDISPASSTIME;

            public void setF_USERID(String F_USERID) {
                this.F_USERID = F_USERID;
            }

            public void setF_USERNAME(String F_USERNAME) {
                this.F_USERNAME = F_USERNAME;
            }

            public void setF_MAINDEPARTID(String F_MAINDEPARTID) {
                this.F_MAINDEPARTID = F_MAINDEPARTID;
            }

            public void setF_MAINUNITID(String F_MAINUNITID) {
                this.F_MAINUNITID = F_MAINUNITID;
            }

            public void setF_POSITIONNAME(String F_POSITIONNAME) {
                this.F_POSITIONNAME = F_POSITIONNAME;
            }

            public void setF_DEPARTMENTNAME(String F_DEPARTMENTNAME) {
                this.F_DEPARTMENTNAME = F_DEPARTMENTNAME;
            }

            public void setF_USERHEADURI(String F_USERHEADURI) {
                this.F_USERHEADURI = F_USERHEADURI;
            }

            public void setF_INTERVAL(String F_INTERVAL) {
                this.F_INTERVAL = F_INTERVAL;
            }

            public void setF_PSDISPASSTIME(int F_PSDISPASSTIME) {
                this.F_PSDISPASSTIME = F_PSDISPASSTIME;
            }

            public String getF_USERID() {
                return F_USERID;
            }

            public String getF_USERNAME() {
                return F_USERNAME;
            }

            public String getF_MAINDEPARTID() {
                return F_MAINDEPARTID;
            }

            public String getF_MAINUNITID() {
                return F_MAINUNITID;
            }

            public String getF_POSITIONNAME() {
                return F_POSITIONNAME;
            }

            public String getF_DEPARTMENTNAME() {
                return F_DEPARTMENTNAME;
            }

            public String getF_USERHEADURI() {
                return F_USERHEADURI;
            }

            public String getF_INTERVAL() {
                return F_INTERVAL;
            }

            public int getF_PSDISPASSTIME() {
                return F_PSDISPASSTIME;
            }
        }
    }
}
