package com.jokeep.swsmep.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.BaseFragment;

import java.util.ArrayList;
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
    Intent intent;
    private int type;
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
    }
    private class ExpandableAdapter extends BaseExpandableListAdapter{
        @Override
        public int getGroupCount() {
            return array.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childarray.get(groupPosition).size();
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
                }else {
                    holder = (ItemHolder) convertView.getTag();
                }
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
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

    class GroipHolder{
        ImageView img;
        TextView textname;
    }
    class ItemHolder{
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
    }
}
