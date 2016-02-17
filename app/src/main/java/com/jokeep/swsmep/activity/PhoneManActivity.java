package com.jokeep.swsmep.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.adapter.SortAdapter;
import com.jokeep.swsmep.base.BaseActivity;
import com.jokeep.swsmep.model.CharacterParser;
import com.jokeep.swsmep.model.PinyinComparator;
import com.jokeep.swsmep.model.SortModel;
import com.jokeep.swsmep.model.UnitBook;
import com.jokeep.swsmep.model.UserBook;
import com.jokeep.swsmep.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wbq501 on 2016-2-16 16:51.
 * SWSMEP
 */
public class PhoneManActivity extends BaseActivity{
    private TextView phone_name;
    private ListView sidebarlist;
    private TextView sidebar_dialog;
    private SideBar sidebar;

    private SortAdapter adapter;

    String PingYin;
    LinearLayout back;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    Intent intent;
    List<UserBook> listuserbook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_man);
        init();
    }

    private void init() {
        phone_name = (TextView) findViewById(R.id.phone_name);
        intent = getIntent();
        listuserbook = new ArrayList<UserBook>();
        phone_name.setText(intent.getExtras().getString("textname"));
        listuserbook = (List<UserBook>) getIntent().getSerializableExtra("userbook");
        Log.d("listuserbook",listuserbook.toString());
        sidebarlist = (ListView) findViewById(R.id.sidebarlist);
        sidebar_dialog = (TextView) findViewById(R.id.sidebar_dialog);
        sidebar = (SideBar) findViewById(R.id.sidebar);
        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAnim();
            }
        });

        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sidebar.setTextView(sidebar_dialog);

        // 设置右侧触摸监听
        sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sidebarlist.setSelection(position);
                }

            }
        });

        sidebarlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    case SCROLL_STATE_IDLE://空闲状态
                        sidebar_dialog.setVisibility(View.GONE);
                        break;
                    case SCROLL_STATE_FLING://滚动状态
                        int firstVisiblePosition = sidebarlist.getFirstVisiblePosition();
                        int lastVisiblePosition = sidebarlist.getLastVisiblePosition();
                        PingYin = characterParser.getSelling(((SortModel) adapter.getItem(firstVisiblePosition)).getName());
                        String sortString = PingYin.substring(0, 1).toUpperCase();
                        sidebar_dialog.setText(sortString);
                        sidebar_dialog.setVisibility(View.VISIBLE);
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL://触摸在屏幕上滚动
                        break;
                }
            }
            /**
             * 正在滚动
             * firstVisibleItem第一个Item的位置
             * visibleItemCount 可见的Item的数量
             * totalItemCount item的总数
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                PingYin = characterParser.getSelling(getResources().getStringArray(R.array.date)[firstVisibleItem]);
            }
        });
//        SourceDateList = filledData((String[]) list1.toArray(new String[0]),(String[]) list2.toArray(new String[0]));
        SourceDateList = filledData(getResources().getStringArray(R.array.date), getResources().getStringArray(R.array.date));

        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(this, SourceDateList);
        sidebarlist.setAdapter(adapter);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            exitAnim();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void exitAnim() {
        finish();
        overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
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
}
