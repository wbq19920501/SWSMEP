package com.jokeep.swsmep.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.jokeep.swsmep.R;
import com.jokeep.swsmep.adapter.PinnedHeaderExpandableAdapter;
import com.jokeep.swsmep.view.PinnedHeaderExpandableListView;
import com.jokeep.swsmep.view.SideBar;

/**
 * Created by wbq501 on 2016-2-15 11:48.
 * SWSMEP
 */
public class Phone2Fragment extends Fragment{
    private String[][] childrenData = new String[10][10];
    private String[] groupData = new String[10];
    private int expandFlag = -1;//控制列表的展开
    private PinnedHeaderExpandableAdapter adapter;

    private PinnedHeaderExpandableListView exlistview;

    private View fragment;
    private SideBar sidebar;
    private TextView sidebar_dialog;
    int type = 2;
    private String string = "z";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragment == null){
            fragment = inflater.inflate(R.layout.phone_fragment,container,false);
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
        initData();
        return fragment;
    }

    private void init() {
        exlistview = (PinnedHeaderExpandableListView) fragment.findViewById(R.id.phone_exlistview);
        sidebar = (SideBar) fragment.findViewById(R.id.sidebar);
        sidebar_dialog = (TextView) fragment.findViewById(R.id.sidebar_dialog);
    }
    /**
     * 初始化数据
     */
    private void initData() {
        for(int i=0;i<10;i++){
            groupData[i] = "分组"+i;
        }

        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                childrenData[i][j] = "好友"+i+"-"+j;
            }
        }
        //设置悬浮头部VIEW
        exlistview.setHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.phone0_head, exlistview, false));
        adapter = new PinnedHeaderExpandableAdapter(childrenData, groupData, getActivity(),exlistview,type,string);
        exlistview.setAdapter(adapter);
        sidebar.setTextView(sidebar_dialog);
        sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(final String s) {
                string = s;
                adapter.notifyDataSetChanged();
            }
        });

        //设置单个分组展开
        //exlistview.setOnGroupClickListener(new GroupClickListener());
    }
    class GroupClickListener implements ExpandableListView.OnGroupClickListener {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {
            if (expandFlag == -1) {
                // 展开被选的group
                exlistview.expandGroup(groupPosition);
                // 设置被选中的group置于顶端
                exlistview.setSelectedGroup(groupPosition);
                expandFlag = groupPosition;
            } else if (expandFlag == groupPosition) {
                exlistview.collapseGroup(expandFlag);
                expandFlag = -1;
            } else {
                exlistview.collapseGroup(expandFlag);
                // 展开被选的group
                exlistview.expandGroup(groupPosition);
                // 设置被选中的group置于顶端
                exlistview.setSelectedGroup(groupPosition);
                expandFlag = groupPosition;
            }
            return true;
        }
    }
}
