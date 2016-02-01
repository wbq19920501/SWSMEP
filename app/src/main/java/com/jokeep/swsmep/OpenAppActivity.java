package com.jokeep.swsmep;

import android.content.Intent;
import android.os.Bundle;

import com.jokeep.swsmep.base.BaseActivity;

/**
 * Created by wbq501 on 2016-2-1 10:51.
 * SWSMEP
 */
public class OpenAppActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_app);
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    Intent intent = new Intent(OpenAppActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
