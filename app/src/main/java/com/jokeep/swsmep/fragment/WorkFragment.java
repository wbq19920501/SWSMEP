package com.jokeep.swsmep.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jokeep.swsmep.R;
import com.jokeep.swsmep.activity.OfficialActivity;
import com.jokeep.swsmep.activity.TrackActivity;
import com.jokeep.swsmep.activity.WorkActivity;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.base.SwsApplication;
import com.jokeep.swsmep.model.Work1Info;
import com.jokeep.swsmep.model.WorkInfo;
import com.jokeep.swsmep.view.WorkPoPw;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbq501 on 2016-1-25 16:52.
 * SWSMEP
 */
public class WorkFragment extends Fragment {

    View fragment;

    PullToRefreshListView list_refresh;
    BaseAdapter adapter;
    ImageButton work_btn;
    WorkPoPw poPw;
//    String[] colors = {"#6BC773","#5C6BC0","#F75D8C","#008CEE"};
    List<String> colors ;
    Intent intent;

    //dengJ
    private String UserID;
    private String TOKENID;
    private SwsApplication application;
    private SharedPreferences sp;
    private List<WorkInfo> infoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragment == null){
            fragment = inflater.inflate(R.layout.work_fragment,container,false);
        }else {
            // 缓存的rootView需要判断是否已经被加过parent
            // 如果有parent需要从parent删除
            ViewGroup parent = (ViewGroup) fragment.getParent();
            if (parent != null) {
                parent.removeView(fragment);
            }
        }
        init();
        connectToServerByPost();
        initdata();
        return fragment;
    }
    //dengJ 工作获取数据；
    private void connectToServerByPost() {
        RequestParams params = new RequestParams(HttpIP.workIp);
        JSONObject object = new JSONObject();
        try {
            object.put("UserID", UserID);
            params.addBodyParameter("parameter", AES.encrypt(object.toString()));
            params.setAsJsonContent(true);
            params.addBodyParameter(SaveMsg.TOKENID, TOKENID);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    String s = AES.desEncrypt(result.toString());
                    Log.d("json", s);
                    try {
                        JSONObject object2 = new JSONObject(s);
                        int code = object2.getInt("ErrorCode");
                        if (code==1){
                            Toast.makeText(getActivity(), object2.getString("ErrorMsg").toString(), Toast.LENGTH_SHORT).show();
                        }else if (code==0){
                            Log.i("json", object2.getString("Result").toString());
                            JSONArray mArray = new JSONArray(object2.getString("Result").toString());
                            JSONObject mObject = (JSONObject) mArray.get(0);
                            JSONArray mArrayOne = new JSONArray(mObject.getString("Table"));
                            for (int i=0;i<mArrayOne.length();i++){
                                JSONObject mObjectTwo = (JSONObject) mArrayOne.get(i);
                                WorkInfo workInfo = new WorkInfo();
                                workInfo.setF_BUSINESSCODE(mObjectTwo.getString("F_BUSINESSCODE"));
                                workInfo.setF_CORLOR(mObjectTwo.getString("F_CORLOR"));
                                workInfo.setF_DATAGUID(mObjectTwo.getString("F_DATAGUID"));
                                workInfo.setF_ISATT(mObjectTwo.getInt("F_ISATT"));
                                workInfo.setF_SORTNAME(mObjectTwo.getString("F_SORTNAME"));
                                workInfo.setF_TODOCOUNT(mObjectTwo.getInt("F_TODOCOUNT"));
                                workInfo.setF_TITLE(mObjectTwo.getString("F_TITLE"));
                                workInfo.setF_SPONSUSER(mObjectTwo.getString("F_SPONSUSER"));
                                infoList.add(workInfo);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.i("json","onError");
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
        work_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.work_btn:
                        poPw = new WorkPoPw(getActivity(),onclick);
                        poPw.showAtLocation(v, Gravity.BOTTOM ,100, 0);
                        poPw.startAnim();
                        break;
                }
            }
        });
    }
    private View.OnClickListener onclick = new View.OnClickListener(){

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_gongwen:
                    intent = new Intent(getActivity(), OfficialActivity.class);
                    break;
                case R.id.bt_genzong:
                    intent = new Intent(getActivity(),TrackActivity.class);
                    break;
                case R.id.bt_xitong:
                    intent = new Intent(getActivity(), WorkActivity.class);
                    break;
            }
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            poPw.dismiss();
        }
    };
    protected void init() {
        infoList = new ArrayList<WorkInfo>();
        colors = new ArrayList<String>();
        //dengJ取TOKENIN,UserID
        application = (SwsApplication) getActivity().getApplication();
        UserID = application.getFUSERID();
        Log.i("json",UserID);
        sp = getActivity().getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE);
        TOKENID = sp.getString(SaveMsg.TOKENID, "");
        Log.i("json",TOKENID);


//        for (int i=0;i<s.length;i++){
//            switch (i){
//                case 0:
//                    colors.add("#6BC773");
//                    break;
//                case 1:
//                    colors.add("#5C6BC0");
//                    break;
//                case 3:
//                    colors.add("#F75D8C");
//                    break;
//                case 4:
//                    colors.add("#008CEE");
//                    break;
//                default:
//                    colors.add("#008CEE");
//                    break;
//            }
//        }
        work_btn = (ImageButton) fragment.findViewById(R.id.work_btn);
        list_refresh = (PullToRefreshListView) fragment.findViewById(R.id.list_refresh);
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return infoList.size();
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
                if (convertView == null){
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.work_item,null);
                    holder = new ViewHolder();
                    holder.work_name = (TextView) convertView.findViewById(R.id.work_name);
                    holder.work_title = (TextView) convertView.findViewById(R.id.work_title);
                    holder.work_context = (TextView) convertView.findViewById(R.id.work_context);
                    holder.work_num = (TextView) convertView.findViewById(R.id.work_num);
                    holder.work_img = (ImageView) convertView.findViewById(R.id.work_img);
                    holder.work_time = (TextView) convertView.findViewById(R.id.work_time);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.work_title.setText(infoList.get(position).getF_TITLE());
                holder.work_context.setText(infoList.get(position).getF_SPONSUSER());
                holder.work_time.setText(infoList.get(position).getF_SORTNAME());
                holder.work_num.setText(infoList.get(position).getF_TODOCOUNT()+"");
                colors.add(infoList.get(position).getF_CORLOR());
                GradientDrawable bgshape = (GradientDrawable) holder.work_name.getBackground();
                bgshape.setColor(Color.parseColor(colors.get(position)));
//                switch (position){
//                    case 0:
////                        holder.work_name.setBackground(getResources().getDrawable(R.drawable.work_round));
//                        break;
//                    case 1:
////                        GradientDrawable bgshape2 = (GradientDrawable) holder.work_name.getBackground();
//                        bgshape.setColor(Color.parseColor(colors[position]));
////                        holder.work_name.setBackground(getResources().getDrawable(R.drawable.work_round2));
//                        break;
//                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                return convertView;
            }
        };
        list_refresh.setAdapter(adapter);
    }
    class ViewHolder{
        TextView work_name,work_title,work_context,work_num,work_time;
        ImageView work_img;
    }
}
