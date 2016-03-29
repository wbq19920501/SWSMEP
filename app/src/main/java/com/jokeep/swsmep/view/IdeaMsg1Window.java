package com.jokeep.swsmep.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.base.SwsApplication;
import com.jokeep.swsmep.model.MeMsg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbq501 on 2016-1-28 09:38.
 * SWSMEP
 */
public class IdeaMsg1Window extends PopupWindow{
    private View mMenuView;
    private ImageButton back;
//    Button btn_sub;
    TextView add_memsg;
    RelativeLayout choose_addmsg;
    private ListView add_msglist;
    Activity context;
    private ShowDialog dialog;
    List<String> listmsg;
    List<String> memsg;
    // 数据接口
    OnGetData ongetdata;
    private List<String> listid;
    BaseAdapter adapter;
    List<MeMsg> dataList;
    List<Integer> listItemID = new ArrayList<Integer>();

    private SwsApplication application;
    String UserID,TOKENID;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    EditText add_context;
    LinearLayout edit_open,msglist;
    boolean addeditting = false;
    boolean adddel = false;
    public IdeaMsg1Window(final Activity context, View.OnClickListener itemclick){
        super();
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.idea_msg, null);
        application = (SwsApplication) context.getApplication();
        sp = context.getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE);
        editor = sp.edit();
        UserID = application.getFUSERID();
        TOKENID = sp.getString(SaveMsg.TOKENID, "");

        dialog = new ShowDialog(context,R.style.MyDialog,context.getResources().getString(R.string.dialogmsg));
        memsg = new ArrayList<String>();
        listmsg = new ArrayList<String>();
        dataList = new ArrayList<MeMsg>();
        listid = new ArrayList<String>();

        back = (ImageButton) mMenuView.findViewById(R.id.back);
//        btn_sub = (Button) mMenuView.findViewById(R.id.btn_sub);
        add_memsg = (TextView) mMenuView.findViewById(R.id.add_memsg);
        add_context = (EditText) mMenuView.findViewById(R.id.add_context);
        edit_open = (LinearLayout) mMenuView.findViewById(R.id.edit_open);
        msglist = (LinearLayout) mMenuView.findViewById(R.id.msglist);
        choose_addmsg = (RelativeLayout) mMenuView.findViewById(R.id.choose_addmsg);
        add_msglist = (ListView) mMenuView.findViewById(R.id.add_msglist);
        add_msglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean checked = dataList.get(position).isChecked();
                if (!checked) {
                    dataList.get(position).setChecked(true);
                } else {
                    dataList.get(position).setChecked(false);
                }
                adapter.notifyDataSetChanged();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
//        btn_sub.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listItemID.clear();// 清空listItemID
//                for (int i = 0;i<dataList.size();i++){
//                    if (dataList.get(i).checked){
//                        listItemID.add(i);
//                    }
//                }
//                if (listItemID.size() == 0) {
//                    Toast.makeText(context, "没有选中任何记录", Toast.LENGTH_SHORT).show();
//                } else {
//                    for (int i=0;i<listItemID.size();i++){
//                        Integer integer = listItemID.get(i);
//                        memsg.add(dataList.get(integer.intValue()).getF_CONTENT());
//                    }
//                    ongetdata.onDataCallBack(memsg);
//                }
//                dismiss();
//            }
//        });
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        int height = wm.getDefaultDisplay().getHeight();
        Rect frame = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(height-statusBarHeight);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom2);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#4Dffffff"));
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

