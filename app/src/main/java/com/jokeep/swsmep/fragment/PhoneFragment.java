package com.jokeep.swsmep.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.adapter.SortAdapter;
import com.jokeep.swsmep.base.BaseFragment;
import com.jokeep.swsmep.model.CharacterParser;
import com.jokeep.swsmep.model.PinyinComparator;
import com.jokeep.swsmep.model.SortModel;
import com.jokeep.swsmep.view.SideBar;
import com.jokeep.swsmep.view.SideBarListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wbq501 on 2016-1-25 17:06.
 * SWSMEP
 */
public class PhoneFragment extends Fragment {
    private ExpandableListView exlistview;
    private List<List<String>> childarray;
    private List<String> array;
    View fragment;
    TextView sidebar_dialog;
    SideBar sidebar2;
    Intent intent;
    private int type;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private SortAdapter adapter;

    int h = 0;

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
        return fragment;
    }

    protected void init() {
        type = 2;
        exlistview = (ExpandableListView) fragment.findViewById(R.id.phone_exlistview);
        sidebar_dialog = (TextView) fragment.findViewById(R.id.sidebar_dialog);
        sidebar2 = (SideBar) fragment.findViewById(R.id.sidebar);
        sidebar2.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
//                    sidebarlistview.setSelection(position);
                }
            }
        });
        childarray = new ArrayList<List<String>>();
        array = new ArrayList<String>();
        array.add("1");
        array.add("2");
        List<String> tempArray = new  ArrayList<String>();
        tempArray.add("第一条");

        for (int  index = 0 ; index <array.size(); ++index) {
            childarray.add(tempArray);
        }
        exlistview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0, count = exlistview
                        .getExpandableListAdapter().getGroupCount(); i < count; i++) {
                    if (groupPosition != i) {// 关闭其他分组
                        exlistview.collapseGroup(i);
                    }
                }
            }
        });
        exlistview.setAdapter(new ExpandableAdapter());

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();
    }
    private class ExpandableAdapter extends BaseExpandableListAdapter{
        @Override
        public int getGroupCount() {
            return array.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
//            return childarray.get(groupPosition).size();
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return array.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childarray.get(groupPosition).get(childPosition);
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
            GroipHolder holder;
            if (convertView == null){
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.phone0,null);
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT, 100);
                convertView.setLayoutParams(lp);
                holder = new GroipHolder();
                holder.img = (ImageView) convertView.findViewById(R.id.phone0_img);
                holder.textname = (TextView) convertView.findViewById(R.id.phone0_text);
                convertView.setTag(holder);
            }else {
                holder = (GroipHolder) convertView.getTag();
            }
            if (isExpanded){
                holder.img.setBackgroundResource(R.mipmap.phone1);
            }else {
                holder.img.setBackgroundResource(R.mipmap.phone0);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ItemHolder holder = null;
            if (type == 1){
                if (convertView == null){
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.phone1,null);
                    holder = new ItemHolder();
                    convertView.setTag(holder);
                    phone1findviewbyid(holder,convertView);
                }else {
                    holder = (ItemHolder) convertView.getTag();
                }
                phone1data(holder);
            }else if (type ==2){
                if (convertView == null){
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.phone2,null);
                    holder = new ItemHolder();
                    convertView.setTag(holder);
                    holder.sidebarlistview = (SideBarListView) convertView.findViewById(R.id.sidebarlist);
                    holder.sidebar_dialog = (TextView) convertView.findViewById(R.id.sidebar_dialog);
//                    holder.sidebar = (SideBar) convertView.findViewById(R.id.sidebar);
                }else {
                    holder = (ItemHolder) convertView.getTag();
                }
                final ItemHolder finalHolder = holder;
                WindowManager wm = (WindowManager) getContext()
                        .getSystemService(Context.WINDOW_SERVICE);
                int height = wm.getDefaultDisplay().getHeight();
                h = fragment.getHeight();
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        60, h);
                lp.gravity = Gravity.RIGHT;
//                holder.sidebar.setLayoutParams(lp);

                FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.FILL_PARENT, h);

                holder.sidebarlistview.setLayoutParams(lp2);
