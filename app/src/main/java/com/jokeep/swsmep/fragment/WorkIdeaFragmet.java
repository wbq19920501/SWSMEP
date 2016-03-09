package com.jokeep.swsmep.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.adapter.WorkIdeaAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbq501 on 2016-3-3 14:50.
 * SWSMEP  意见
 */
public class WorkIdeaFragmet extends Fragment{
    View fragment;
    private ExpandableListView idea_list;
    WorkIdeaAdapter adapter;
    //定义两个List用来控制Group和Child中的String;

    private  List<String> groupArray;//组列表
    private  List<List<String>> childArray;//子列表
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragment == null){
            fragment = inflater.inflate(R.layout.work_idea,container,false);
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
    private void addInfo(String group,String []child) {

        groupArray.add(group);

        List<String>  childItem =new ArrayList<String>();

        for(int index=0;index<child.length;index++)
        {
            childItem.add(child[index]);
        }
        childArray.add(childItem);
    }
    private void init() {

        groupArray =new ArrayList<String>();
        childArray = new ArrayList<List<String>>();

        addInfo("语言", new String[]{"Oracle", "Java", "Linux", "Jquery"});
        addInfo("男人的需求", new String[]{"金钱", "事业", "权力", "女人", "房子", "车", "球"});

        adapter = new WorkIdeaAdapter(getActivity(),groupArray,childArray);

        idea_list = (ExpandableListView) fragment.findViewById(R.id.idea_list);
        idea_list.setGroupIndicator(null);
        idea_list.setAdapter(adapter);
        int groupCount = idea_list.getCount();
        for (int i = 0; i < groupCount; i++) {
            idea_list.expandGroup(i);
        }
        idea_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true;
            }
        });
    }
}