//        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
//        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        init();
        initdata();
        add_memsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String btnmsg = add_memsg.getText().toString().trim();
                if (btnmsg.equals("完成")||btnmsg=="完成"){
                    if (adddel){
                        addmsg();
                    }else{
                        delmsg();
                    }
                }else {
                    for (int i=0;i<dataList.size();i++){
                        dataList.get(i).setChecked(false);
                    }
                    adapter.notifyDataSetChanged();
                    if (!addeditting){
                        add_memsg.setText("完成");
                        choose_addmsg.setVisibility(View.VISIBLE);
                        add_msglist.setVisibility(View.VISIBLE);
                        addeditting = true;
                        adddel = false;
                        for (int i = 0; i < dataList.size(); i++) {
                            dataList.get(i).type = true;
                        }
                    }else {
                        add_memsg.setText("编辑");
                        for (int i = 0; i < dataList.size(); i++) {
                            dataList.get(i).checked = false;
                        }
                        choose_addmsg.setVisibility(View.GONE);
                        addeditting = false;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        choose_addmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msglist.setVisibility(View.GONE);
                add_msglist.setVisibility(View.GONE);
                choose_addmsg.setVisibility(View.GONE);
                edit_open.setVisibility(View.VISIBLE);
                add_context.setVisibility(View.VISIBLE);
                adddel = true;
            }
        });

        add_msglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < dataList.size(); i++) {
                    if (i==position){
                        dataList.get(i).checked = true;
                    }else {
                        dataList.get(i).checked = false;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void init() {
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return dataList.size();
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
                ViewHolder holder = null;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.add_me_msg, null);
                    holder.msg_name = (TextView) convertView.findViewById(R.id.msg_name);
                    holder.checkbox_memsg = (CheckBox) convertView.findViewById(R.id.checkbox_memsg);
                    holder.delmsg = (ImageButton) convertView.findViewById(R.id.delmsg);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                final MeMsg meMsg = dataList.get(position);
                holder.msg_name.setText((String) meMsg.getF_CONTENT());
                holder.checkbox_memsg.setChecked(meMsg.checked);
                final boolean type = meMsg.isType();
                if (type){
                    holder.delmsg.setVisibility(View.VISIBLE);
                }else {
                    holder.delmsg.setVisibility(View.GONE);
                }
                holder.delmsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listid.add(meMsg.getF_USERPHRASEBOOKID().toString());
                        dataList.remove(position);
                        notifyDataSetChanged();
                    }
                });
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        memsg.add(dataList.get(position).getF_CONTENT());
                        ongetdata.onDataCallBack(memsg);
                        dismiss();
                    }
                });
                return convertView;
            }
        };
        add_msglist.setAdapter(adapter);
    }

    private void delmsg() {
        if (listid.size()==0){
            Toast.makeText(context,"没有改变信息哦...",Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.show();
        RequestParams params = new RequestParams(HttpIP.MainService+HttpIP.UserPhrasebook_Delete);
        JSONObject object = new JSONObject();
        JSONArray arrayfile = new JSONArray();
        try {
            for (int i=0;i<listid.size();i++){
                JSONObject object1 = new JSONObject();
                object1.put("F_USERPHRASEBOOKID",listid.get(i).toString());
                arrayfile.put(object1);
            }
            object.put("F_USERPHRASEBOOKIDS",arrayfile);
            params.addBodyParameter("parameter", AES.encrypt(object.toString()));
            params.setAsJsonContent(true);
            params.addBodyParameter(SaveMsg.TOKENID,TOKENID);
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

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    dismiss();
                    add_memsg.setText("编辑");
                    addeditting = false;
                    adddel = false;
                    dataList.clear();
                    choose_addmsg.setVisibility(View.GONE);
                    add_msglist.setVisibility(View.VISIBLE);
                    initdata();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    dialog.dismiss();
                    Log.d("ex", ex.getMessage());
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

    private void addmsg() {
        final String contentmsg = add_context.getText().toString().trim();
        if (contentmsg.equals("")||contentmsg==null){
            Toast.makeText(context,"请输入常用语",Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.show();
        RequestParams params = new RequestParams(HttpIP.MainService+HttpIP.UserPhrasebook_Insert);
        JSONObject object = new JSONObject();
        try {
            object.put("UserID",UserID);
            object.put("Content",contentmsg);
            params.addBodyParameter("parameter", AES.encrypt(object.toString()));
            params.setAsJsonContent(true);
            params.addBodyParameter(SaveMsg.TOKENID,TOKENID);
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

                            //添加成功直接返回,zxp修改
                            memsg.clear();
                            memsg.add(contentmsg);
                            ongetdata.onDataCallBack(memsg);
                            dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    add_memsg.setText("编辑");
                    addeditting = false;
                    adddel = false;
                    dataList.clear();
                    choose_addmsg.setVisibility(View.GONE);
                    add_msglist.setVisibility(View.VISIBLE);
                    add_context.setVisibility(View.GONE);
                    add_context.setText("");
                    initdata();
//                    dismiss();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    dialog.dismiss();
                    Log.d("ex", ex.getMessage());
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

    private void initdata() {
        dialog.show();
        RequestParams params = new RequestParams(HttpIP.MainService+HttpIP.UserPhrasebook_Filter);
        final JSONObject object = new JSONObject();
        try {
            object.put("UserID",UserID);
            params.addBodyParameter("parameter", AES.encrypt(object.toString()));
            params.setAsJsonContent(true);
            params.addBodyParameter(SaveMsg.TOKENID,TOKENID);
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
                            String resulttab = object2.getString("Result");
                            JSONObject o = (JSONObject) new JSONArray(resulttab).get(0);
                            JSONArray array = new JSONArray(o.getString("Table"));
                            for (int i=0;i<array.length();i++){
                                JSONObject objectarray = (JSONObject) array.get(i);
                                MeMsg meMsg = new MeMsg();
                                meMsg.setF_CONTENT(objectarray.getString("F_CONTENT"));
                                meMsg.setF_USERPHRASEBOOKID(objectarray.getString("F_USERPHRASEBOOKID"));
                                meMsg.setType(false);
                                dataList.add(meMsg);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    dialog.dismiss();
                    Log.d("ex", ex.getMessage());
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

    // 数据接口设置,数据源接口传入
    public void setOnData(OnGetData sd) {
        ongetdata = sd;
        listmsg = new ArrayList<String>();
    }
    public interface OnGetData{
        abstract void onDataCallBack(List<String> listmsg);
    }
    class ViewHolder {
        public TextView msg_name;
        public CheckBox checkbox_memsg;
        ImageButton delmsg;
    };

}
