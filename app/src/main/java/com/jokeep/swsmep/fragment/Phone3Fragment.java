package com.jokeep.swsmep.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.activity.PhoneManActivity;
import com.jokeep.swsmep.activity.PhoneManMsgActivity;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.model.UnitInfo;
import com.jokeep.swsmep.model.UserBook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbq501 on 2016-2-16 16:15.
 * SWSMEP
 */
public class Phone3Fragment extends Fragment{
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    View fragment;
    private ListView list;
    private BaseAdapter adapter;
    List<UnitInfo> listunitinfo;
    List<UserBook> listuserbook;
    Intent intent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragment == null){
            fragment = inflater.inflate(R.layout.phone3_fragment,container,false);
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
        initdata();
        return fragment;
    }

    private void initdata() {
        RequestParams params = new RequestParams(HttpIP.MainService+HttpIP.PhoneMan);
        params.addBodyParameter("parameter","");
        params.setAsJsonContent(true);
        params.addBodyParameter(SaveMsg.TOKENID, sp.getString(SaveMsg.TOKENID, ""));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                String s = AES.desEncrypt(result.toString());
                Log.d("result", result.toString());
                try {
                    JSONObject object2 = new JSONObject(s);
                    int code = object2.getInt("ErrorCode");
                    if (code == 1) {
                        Toast.makeText(getActivity(), object2.getString("ErrorMsg").toString(), Toast.LENGTH_SHORT).show();
                    } else if (code == 0) {
                        String resultman = object2.getString("Result").toString();
                        Log.d("resultman", resultman);
                        JSONArray array = new JSONArray(resultman);
                        JSONObject object3 = array.getJSONObject(0);
                        String UnitBook = object3.getString("UnitBook").toString();
                        Log.d("UnitBook", UnitBook);
                        String UnitInfo = object3.getString("UnitInfo").toString();
                        Log.d("UnitInfo", UnitInfo);
                        String UserBook = object3.getString("UserBook").toString();
                        Log.d("UserBook", UserBook);
                        MsgUnitInfo(UnitInfo);
                        MsgUserBook(UserBook);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("ex", ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void MsgUserBook(String UserBook) throws JSONException {
        JSONArray array = new JSONArray(UserBook);
        for (int i=0;i<array.length();i++){
            JSONObject object = (JSONObject) array.get(i);
            UserBook userbook = new UserBook();
            userbook.setF_POSITIONNAME(object.getString("F_POSITIONNAME"));
            userbook.setF_CALLPHONETYPE(object.getInt("F_CALLPHONETYPE"));
            userbook.setF_USERID(object.getString("F_USERID"));
            userbook.setF_CALLPHONE(object.getString("F_CALLPHONE"));
            userbook.setF_USERNAME(object.getString("F_USERNAME"));
            userbook.setF_DEPARTMENTNAME(object.getString("F_DEPARTMENTNAME"));
            listuserbook.add(userbook);
        }
    }

    private void MsgUnitInfo(String UnitInfo) throws JSONException {
        JSONArray array = new JSONArray(UnitInfo);
        for (int i=0;i<array.length();i++){
            JSONObject object = (JSONObject) array.get(i);
            UnitInfo unitInfo = new UnitInfo();
            unitInfo.setF_UNITNAME(object.getString("F_UNITNAME").toString());
            unitInfo.setF_UNITTYPE(object.getInt("F_UNITTYPE"));
            unitInfo.setF_ORDERLEVEL(object.getInt("F_ORDERLEVEL"));
            unitInfo.setF_OTHERUNITID(object.getString("F_OTHERUNITID").toString());
            listunitinfo.add(unitInfo);
        }
        adapter.notifyDataSetChanged();
    }

    private void init() {
        sp = getActivity().getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE);
        editor = sp.edit();
        listunitinfo = new ArrayList<UnitInfo>();
        listuserbook = new ArrayList<UserBook>();
        list = (ListView) fragment.findViewById(R.id.phone3_list);
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return listunitinfo.size();
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
                final UnitInfo unitInfo = listunitinfo.get(position);
                final ViewHolder holder;
                if (convertView == null){
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.phone3_list_item,null);
                    holder = new ViewHolder();
                    holder.textname = (TextView) convertView.findViewById(R.id.phone3_list_text);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.textname.setText(unitInfo.getF_UNITNAME());
                final int type = unitInfo.getF_UNITTYPE();
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type == 0){
                            intent = new Intent(getActivity(), PhoneManActivity.class);
                            intent.putExtra("userbook", (Serializable) listuserbook);
                        }else {
                            intent = new Intent(getActivity(), PhoneManMsgActivity.class);
                        }
                        intent.putExtra("textname", unitInfo.getF_UNITNAME());
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                    }
                });
                return convertView;
            }
        };
        list.setAdapter(adapter);
    }
    static class ViewHolder{
        TextView textname;
    }
}
