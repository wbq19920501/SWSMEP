package com.jokeep.swsmep.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.BaseActivity;

/**
 * Created by wbq501 on 2016-3-23 15:00.
 * trunk
 */
public class ScheduleMeetingActivity extends BaseActivity{
    private Button btn_cancel,btn_agree;
    private ImageView btn_cancel_img,btn_agree_img;
    private boolean choosebtn = true;
    private ImageButton back;
    Intent intent;
    String suggestionmsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_meeting);
        init();
    }

    private void init() {
        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAnim();
            }
        });

        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_agree = (Button) findViewById(R.id.btn_agree);
        btn_cancel_img = (ImageView) findViewById(R.id.btn_cancel_img);
        btn_agree_img = (ImageView) findViewById(R.id.btn_agree_img);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (choosebtn) {
                    intent = new Intent(ScheduleMeetingActivity.this, ScheduleMeeting_CancelActivity.class);
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } else {
                    Toast.makeText(ScheduleMeetingActivity.this, suggestionmsg + "", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosebtn = true;
                if (choosebtn) {
                    btn_agree.setBackground(getResources().getDrawable(R.drawable.schedule_btn3));
                    btn_agree_img.setVisibility(View.VISIBLE);
                    btn_cancel.setBackground(getResources().getDrawable(R.drawable.schedule_btn));
                    btn_cancel_img.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                choosebtn = false;
                if (!choosebtn){
                    btn_agree.setBackground(getResources().getDrawable(R.drawable.schedule_btn));
                    btn_agree_img.setVisibility(View.GONE);
                    btn_cancel.setBackground(getResources().getDrawable(R.drawable.schedule_btn2));
                    btn_cancel_img.setVisibility(View.VISIBLE);
                }
                suggestionmsg = data.getStringExtra("suggestion").toString().trim();
                if (suggestionmsg.equals("")||suggestionmsg==null){
                    choosebtn = true;
                    btn_cancel.setBackground(getResources().getDrawable(R.drawable.schedule_btn));
                    btn_cancel_img.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitAnim();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void exitAnim() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}
