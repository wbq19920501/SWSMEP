package com.jokeep.swsmep.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jokeep.swsmep.R;

/**
 * Created by wbq501 on 2016-3-3 16:26.
 * SWSMEP
 */
public class WorkAnnexFragment extends Fragment{
    View fragment;
    ListView list;
    BaseAdapter adapter;
    String[] s = {"1","2","3"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragment == null){
            fragment = inflater.inflate(R.layout.workannex,container,false);
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

    }

    private void init() {
        list = (ListView) fragment.findViewById(R.id.listwork_annex);
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
                if (convertView==null){
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.look_fileitem,null);
                    holder = new ViewHolder();
                    holder.filename = (TextView) convertView.findViewById(R.id.file_name);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                return convertView;
            }
        };
        list.setAdapter(adapter);
    }
    class ViewHolder{
        TextView filename;
    }
}
