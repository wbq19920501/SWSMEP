package com.jokeep.swsmep.base;

/**
 * Created by wbq501 on 2016-1-7 11:31.
 * SWSMEP
 */
public class HttpIP {
//    public static String Base = "171.221.173.66:20083/SWSMEP/";
//    public static String Base = "http://192.168.2.109/SWSMEP/";
    public static String Base = "http://192.168.1.212/SWSMEP/";
    public static String IP = Base+"SSO/Service.asmx";
    public static String MainService = Base+"AndroidService/MainService.svc";
    // 获取加密key,同步tokenId
    public static final String ACTION_KEY = "getEncryptKey";
    public static final String Login = "AndroidLoginCheck";
    public static final String ResetPassword = "/ResetPassword";
    public static final String UpImage = Base+"Web/AndroidUpLoadFileService.svc/UpdatePhoto";
    public static final String PhoneMan = "/AddressBook_Filter";
}
