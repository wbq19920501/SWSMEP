package com.jokeep.swsmep.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.jokeep.swsmep.R;

/**
 * Created by wbq501 on 2016-2-24 15:34.
 * SWSMEP
 */
public class DialogMsg extends Dialog{
    String msg;
    public DialogMsg(Context context,String msg) {
        super(context);
        this.msg = msg;
    }

    public DialogMsg(Context context, int themeResId,String msg) {
        super(context, themeResId);
        setOwnerActivity((Activity)context);
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_msg);
        TextView phone_num = (TextView) findViewById(R.id.phone_num);
        if (msg.equals("")||msg==null)
            msg = "暂无手机号码";
        phone_num.setText("手机："+msg);
    }
}
