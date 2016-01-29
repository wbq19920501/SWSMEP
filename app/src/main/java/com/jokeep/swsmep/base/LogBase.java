package com.jokeep.swsmep.base;

import android.util.Log;

/**
 * Created by wbq501 on 2016-1-7 09:52.
 * SWSMEP
 */
public class LogBase {
    /**
     * 是否开启日志
     */
    private static boolean logmsg = true;
    /**
     * 错误
     * @param clazz
     * @param msg
     */
    public static void e(Class<?> clazz,String msg){
        if(logmsg){
            Log.e(clazz.getSimpleName(), msg + "");
        }
    }
    /**
     * 信息
     * @param clazz
     * @param msg
     */
    public static void i(Class<?> clazz,String msg){
        if(logmsg){
            Log.i(clazz.getSimpleName(), msg+"");
        }
    }
    /**
     * 警告
     * @param clazz
     * @param msg
     */
    public static void w(Class<?> clazz,String msg){
        if(logmsg){
            Log.w(clazz.getSimpleName(), msg+"");
        }
    }
}
