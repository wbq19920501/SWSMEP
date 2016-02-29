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
    private  String FUSERID;

    public String getFUSERID() {
        return FUSERID;
    }

    public void setFUSERID(String FUSERID) {
        this.FUSERID = FUSERID;
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
