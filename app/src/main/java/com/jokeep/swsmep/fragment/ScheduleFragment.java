package com.jokeep.swsmep.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jokeep.swsmep.R;
import com.jokeep.swsmep.activity.AddScheduleActivity;
import com.jokeep.swsmep.activity.ScheduleMeetingActivity;
import com.jokeep.swsmep.view.ListViewForScrollView;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wbq501 on 2016-1-25 17:03.
 * SWSMEP
 */
public class ScheduleFragment extends Fragment {
    View fragment;
    ImageButton schedule_btn;
    Intent intent;
    CalendarPickerView calendar;
    ListViewForScrollView schedule_list;
    PullToRefreshScrollView schedule_scrolloview;
    BaseAdapter adapter;
    List<String> schedulelist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragment == null){
            fragment = inflater.inflate(R.layout.schedule_fragment,container,false);
        }else {
            // 缓存的rootView需要判断是否已经被加过parent
            // 如果有parent需要从parent删除
            ViewGroup parent = (ViewGroup) fragment.getParent();
            if (parent != null) {
                parent.removeView(fragment);
            }
        }
        init();
        return fragment;
    }

    protected void init() {
        schedulelist = new ArrayList<String>();
        schedulelist.add("1");
        schedulelist.add("1");
        schedulelist.add("1");
        schedulelist.add("1");
        schedulelist.add("1");

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.MONTH, 1);

        schedule_scrolloview = (PullToRefreshScrollView) fragment.findViewById(R.id.schedule_scrolloview);
        schedule_list = (ListViewForScrollView) fragment.findViewById(R.id.schedule_list);
        calendar = (CalendarPickerView) fragment.findViewById(R.id.calendar_view);

        Date today = new Date();
        calendar.init(today, nextYear.getTime())
                .withSelectedDate(today);

        schedule_btn = (ImageButton) fragment.findViewById(R.id.schedule_btn);
        schedule_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), AddScheduleActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        adapter = new ScheduleListAdapter(getActivity());
        schedule_list.setAdapter(adapter);
    }
    class ScheduleListAdapter extends BaseAdapter{
        private LayoutInflater Inflater;


        public ScheduleListAdapter(Context context){
            this.Inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return schedulelist.size();
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
                convertView = Inflater.inflate(R.layout.schedule_list_item,null);
                holder = new ViewHolder();
                holder.schedule_time = (TextView) convertView.findViewById(R.id.schedule_time);
                holder.schedule_item_title = (TextView) convertView.findViewById(R.id.schedule_item_title);
                holder.schedule_item_time = (TextView) convertView.findViewById(R.id.schedule_item_time);
                holder.schedule_item = (RelativeLayout) convertView.findViewById(R.id.schedule_item);
                holder.schedule_file = (ImageView) convertView.findViewById(R.id.schedule_file);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position==0){
                holder.schedule_item.setBackground(getResources().getDrawable(R.drawable.schedule_itemback));
                holder.schedule_item_title.setTextColor(Color.parseColor("#999999"));
                holder.schedule_item_time.setTextColor(Color.parseColor("#999999"));
                holder.schedule_file.setImageDrawable(getResources().getDrawable(R.mipmap.schedule_file1));
            }else {
                holder.schedule_item.setBackground(getResources().getDrawable(R.drawable.schedule_itemback2));
                holder.schedule_item_title.setTextColor(Color.WHITE);
                holder.schedule_item_time.setTextColor(Color.WHITE);
                holder.schedule_file.setImageDrawable(getResources().getDrawable(R.mipmap.schedule_file));
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(getActivity(), ScheduleMeetingActivity.class);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
    class ViewHolder{
        TextView schedule_time,schedule_item_title,schedule_item_time;
        RelativeLayout schedule_item;
        ImageView schedule_file;
    }
}
