package com.jokeep.swsmep.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.activity.AddWorkActivity;
import com.jokeep.swsmep.activity.LookWorkActivity;
import com.jokeep.swsmep.model.Work1Info;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wbq501 on 2016-2-25 15:34.
 * SWSMEP
 */
public class WorkTabAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<Work1Info> work1Infos;
    private int typeopen;
    Intent intent;
    String TOKENID;
    public WorkTabAdapter(Context context, List<Work1Info> work1Infos,int typeopen,String TOKENID) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.work1Infos = work1Infos;
        this.typeopen = typeopen;
        this.TOKENID = TOKENID;
    }

    @Override
    public int getCount() {
        return work1Infos.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.worktab_item,null);
            holder = new ViewHolder();
            holder.worktab_title = (TextView) convertView.findViewById(R.id.worktab_title);
            holder.worktab_name = (TextView) convertView.findViewById(R.id.worktab_name);
            holder.worktab_time = (TextView) convertView.findViewById(R.id.worktab_time);
            holder.worktab_state = (TextView) convertView.findViewById(R.id.worktab_state);
            holder.worktab_img = (ImageView) convertView.findViewById(R.id.worktab_img);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (typeopen){
                    case 1:
                        intent = new Intent(context,LookWorkActivity.class);
                        break;
                    case 2:
                        intent = new Intent(context, AddWorkActivity.class);
                        break;
                    case 3:
                        intent = new Intent(context,LookWorkActivity.class);
                        break;
                    case 4:
                        intent = new Intent(context,LookWorkActivity.class);
                        break;
                }
                intent.putExtra("typeopen",typeopen);
                intent.putExtra("work1Infos", (Serializable) work1Infos);
                intent.putExtra("intposition",position);
                intent.putExtra("TOKENID",TOKENID);
                context.startActivity(intent);
                ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            }
        });
        Work1Info work1Info = work1Infos.get(position);
        holder.worktab_title.setText(work1Info.getF_TITLE());
        int typename = work1Info.getTypename();
        if (typename==0){
            holder.worktab_name.setVisibility(View.GONE);
        }else {
            holder.worktab_name.setVisibility(View.VISIBLE);
            holder.worktab_name.setText(work1Info.getF_SPONSUSER());
        }
        holder.worktab_time.setText(work1Info.getF_SPONSTIME());
        int type = work1Info.getType();
        if (type == 0){
            holder.worktab_state.setVisibility(View.GONE);
        }else {
            holder.worktab_state.setVisibility(View.VISIBLE);
            holder.worktab_state.setText(work1Info.getF_STATENAME());
            holder.worktab_state.setTextColor(Color.parseColor(work1Info.getF_COLOR()));
        }
        int f_isatt = work1Info.getF_ISATT();
        if (f_isatt == 0){
            holder.worktab_img.setVisibility(View.GONE);
        }else {
            holder.worktab_img.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder{
        TextView worktab_title,worktab_name,
                worktab_time,worktab_state;
        ImageView worktab_img;
    }
}
