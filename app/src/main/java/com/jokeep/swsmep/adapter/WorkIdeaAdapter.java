package com.jokeep.swsmep.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.model.SuggestionFilesInfo;
import com.jokeep.swsmep.model.SuggestionInfo;
import com.jokeep.swsmep.model.SuggestionsInfo;
import com.jokeep.swsmep.view.RoundImageView;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * Created by wbq501 on 2016-3-4 15:50.
 * SWSMEP
 */
public class WorkIdeaAdapter extends BaseExpandableListAdapter{
    private Context context;
    private LayoutInflater inflater;
    List<SuggestionInfo> suggestionInfos;

    public WorkIdeaAdapter(Context context, List<SuggestionInfo> suggestionInfos) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.suggestionInfos = suggestionInfos;
    }
    @Override
    public int getGroupCount() {
        return suggestionInfos.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return suggestionInfos.get(groupPosition).getSuggestionsInfos().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return getGroup(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return suggestionInfos.get(groupPosition).getSuggestionsInfos().get(childPosition);
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
            holder.man_name = (TextView) convertView.findViewById(R.id.man_name);
            holder.man_type = (TextView) convertView.findViewById(R.id.man_type);
            holder.man_time = (TextView) convertView.findViewById(R.id.man_time);
            holder.man_img = (RoundImageView) convertView.findViewById(R.id.man_img);
            holder.man_context = (TextView) convertView.findViewById(R.id.man_context);
            holder.files = (LinearLayout) convertView.findViewById(R.id.files);
            holder.man_imgs = (ImageView) convertView.findViewById(R.id.man_imgs);
            holder.addview = (LinearLayout) convertView.findViewById(R.id.addview);
            holder.return_msg = (Button) convertView.findViewById(R.id.return_msg);
            convertView.setTag(holder);
        }else {
            holder = (ViewGroipHolder) convertView.getTag();
        }
        SuggestionInfo suggestionInfo = suggestionInfos.get(groupPosition);
        List<SuggestionFilesInfo> suggestionFilesInfos = suggestionInfo.getSuggestionFilesInfos();
        holder.man_name.setText(suggestionInfo.getF_HANDLENAME());
        holder.man_type.setText(suggestionInfo.getF_DEPARTMENTNAME()+"-"+suggestionInfo.getF_POSITIONNAME());
        holder.man_time.setText(suggestionInfo.getF_HANDLETIME());
        holder.man_context.setText(suggestionInfo.getF_OPINION());
        for (int i=0;i<suggestionFilesInfos.size();i++){
            TextView textView = new TextView(context);
            textView.setText(suggestionFilesInfos.get(i).getF_FILENAME());
            textView.setTextColor(Color.parseColor("#21ac69"));
            holder.addview.addView(textView);
        }
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setRadius(DensityUtil.dip2px(5))//ImageView圆角半径
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.mipmap.ic_launcher)
                .setFailureDrawableId(R.mipmap.ic_launcher)
                .build();
        x.image().bind(holder.man_img, suggestionInfo.getF_USERHEADURI(), imageOptions);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewChildHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.wrok_idea_item2,null);
            holder = new ViewChildHolder();
            holder.man_name = (TextView) convertView.findViewById(R.id.man_name);
            holder.man_type = (TextView) convertView.findViewById(R.id.man_type);
            holder.man_context = (TextView) convertView.findViewById(R.id.man_context);
            holder.files = (LinearLayout) convertView.findViewById(R.id.files);
            holder.man_imgs = (ImageView) convertView.findViewById(R.id.man_imgs);
            holder.man_time = (TextView) convertView.findViewById(R.id.man_time);
            convertView.setTag(holder);
        }else {
            holder = (ViewChildHolder) convertView.getTag();
        }
        SuggestionsInfo suggestionsInfo = suggestionInfos.get(groupPosition).getSuggestionsInfos().get(childPosition);
        holder.man_name.setText(suggestionsInfo.getF_USERNAME());
        holder.man_type.setText(suggestionsInfo.getF_DEPARTMENTNAME()+"-"+suggestionsInfo.getF_POSITIONNAME());
        holder.man_context.setText(suggestionsInfo.getF_OPINION());
        holder.man_time.setText(suggestionsInfo.getF_REPLYTIME());
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
        ImageView man_imgs;
        LinearLayout files;
        Button return_msg;
        LinearLayout addview;
    }
    class ViewChildHolder{
        TextView man_name,man_type,man_time;
        TextView man_context;
        ImageView man_imgs;
        LinearLayout files;
    }
}
