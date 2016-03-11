package com.jokeep.swsmep.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.BaseActivity;

/**
 * Created by dengJ on 2016/3/7.
 * 展示跟踪事项的页面
 */

public class TrackActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout mLinearLayout;
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_layout);
        findView();
    }

    //初始化id
    private void findView(){
        mLinearLayout = (LinearLayout) findViewById(R.id.iv_back);
        mListView = (ListView) findViewById(R.id.lv_track);
        mLinearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                exitanim();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            exitanim();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void exitanim(){
        finish();
        overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
}
