package com.jokeep.swsmep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jokeep.swsmep.R;

import java.util.HashMap;

/**
 * Created by wbq501 on 2016-2-29 16:52.
 * SWSMEP
 */
public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private String[] beans;

    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;

    class ViewHolder{
        TextView link_name,link_type;
        CheckBox checkbox;
    }

    public ListViewAdapter(Context context, String[] beans) {
        this.beans = beans;
        this.context = context;
        isSelected = new HashMap<Integer, Boolean>();
        // 初始化数据
        initDate();
    }
    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < beans.length; i++) {
            getIsSelected().put(i, false);
        }
    }
    @Override
    public int getCount() {
        return beans.length;
    }

    @Override
    public Object getItem(int position) {
        return beans[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isSelected.get(position)) {
                    isSelected.put(position, false);
                    setIsSelected(isSelected);
                } else {
                    isSelected.put(position, true);
                    setIsSelected(isSelected);
                }
            }
        });
        // 根据isSelected来设置checkbox的选中状况
        holder.checkbox.setChecked(getIsSelected().get(position));
        return convertView;
    }
    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        ListViewAdapter.isSelected = isSelected;
    }
}
