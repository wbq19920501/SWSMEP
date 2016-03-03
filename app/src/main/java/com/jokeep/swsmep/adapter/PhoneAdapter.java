package com.jokeep.swsmep.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jokeep.swsmep.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wbq501 on 2016-3-1 16:44.
 * SWSMEP
 */
public class PhoneAdapter extends BaseAdapter {
    Context context;
    String[] s;
    /** 标记CheckBox是否被选中 **/
    public List<Boolean> mChecked = new ArrayList<Boolean>();
    /** 存放要显示的Item数据 **/
    /** 一个HashMap对象 **/
    @SuppressLint("UseSparseArrays")
    HashMap<Integer, View> map = new HashMap<Integer, View>();

    public PhoneAdapter() {
        mChecked = new ArrayList<Boolean>();
        for (int i = 0; i < s.length; i++) {// 遍历且设置CheckBox默认状态为未选中
            mChecked.add(false);
        }
    }
    public PhoneAdapter(Context context ,String[] s) {
        this.context = context;
        this.s = s;
    }
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
        if (map.get(position) == null) {// 根据position判断View是否为空
            convertView = LayoutInflater.from(context).inflate(R.layout.select_man1_item,null);
            holder = new ViewHolder();
            holder.link_name = (TextView) convertView.findViewById(R.id.link_name);
            holder.link_type = (TextView) convertView.findViewById(R.id.link_type);
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            map.put(position, convertView);// 存储视图信息
            convertView.setTag(holder);
        } else {
            convertView = map.get(position);
            holder = (ViewHolder) convertView.getTag();
        }
        final int p = position;
        for (int i = 0; i < s.length; i++) {// 遍历且设置CheckBox默认状态为未选中
            mChecked.add(false);
        }
        holder.checkbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                mChecked.set(p, cb.isChecked());// 设置CheckBox为选中状态
            }
        });
        holder.checkbox.setChecked(mChecked.get(position));
        holder.link_name.setText(s[position]);
        holder.link_type.setText(s[position]);
        return convertView;
    }
    class ViewHolder{
        TextView link_name,link_type;
        CheckBox checkbox;
    }
}
