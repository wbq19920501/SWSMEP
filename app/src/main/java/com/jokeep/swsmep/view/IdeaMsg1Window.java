package com.jokeep.swsmep.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.adapter.IdeaMsgAdapter;
import com.jokeep.swsmep.model.DataHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbq501 on 2016-1-28 09:38.
 * SWSMEP
 */
public class IdeaMsg1Window extends PopupWindow{
    private View mMenuView;
    private LinearLayout back;
    Button btn_sub;
    TextView add_memsg;
    RelativeLayout choose_addmsg;
    private ListView add_msglist;
    Activity context;
    private ShowDialog dialog;
    List<String> listmsg;
    List<String> memsg;
    // 数据接口
    OnGetData ongetdata;
    IdeaMsgAdapter adapter;
    List<DataHolder> dataList;
    public IdeaMsg1Window(final Activity context, View.OnClickListener itemclick){
        super();
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.idea_msg, null);
        dialog = new ShowDialog(context,R.style.MyDialog,context.getResources().getString(R.string.dialogmsg));
        memsg = new ArrayList<String>();
        listmsg = new ArrayList<String>();
        dataList = new ArrayList<DataHolder>();

        back = (LinearLayout) mMenuView.findViewById(R.id.back);
        btn_sub = (Button) mMenuView.findViewById(R.id.btn_sub);
        add_memsg = (TextView) mMenuView.findViewById(R.id.add_memsg);
        choose_addmsg = (RelativeLayout) mMenuView.findViewById(R.id.choose_addmsg);
        add_msglist = (ListView) mMenuView.findViewById(R.id.add_msglist);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ongetdata.onDataCallBack(memsg);
            }
        });

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom2);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#4Dffffff"));
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        for (int i=0;i<20;i++){
            dataList.add(new DataHolder("我是内容"+i,false));
        }
        adapter = new IdeaMsgAdapter(context,dataList);
        add_msglist.setAdapter(adapter);
        add_msglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < dataList.size(); i++) {
                    if (i==position){
                        dataList.get(i).checked = true;
                    }else {
                        dataList.get(i).checked = false;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
    // 数据接口设置,数据源接口传入
    public void setOnData(OnGetData sd) {
        ongetdata = sd;
        listmsg = new ArrayList<String>();
    }
    public interface OnGetData{
        abstract void onDataCallBack(List<String> listmsg);
    }
    class ViewHolder{
        CheckBox checkbox_memsg;
    }
}
