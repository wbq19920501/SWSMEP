package com.jokeep.swsmep.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.adapter.ListViewAdapter;

/**
 * Created by wbq501 on 2016-1-28 09:38.
 * SWSMEP
 */
public class SelectMan2Window extends PopupWindow{
    private View mMenuView;
    private LinearLayout back;
    private ListView selectman_list1;
    private Button btn_sub;
    BaseAdapter adapter;
    String[] s = new String[]{"1","2","3","3","3","3","3","3","3","3","3","3","3","3"};

    public SelectMan2Window(Activity context, View.OnClickListener itemclick){
        super();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.select_man2, null);

        back = (LinearLayout) mMenuView.findViewById(R.id.back);
        selectman_list1 = (ListView) mMenuView.findViewById(R.id.selectman_list1);
        adapter = new ListViewAdapter(context, s);
        selectman_list1.setAdapter(adapter);
        selectman_list1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        btn_sub = (Button) mMenuView.findViewById(R.id.btn_sub);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_sub.setOnClickListener(itemclick);

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom2);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#4Dffffff"));
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

    }
}
