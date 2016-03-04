package com.jokeep.swsmep.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.adapter.MyFragmentPager;
import com.jokeep.swsmep.fragment.WorkAnnexFragment;
import com.jokeep.swsmep.fragment.WorkMainBodyFragment;

import java.util.ArrayList;

/**
 * Created by wbq501 on 2016-3-3 11:44.
 * SWSMEP
 */
public class LookWorkActivity extends FragmentActivity implements View.OnClickListener{
    private TextView look_tab1,look_tab2;
    private View look_tabview1,look_tabview2;
    private ViewPager pager;
    private ArrayList<Fragment> fragmentList;
    WorkMainBodyFragment workMainBodyFragment;
    WorkAnnexFragment workAnnexFragment;
    Button btn_next;
    ImageButton work_idea,work_flow;
    private LinearLayout flow_linear,idea_linear,look_workmsg;
    private LinearLayout back;
    private Boolean look1 = true;
    private Boolean look2 = true;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_work);
        initpage();
        init();
        initdata();
    }

    private void initdata() {

    }

    private void init() {
        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAnim();
            }
        });
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LookWorkActivity.this,WorkReturnIdeaActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        work_idea = (ImageButton) findViewById(R.id.work_idea);
        work_flow = (ImageButton) findViewById(R.id.work_flow);
        flow_linear = (LinearLayout) findViewById(R.id.flow_linear);
        idea_linear = (LinearLayout) findViewById(R.id.idea_linear);
        look_workmsg = (LinearLayout) findViewById(R.id.look_workmsg);
        work_flow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (look1){
                    look1 = false;
                    look2 = true;
                    work_flow.setBackgroundResource(R.mipmap.look_work);
                    flow_linear.setVisibility(View.VISIBLE);
                    look_workmsg.setVisibility(View.GONE);
                }else {
                    look1 = true;
                    look2 = false;
                    work_flow.setBackgroundResource(R.color.status_bar);
                    flow_linear.setVisibility(View.GONE);
                    look_workmsg.setVisibility(View.VISIBLE);
                }

            }
        });
        work_idea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (look2){
                    look2 = false;
                    look1 = true;
                    work_idea.setBackgroundResource(R.mipmap.look_work);
                    idea_linear.setVisibility(View.VISIBLE);
                    look_workmsg.setVisibility(View.GONE);
                }else {
                    look2 = true;
                    look1 = false;
                    work_idea.setBackgroundResource(R.color.status_bar);
                    idea_linear.setVisibility(View.GONE);
                    look_workmsg.setVisibility(View.VISIBLE);
                }
            }
        });
    }
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
    private void initpage() {
        look_tab1 = (TextView) findViewById(R.id.look_tab1);
        look_tab1.setOnClickListener(this);
        look_tab2 = (TextView) findViewById(R.id.look_tab2);
        look_tab2.setOnClickListener(this);
        look_tabview1 = findViewById(R.id.look_tabview1);
        look_tabview2 = findViewById(R.id.look_tabview2);
        pager = (ViewPager) findViewById(R.id.look_page);
        fragmentList = new ArrayList<Fragment>();
        workMainBodyFragment = new WorkMainBodyFragment();
        workAnnexFragment = new WorkAnnexFragment();
        fragmentList.add(workMainBodyFragment);
        fragmentList.add(workAnnexFragment);
        pager.setAdapter(new MyFragmentPager(getSupportFragmentManager(), fragmentList));
        pager.setOffscreenPageLimit(2);
        pager.setCurrentItem(0);
        pager.setOnPageChangeListener(new MyOnPageChangeListener());
    }
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            changepage(position+1);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.look_tab1:
                changepage(1);
                pager.setCurrentItem(0);
                break;
            case R.id.look_tab2:
                changepage(2);
                pager.setCurrentItem(1);
                break;
        }
    }

    private void changepage(int i) {
        switch (i){
            case 1:
                look_tab1.setTextColor(getResources().getColor(R.color.status_bar));
                look_tab2.setTextColor(getResources().getColor(R.color.work_textcolor));
                look_tabview1.setVisibility(View.VISIBLE);
                look_tabview2.setVisibility(View.GONE);
                break;
            case 2:
                look_tab1.setTextColor(getResources().getColor(R.color.work_textcolor));
                look_tab2.setTextColor(getResources().getColor(R.color.status_bar));
                look_tabview1.setVisibility(View.GONE);
                look_tabview2.setVisibility(View.VISIBLE);
                break;
        }
    }
}
