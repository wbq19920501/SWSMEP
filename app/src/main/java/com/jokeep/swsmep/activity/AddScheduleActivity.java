package com.jokeep.swsmep.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.BaseActivity;

/**
 * Created by wbq501 on 2016-3-22 17:06.
 * trunk
 */
public class AddScheduleActivity extends BaseActivity{
    private ImageButton back;
    private TextView schedule_del;
    private EditText add_schedule_title;
    private TextView schedule_timechoose_start,schedule_timechoose_end;
    private EditText schedule_context;
    private ImageButton schedule_switch;
    private LinearLayout schedule_remind;
    private TextView remind_time,remind_time_start
            ,remind_time2,remind_time_start2;
    private boolean switchopen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_schedule);
        init();
        initdata();
    }

    private void initdata() {

    }

    private void init() {
        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAnim();
            }
        });
        schedule_del = (TextView) findViewById(R.id.schedule_del);

        add_schedule_title = (EditText) findViewById(R.id.add_schedule_title);
        schedule_timechoose_start = (TextView) findViewById(R.id.schedule_timechoose_start);
        schedule_timechoose_end = (TextView) findViewById(R.id.schedule_timechoose_end);
        schedule_context = (EditText) findViewById(R.id.schedule_context);
        schedule_switch = (ImageButton) findViewById(R.id.schedule_switch);

        schedule_remind = (LinearLayout) findViewById(R.id.schedule_remind);
        remind_time = (TextView) findViewById(R.id.remind_time);
        remind_time_start = (TextView) findViewById(R.id.remind_time_start);
        remind_time2 = (TextView) findViewById(R.id.remind_time2);
        remind_time_start2 = (TextView) findViewById(R.id.remind_time_start2);

        schedule_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchopen){
                    schedule_switch.setImageResource(R.mipmap.schedule_check2 );
                    schedule_remind.setVisibility(View.GONE);
                    switchopen = false;
                }else {
                    schedule_switch.setImageResource(R.mipmap.schedule_check1);
                    schedule_remind.setVisibility(View.VISIBLE);
                    switchopen = true;
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
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}
