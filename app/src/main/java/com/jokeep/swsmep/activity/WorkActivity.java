package com.jokeep.swsmep.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.adapter.MyFragmentPager;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.base.SwsApplication;
import com.jokeep.swsmep.fragment.Work1Fragment;
import com.jokeep.swsmep.fragment.Work2Fragment;
import com.jokeep.swsmep.fragment.Work3Fragment;
import com.jokeep.swsmep.fragment.Work4Fragment;

import java.util.ArrayList;

/**
 * Created by wbq501 on 2016-2-25 11:17.
 * SWSMEP
 */
public class WorkActivity extends FragmentActivity implements View.OnClickListener{
    private LinearLayout back;
    private ImageView add_work;
    private RelativeLayout worktab1,worktab2,worktab3,worktab4;
    private TextView worktab_text1,worktab_text2,worktab_text3,worktab_text4;
    private TextView worktab_num1,worktab_num2;
    private View worktab_view1,worktab_view2,worktab_view3,worktab_view4;
    private ViewPager pager;
    private ArrayList<Fragment> fragmentList;
    Work1Fragment work1Fragment;
    Work2Fragment work2Fragment;
    Work3Fragment work3Fragment;
    Work4Fragment work4Fragment;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private SwsApplication application;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work);
        inittab();
        init();
        initdata();
    }

    private void initdata() {

    }


    private void init() {
        sp = WorkActivity.this.getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE);
        editor = sp.edit();
        application = (SwsApplication) getApplication();
        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitanim();
            }
        });
        add_work = (ImageView) findViewById(R.id.add_work);
        add_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(WorkActivity.this,AddWorkActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            }
        });
        pager = (ViewPager) findViewById(R.id.worktab_viewpager);
        fragmentList = new ArrayList<Fragment>();
        work1Fragment = new Work1Fragment();
        work2Fragment = new Work2Fragment();
        work3Fragment = new Work3Fragment();
        work4Fragment = new Work4Fragment();
        fragmentList.add(work1Fragment);
        fragmentList.add(work2Fragment);
        fragmentList.add(work3Fragment);
        fragmentList.add(work4Fragment);
        Bundle data = new Bundle();
        data.putString("TOKENID",sp.getString(SaveMsg.TOKENID, ""));
        data.putString("UserID", application.getFUSERID());
        work1Fragment.setArguments(data);
        work2Fragment.setArguments(data);
        work3Fragment.setArguments(data);
        work4Fragment.setArguments(data);
        pager.setAdapter(new MyFragmentPager(getSupportFragmentManager(), fragmentList));
        pager.setOffscreenPageLimit(4);
        pager.setCurrentItem(0);
        pager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        WorkActivity.this.finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            changwork(position + 1);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    private void inittab() {
        worktab1 = (RelativeLayout) findViewById(R.id.worktab1);
        worktab1.setOnClickListener(this);
        worktab2 = (RelativeLayout) findViewById(R.id.worktab2);
        worktab2.setOnClickListener(this);
        worktab3 = (RelativeLayout) findViewById(R.id.worktab3);
        worktab3.setOnClickListener(this);
        worktab4 = (RelativeLayout) findViewById(R.id.worktab4);
        worktab4.setOnClickListener(this);
        worktab_text1 = (TextView) findViewById(R.id.worktab_text1);
        worktab_text2 = (TextView) findViewById(R.id.worktab_text2);
        worktab_text3 = (TextView) findViewById(R.id.worktab_text3);
        worktab_text4 = (TextView) findViewById(R.id.worktab_text4);

        worktab_num1 = (TextView) findViewById(R.id.worktab_num1);
        worktab_num2 = (TextView) findViewById(R.id.worktab_num2);
        worktab_view1 = findViewById(R.id.worktab_view1);
        worktab_view2 = findViewById(R.id.worktab_view2);
        worktab_view3 = findViewById(R.id.worktab_view3);
        worktab_view4 = findViewById(R.id.worktab_view4);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.worktab1:
                changwork(1);
                pager.setCurrentItem(0);
                break;
            case R.id.worktab2:
                changwork(2);
                pager.setCurrentItem(1);
                break;
            case R.id.worktab3:
                changwork(3);
                pager.setCurrentItem(2);
                break;
            case R.id.worktab4:
                changwork(4);
                pager.setCurrentItem(3);
                break;
        }
    }

    private void changwork(int i) {
        switch (i){
            case 1:
                worktab_text1.setTextColor(getResources().getColor(R.color.status_bar));
                worktab_text2.setTextColor(getResources().getColor(R.color.work_textcolor));
                worktab_text3.setTextColor(getResources().getColor(R.color.work_textcolor));
                worktab_text4.setTextColor(getResources().getColor(R.color.work_textcolor));
                worktab_view1.setVisibility(View.VISIBLE);
                worktab_view2.setVisibility(View.GONE);
                worktab_view3.setVisibility(View.GONE);
                worktab_view4.setVisibility(View.GONE);
                break;
            case 2:
                worktab_text1.setTextColor(getResources().getColor(R.color.work_textcolor));
                worktab_text2.setTextColor(getResources().getColor(R.color.status_bar));
                worktab_text3.setTextColor(getResources().getColor(R.color.work_textcolor));
                worktab_text4.setTextColor(getResources().getColor(R.color.work_textcolor));
                worktab_view1.setVisibility(View.GONE);
                worktab_view2.setVisibility(View.VISIBLE);
                worktab_view3.setVisibility(View.GONE);
                worktab_view4.setVisibility(View.GONE);
                break;
            case 3:
                worktab_text1.setTextColor(getResources().getColor(R.color.work_textcolor));
                worktab_text2.setTextColor(getResources().getColor(R.color.work_textcolor));
                worktab_text3.setTextColor(getResources().getColor(R.color.status_bar));
                worktab_text4.setTextColor(getResources().getColor(R.color.work_textcolor));
                worktab_view1.setVisibility(View.GONE);
                worktab_view2.setVisibility(View.GONE);
                worktab_view3.setVisibility(View.VISIBLE);
                worktab_view4.setVisibility(View.GONE);
                break;
            case 4:
                worktab_text1.setTextColor(getResources().getColor(R.color.work_textcolor));
                worktab_text2.setTextColor(getResources().getColor(R.color.work_textcolor));
                worktab_text3.setTextColor(getResources().getColor(R.color.work_textcolor));
                worktab_text4.setTextColor(getResources().getColor(R.color.status_bar));
                worktab_view1.setVisibility(View.GONE);
                worktab_view2.setVisibility(View.GONE);
                worktab_view3.setVisibility(View.GONE);
                worktab_view4.setVisibility(View.VISIBLE);
                break;
        }
    }
}
