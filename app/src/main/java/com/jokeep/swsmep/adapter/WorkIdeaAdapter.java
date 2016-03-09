package com.jokeep.swsmep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.view.RoundImageView;

import java.util.List;

/**
 * Created by wbq501 on 2016-3-4 15:50.
 * SWSMEP
 */
public class WorkIdeaAdapter extends BaseExpandableListAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<String> groupArray;
    private List<List<String>> childArray;
    public WorkIdeaAdapter(Context context, List<String> groupArray, List<List<String>> childArray){
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.groupArray = groupArray;
        this.childArray = childArray;
    }
    @Override
    public int getGroupCount() {
        return groupArray.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
//        return groupPosition;
        return childArray.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return getGroup(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childArray.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewGroipHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.wrok_idea_item1,null);
            holder = new ViewGroipHolder();
            convertView.setTag(holder);
        }else {
            holder = (ViewGroipHolder) convertView.getTag();
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewChildHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.wrok_idea_item2,null);
            holder = new ViewChildHolder();
            convertView.setTag(holder);
        }else {
            holder = (ViewChildHolder) convertView.getTag();
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    class ViewGroipHolder{
        RoundImageView man_img;
        TextView man_name,man_type,man_time;
        TextView man_context;
        TextView man_files;
        ImageView man_imgs;
        LinearLayout files;
        Button return_msg;
        LinearLayout addview;
    }
    class ViewChildHolder{
        TextView man_name,man_type,man_time;
        TextView man_context;
        TextView man_files;
        ImageView man_imgs;
        LinearLayout files;
    }
}
