package com.jokeep.swsmep.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.BaseActivity;
import com.jokeep.swsmep.db.MsgDb;
import com.jokeep.swsmep.model.CharacterParser;
import com.jokeep.swsmep.model.SortModel;
import com.jokeep.swsmep.model.UserBook;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wbq501 on 2016-2-22 09:25.
 * SWSMEP
 */
public class SearchManActivity extends BaseActivity{
    private ImageButton back;
    private EditText btn_search;
    private ListView list_msg;
    LinearLayout nomsg;

    BaseAdapter adapter;
    List<UserBook> list;
    List<SortModel> listsearch;
    private List<SortModel> SourceDateList;
    Intent intent;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_man);
        init();
        initdata();
    }

    private void initdata() {
        btn_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {//修改回车键功能
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchManActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                }
                listsearch.clear();
                String search = btn_search.getText().toString().trim();
                if (search.equals("") || search == null) {

                } else {
                    int length = search.length();
                    if (length == 1) {
                        searchname1(search);
                    } else {
                        String namestring = search.substring(0, 1).toString();
                        Pattern p = Pattern.compile("[a-zA-Z]");
                        Matcher m = p.matcher(namestring);
                        if (m.matches()) {
                            String pingyin = characterParser.getSelling(namestring);
                            String sortString = pingyin.substring(0, 1).toUpperCase();
                            for (int i = 0; i < SourceDateList.size(); i++) {
                                if (SourceDateList.get(i).getSortLetters().equals(sortString)) {
                                    SortModel sortmodel = SourceDateList.get(i);
                                    listsearch.add(sortmodel);
                                }
                            }
                        } else {
                            for (int i = 0; i < SourceDateList.size(); i++) {
                                if (SourceDateList.get(i).getName().equals(search)) {
                                    SortModel sortmodel = SourceDateList.get(i);
                                    listsearch.add(sortmodel);
                                }
                            }
                        }
                    }
                }
                if (listsearch.size() == 0) {
                    nomsg.setVisibility(View.VISIBLE);
                    list_msg.setVisibility(View.GONE);
                } else {
                    nomsg.setVisibility(View.GONE);
                    list_msg.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            exitAnim();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void exitAnim() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
    private void searchname1(String search) {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(search);
        if (m.matches()) {

        }
        p = Pattern.compile("[a-zA-Z]");
        m = p.matcher(search);
        if (m.matches()) {
            String pingyin = characterParser.getSelling(search);
            String sortString = pingyin.substring(0, 1).toUpperCase();
            for (int i = 0; i < SourceDateList.size(); i++) {
                if (SourceDateList.get(i).getSortLetters().equals(sortString)) {
                    SortModel sortmodel = SourceDateList.get(i);
                    listsearch.add(sortmodel);
                }
            }
        }
        p = Pattern.compile("[\u4e00-\u9fa5]");
        m = p.matcher(search);
        if (m.matches()) {
            String namestring = search.substring(0, 1).toString();
            for (int i = 0; i < SourceDateList.size(); i++) {
                if (SourceDateList.get(i).getName().substring(0, 1).equals(namestring)) {
                    SortModel sortmodel = SourceDateList.get(i);
                    listsearch.add(sortmodel);
                }
            }
        }
    }

    private void init() {
        list = new ArrayList<UserBook>();
        listsearch = new ArrayList<SortModel>();
        SourceDateList = new ArrayList<SortModel>();
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        DbManager.DaoConfig daoConfig= MsgDb.getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            list = db.findAll(UserBook.class);
            SourceDateList = filledData(list);
        } catch (DbException e) {
            e.printStackTrace();
        }
        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAnim();
            }
        });
        btn_search = (EditText) findViewById(R.id.btn_search);
        list_msg = (ListView) findViewById(R.id.list_msg);
        nomsg = (LinearLayout) findViewById(R.id.nomsg);
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return listsearch.size();
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
            public View getView(final int position, View view, ViewGroup parent) {
                ViewHolder viewHolder = null;
                if (view == null) {
                    viewHolder = new ViewHolder();
                    view = LayoutInflater.from(SearchManActivity.this).inflate(R.layout.linkman_item, null);
                    viewHolder.img_head = (ImageView) view.findViewById(R.id.link_img);
                    viewHolder.name = (TextView) view.findViewById(R.id.link_name);
                    viewHolder.nametype = (TextView) view.findViewById(R.id.link_type);
                    viewHolder.link_msg = (ImageView) view.findViewById(R.id.link_msg);
                    viewHolder.call_img = (ImageView) view.findViewById(R.id.link_call);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }
                final SortModel sortModel = listsearch.get(position);
                viewHolder.name.setText(sortModel.getName());
                viewHolder.nametype.setText(sortModel.getNametype());
                viewHolder.link_msg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = sortModel.getF_CALLPHONETYPE();
                        if (i == 0){
                            //0 可以打电话 1号码是空
                            String callnumber = sortModel.getCallphone().toString();
                            intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + callnumber));
                            startActivity(intent);
                        }else {
                            Toast.makeText(SearchManActivity.this, "暂无手机号码", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                viewHolder.call_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = sortModel.getF_CALLPHONETYPE();
                        if (i == 0){
                            //0 可以打电话 1号码是空
                            String callnumber = sortModel.getCallphone().toString();
                            intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callnumber));
                            startActivity(intent);
                        }else {
                            Toast.makeText(SearchManActivity.this, "暂无手机号码", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return view;
            }
        };
        list_msg.setAdapter(adapter);
    }
    static class ViewHolder{
        ImageView img_head;
        TextView name;
        TextView nametype;
        ImageView link_msg,call_img;
    }
    private List<SortModel> filledData(List<UserBook> listuserbook) {
        List<SortModel> mSortList = new ArrayList<SortModel>();
        for(int i=0; i<listuserbook.size(); i++){
            SortModel sortModel = new SortModel();
            UserBook userBook = listuserbook.get(i);
            sortModel.setName(userBook.getF_USERNAME());
            sortModel.setNametype(userBook.getF_DEPARTMENTNAME()+"-"+userBook.getF_POSITIONNAME());
            sortModel.setCallphone(userBook.getF_CALLPHONE());
            sortModel.setF_USERID(userBook.getF_USERID());
            sortModel.setF_CALLPHONETYPE(userBook.getF_CALLPHONETYPE());
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(userBook.getF_USERNAME());
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
