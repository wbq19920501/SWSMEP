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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jokeep.swsmep.R;
import com.jokeep.swsmep.activity.OfficialActivity;
import com.jokeep.swsmep.activity.TrackActivity;
import com.jokeep.swsmep.activity.WorkActivity;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.MyData;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.base.SwsApplication;
import com.jokeep.swsmep.model.WorkInfo;
import com.jokeep.swsmep.model.WorkNumber;
import com.jokeep.swsmep.view.ShowDialog;
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
    String[] colors = {"#6BC773","#5C6BC0","#F75D8C","#008CEE"};
    private String[] str = {"收文","签报","发文","协同"};
    Intent intent;
    String colorOne = "#E85A4F";
    //dengJ
    private List<WorkNumber> NumberList;
    private MyData mMyData = MyData.getInstance();
    private TextView main_tabnum1,main_tabnum3;
    private String UserID;
    private String TOKENID;
    private SwsApplication application;
    private SharedPreferences sp;
    private List<WorkInfo> infoList;
    private ShowDialog dialog;
    private WorkInfo[] mWorkInfo = new WorkInfo[4];


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
        requseMsg();
        initdata();
        return fragment;
    }
    //dengJ 工作获取个数数据；
    private void connectToServerByPostMain() {
        RequestParams params = new RequestParams(HttpIP.mainNumber);
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
                    Log.d("json2", s);
                    try {
                        JSONObject object2 = new JSONObject(s);
                        int code = object2.getInt("ErrorCode");
                        if (code==1){
                        }else if (code==0){
                            Log.i("json2", object2.getString("Result").toString());
                            JSONArray mArray = new JSONArray(object2.getString("Result").toString());
                            JSONObject mObject = (JSONObject) mArray.get(0);
                            JSONArray mArrayOne = new JSONArray(mObject.getString("Table"));
                            for (int i=0;i<mArrayOne.length();i++){
                                JSONObject mObjectTwo = (JSONObject) mArrayOne.get(i);
                                WorkNumber workNumber = new WorkNumber();
                                workNumber.setF_TODOCOUNT(mObjectTwo.getInt("F_TODOCOUNT"));
                                workNumber.setF_MENUCODE(mObjectTwo.getString("F_MENUCODE"));
                                NumberList.add(workNumber);
                            }
                            //判断工作是否加number
                            if(NumberList.get(0).getF_MENUCODE().equals("0001")&&NumberList.get(0).getF_TODOCOUNT()>=1){
                                main_tabnum1.setVisibility(View.VISIBLE);
                                main_tabnum1.setText(NumberList.get(0).getF_TODOCOUNT()+"");
                            }else {
                                main_tabnum1.setVisibility(View.GONE);
                            }
                            //判断日程是否加number
                            if(NumberList.get(1).getF_MENUCODE().equals("0002")&&NumberList.get(1).getF_TODOCOUNT()>=1){
                                main_tabnum3.setVisibility(View.VISIBLE);
                                main_tabnum3.setText(NumberList.get(1).getF_TODOCOUNT()+"");
                            }else {
                                main_tabnum3.setVisibility(View.GONE);
                            }
                            //传值到PopWindow跟踪
                            if(NumberList.get(2).getF_MENUCODE().equals("0003")){
                                mMyData.setmGenZongNum(NumberList.get(2).getF_TODOCOUNT()+"");
                            }
                            //传值到PopWindow公文
                            if(NumberList.get(3).getF_MENUCODE().equals("0004")){
                                mMyData.setmGongWenNum(NumberList.get(3).getF_TODOCOUNT()+"");
                            }
                            //传值到PopWindow协同
                            if(NumberList.get(4).getF_MENUCODE().equals("0005")){
                                mMyData.setmXieTongNum(NumberList.get(4).getF_TODOCOUNT()+"");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    list_refresh.onRefreshComplete();
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(getActivity(), "数据错误", Toast.LENGTH_SHORT).show();
                    Log.i("json2","onError");
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
                    dialog.dismiss();
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
                            getData();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    list_refresh.onRefreshComplete();
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    dialog.dismiss();
                    Log.i("json", "onError");
                    Toast.makeText(getActivity(), "数据错误", Toast.LENGTH_SHORT).show();
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

    private void requseMsg(){
        dialog.show();
        connectToServerByPost();
        connectToServerByPostMain();
        list_refresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                infoList.clear();
                NumberList.clear();
                connectToServerByPost();
                getData();
                connectToServerByPostMain();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
    }
    //点击按钮打开PopWidow
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
    //操作PopWindow中按钮的数据
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
    private void getData(){
        for(int i=0;i<infoList.size();i++){
            if(infoList.get(i).getF_BUSINESSCODE().equals("000001")){
                mWorkInfo[2] = infoList.get(i);
            }else if(infoList.get(i).getF_BUSINESSCODE().equals("000002")){
                mWorkInfo[0] = infoList.get(i);
            }else if(infoList.get(i).getF_BUSINESSCODE().equals("000003")){
                mWorkInfo[3] = infoList.get(i);
            }else if(infoList.get(i).getF_BUSINESSCODE().equals("000005")){
                mWorkInfo[1] = infoList.get(i);
            }
        }
    }
    protected void init() {
        infoList = new ArrayList<WorkInfo>();
        NumberList = new ArrayList<WorkNumber>();
        main_tabnum1 = (TextView) getActivity().findViewById(R.id.main_tabnum1);
        main_tabnum3 = (TextView) getActivity().findViewById(R.id.main_tabnum3);
        dialog = new ShowDialog(getActivity(),R.style.MyDialog,getResources().getString(R.string.dialogmsg));
        WorkInfo workInfo = new WorkInfo();
        workInfo.setF_BUSINESSCODE("");
        workInfo.setF_CORLOR("");
        workInfo.setF_DATAGUID("");
        workInfo.setF_ISATT(0);
        workInfo.setF_SORTNAME("");
        workInfo.setF_TODOCOUNT(0);
        workInfo.setF_TITLE("暂无");
        workInfo.setF_SPONSUSER("");
        for(int i = 0;i<mWorkInfo.length;i++){
            mWorkInfo[i]=workInfo;
        }
        //dengJ取TOKENIN,UserID
        application = (SwsApplication) getActivity().getApplication();
        UserID = application.getFUSERID();
        Log.i("json",UserID);
        sp = getActivity().getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE);
        TOKENID = sp.getString(SaveMsg.TOKENID, "");
        Log.i("json",TOKENID);
        work_btn = (ImageButton) fragment.findViewById(R.id.work_btn);
        list_refresh = (PullToRefreshListView) fragment.findViewById(R.id.list_refresh);
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return mWorkInfo.length;
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

                holder.work_title.setText(mWorkInfo[position].getF_TITLE());
                holder.work_context.setText(mWorkInfo[position].getF_SPONSUSER());
                holder.work_time.setText(mWorkInfo[position].getF_SORTNAME());
                holder.work_name.setText(str[position]);
                holder.work_num.setText(mWorkInfo[position].getF_TODOCOUNT() + "");
                if(mWorkInfo[position].getF_ISATT()==0){
                    holder.work_img.setVisibility(View.GONE);
                }else {
                    holder.work_img.setVisibility(View.VISIBLE);
                }
                if(mWorkInfo[position].getF_TODOCOUNT()==0){
                    holder.work_num.setVisibility(View.GONE);
                }else {
                    holder.work_num.setVisibility(View.VISIBLE);
                }
                GradientDrawable bgshapeone = (GradientDrawable) holder.work_num.getBackground();
                bgshapeone.setColor(Color.parseColor(colorOne));
                GradientDrawable bgshape = (GradientDrawable) holder.work_name.getBackground();
                bgshape.setColor(Color.parseColor(colors[position]));
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (position){
                            case 0:
                                intent = new Intent(getActivity(),OfficialActivity.class);
                                intent.putExtra("out","out");
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                break;
                            case 1:
                                intent = new Intent(getActivity(),OfficialActivity.class);
                                intent.putExtra("sign","sign");
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                break;
                            case 2:
                                intent = new Intent(getActivity(),OfficialActivity.class);
                                intent.putExtra("incom","incom");
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                break;
                            case 3:
                                intent = new Intent(getActivity(), WorkActivity.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                                break;
                        }
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
