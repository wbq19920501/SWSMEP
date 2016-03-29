package com.jokeep.swsmep.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.BaseActivity;
import com.jokeep.swsmep.utls.DateTimePickDialogUtil;

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
    private String OneTime,TwoTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_schedule);
        init();
        initdata();
    }

    private void initdata() {
        request();
    }

    private void request() {

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

        schedule_timechoose_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosetime(1);
            }
        });
        schedule_timechoose_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosetime(2);
            }
        });

        schedule_remind = (LinearLayout) findViewById(R.id.schedule_remind);
        remind_time = (TextView) findViewById(R.id.remind_time);
        remind_time_start = (TextView) findViewById(R.id.remind_time_start);
        remind_time2 = (TextView) findViewById(R.id.remind_time2);
        remind_time_start2 = (TextView) findViewById(R.id.remind_time_start2);

        remind_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosetime(3);
            }
        });
        remind_time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] s = {"1分钟", "10分钟", "30分钟" , "1小时"};
                new  AlertDialog.Builder(AddScheduleActivity.this)
                        .setTitle("请选择间隔时间")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(s,  0 ,
                                new  DialogInterface.OnClickListener() {
                                    public   void  onClick(DialogInterface dialog,  int  which) {
                                        remind_time2.setText(s[which]);
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

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

    private void choosetime(int i) {
        if (i==1){
            DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                    AddScheduleActivity.this, null,1);
            dateTimePicKDialog.dateTimePicKDialog(schedule_timechoose_start);
            OneTime = DateTimePickDialogUtil.SizeTime();
        }else if (i==2){
            DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                    AddScheduleActivity.this, OneTime,2);
            dateTimePicKDialog.dateTimePicKDialog(schedule_timechoose_end);
            TwoTime = DateTimePickDialogUtil.SizeTime();
        }else if(i==3){
            DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                    AddScheduleActivity.this, null,3);
            dateTimePicKDialog.dateTimePicKDialog(remind_time);
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
