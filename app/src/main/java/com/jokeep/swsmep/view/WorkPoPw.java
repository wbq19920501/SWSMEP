package com.jokeep.swsmep.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.MyData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbq501 on 2016-2-25 10:06.
 * SWSMEP
 */
public class WorkPoPw extends PopupWindow{
    private int[] res = {R.id.rl_xitong,R.id.rl_gongwen,R.id.rl_genzong};
    private List<LinearLayout> mLinearLayout = new ArrayList<>();
    private boolean flag = false;
    private int h;
    private View mView;
    private Context context;
    private MyData mMyData = MyData.getInstance();
    private Button bt_genzong_num,bt_gongwen_num,bt_xitong_num;
    ImageButton bt_genzong,bt_gongwen,bt_xitong;
    public WorkPoPw(final Activity context,View.OnClickListener itemclick){
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.work_popw,null);
        for (int i = 0; i < res.length; i++) {
            LinearLayout ll = (LinearLayout) mView.findViewById(res[i]);
            mLinearLayout.add(ll);
        }
        bt_genzong_num = (Button) mView.findViewById(R.id.bt_genzong_num);
        bt_gongwen_num = (Button) mView.findViewById(R.id.bt_gongwen_num);
        bt_xitong_num = (Button) mView.findViewById(R.id.bt_xitong_num);
        bt_genzong = (ImageButton) mView.findViewById(R.id.bt_genzong);
        bt_gongwen = (ImageButton) mView.findViewById(R.id.bt_gongwen);
        bt_xitong = (ImageButton) mView.findViewById(R.id.bt_xitong);
        bt_genzong.setOnClickListener(itemclick);
        bt_gongwen.setOnClickListener(itemclick);
        bt_xitong.setOnClickListener(itemclick);
        setNumber();
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAnim();
                dismiss();
            }
        });
        h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        this.setContentView(mView);
        this.setHeight(h-49);
        this.setWidth(w);
        this.setFocusable(true);
        setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0x9fffffff);
        this.setBackgroundDrawable(dw);
    }
    //设置待办个数
    private void setNumber(){
        if(mMyData.getmGenZongNum().equals("0")){
            bt_genzong_num.setVisibility(View.GONE);
        }
        if(mMyData.getmGongWenNum().equals("0")){
            bt_gongwen_num.setVisibility(View.GONE);
        }
        if(mMyData.getmXieTongNum().equals("0")){
            bt_xitong_num.setVisibility(View.GONE);
        }
        bt_genzong_num.setText(mMyData.getmGenZongNum());
        bt_gongwen_num.setText(mMyData.getmGongWenNum());
        bt_xitong_num.setText(mMyData.getmXieTongNum());
    }
    private void closeAnim() {
        for (int i = 1; i < res.length; i++) {
            float curTranslationY = mLinearLayout.get(i).getTranslationY();
            ObjectAnimator animator = ObjectAnimator.ofFloat(mLinearLayout.get(i), "translationY", -curTranslationY * i, 15f);
            animator.setDuration(100);
            animator.setStartDelay(i * 100);
            animator.start();
            animator.setInterpolator(new AccelerateInterpolator());
        }
        flag = true;
    }

    public void startAnim() {
        for (int i = 1; i < res.length; i++) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mLinearLayout.get(i), "translationY",-(h/20) ,-(h/8)* i);
            AnimatorSet set = new AnimatorSet();
            set.setInterpolator(new LinearInterpolator());
            //三个动画同时执行
            set.playTogether(animator);
            set.start();
        }
        flag=false;
    }
}
