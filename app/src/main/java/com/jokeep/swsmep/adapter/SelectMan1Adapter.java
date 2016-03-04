package com.jokeep.swsmep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.model.Work2Info;

import java.util.List;

/**
 * Created by wbq501 on 2016-3-4 14:38.
 * SWSMEP
 */
public class SelectMan1Adapter extends BaseAdapter {

    private List<Work2Info> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public SelectMan1Adapter(Context context, List<Work2Info> list) {
        mList = list;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.select_man1_item, null);
            holder.onclick = (LinearLayout) convertView.findViewById(R.id.onclick);
            holder.link_name = (TextView) convertView.findViewById(R.id.link_name);
            holder.link_type = (TextView) convertView.findViewById(R.id.link_type);
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }
        Work2Info work2Info = mList.get(position);
        holder.link_name.setText(work2Info.getF_USERNAME());
        holder.link_type.setText(work2Info.getF_DEPARTMENTNAME()+"-"+work2Info.getF_POSITIONNAME());
//        holder.link_name.setText((String) mList.get(position).title);
//        holder.link_type.setText((String) mList.get(position).context);
        holder.checkbox.setChecked(work2Info.getCheck());
        return convertView;
    }

    public class ViewHolder {
        TextView link_name,link_type;
        CheckBox checkbox;
        LinearLayout onclick;
    };
}