//                holder.sidebar.setTextView(sidebar_dialog);
//                holder.sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
//                    @Override
//                    public void onTouchingLetterChanged(String s) {
//                        //该字母首次出现的位置
//                        int position = adapter.getPositionForSection(s.charAt(0));
//                        if (position != -1) {
//                            finalHolder.sidebarlistview.setSelection(position);
//                        }
//                    }
//                });
//                holder.sidebarlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        //这里要利用adapter.getItem(position)来获取当前position所对应的对象
//                        Toast.makeText(getActivity(), ((SortModel) adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
//                    }
//                });
                List<String> list1 = new ArrayList<String>();
                list1.add("阿妹");
                list1.add("陈奕迅");
                list1.add("曾一鸣");
                list1.add("成龙");
                list1.add("王力宏");
                list1.add("李德华");
                list1.add("白水水");
                list1.add("齐天大圣");
                list1.add("阿妹");
                list1.add("陈奕迅");
                list1.add("曾一鸣");
                list1.add("成龙");
                list1.add("王力宏");
                list1.add("李德华");
                list1.add("白水水");
                list1.add("齐天大圣");
                List<String> list2 = new ArrayList<String>();
                list2.add("办公司处长1");
                list2.add("办公司委员2");
                list2.add("办公司处长3");
                list2.add("办公司委员4");
                list2.add("办公司处长5");
                list2.add("办公司委员6");
                list2.add("办公司处长7");
                list2.add("办公司委员8");
                list2.add("办公司处长1");
                list2.add("办公司委员2");
                list2.add("办公司处长3");
                list2.add("办公司委员4");
                list2.add("办公司处长5");
                list2.add("办公司委员6");
                list2.add("办公司处长7");
                list2.add("办公司委员8");
                SourceDateList = filledData((String[]) list1.toArray(new String[0]),(String[]) list2.toArray(new String[0]));
