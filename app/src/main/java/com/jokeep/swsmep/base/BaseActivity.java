package com.jokeep.swsmep.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by wbq501 on 2016-1-20 10:30.
 * SWSMEP
 */
public class BaseActivity extends Activity{
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        sp = getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE);
        editor = sp.edit();
    }
}
