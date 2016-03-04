package com.jokeep.swsmep.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.BaseActivity;
import com.jokeep.swsmep.view.IdeaMsg1Window;

/**
 * Created by wbq501 on 2016-3-3 18:51.
 * SWSMEP
 */
public class WorkReturnIdeaActivity extends BaseActivity{
    private LinearLayout back;
    private Button btn_agree,btn_cancel,me_msg;
    private EditText add_context;
    private ImageView add_file;
    private ListView files_list;
    IdeaMsg1Window popw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workreturn_idea);
        init();
        initdata();
    }

    private void initdata() {

    }

    private void init() {
        btn_agree = (Button) findViewById(R.id.btn_agree);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        add_context = (EditText) findViewById(R.id.add_context);
        me_msg = (Button) findViewById(R.id.me_msg);
        add_file = (ImageView) findViewById(R.id.add_file);
        files_list = (ListView) findViewById(R.id.files_list);
        me_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popw = new IdeaMsg1Window(WorkReturnIdeaActivity.this,itemsOnClick);
                //显示窗口
                popw.showAtLocation(v, Gravity.RIGHT, 0, 0);
                //设置layout在PopupWindow中显示的位置
            }
        });
        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAnim();
            }
        });
    }
    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener(){

        public void onClick(View v) {
            switch (v.getId()){

            }
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
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
