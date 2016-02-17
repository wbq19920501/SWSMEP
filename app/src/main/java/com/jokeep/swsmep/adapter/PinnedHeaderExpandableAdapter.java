package com.jokeep.swsmep.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.model.CharacterParser;
import com.jokeep.swsmep.model.PinyinComparator;
import com.jokeep.swsmep.model.SortModel;
import com.jokeep.swsmep.view.PinnedHeaderExpandableListView;
import com.jokeep.swsmep.view.SideBarListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PinnedHeaderExpandableAdapter extends  BaseExpandableListAdapter implements PinnedHeaderExpandableListView.HeaderAdapter {
	private String[][] childrenData;
	private String[] groupData;
	private Context context;
	private PinnedHeaderExpandableListView listView;
	private LayoutInflater inflater;

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

	private int type = 0;
	private String s;
	public PinnedHeaderExpandableAdapter(String[][] childrenData,String[] groupData
			,Context context,PinnedHeaderExpandableListView listView,int type,String s){
		this.groupData = groupData; 
		this.childrenData = childrenData;
		this.context = context;
		this.listView = listView;
		this.type = type;
		this.s = s;
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();
		inflater = LayoutInflater.from(this.context);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childrenData[groupPosition][childPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ItemHolder holder = null;
		if (type == 1){
			if (convertView == null){
				convertView = LayoutInflater.from(context).inflate(R.layout.phone1,null);
				holder = new ItemHolder();
				convertView.setTag(holder);
				phone1findviewbyid(holder,convertView);
			}else {
				holder = (ItemHolder) convertView.getTag();
			}
			phone1data(holder);
		}else if (type ==2){
			if (convertView == null){
				convertView = LayoutInflater.from(context).inflate(R.layout.phone2,null);
				holder = new ItemHolder();
				convertView.setTag(holder);
				holder.sidebarlistview = (SideBarListView) convertView.findViewById(R.id.sidebarlist);
				holder.sidebar_dialog = (TextView) convertView.findViewById(R.id.sidebar_dialog);
//                    holder.sidebar = (SideBar) convertView.findViewById(R.id.sidebar);
			}else {
				holder = (ItemHolder) convertView.getTag();
			}
			final ItemHolder finalHolder = holder;
//			holder.sidebar.setTextView(sidebar_dialog);
//			holder.sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
//				@Override
//				public void onTouchingLetterChanged(String s) {
//					//该字母首次出现的位置
//					int position = adapter.getPositionForSection(s.charAt(0));
//					if (position != -1) {
//						finalHolder.sidebarlistview.setSelection(position);
//					}
//				}
//			});
//			holder.sidebarlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//					//这里要利用adapter.getItem(position)来获取当前position所对应的对象
//					Toast.makeText(context, ((SortModel) adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
//				}
//			});
			List<String> list1 = new ArrayList<String>();
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
			SourceDateList = filledData((String[]) list1.toArray(new String[0]),(String[]) list2.toArray(new String[0]));
//                SourceDateList = filledData(getResources().getStringArray(R.array.date));

			// 根据a-z进行排序源数据
			Collections.sort(SourceDateList, pinyinComparator);
			adapter = new SortAdapter(context, SourceDateList);
			holder.sidebarlistview.setAdapter(adapter);

			int position = adapter.getPositionForSection(s.charAt(0));
			if (position != -1) {
				finalHolder.sidebarlistview.setSelection(position);
			}
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
//		return childrenData[groupPosition].length;
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupData[groupPosition];
	}

	@Override
	public int getGroupCount() {
		return groupData.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view = null;  
        if (convertView != null) {  
            view = convertView;  
        } else {  
            view = createGroupView();  
        } 
        
        ImageView iv = (ImageView)view.findViewById(R.id.phone0_img);
		
		if (isExpanded) {
			iv.setBackgroundResource(R.mipmap.phone1);
		}
		else{
			iv.setBackgroundResource(R.mipmap.phone0);
		}
        
        TextView text = (TextView)view.findViewById(R.id.phone0_text);
        text.setText(groupData[groupPosition]);  
        return view;  
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	private View createChildrenView() {
		return inflater.inflate(R.layout.phone1, null);
	}
	
	private View createGroupView() {
		return inflater.inflate(R.layout.phone0, null);
	}

	@Override
	public int getHeaderState(int groupPosition, int childPosition) {
		final int childCount = getChildrenCount(groupPosition);
		if (childPosition == childCount - 1) {
			return PINNED_HEADER_PUSHED_UP;
		} else if (childPosition == -1 && !listView.isGroupExpanded(groupPosition)) {
			return PINNED_HEADER_GONE;
		} else {
			return PINNED_HEADER_VISIBLE;
		}
	}

	@Override
	public void configureHeader(View header, int groupPosition,
			int childPosition, int alpha) {
		String groupData =  this.groupData[groupPosition];
		((TextView) header.findViewById(R.id.phone0_text)).setText(groupData);
		
	}
	
	private SparseIntArray groupStatusMap = new SparseIntArray(); 
	
	@Override
	public void setGroupClickStatus(int groupPosition, int status) {
		groupStatusMap.put(groupPosition, status);
	}

	@Override
	public int getGroupClickStatus(int groupPosition) {
		if (groupStatusMap.keyAt(groupPosition)>=0) {
			return groupStatusMap.get(groupPosition);
		} else {
			return 0;
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
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
		context.startActivity(intent);
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

		SideBarListView sidebarlistview;
		TextView sidebar_dialog;
//        SideBar sidebar;
	}
}
