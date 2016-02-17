package com.jokeep.swsmep.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.activity.PhoneManActivity;
import com.jokeep.swsmep.activity.PhoneManMsgActivity;

/**
 * Created by wbq501 on 2016-2-16 16:15.
 * SWSMEP
 */
public class Phone3Fragment extends Fragment{
    View fragment;
    private ListView list;
    private BaseAdapter adapter;
    String[] s = {"测试1","测试2"};
    Intent intent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragment == null){
            fragment = inflater.inflate(R.layout.phone3_fragment,container,false);
//            fragment = inflater.inflate(R.layout.phone1,container,false);
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

    private void init() {
        list = (ListView) fragment.findViewById(R.id.phone3_list);
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
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (convertView == null){
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.phone3_list_item,null);
                    holder = new ViewHolder();
                    holder.textname = (TextView) convertView.findViewById(R.id.phone3_list_text);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.textname.setText(s[position]);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position == 0){
                            intent = new Intent(getActivity(), PhoneManActivity.class);
                            intent.putExtra("textname", s[position]);
                        }else {
                            intent = new Intent(getActivity(), PhoneManMsgActivity.class);
                        }
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                    }
                });
                return convertView;
            }
        };
        list.setAdapter(adapter);
    }
    static class ViewHolder{
        TextView textname;
    }
}
