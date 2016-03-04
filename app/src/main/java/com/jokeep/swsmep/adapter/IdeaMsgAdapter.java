package com.jokeep.swsmep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.model.DataHolder;

import java.util.List;

/**
 * Created by wbq501 on 2016-3-4 14:19.
 * SWSMEP
 */
public class IdeaMsgAdapter extends BaseAdapter {

    private List<DataHolder> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public IdeaMsgAdapter(Context context, List<DataHolder> list) {
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

            convertView = mInflater.inflate(R.layout.add_me_msg, null);
            holder.msg_name = (TextView) convertView.findViewById(R.id.msg_name);
            holder.checkbox_memsg = (CheckBox) convertView.findViewById(R.id.checkbox_memsg);
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        holder.msg_name.setText((String) mList.get(position).context);
        holder.checkbox_memsg.setChecked(mList.get(position).checked);
        return convertView;
    }

    public class ViewHolder {
        public TextView msg_name;
        public CheckBox checkbox_memsg;
    };
}
