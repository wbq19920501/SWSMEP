package com.jokeep.swsmep.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jokeep.swsmep.R;
import com.jokeep.swsmep.activity.WorkActivity;
import com.jokeep.swsmep.view.WorkPoPw;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbq501 on 2016-1-25 16:52.
 * SWSMEP
 */
public class WorkFragment extends Fragment {

    View fragment;

    PullToRefreshListView list_refresh;
    BaseAdapter adapter;
    ImageButton work_btn;
    WorkPoPw poPw;
    String[] s = {"测试1","测试2","测试3","测试4","测试5","测试6","测试7","测试8","测试9","测试10","测试11"};
//    String[] colors = {"#6BC773","#5C6BC0","#F75D8C","#008CEE"};
    List<String> colors ;
    Intent intent;
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
                poPw = new WorkPoPw(getActivity(),onclick);
                poPw.showAtLocation(work_btn, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }
    private View.OnClickListener onclick = new View.OnClickListener(){

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img1:
                    intent = new Intent(getActivity(), WorkActivity.class);
                    startActivity(intent);
                    break;
                case R.id.img2:
                    break;
                case R.id.img3:
                    break;
            }
            getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            poPw.dismiss();
        }
    };
    protected void init() {
        colors = new ArrayList<String>();
//        for (int i=0;i<s.length;i++){
//            switch (i){
//                case 0:
//                    colors.add("#6BC773");
//                    break;
//                case 1:
//                    colors.add("#5C6BC0");
//                    break;
//                case 3:
//                    colors.add("#F75D8C");
//                    break;
//                case 4:
//                    colors.add("#008CEE");
//                    break;
//                default:
//                    colors.add("#008CEE");
//                    break;
//            }
//        }
        work_btn = (ImageButton) fragment.findViewById(R.id.work_btn);
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
                holder.work_title.setText(s[position]);
                switch (position){
                    case 0:
                        colors.add("#6BC773");
                        break;
                    case 1:
                        colors.add("#5C6BC0");
                        break;
                    case 3:
                        colors.add("#F75D8C");
                        break;
                    case 4:
                        colors.add("#008CEE");
                        break;
                    default:
                        colors.add("#008CEE");
                        break;
                }
                GradientDrawable bgshape = (GradientDrawable) holder.work_name.getBackground();
                bgshape.setColor(Color.parseColor(colors.get(position)));
//                switch (position){
//                    case 0:
////                        holder.work_name.setBackground(getResources().getDrawable(R.drawable.work_round));
//                        break;
//                    case 1:
////                        GradientDrawable bgshape2 = (GradientDrawable) holder.work_name.getBackground();
//                        bgshape.setColor(Color.parseColor(colors[position]));
////                        holder.work_name.setBackground(getResources().getDrawable(R.drawable.work_round2));
//                        break;
//                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                return convertView;
            }
        };
        list_refresh.setAdapter(adapter);
    }
    class ViewHolder{
        TextView work_name,work_title,work_context,work_num;
        ImageView work_img;
    }
}
