package com.jokeep.swsmep.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.model.WorkTable;
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
 * Created by wbq501 on 2016-3-3 16:26.
 * SWSMEP
 */
public class WorkAnnexFragment extends Fragment{
    View fragment;
    ListView list;
    BaseAdapter adapter;
    String TOKENID,f_jointid;
    LinearLayout nomsg;
    List<WorkTable> workTables;
    private ShowDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragment == null){
            fragment = inflater.inflate(R.layout.workannex,container,false);
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
        dialog.show();
        RequestParams params = new RequestParams(HttpIP.MainService+HttpIP.JointAttByID);
        JSONObject object = new JSONObject();
        try {
            object.put("JointID", f_jointid);
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
                            Toast.makeText(getActivity(), object2.getString("ErrorMsg").toString(), Toast.LENGTH_SHORT).show();
                        }else if (code==0){
                            String Result = object2.getString("Result");
                            JSONArray array0 = new JSONArray(Result);
                            JSONArray arrayTable = new JSONArray(((JSONObject)array0.get(0)).getString("Table"));
                            JSONArray arrayTable1 = new JSONArray(((JSONObject)array0.get(0)).getString("Table1"));
                            JSONObject object3 = (JSONObject) arrayTable.get(0);
                            f_jointid = object3.getString("F_JOINTID");
                            for (int i=0;i<arrayTable1.length();i++){
                                JSONObject jsonObject = (JSONObject) arrayTable1.get(i);
                                WorkTable workTable = new WorkTable();
                                workTable.setF_FILENAME(jsonObject.getString("F_FILENAME"));
                                workTable.setF_FILETYPE(jsonObject.getString("F_FILETYPE"));
                                workTable.setF_FILESIZE(jsonObject.getInt("F_FILESIZE"));
                                workTable.setF_STORAGEPATH(jsonObject.getString("F_STORAGEPATH"));
                                workTable.setIsUp(true);
                                workTables.add(workTable);
                            }
                            if (workTables.size() == 0){
                                nomsg.setVisibility(View.VISIBLE);
                                list.setVisibility(View.GONE);
                            }else {
                                list.setVisibility(View.VISIBLE);
                                nomsg.setVisibility(View.GONE);
                            }
                            adapter.notifyDataSetChanged();
                        }
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

    private void init() {
        workTables = new ArrayList<WorkTable>();
        Bundle data = getArguments();
        TOKENID = data.getString("TOKENID");
        f_jointid = data.getString("JointID");

        dialog = new ShowDialog(getActivity(),R.style.MyDialog,getResources().getString(R.string.dialogmsg));

        list = (ListView) fragment.findViewById(R.id.listwork_annex);
        nomsg = (LinearLayout) fragment.findViewById(R.id.nomsg);
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return workTables.size();
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
                if (convertView==null){
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.look_fileitem,null);
                    holder = new ViewHolder();
                    holder.filename = (TextView) convertView.findViewById(R.id.file_name);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                WorkTable workTable = workTables.get(position);
                holder.filename.setText(workTable.getF_FILENAME());
                return convertView;
            }
        };
        list.setAdapter(adapter);
    }
    class ViewHolder{
        TextView filename;
    }
}
