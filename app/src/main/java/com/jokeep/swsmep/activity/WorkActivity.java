package com.jokeep.swsmep.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.adapter.MyFragmentPager;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.base.SwsApplication;
import com.jokeep.swsmep.fragment.Work1Fragment;
import com.jokeep.swsmep.fragment.Work2Fragment;
import com.jokeep.swsmep.fragment.Work3Fragment;
import com.jokeep.swsmep.fragment.Work4Fragment;
import com.jokeep.swsmep.view.ShowDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by wbq501 on 2016-2-25 11:17.
 * SWSMEP
 */
public class WorkActivity extends FragmentActivity implements View.OnClickListener{
    public static final String action = "com.swsmep.work";
    private ImageButton back;
    private ImageButton add_work;
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
    private ShowDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work);
        IntentFilter filter = new IntentFilter(action);
        registerReceiver(broadcastReceiver, filter);
        inittab();
        init();
        initdata();
    }

    private void initdata() {
        request();
    }

    public void request() {
        RequestParams params = new RequestParams(HttpIP.MainService+HttpIP.JointToDoCount);
        JSONObject object = new JSONObject();
        try {
            object.put("UserID", application.getFUSERID());
            params.addBodyParameter("parameter", AES.encrypt(object.toString()));
            params.setAsJsonContent(true);
            params.addBodyParameter(SaveMsg.TOKENID, sp.getString(SaveMsg.TOKENID, ""));
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    String s = AES.desEncrypt(result.toString());
                    Log.d("s", s);
                    dialog.dismiss();
                    try {
                        JSONObject object2 = new JSONObject(s);
                        int code = object2.getInt("ErrorCode");
                        if (code == 1) {
                            Toast.makeText(WorkActivity.this, object2.getString("ErrorMsg").toString(), Toast.LENGTH_SHORT).show();
                        } else if (code == 0) {
                            String Result = object2.getString("Result");
                            JSONArray array = new JSONArray(Result);
                            JSONObject jsonObject = (JSONObject) array.get(0);
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("Table"));
                            int f_count1 = new Double(((JSONObject) jsonArray.get(0)).getString("F_COUNT")).intValue();
                            int f_count2 = new Double(((JSONObject) jsonArray.get(1)).getString("F_COUNT")).intValue();
                            if (f_count1==0){
                                worktab_num1.setVisibility(View.GONE);
                            }else {
                                worktab_num1.setText(f_count1+"");
                            }
                            if (f_count2==0){
                                worktab_num2.setVisibility(View.GONE);
                            }else {
                                worktab_num2.setText(f_count2+"");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    dialog.dismiss();
                    Log.d("ex", ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void init() {
        dialog = new ShowDialog(WorkActivity.this, R.style.MyDialog, getResources().getString(R.string.dialogmsg));
        sp = WorkActivity.this.getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE);
        editor = sp.edit();
        application = (SwsApplication) getApplication();
        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitanim();
            }
        });
        add_work = (ImageButton) findViewById(R.id.add_work);
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
        pager.setOffscreenPageLimit(1);
        pager.setCurrentItem(0);
        pager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        request();
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            request();
            changwork(4);
            pager.setCurrentItem(3);
            work1Fragment.refreshfragment();
            work2Fragment.refreshfragment();
            //work3Fragment.refreshfragment();
            //work4Fragment.refreshfragment();
        }
    };

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
