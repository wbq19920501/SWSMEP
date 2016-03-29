package com.jokeep.swsmep.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.BaseActivity;

/**
 * Created by wbq501 on 2016-3-24 14:53.
 * trunk
 */
public class ScheduleMeeting_CancelActivity extends BaseActivity{
    private ImageButton back;
    private Button btn_sub;
    private EditText context;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_meeting_cacel);
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
        context = (EditText) findViewById(R.id.context);
        btn_sub = (Button) findViewById(R.id.btn_sub);
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String scontext = context.getText().toString().trim();
                if (scontext.equals("") || scontext == null) {
                    Toast.makeText(ScheduleMeeting_CancelActivity.this,"请输入意见...",Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    intent = new Intent();
                    intent.putExtra("suggestion",scontext);
                    setResult(1, intent);
                    finish();
                }
            }
        });
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
        intent = new Intent();
        intent.putExtra("suggestion","");
        setResult(1,intent);
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}
