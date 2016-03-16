package com.jokeep.swsmep.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.adapter.SelectMan1Adapter;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.model.Work2Info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wbq501 on 2016-1-28 09:38.
 * SWSMEP
 */
public class SelectMan2Window extends PopupWindow{
    private View mMenuView;
    private ImageButton back;
    private ListView selectman_list1;
    private Button btn_sub;
//    PhoneAdapter adapter;
    SelectMan1Adapter adapter;
    Activity context;
    String userID, TOKENID;
    private ShowDialog dialog;
    List<Integer> listItemID = new ArrayList<Integer>();
    List<Work2Info> list ;
    List<Work2Info> getlist;
    // 数据接口
    OnGetData ongetdata;

    public SelectMan2Window(final Activity context, final View.OnClickListener itemclick, String userID, String TOKENID){
        super();
        this.context = context;
        this.userID = userID;
        this.TOKENID = TOKENID;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.select_man2, null);
        list = new ArrayList<Work2Info>();
        getlist = new ArrayList<Work2Info>();
        dialog = new ShowDialog(context,R.style.MyDialog,context.getResources().getString(R.string.dialogmsg));

        back = (ImageButton) mMenuView.findViewById(R.id.back);
        selectman_list1 = (ListView) mMenuView.findViewById(R.id.selectman_list1);
//        adapter = new PhoneAdapter();
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
                adapter.notifyDataSetChanged();
            }
        });
        selectman_list1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        btn_sub = (Button) mMenuView.findViewById(R.id.btn_sub);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItemID.clear();// 清空listItemID
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
            }
        });

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom2);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#4Dffffff"));
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        initdata();
    }
    // 数据接口设置,数据源接口传入
    public void setOnData(OnGetData sd) {
        ongetdata = sd;
        getlist = new ArrayList<Work2Info>();
    }
    public interface OnGetData{
        abstract void onDataCallBack(List<Work2Info> getlist);
    }
    private void initdata() {
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
                        }
                    } catch (JSONException e) {
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
    class PhoneAdapter extends BaseAdapter {
        /** 标记CheckBox是否被选中 **/
        List<Boolean> mChecked = new ArrayList<Boolean>();
        /** 存放要显示的Item数据 **/
        /** 一个HashMap对象 **/
        @SuppressLint("UseSparseArrays")
        HashMap<Integer, View> map = new HashMap<Integer, View>();

        public PhoneAdapter() {
            mChecked = new ArrayList<Boolean>();
            for (int i = 0; i < list.size(); i++) {// 遍历且设置CheckBox默认状态为未选中
                mChecked.add(false);
            }
        }
        @Override
        public int getCount() {
            return list.size();
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
            if (map.get(position) == null) {// 根据position判断View是否为空
                convertView = LayoutInflater.from(context).inflate(R.layout.select_man1_item,null);
                holder = new ViewHolder();
                holder.onclick = (LinearLayout) convertView.findViewById(R.id.onclick);
                holder.link_name = (TextView) convertView.findViewById(R.id.link_name);
                holder.link_type = (TextView) convertView.findViewById(R.id.link_type);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
                map.put(position, convertView);// 存储视图信息
                convertView.setTag(holder);
            } else {
                convertView = map.get(position);
                holder = (ViewHolder) convertView.getTag();
            }
            final int p = position;
            for (int i = 0; i < list.size(); i++) {// 遍历且设置CheckBox默认状态为未选中
                mChecked.add(false);
            }
//            final ViewHolder finalHolder = holder;
//            holder.onclick.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context,"??",Toast.LENGTH_SHORT).show();
//                    mChecked.set(p, finalHolder.checkbox.isChecked());
//                }
//            });
            holder.checkbox.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    mChecked.set(p, cb.isChecked());// 设置CheckBox为选中状态
                }
            });
            Work2Info work2Info = list.get(position);
            holder.link_name.setText(work2Info.getF_USERNAME());
            holder.link_type.setText(work2Info.getF_DEPARTMENTNAME() + "-" + work2Info.getF_POSITIONNAME());
            holder.checkbox.setChecked(mChecked.get(position));
            return convertView;
        }
    }
    class ViewHolder{
        TextView link_name,link_type;
        CheckBox checkbox;
        LinearLayout onclick;
    }
}
