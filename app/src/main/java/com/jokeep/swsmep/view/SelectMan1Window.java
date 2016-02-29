package com.jokeep.swsmep.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jokeep.swsmep.R;

/**
 * Created by wbq501 on 2016-1-28 09:38.
 * SWSMEP
 */
public class SelectMan1Window extends PopupWindow{
    private View mMenuView;
    private LinearLayout back;
    private ImageView selectman_sx;
    private EditText selectman_search;
    private ListView selectman_list1;
    SelectMan2Window popw2;
    BaseAdapter adapter;
    String[] s = {"1","2","3","3","3","3","3","3","3","3","3","3","3","3"};

    public SelectMan1Window(final Activity context, View.OnClickListener itemclick){
        super();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.select_man1, null);

        back = (LinearLayout) mMenuView.findViewById(R.id.back);
        selectman_sx = (ImageView) mMenuView.findViewById(R.id.selectman_sx);
        selectman_search = (EditText) mMenuView.findViewById(R.id.selectman_search);
        selectman_list1 = (ListView) mMenuView.findViewById(R.id.selectman_list1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        selectman_search.setOnClickListener(itemclick);
        selectman_sx.setOnClickListener(itemclick);
        initdata(context);

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
    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick2 = new View.OnClickListener(){

        public void onClick(View v) {
            popw2.dismiss();
        }
    };
    private void initdata(final Activity context) {
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return s.length;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;
                if (convertView == null){
                    convertView = LayoutInflater.from(context).inflate(R.layout.select_man1_item,null);
                    holder = new ViewHolder();
                    holder.link_name = (TextView) convertView.findViewById(R.id.link_name);
                    holder.link_type = (TextView) convertView.findViewById(R.id.link_type);
                    holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                return convertView;
            }
        };
        selectman_list1.setAdapter(adapter);
    }
    static class ViewHolder{
        TextView link_name,link_type;
        CheckBox checkbox;
    }
}
