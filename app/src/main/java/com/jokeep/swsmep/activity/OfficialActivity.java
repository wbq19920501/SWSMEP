package com.jokeep.swsmep.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.BaseActivity;

/**
 * 展示公文办理的页面
 */

public class OfficialActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout mLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        findView();
    }
    private void findView(){
        mLinearLayout = (LinearLayout) findViewById(R.id.iv_official);
        mLinearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_official:
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

    private void exitanim() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}
