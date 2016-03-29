package com.jokeep.swsmep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.model.Work2Info;

import java.util.List;

/**
 * Created by wbq501 on 2016-3-28 16:16.
 * trunk
 */
public class ZimuAdapter extends BaseAdapter implements SectionIndexer {
    private List<Work2Info> list = null;
    private Context mContext;

    public ZimuAdapter(Context mContext, List<Work2Info> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void updateListView(List<Work2Info> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.select_man1_item, null);
            holder.onclick = (LinearLayout) view.findViewById(R.id.onclick);
            holder.link_name = (TextView) view.findViewById(R.id.link_name);
            holder.link_type = (TextView) view.findViewById(R.id.link_type);
            holder.checkbox = (CheckBox) view.findViewById(R.id.checkbox);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Work2Info work2Info = list.get(position);
        holder.link_name.setText(work2Info.getName());
        holder.link_type.setText(work2Info.getNametype());
        holder.checkbox.setChecked(work2Info.getCheck());
        return view;
    }

    public class ViewHolder {
        TextView link_name,link_type;
        CheckBox checkbox;
        LinearLayout onclick;
    };


    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public Object[] getSections() {
        return null;
    }
}
