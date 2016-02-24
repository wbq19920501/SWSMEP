package com.jokeep.swsmep.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jokeep.swsmep.R;

/**
 * Created by wbq501 on 2016-1-25 16:52.
 * SWSMEP
 */
public class WorkFragment extends Fragment {

    View fragment;

    PullToRefreshListView list_refresh;
    BaseAdapter adapter;
    Button work_btn;
    String[] s = {"测试-","测试-","测试-","测试-","测试-","测试-","测试-","测试-","测试-","测试-","测试-"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragment == null){
            fragment = inflater.inflate(R.layout.work_fragment,container,false);
        }else {
            // 缓存的rootView需要判断是否已经被加过parent
            // 如果有parent需要从parent删除
            ViewGroup parent = (ViewGroup) fragment.getParent();
            if (parent != null) {
                parent.removeView(fragment);
            }
        }
        init();
        initdata();
        return fragment;
    }

    private void initdata() {
        work_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    protected void init() {
        work_btn = (Button) fragment.findViewById(R.id.work_btn);
        list_refresh = (PullToRefreshListView) fragment.findViewById(R.id.list_refresh);
        adapter = new BaseAdapter() {
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
                if (convertView == null){
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.work_item,null);
                    holder = new ViewHolder();
                    holder.work_name = (TextView) convertView.findViewById(R.id.work_name);
                    holder.work_title = (TextView) convertView.findViewById(R.id.work_title);
                    holder.work_context = (TextView) convertView.findViewById(R.id.work_context);
                    holder.work_num = (TextView) convertView.findViewById(R.id.work_num);
                    holder.work_img = (ImageView) convertView.findViewById(R.id.work_img);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                switch (position){
                    case 0:
//                        GradientDrawable bgshape = (GradientDrawable) holder.work_name.getBackground();
//                        bgshape.setColor(Color.RED);
                        holder.work_name.setBackground(getResources().getDrawable(R.drawable.work_round));
                        break;
                    case 1:
//                        GradientDrawable bgshape2 = (GradientDrawable) holder.work_name.getBackground();
//                        bgshape2.setColor(Color.BLUE);
                        holder.work_name.setBackground(getResources().getDrawable(R.drawable.work_round2));
                        break;
                }
                holder.work_title.setText(s[position]);
                return convertView;
            }
        };
        list_refresh.setAdapter(adapter);
    }
    static class ViewHolder{
        TextView work_name,work_title,work_context,work_num;
        ImageView work_img;
    }
}
