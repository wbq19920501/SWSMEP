package com.jokeep.swsmep.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jokeep.swsmep.R;
import com.jokeep.swsmep.adapter.WorkTabAdapter;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.model.Work1Info;
import com.jokeep.swsmep.view.ShowDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbq501 on 2016-2-25 15:05.
 * SWSMEP
 */
public class Work1Fragment extends Fragment{
    View fragment;
    private WorkTabAdapter adapter;
    private PullToRefreshListView work1_list;
    private String TOKENID;
    private String UserID;
    private ShowDialog dialog;
    private int page = 1;
    List<Work1Info> work1Infos;
    LinearLayout no_msg;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragment == null){
            fragment = inflater.inflate(R.layout.work1,container,false);
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
        requestmsg();
        work1_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                work1Infos.clear();
                requestmsg();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                requestmsg();
            }
        });
    }

    private void requestmsg() {
        dialog.show();
        RequestParams params = new RequestParams(HttpIP.MainService+HttpIP.JointToDo_Filter);
        JSONObject object = new JSONObject();
        try {
            object.put("UserID",UserID);
            object.put("State","0");
            object.put("PageNo",page);
            object.put("PageSize","10");
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
                            Toast.makeText(getActivity(),object2.getString("ErrorMsg").toString(),Toast.LENGTH_SHORT).show();
                        }else if (code==0){
                            String Result = object2.getString("Result");
                            JSONArray array0 = new JSONArray(Result);
                            JSONArray array = new JSONArray(((JSONObject)array0.get(0)).getString("Table"));
                            for (int i=0;i<array.length();i++){
                                JSONObject object3 = (JSONObject) array.get(i);
                                Work1Info work1Info = new Work1Info();
                                work1Info.setF_TITLE(object3.getString("F_TITLE"));
                                work1Info.setF_SPONSUSER(object3.getString("F_SPONSUSER"));
                                work1Info.setF_SPONSTIME(object3.getString("F_SPONSTIME"));
                                work1Info.setF_ISATT(object3.getInt("F_ISATT"));
                                work1Info.setF_LINKURL(object3.getString("F_LINKURL"));
                                work1Info.setF_JOINTID(object3.getString("F_DATAGUID"));
                                work1Info.setF_EXECUTMAINID(object3.getString("F_EXECUTMAINID"));
                                work1Info.setF_BUSINESSCODE(object3.getString("F_BUSINESSCODE"));
                                work1Info.setF_isView(object3.getInt("F_ISVIEW"));
                                work1Info.setType(0);
                                work1Info.setTypename(1);
                                work1Infos.add(work1Info);
                            }
                            if (page == 1){
                                if (array.length()==0){
                                    no_msg.setVisibility(View.VISIBLE);
                                    work1_list.setVisibility(View.GONE);
                                }else {
                                    no_msg.setVisibility(View.GONE);
                                    work1_list.setVisibility(View.VISIBLE);
                                }
                            }
                            work1_list.onRefreshComplete();
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        no_msg.setVisibility(View.VISIBLE);
                        work1_list.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    work1_list.onRefreshComplete();
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "数据错误", Toast.LENGTH_SHORT).show();
                    no_msg.setVisibility(View.VISIBLE);
                    work1_list.setVisibility(View.GONE);
                    Log.d("ex",ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
        }
    }

    public void refreshfragment(){
        page = 1;
        work1Infos=new ArrayList<Work1Info>();
        adapter = new WorkTabAdapter(getActivity(),work1Infos,1,TOKENID);
        work1_list.setAdapter(adapter);
        requestmsg();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        page = 1;
//        work1Infos.clear();
//        requestmsg();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        page = 1;
//        work1Infos.clear();
//        requestmsg();
//    }

    private void init() {
        Bundle data = getArguments();
        TOKENID = data.getString("TOKENID");
        UserID = data.getString("UserID");
        dialog = new ShowDialog(getActivity(),R.style.MyDialog,getResources().getString(R.string.dialogmsg));
        work1Infos = new ArrayList<Work1Info>();
        work1_list = (PullToRefreshListView) fragment.findViewById(R.id.work1_list);
        work1_list.setMode(PullToRefreshBase.Mode.BOTH);
        adapter = new WorkTabAdapter(getActivity(),work1Infos,1,TOKENID);
        work1_list.setAdapter(adapter);
        no_msg = (LinearLayout) fragment.findViewById(R.id.no_msg);
    }
}
