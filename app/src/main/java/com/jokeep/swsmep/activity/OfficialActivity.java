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
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jokeep.swsmep.R;
import com.jokeep.swsmep.adapter.MyFragmentPager;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.base.SwsApplication;
import com.jokeep.swsmep.fragment.IncomingFragment;
import com.jokeep.swsmep.fragment.OutgoingFragment;
import com.jokeep.swsmep.fragment.SignFragment;
import com.jokeep.swsmep.view.ShowDialog;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 展示公文办理的页面
 */

public class OfficialActivity extends FragmentActivity implements View.OnClickListener{
    private LinearLayout mLinearLayout;
    private RelativeLayout  mOfficial_one,mOfficial_two,mOfficial_three;
    private TextView mOfficial_text_one,mOfficial_text_two,mOfficial_text_three;
    private TextView mOfficial_num_one,mOfficial_num_two,mOfficial_num_three;
    private View mOfficial_view_one,mOfficial_view_two,mOfficial_view_three;
    private ViewPager mViewPager;
    private IncomingFragment mIncomingFragment;
    private OutgoingFragment mOutgoingFragment;
    private SignFragment mSignFragment;
    private ArrayList<Fragment> mArrayListFragment;
    private ShowDialog dialog;
    private  String TOKENID="";
    private  SharedPreferences spref;
    private SwsApplication application;
    private String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        findView();
        init();
        LoadData();
    }

    private void findView(){
        mLinearLayout = (LinearLayout) findViewById(R.id.iv_official);
        mOfficial_one = (RelativeLayout) findViewById(R.id.official_one);
        mOfficial_one.setOnClickListener(this);
        mOfficial_two = (RelativeLayout) findViewById(R.id.official_two);
        mOfficial_two.setOnClickListener(this);
        mOfficial_three = (RelativeLayout) findViewById(R.id.official_three);
        mOfficial_three.setOnClickListener(this);
        mOfficial_text_one = (TextView) findViewById(R.id.official_text_one);
        mOfficial_text_two = (TextView) findViewById(R.id.official_text_two);
        mOfficial_text_three = (TextView) findViewById(R.id.official_text_three);
        mOfficial_num_one = (TextView) findViewById(R.id.official_num_one);
        mOfficial_num_two = (TextView) findViewById(R.id.official_num_two);
        mOfficial_num_three = (TextView) findViewById(R.id.official_num_three);
        mOfficial_view_one = findViewById(R.id.official_view_one);
        mOfficial_view_two = findViewById(R.id.official_view_two);
        mOfficial_view_three = findViewById(R.id.official_view_three);
        mViewPager = (ViewPager) findViewById(R.id.official_viewpager);
        mIncomingFragment = new IncomingFragment();
        mSignFragment = new SignFragment();
        mOutgoingFragment = new OutgoingFragment();
        mLinearLayout.setOnClickListener(this);
    }
    private void init(){
        application = (SwsApplication)getApplication();
        UserID = application.getFUSERID();
        spref =  getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE);
        TOKENID = spref.getString(SaveMsg.TOKENID, "");
        mArrayListFragment = new ArrayList<>();
        mArrayListFragment.add(mOutgoingFragment);
        mArrayListFragment.add(mSignFragment);
        mArrayListFragment.add(mIncomingFragment);
        mViewPager.setAdapter(new MyFragmentPager(getSupportFragmentManager(),mArrayListFragment));
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(new MyViewPagerOnPageChangeListener());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_official:
                exitanim();
                break;
            case R.id.official_one:
                changeClor(0);
                mViewPager.setCurrentItem(0,false);
                break;
            case R.id.official_two:
                changeClor(1);
                mViewPager.setCurrentItem(1,false);
                break;
            case R.id.official_three:
                changeClor(2);
                mViewPager.setCurrentItem(2,false);
                break;

        }
    }
    private void changeClor(int position){
        switch (position){
            case 0:
                mOfficial_text_one.setTextColor(getResources().getColor(R.color.status_bar));
                mOfficial_text_two.setTextColor(getResources().getColor(R.color.work_textcolor));
                mOfficial_text_three.setTextColor(getResources().getColor(R.color.work_textcolor));
                mOfficial_view_one.setVisibility(View.VISIBLE);
                mOfficial_view_two.setVisibility(View.GONE);
                mOfficial_view_three.setVisibility(View.GONE);
                break;
            case 1:
                mOfficial_text_one.setTextColor(getResources().getColor(R.color.work_textcolor));
                mOfficial_text_two.setTextColor(getResources().getColor(R.color.status_bar));
                mOfficial_text_three.setTextColor(getResources().getColor(R.color.work_textcolor));
                mOfficial_view_one.setVisibility(View.GONE);
                mOfficial_view_two.setVisibility(View.VISIBLE);
                mOfficial_view_three.setVisibility(View.GONE);
                break;
            case 2:
                mOfficial_text_one.setTextColor(getResources().getColor(R.color.work_textcolor));
                mOfficial_text_two.setTextColor(getResources().getColor(R.color.work_textcolor));
                mOfficial_text_three.setTextColor(getResources().getColor(R.color.status_bar));
                mOfficial_view_one.setVisibility(View.GONE);
                mOfficial_view_two.setVisibility(View.GONE);
                mOfficial_view_three.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
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
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    //加载数据
    private void LoadData()
    {
        //显示进度条
        dialog = new ShowDialog(OfficialActivity.this,R.style.MyDialog,"数据加载，请稍候...");
        dialog.show();
        //创建调用接口请求对象
        RequestParams params = new RequestParams(HttpIP.MainService+"/Refresh_GWToDoCount");
        //创建JSON参数变量
        final JSONObject object = new JSONObject();
        try {
            //传入参数设置
            object.put("UserID",application.getFUSERID());
            params.addBodyParameter("parameter", AES.encrypt(object.toString()));
            params.setAsJsonContent(true);
            params.addBodyParameter(SaveMsg.TOKENID, TOKENID);
            //调用服务接口取数
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    //获得返回数据，并进行解密处理
                    String s = AES.desEncrypt(result.toString());
                    //关闭提示窗体
                    dialog.dismiss();
                    try {
                        //将解密出来的数据进行JSON转换
                        JSONObject object2 = new JSONObject(s);
                        //获得错误编码
                        int code = object2.getInt("ErrorCode");
                        if (code==1){
                            //失败
                            Toast.makeText(OfficialActivity.this, object2.getString("ErrorMsg").toString(), Toast.LENGTH_SHORT).show();
                        }else if (code==0){
                            //****成功,提示数量不显示
                            mOfficial_num_one.setVisibility(View.GONE);
                            mOfficial_num_two.setVisibility(View.GONE);
                            mOfficial_num_three.setVisibility(View.GONE);
                            //****将返回数据转换成JSON数组
                            JSONArray ResultArr = new JSONArray(object2.getString("Result").toString());
                            if(ResultArr.length()==0)return;
                            //获得一行
                            JSONObject ResultItem= (JSONObject)ResultArr.get(0);
                            //获得第一个Table
                            JSONArray mArray = new JSONArray(ResultItem.getString("Table"));
                            //获得相关提示数据并显示
                            //判断工作是否加number{ F_MENUCODE 类型编号（000001：发文; 000002:收文; 000005:签报）F_TODOCOUNT  待办条数}
                            for(int i=0;i<mArray.length();i++) {
                                JSONObject item= (JSONObject) mArray.get(i);
                                if (item.getString("F_MENUCODE").equals("000001") == true
                                        && item.getInt("F_TODOCOUNT") > 0) {
                                    //发文
                                    mOfficial_num_one.setText(item.getString("F_TODOCOUNT").toString());
                                    mOfficial_num_one.setVisibility(View.VISIBLE);
                                }else  if (item.getString("F_MENUCODE").equals("000005") == true
                                        && item.getInt("F_TODOCOUNT") > 0) {
                                    //签报
                                    mOfficial_num_two.setText(item.getString("F_TODOCOUNT").toString());
                                    mOfficial_num_two.setVisibility(View.VISIBLE);
                                }else  if (item.getString("F_MENUCODE").equals("000002") == true
                                        && item.getInt("F_TODOCOUNT") > 0) {
                                    //收文
                                    mOfficial_num_three.setText(item.getString("F_TODOCOUNT").toString());
                                    mOfficial_num_three.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        }catch (Exception e){}
                    }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(OfficialActivity.this, "请求服务失败,请稍后再试", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
        }
    }

    public class MyViewPagerOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            changeClor(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