//                SourceDateList = filledData(getResources().getStringArray(R.array.date));

                // 根据a-z进行排序源数据
                Collections.sort(SourceDateList, pinyinComparator);
                adapter = new SortAdapter(getActivity(), SourceDateList);
                holder.sidebarlistview.setAdapter(adapter);
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    private List<SortModel> filledData(String[] date,String[] date2) {
        List<SortModel> mSortList = new ArrayList<SortModel>();
        for(int i=0; i<date.length; i++){
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            sortModel.setNametype(date2[i]);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }
            mSortList.add(sortModel);
        }
        return mSortList;
    }

    private void phone1data(final ItemHolder holder) {
        callphoneup(holder);
        callphonedown(holder);
    }

    private void callphonedown(final ItemHolder holder) {
        holder.call2_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = holder.call2_1.getText().toString();
                callphone(number);
            }
        });
        holder.call2_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = holder.call2_3.getText().toString();
                callphone(number);
            }
        });
        holder.callphone2_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = holder.callphone2_1.getText().toString();
                callphone(number);
            }
        });
        holder.callphone2_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = holder.callphone2_3.getText().toString();
                callphone(number);
            }
        });
    }

    private void callphoneup(final ItemHolder holder) {
        holder.call1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = holder.call1_1.getText().toString();
                callphone(number);
            }
        });
        holder.call1_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = holder.call1_3.getText().toString();
                callphone(number);
            }
        });
        holder.callphone1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = holder.callphone1_1.getText().toString();
                callphone(number);
            }
        });
        holder.callphone1_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = holder.callphone1_3.getText().toString();
                callphone(number);
            }
        });
    }

    private void callphone(String number) {
        intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private void phone1findviewbyid(ItemHolder holder,View convertView) {
        holder.phone1_call1  = (LinearLayout) convertView.findViewById(R.id.phone1_call1);
        holder.phone1_call2  = (LinearLayout) convertView.findViewById(R.id.phone1_call2);
        holder.call1  = (LinearLayout) convertView.findViewById(R.id.call1);
        holder.call1_0  = (LinearLayout) convertView.findViewById(R.id.call1_0);
        holder.callphone1  = (LinearLayout) convertView.findViewById(R.id.callphone1);
        holder.callphone1_0  = (LinearLayout) convertView.findViewById(R.id.callphone1_0);
        holder.fax1_1  = (LinearLayout) convertView.findViewById(R.id.fax1_1);
        holder.postcode1_1  = (LinearLayout) convertView.findViewById(R.id.postcode1_1);
        holder.email1_1  = (LinearLayout) convertView.findViewById(R.id.email1_1);
        holder.adress1_1  = (LinearLayout) convertView.findViewById(R.id.adress1_1);

        holder.call1_1  = (TextView) convertView.findViewById(R.id.call1_1);
        holder.call1_3  = (TextView) convertView.findViewById(R.id.call1_3);
        holder.callphone1_1  = (TextView) convertView.findViewById(R.id.callphone1_1);
        holder.callphone1_3  = (TextView) convertView.findViewById(R.id.callphone1_3);
        holder.fax1_2  = (TextView) convertView.findViewById(R.id.fax1_2);
        holder.postcode1_2  = (TextView) convertView.findViewById(R.id.postcode1_2);
        holder.email1_2  = (TextView) convertView.findViewById(R.id.email1_2);
        holder.adress1_2  = (TextView) convertView.findViewById(R.id.adress1_2);

        holder.call1_2  = (ImageView) convertView.findViewById(R.id.call1_2);
        holder.call1_4  = (ImageView) convertView.findViewById(R.id.call1_4);
        holder.callphone1_2  = (ImageView) convertView.findViewById(R.id.callphone1_2);
        holder.callphone1_4  = (ImageView) convertView.findViewById(R.id.callphone1_4);

        holder.call2  = (LinearLayout) convertView.findViewById(R.id.call2);
        holder.call2_0  = (LinearLayout) convertView.findViewById(R.id.call2_0);
        holder.callphone2  = (LinearLayout) convertView.findViewById(R.id.callphone2);
        holder.callphone2_0  = (LinearLayout) convertView.findViewById(R.id.callphone2_0);
        holder.fax2_1  = (LinearLayout) convertView.findViewById(R.id.fax2_1);
        holder.postcode2_1  = (LinearLayout) convertView.findViewById(R.id.postcode2_1);
        holder.email2_1  = (LinearLayout) convertView.findViewById(R.id.email2_1);
        holder.adress2_1  = (LinearLayout) convertView.findViewById(R.id.adress2_1);

        holder.call2_1  = (TextView) convertView.findViewById(R.id.call2_1);
        holder.call2_3  = (TextView) convertView.findViewById(R.id.call2_3);
        holder.callphone2_1  = (TextView) convertView.findViewById(R.id.callphone2_1);
        holder.callphone2_3  = (TextView) convertView.findViewById(R.id.callphone2_3);
        holder.fax2_2  = (TextView) convertView.findViewById(R.id.fax2_2);
        holder.postcode2_2  = (TextView) convertView.findViewById(R.id.postcode2_2);
        holder.email2_2  = (TextView) convertView.findViewById(R.id.email2_2);
        holder.adress2_2  = (TextView) convertView.findViewById(R.id.adress2_2);

        holder.call2_2  = (ImageView) convertView.findViewById(R.id.call2_2);
        holder.call2_4  = (ImageView) convertView.findViewById(R.id.call2_4);
        holder.callphone2_2  = (ImageView) convertView.findViewById(R.id.callphone2_2);
        holder.callphone2_4  = (ImageView) convertView.findViewById(R.id.callphone2_4);
    }

    static class GroipHolder{
        ImageView img;
        TextView textname;
    }
    static class ItemHolder{
        LinearLayout phone1_call1,phone1_call2;
        LinearLayout call1,call1_0,callphone1,callphone1_0,
                fax1_1,postcode1_1,email1_1,adress1_1;
        TextView call1_1,call1_3,callphone1_1,callphone1_3,
                fax1_2,postcode1_2,email1_2,adress1_2;
        ImageView call1_2,call1_4,callphone1_2,callphone1_4;

        LinearLayout call2,call2_0,callphone2,callphone2_0,
                fax2_1,postcode2_1,email2_1,adress2_1;
        TextView call2_1,call2_3,callphone2_1,callphone2_3,
                fax2_2,postcode2_2,email2_2,adress2_2;
        ImageView call2_2,call2_4,callphone2_2,callphone2_4;

        SideBarListView sidebarlistview;
        TextView sidebar_dialog;
//        SideBar sidebar;
    }
}
