package com.jokeep.swsmep.base;

import android.app.Application;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * Created by wbq501 on 2016-1-7 11:22.
 * SWSMEP
 */
public class SwsApplication extends Application{
    private String CER_12306 = "-----BEGIN CERTIFICATE-----\n" +
            "MIICmjCCAgOgAwIBAgIIbyZr5/jKH6QwDQYJKoZIhvcNAQEFBQAwRzELMAkGA1UEBhMCQ04xKTAn\n" +
            "BgNVBAoTIFNpbm9yYWlsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MQ0wCwYDVQQDEwRTUkNBMB4X\n" +
            "DTA5MDUyNTA2NTYwMFoXDTI5MDUyMDA2NTYwMFowRzELMAkGA1UEBhMCQ04xKTAnBgNVBAoTIFNp\n" +
            "bm9yYWlsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MQ0wCwYDVQQDEwRTUkNBMIGfMA0GCSqGSIb3\n" +
            "DQEBAQUAA4GNADCBiQKBgQDMpbNeb34p0GvLkZ6t72/OOba4mX2K/eZRWFfnuk8e5jKDH+9BgCb2\n" +
            "9bSotqPqTbxXWPxIOz8EjyUO3bfR5pQ8ovNTOlks2rS5BdMhoi4sUjCKi5ELiqtyww/XgY5iFqv6\n" +
            "D4Pw9QvOUcdRVSbPWo1DwMmH75It6pk/rARIFHEjWwIDAQABo4GOMIGLMB8GA1UdIwQYMBaAFHle\n" +
            "tne34lKDQ+3HUYhMY4UsAENYMAwGA1UdEwQFMAMBAf8wLgYDVR0fBCcwJTAjoCGgH4YdaHR0cDov\n" +
            "LzE5Mi4xNjguOS4xNDkvY3JsMS5jcmwwCwYDVR0PBAQDAgH+MB0GA1UdDgQWBBR5XrZ3t+JSg0Pt\n" +
            "x1GITGOFLABDWDANBgkqhkiG9w0BAQUFAAOBgQDGrAm2U/of1LbOnG2bnnQtgcVaBXiVJF8LKPaV\n" +
            "23XQ96HU8xfgSZMJS6U00WHAI7zp0q208RSUft9wDq9ee///VOhzR6Tebg9QfyPSohkBrhXQenvQ\n" +
            "og555S+C3eJAAVeNCTeMS3N/M5hzBRJAoffn3qoYdAO1Q8bTguOi+2849A==\n" +
            "-----END CERTIFICATE-----";
    private DbManager.DaoConfig daoConfig;
    public DbManager.DaoConfig getDaoConfig(){
        return daoConfig;
    };
    private String FUSERID;//登录人id
    private String F_USERNAME;//登录人名称
    private String F_MAINDEPARTID;//登录人部门id
    private String F_MAINUNITID;//登录人单位id
    private String F_POSITIONNAME;//登录人岗位名称
    private String F_DEPARTMENTNAME;//登录人部门名称

    public String getF_DEPARTMENTNAME() {
        return F_DEPARTMENTNAME;
    }

    public void setF_DEPARTMENTNAME(String f_DEPARTMENTNAME) {
        F_DEPARTMENTNAME = f_DEPARTMENTNAME;
    }

    public String getF_USERNAME() {
        return F_USERNAME;
    }

    public void setF_USERNAME(String f_USERNAME) {
        F_USERNAME = f_USERNAME;
    }

    public String getFUSERID() {
        return FUSERID;
    }

    public void setFUSERID(String FUSERID) {
        this.FUSERID = FUSERID;
    }

    public String getF_MAINDEPARTID() {
        return F_MAINDEPARTID;
    }

    public void setF_MAINDEPARTID(String f_MAINDEPARTID) {
        F_MAINDEPARTID = f_MAINDEPARTID;
    }

    public String getF_MAINUNITID() {
        return F_MAINUNITID;
    }

    public void setF_MAINUNITID(String f_MAINUNITID) {
        F_MAINUNITID = f_MAINUNITID;
    }

    public String getF_POSITIONNAME() {
        return F_POSITIONNAME;
    }

    public void setF_POSITIONNAME(String f_POSITIONNAME) {
        F_POSITIONNAME = f_POSITIONNAME;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
//        x.Ext.setDebug(true);
//        OkHttpUtils.getInstance().setCertificates(new InputStream[]{
//                new Buffer()
//                        .writeUtf8(CER_12306)
//                        .inputStream()});
//        OkHttpUtils.getInstance().debug("testDebug").setConnectTimeout(100000, TimeUnit.MILLISECONDS);
//        daoConfig = new DbManager.DaoConfig()
//                    .setDbName("swsmep")
//                    .setDbVersion(1)
//                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
//                        @Override
//                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
//
//                        }
//                    });
    }
}
