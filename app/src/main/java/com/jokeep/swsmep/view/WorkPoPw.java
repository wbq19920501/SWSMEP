package com.jokeep.swsmep.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.jokeep.swsmep.R;

/**
 * Created by wbq501 on 2016-2-25 10:06.
 * SWSMEP
 */
public class WorkPoPw extends PopupWindow{
    private View mMenuView;
    RelativeLayout anim1,anim2,anim3;
    ImageButton img1,img2,img3;
    public WorkPoPw(Activity context,View.OnClickListener itemclick) {
        super();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.work_popw, null);
        init(itemclick);
        WindowManager wm = context.getWindowManager();
        final int height = wm.getDefaultDisplay().getHeight();
        int img1height = img1.getHeight()-img1.getTop();
        anim1 = (RelativeLayout) mMenuView.findViewById(R.id.anim1);
        anim2 = (RelativeLayout) mMenuView.findViewById(R.id.anim2);
        anim3 = (RelativeLayout) mMenuView.findViewById(R.id.anim3);
        final int changeh = img1height+100+40;
        final AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator o1 = ObjectAnimator.ofFloat(anim3,"Y",height-changeh,height-changeh);
        ObjectAnimator o2 = ObjectAnimator.ofFloat(anim2,"Y",height-changeh,height-changeh-200);
        ObjectAnimator o3 = ObjectAnimator.ofFloat(anim1,"Y",height-changeh-200,height-changeh-400);
        animatorSet.setDuration(300);
        animatorSet.play(o3).after(o2);
        animatorSet.start();


        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#CCffffff"));
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        final int changenewh = changeh+40;
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                mMenuView.setEnabled(false);
                ObjectAnimator o1 = ObjectAnimator.ofFloat(anim3,"Y",height-changenewh,height-changenewh);
                ObjectAnimator o2 = ObjectAnimator.ofFloat(anim2,"Y",height-changenewh -200,height-changenewh);
                ObjectAnimator o3 = ObjectAnimator.ofFloat(anim1,"Y",height-changenewh- 400,height-changenewh);
                animatorSet.setDuration(500);
                animatorSet.play(o3).with(o2);
                animatorSet.start();
                animatorSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dismiss();
                        mMenuView.setEnabled(true);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                return true;
            }
        });
    }

    private void init(View.OnClickListener itemclick) {
        img1 = (ImageButton) mMenuView.findViewById(R.id.img1);
        img2 = (ImageButton) mMenuView.findViewById(R.id.img2);
        img3 = (ImageButton) mMenuView.findViewById(R.id.img3);
        img1.setOnClickListener(itemclick);
        img2.setOnClickListener(itemclick);
        img3.setOnClickListener(itemclick);
    }
}
