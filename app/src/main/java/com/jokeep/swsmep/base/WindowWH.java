package com.jokeep.swsmep.base;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by wbq501 on 2016-3-9 18:32.
 * SWSMEP
 */
public class WindowWH {
    public static int WindowHeight(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    public static int WindowWidth(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

}
