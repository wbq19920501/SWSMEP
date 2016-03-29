package com.jokeep.swsmep.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.adapter.SelectMan1Adapter;
import com.jokeep.swsmep.adapter.ZimuAdapter;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.model.CharacterParser;
import com.jokeep.swsmep.model.PinyinComparator2;
import com.jokeep.swsmep.model.Work2Info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wbq501 on 2016-1-28 09:38.
 * SWSMEP
 */
public class SelectMan1Window extends PopupWindow{
    private View mMenuView;
    ImageButton back;
    private LinearLayout sear_edit;
    private ImageView selectman_sx;
    private EditText selectman_search;
    private ListView selectman_list1;
    SelectMan1Adapter adapter;
    Activity context;
    private ShowDialog dialog;
    Button btn_sub;
    List<Integer> listItemID = new ArrayList<Integer>();
    List<Work2Info> list;
    List<Work2Info> getlist;
    TextView phone_name;
    // 数据接口
    OnGetData ongetdata;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator2 pinyinComparator;
    ZimuAdapter adapter2;
    boolean adapter2choose = false;
    private TextView sidebar_dialog;
    FrameLayout msg_list;
    LinearLayout no_no_msg;


    public SelectMan1Window(final Activity context1, View.OnClickListener itemclick,String UserID, final String TOKENID){
        super();
        this.context = context1;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.select_man1, null);
        list = new ArrayList<Work2Info>();
        getlist = new ArrayList<Work2Info>();
        dialog = new ShowDialog(context,R.style.MyDialog,context.getResources().getString(R.string.dialogmsg));

        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator2();

        back = (ImageButton) mMenuView.findViewById(R.id.back);
        phone_name = (TextView) mMenuView.findViewById(R.id.phone_name);
        selectman_sx = (ImageView) mMenuView.findViewById(R.id.selectman_sx);
        selectman_search = (EditText) mMenuView.findViewById(R.id.selectman_search);
        btn_sub = (Button) mMenuView.findViewById(R.id.btn_sub);
        selectman_list1 = (ListView) mMenuView.findViewById(R.id.selectman_list1);
        sidebar_dialog = (TextView) mMenuView.findViewById(R.id.sidebar_dialog);
        sear_edit = (LinearLayout) mMenuView.findViewById(R.id.sear_edit);
        msg_list = (FrameLayout) mMenuView.findViewById(R.id.msg_list);
        no_no_msg = (LinearLayout) mMenuView.findViewById(R.id.no_no_msg);
        adapter = new SelectMan1Adapter(context,list);
        selectman_list1.setAdapter(adapter);
        selectman_list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean checked = list.get(position).getCheck();
                if (!checked) {
                    list.get(position).setCheck(true);
                } else {
                    list.get(position).setCheck(false);
                }
                if (adapter2choose) {
                    adapter2.notifyDataSetChanged();
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });
        selectman_list1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        if (adapter2choose) {
            selectman_list1.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        case SCROLL_STATE_IDLE://空闲状态
                            sidebar_dialog.setVisibility(View.GONE);
                            break;
                        case SCROLL_STATE_FLING://滚动状态
                            break;
                        case SCROLL_STATE_TOUCH_SCROLL://触摸在屏幕上滚动
                            int firstVisiblePosition = selectman_list1.getFirstVisiblePosition();
                            int lastVisiblePosition = selectman_list1.getLastVisiblePosition();
                            String PingYin = characterParser.getSelling(((Work2Info) adapter2.getItem(firstVisiblePosition)).getName());
                            String sortString = PingYin.substring(0, 1).toUpperCase();
                            sidebar_dialog.setText(sortString);
                            sidebar_dialog.setVisibility(View.VISIBLE);
                            break;
                    }
                }
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //***验证码
        selectman_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT || event.getKeyCode() == 66) {
                    // 在这里编写自己想要实现的功能
                       String searchman = selectman_search.getText().toString().trim();
                      if (searchman.toString().length()>0) {
                          list.clear();
                          searchmsg(TOKENID, searchman);
                      }
                    return true;
                }
                return false;
            }
        });
