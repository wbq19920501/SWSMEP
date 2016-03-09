package com.jokeep.swsmep.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.adapter.MyFragmentPager;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.base.WebSeting;
import com.jokeep.swsmep.fragment.WorkAnnexFragment;
import com.jokeep.swsmep.fragment.WorkMainBodyFragment;
import com.jokeep.swsmep.model.Work1Info;
import com.jokeep.swsmep.view.ShowDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    int typeopen;
    List<Work1Info> work1Infos;
    String f_jointid;
    private ShowDialog dialog;
    String TOKENID;
    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_work);
        initpage();
        init();
//        initdata();
    }

    private void initdata() {
        dialog.show();
        RequestParams params = new RequestParams(HttpIP.MainService+HttpIP.JointAttByID);
        JSONObject object = new JSONObject();
        try {
            object.put("JointID",f_jointid);
            params.addBodyParameter("parameter", AES.encrypt(object.toString()));
            params.setAsJsonContent(true);
            params.addBodyParameter(SaveMsg.TOKENID, TOKENID);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    String s = AES.desEncrypt(result.toString());
                    Log.d("s", s);
                    dialog.dismiss();
                    try {
                        JSONObject object2 = new JSONObject(s);
                        int code = object2.getInt("ErrorCode");
                        if (code==1){
                            Toast.makeText(LookWorkActivity.this, object2.getString("ErrorMsg").toString(), Toast.LENGTH_SHORT).show();
                        }else if (code==0){

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
        typeopen = getIntent().getIntExtra("typeopen", 1);
        work1Infos = new ArrayList<Work1Info>();
        work1Infos = (List<Work1Info>) getIntent().getSerializableExtra("work1Infos");
        int position = getIntent().getIntExtra("intposition", 0);
        TOKENID = getIntent().getStringExtra("TOKENID");
        Work1Info work1Info = work1Infos.get(position);
        f_jointid = work1Info.getF_JOINTID();

        Bundle data = new Bundle();
        data.putString("TOKENID", TOKENID);
        data.putString("JointID", f_jointid);
        data.putString("F_LINKURL", work1Infos.get(position).getF_LINKURL());
        data.putString("F_EXECUTMAINID", work1Infos.get(position).getF_EXECUTMAINID());
        workMainBodyFragment.setArguments(data);
        workAnnexFragment.setArguments(data);

        webview = (WebView) findViewById(R.id.webview);
        openwebview(work1Infos.get(position).getF_EXECUTMAINID());

        dialog = new ShowDialog(LookWorkActivity.this,R.style.MyDialog,getResources().getString(R.string.dialogmsg));
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
                intent.putExtra("work1Infos", (Serializable) work1Infos);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        if (typeopen == 1){

        }else if (typeopen == 3 || typeopen==4){
            btn_next.setVisibility(View.GONE);
        }
        work_idea = (ImageButton) findViewById(R.id.work_idea);
        work_flow = (ImageButton) findViewById(R.id.work_flow);
        flow_linear = (LinearLayout) findViewById(R.id.flow_linear);
        idea_linear = (LinearLayout) findViewById(R.id.idea_linear);
        look_workmsg = (LinearLayout) findViewById(R.id.look_workmsg);
        work_flow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (look1){
                    if (!look2){
                        work_idea.setBackgroundResource(R.color.status_bar);
                        flow_linear.setVisibility(View.GONE);
                        idea_linear.setVisibility(View.GONE);
                        look_workmsg.setVisibility(View.VISIBLE);
                    }
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
                    if (!look1){
                        work_flow.setBackgroundResource(R.color.status_bar);
                        idea_linear.setVisibility(View.GONE);
                        flow_linear.setVisibility(View.GONE);
                        look_workmsg.setVisibility(View.VISIBLE);
                    }
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

    private void openwebview(String f_executmainid) {
        String url = HttpIP.FlowPreview+"?F_ExecutMainID="+f_executmainid+"&TokenID="+TOKENID+"&Ismobile=1";
        WebSeting.openweb(webview);
        //WebView加载web资源
        webview.loadUrl(url);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                } else {
                    // 加载中
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
