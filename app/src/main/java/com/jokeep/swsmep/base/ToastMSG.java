package com.jokeep.swsmep.base;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by wbq501 on 2016-1-7 09:29.
 * SWSMEP
 */
public class ToastMSG {
    /**
     * 开关提示
     */
    private static boolean toast = true;
    public void ToastShowLENGTH_LONG(Context context ,String msg){
        if (toast){
            Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
        }else {
            return;
        }
    };
    public void ToastShowLENGTH_SHORT(Context context ,String msg){
        if (toast){
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        }else {
            return;
        }
    };
}