//        selectman_search.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER) {//修改回车键功能
//                    // 先隐藏键盘
//                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
//                            .hideSoftInputFromWindow(context
//                                            .getCurrentFocus()
//                                            .getWindowToken(),
//                                    InputMethodManager.HIDE_NOT_ALWAYS);
//                }
//                list.clear();
//                String searchman = selectman_search.getText().toString().trim();
//                if (searchman.equals("") || searchman == null) {
//
//                } else {
//                    searchmsg(TOKENID, searchman);
//                }
//                return false;
//            }
//        });
//        selectman_sx.setOnClickListener(itemclick);
        selectman_sx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sear_edit.setVisibility(View.GONE);
                phone_name.setText("筛选");
                selectman_sx.setVisibility(View.GONE);
                list.clear();
                no_no_msg.setVisibility(View.GONE);
                msg_list.setVisibility(View.VISIBLE);
                initdataMan(TOKENID);
            }
        });
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItemID.clear();// 清空listItemID
                String searchman = selectman_search.getText().toString().trim();
                if (searchman.equals("") || searchman == null) {
                    for (int i = 0;i<list.size();i++){
                        if (list.get(i).getCheck()){
                            listItemID.add(i);
                        }
                    }
                    if (listItemID.size() == 0) {
                        Toast.makeText(context, "没有选中任何记录", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i=0;i<listItemID.size();i++){
                            Integer integer = listItemID.get(i);
                            Work2Info work2Info = new Work2Info();
                            work2Info.setF_USERID(list.get(integer.intValue()).getF_USERID());
                            work2Info.setF_USERNAME(list.get(integer.intValue()).getF_USERNAME());
                            work2Info.setF_DEPARTMENTNAME(list.get(integer.intValue()).getF_DEPARTMENTNAME());
                            work2Info.setF_POSITIONNAME(list.get(integer.intValue()).getF_POSITIONNAME());
                            getlist.add(work2Info);
                        }
                        ongetdata.onDataCallBack(getlist);
                    }
                    dismiss();
                } else {
                    selectman_search.setText("");
                    list.clear();
                    searchmsg(TOKENID, searchman);
                }
            }
        });
        Rect frame = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        int height = wm.getDefaultDisplay().getHeight();
        int statusBarHeight = frame.top;
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setTouchable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom2);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#4Dffffff"));
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
//        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
//        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        initdata(UserID, TOKENID);

    }
    // 数据接口设置,数据源接口传入
    public void setOnData(OnGetData sd) {
        ongetdata = sd;
        getlist = new ArrayList<Work2Info>();
    }
    public interface OnGetData{
        abstract void onDataCallBack(List<Work2Info> getlist);
    }
    private void searchmsg(String TOKENID,String UserName) {
        dialog.show();
        RequestParams params = new RequestParams(HttpIP.MainService+HttpIP.User_Filter);
        final JSONObject object = new JSONObject();
        try {
            object.put("UserName", UserName);
            params.addBodyParameter("parameter", AES.encrypt(object.toString()));
            params.setAsJsonContent(true);
            params.addBodyParameter(SaveMsg.TOKENID, TOKENID);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    String s = AES.desEncrypt(result.toString());
                    Log.d("s", s);
                    try {
                        JSONObject object2 = new JSONObject(s);
                        int code = object2.getInt("ErrorCode");
                        if (code==1){
                            Toast.makeText(context, object2.getString("ErrorMsg").toString(), Toast.LENGTH_SHORT).show();
                        }else if (code==0){
                            String Result = object2.getString("Result");
                            JSONArray array0 = new JSONArray(Result);
                            JSONArray array = new JSONArray(((JSONObject)array0.get(0)).getString("Table"));
                            for (int i=0;i<array.length();i++){
                                JSONObject object3 = (JSONObject) array.get(i);
                                Work2Info work2Info = new Work2Info();
                                work2Info.setF_USERNAME(object3.getString("F_USERNAME"));
                                work2Info.setF_DEPARTMENTNAME(object3.getString("F_DEPARTMENTNAME"));
                                work2Info.setF_POSITIONNAME(object3.getString("F_POSITIONNAME"));
                                work2Info.setF_USERID(object3.getString("F_USERID"));
                                work2Info.setCheck(false);
                                list.add(work2Info);
                            }
                            if (array.length() == 0){
                                no_no_msg.setVisibility(View.VISIBLE);
                                msg_list.setVisibility(View.GONE);
                            }else {
                                no_no_msg.setVisibility(View.GONE);
                                msg_list.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (JSONException e) {
                        no_no_msg.setVisibility(View.VISIBLE);
                        msg_list.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    dialog.dismiss();
                    Toast.makeText(context, "数据错误", Toast.LENGTH_SHORT).show();
                    Log.d("ex",ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void initdataMan(String TOKENID) {
        dialog.show();
        RequestParams params = new RequestParams(HttpIP.MainService+HttpIP.User_Filter);
        final JSONObject object = new JSONObject();
        try {
            object.put("UserName", "");
            params.addBodyParameter("parameter", AES.encrypt(object.toString()));
            params.setAsJsonContent(true);
            params.addBodyParameter(SaveMsg.TOKENID, TOKENID);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    String s = AES.desEncrypt(result.toString());
                    Log.d("s", s);
                    try {
                        JSONObject object2 = new JSONObject(s);
                        int code = object2.getInt("ErrorCode");
                        if (code==1){
                            Toast.makeText(context, object2.getString("ErrorMsg").toString(), Toast.LENGTH_SHORT).show();
                        }else if (code==0){
                            String Result = object2.getString("Result");
                            JSONArray array0 = new JSONArray(Result);
                            JSONArray array = new JSONArray(((JSONObject)array0.get(0)).getString("Table"));
                            for (int i=0;i<array.length();i++){
                                JSONObject object3 = (JSONObject) array.get(i);
                                Work2Info work2Info = new Work2Info();
                                work2Info.setF_USERNAME(object3.getString("F_USERNAME"));
                                work2Info.setF_DEPARTMENTNAME(object3.getString("F_DEPARTMENTNAME"));
                                work2Info.setF_POSITIONNAME(object3.getString("F_POSITIONNAME"));
                                work2Info.setF_USERID(object3.getString("F_USERID"));
                                work2Info.setCheck(false);
                                list.add(work2Info);
                            }
                            adapter2choose = true;
                            list = filledData(list);
                            Collections.sort(list, pinyinComparator);
                            adapter2 = new ZimuAdapter(context, list);
                            selectman_list1.setAdapter(adapter2);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter2.notifyDataSetChanged();
                    dialog.dismiss();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    dialog.dismiss();
                    Toast.makeText(context, "数据错误", Toast.LENGTH_SHORT).show();
                    Log.d("ex",ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void initdata(String userID,String TOKENID) {
        dialog.show();
        RequestParams params = new RequestParams(HttpIP.MainService+HttpIP.CommonGroupPersonnel_Filter);
        final JSONObject object = new JSONObject();
        try {
            object.put("UserID", userID);
            params.addBodyParameter("parameter", AES.encrypt(object.toString()));
            params.setAsJsonContent(true);
            params.addBodyParameter(SaveMsg.TOKENID, TOKENID);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    String s = AES.desEncrypt(result.toString());
                    Log.d("s", s);
                    dialog.dismiss();
                    try {
                        JSONObject object2 = new JSONObject(s);
                        int code = object2.getInt("ErrorCode");
                        if (code==1){
                            Toast.makeText(context, object2.getString("ErrorMsg").toString(), Toast.LENGTH_SHORT).show();
                        }else if (code==0){
                            String Result = object2.getString("Result");
                            JSONArray array0 = new JSONArray(Result);
                            JSONArray array = new JSONArray(((JSONObject)array0.get(0)).getString("Table"));
                            for (int i=0;i<array.length();i++){
                                JSONObject object3 = (JSONObject) array.get(i);
                                Work2Info work2Info = new Work2Info();
                                work2Info.setF_USERNAME(object3.getString("F_USERNAME"));
                                work2Info.setF_DEPARTMENTNAME(object3.getString("F_DEPARTMENTNAME"));
                                work2Info.setF_POSITIONNAME(object3.getString("F_POSITIONNAME"));
                                work2Info.setF_USERID(object3.getString("F_USERID"));
                                work2Info.setCheck(false);
                                list.add(work2Info);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();


                    selectman_search.requestFocus();
                    InputMethodManager imm = (InputMethodManager) selectman_search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    dialog.dismiss();
                    Toast.makeText(context, "数据错误", Toast.LENGTH_SHORT).show();
                    Log.d("ex",ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private List<Work2Info> filledData(List<Work2Info > listuserbook) {
        List<Work2Info> mSortList = new ArrayList<Work2Info>();
        for(int i=0; i<listuserbook.size(); i++){
            Work2Info sortModel = new Work2Info();
            Work2Info userBook = listuserbook.get(i);
            sortModel.setName(userBook.getF_USERNAME());
            sortModel.setNametype(userBook.getF_DEPARTMENTNAME() + "-" + userBook.getF_POSITIONNAME());
            sortModel.setF_USERID(userBook.getF_USERID());
            sortModel.setF_CALLPHONETYPE(userBook.getF_CALLPHONETYPE());
            sortModel.setCheck(false);
            sortModel.setF_USERNAME(userBook.getF_USERNAME());
            sortModel.setF_DEPARTMENTNAME(userBook.getF_DEPARTMENTNAME());
            sortModel.setF_POSITIONNAME(userBook.getF_POSITIONNAME());
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
