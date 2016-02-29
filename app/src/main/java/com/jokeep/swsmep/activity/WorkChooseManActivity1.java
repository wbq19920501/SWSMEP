package com.jokeep.swsmep.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.BaseActivity;
import com.jokeep.swsmep.view.SelectMan1Window;
import com.jokeep.swsmep.view.SelectMan2Window;

/**
 * Created by wbq501 on 2016-2-29 10:39.
 * SWSMEP
 */
public class WorkChooseManActivity1 extends BaseActivity{
    private LinearLayout back;
    private TextView choose_man;
    private ListView man_list;
    SelectMan1Window popw1;
    SelectMan2Window popw2;
    BaseAdapter adapter;
    String[] s= {"1","2"};
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_man1);
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
        choose_man = (TextView) findViewById(R.id.choose_man);
        choose_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popw1 = new SelectMan1Window(WorkChooseManActivity1.this,itemsOnClick);
                //显示窗口
                popw1.showAtLocation(v, Gravity.RIGHT, 0, 0);
                //设置layout在PopupWindow中显示的位置
            }
        });
        man_list = (ListView) findViewById(R.id.man_list);
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
                    convertView = LayoutInflater.from(WorkChooseManActivity1.this).inflate(R.layout.choose_manitem,null);
                    holder = new ViewHolder();
                    holder.link_name = (TextView) convertView.findViewById(R.id.link_name);
                    holder.link_type = (TextView) convertView.findViewById(R.id.link_type);
                    holder.del_man = (ImageView) convertView.findViewById(R.id.del_man);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.link_name.setText(s[position]);
                return convertView;
            }
        };
        man_list.setAdapter(adapter);
    }
    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener(){

        public void onClick(View v) {
            switch (v.getId()){
                case R.id.selectman_sx:
                    LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view=inflater.inflate(R.layout.work_man1, null);
                    popw2 = new SelectMan2Window(WorkChooseManActivity1.this,itemsOnClick2);
                    //显示窗口
                    popw2.showAtLocation(view, Gravity.RIGHT, 0, 0);
                    //设置layout在PopupWindow中显示的位置
                    break;
                case R.id.selectman_search:
                    break;
            }
        }
    };
    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick2 = new View.OnClickListener(){

        public void onClick(View v) {
            popw2.dismiss();
        }
    };
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
    static class ViewHolder{
        TextView link_name,link_type;
        ImageView del_man;
    }
}
