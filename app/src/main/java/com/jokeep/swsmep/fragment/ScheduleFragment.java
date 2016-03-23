package com.jokeep.swsmep.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jokeep.swsmep.R;
import com.jokeep.swsmep.activity.AddScheduleActivity;
import com.jokeep.swsmep.view.ListViewForScrollView;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

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
    }
}
