package com.jokeep.swsmep.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.BaseActivity;
import com.jokeep.swsmep.base.Client;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.base.SwsApplication;
import com.jokeep.swsmep.model.FileMsg;
import com.jokeep.swsmep.model.Work2Info;
import com.jokeep.swsmep.utls.FileUtils;
import com.jokeep.swsmep.view.SelectMan1Window;
import com.jokeep.swsmep.view.SelectMan2Window;
import com.jokeep.swsmep.view.ShowDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbq501 on 2016-2-29 10:39.
 * SWSMEP
 */
public class WorkChooseManActivity1 extends BaseActivity{
    private LinearLayout back;
    private TextView choose_man;
    private ListView man_list;
    SelectMan1Window popw1;
    SelectMan2Window popw2;
    BaseAdapter adapter;
    Intent intent;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    String UserID,TOKENID;
    List<Work2Info> list ;
    List<FileMsg> listfile;
    List<String> filespath;
    String title,context;
    Button btn_sub;
    private ShowDialog dialog;
    List<byte[]> listbyte;
    String SaveType,JointID;
    private SwsApplication application;
    List<String> resultpath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_man1);
        ActivityList.getInstance().addActivity(this);
        init();
        initdata();
    }

    private void initdata() {
        listbyte = new ArrayList<byte[]>();
        listfile = new ArrayList<FileMsg>();
        filespath = new ArrayList<String>();
        resultpath = new ArrayList<String>();
        filespath = getIntent().getStringArrayListExtra("filespath");

        title = getIntent().getStringExtra("title");
        context = getIntent().getStringExtra("context");
        btn_sub = (Button) findViewById(R.id.btn_sub);
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upfile();
            }
        });
    }

    private void upfile() {
        dialog.show();
        byte[] bytes = null;
        InputStream is;
        try {
            for (int i=0;i<filespath.size();i++){
                String path = filespath.get(i);
                File myfile = new File(path);
                FileMsg fileMsg = new FileMsg();
                String filetype = myfile.getName().substring(myfile.getName().lastIndexOf(".") + 1);
                String filename = FileUtils.getFileName(path);
                JSONObject object = new JSONObject();
                is = new FileInputStream(path);
                bytes = new byte[(int) myfile.length()];
                int filesize = is.available();
                int len = 0;
                int curLen = 0;
                while ((len = is.read(bytes)) != -1) {
                    curLen += len;
                    is.read(bytes);
                }
                is.close();
                object.put("FileType", filetype);
                byte[] updata = Client.getPacket(object.toString(), bytes);// 参数与文件封装成单个数据
                listbyte.add(updata);
                fileMsg.setFileName(filename);
                fileMsg.setFilePath(path);
                fileMsg.setFileSize(filesize);
                fileMsg.setFileType(filetype);
                listfile.add(fileMsg);
            }
            requesthttp(listbyte);
        } catch (Exception e) {
            dialog.dismiss();
            Toast.makeText(WorkChooseManActivity1.this, "上传文件失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void requesthttp(List<byte[]> updata) {
        RequestParams params = new RequestParams(HttpIP.UploadFiles);
        for (int i=0;i<filespath.size();i++){
            params.addBodyParameter("FileContext"+i,new File(filespath.get(i)));
//            params.addBodyParameter("FileContext"+i, updata.get(i), null);
        }
        params.addBodyParameter("SavaPath","UploadFiles");
        params.setAsJsonContent(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object2 = new JSONObject(result.toString());
                    int code = object2.getInt("ErrorCode");
                    if (code == 1){
                        dialog.dismiss();
                        Toast.makeText(WorkChooseManActivity1.this,object2.getString("ErrorMsg")+"",Toast.LENGTH_SHORT).show();
                    }else if (code == 0){
                        JSONArray array = new JSONArray(object2.getString("Result").toString());
                        for (int i=0;i<array.length();i++){
                            JSONObject object = (JSONObject) array.get(i);
                            resultpath.add(object.getString("SaveFilePath"));
                        }
                        upmsg();
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    Toast.makeText(WorkChooseManActivity1.this,"上传文件失败",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog.dismiss();
                Log.i("Throwable", ex.getMessage());
            }
            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });
    }

    private void upmsg() {
        RequestParams params = new RequestParams(HttpIP.MainService+HttpIP.Joint_Save);
        JSONArray arrayman = new JSONArray();
        JSONArray arrayfile = new JSONArray();
        JSONObject object = new JSONObject();
        JSONObject objectman = new JSONObject();
        JSONObject objectfile = new JSONObject();
        if (list.size()==0){
            Toast.makeText(WorkChooseManActivity1.this,"请选择办理人",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            for (int i=0;i<list.size();i++){
                objectman.put("F_DATAID",list.get(i).getF_USERID());
                objectman.put("F_NAME",list.get(i).getF_USERNAME());
                arrayman.put(objectman);
            }
            for (int i=0;i<listfile.size();i++){
                objectfile.put("F_FILENAME",listfile.get(i).getFileName());
                objectfile.put("F_FILESIZE",listfile.get(i).getFileSize());
                objectfile.put("F_STORAGEPATH",resultpath.get(i));
                objectfile.put("F_FILETYPE",listfile.get(i).getFileType());
                arrayfile.put(objectfile);
            }
            object.put("SaveType",SaveType);
            object.put("JointID","");
            object.put("Title",title);
            object.put("Content",context);
            object.put("Attachment",arrayfile);//附件集合
            object.put("JonintScope",arrayman);//协同人员范围集合
            object.put("UserID",application.getFUSERID());
            object.put("UserName",application.getF_USERNAME());
            object.put("UnitID",application.getF_MAINUNITID());
            object.put("DepartmentID",application.getF_MAINDEPARTID());
            params.addBodyParameter("parameter", AES.encrypt(object.toString()));
            params.setAsJsonContent(true);
            params.addBodyParameter(SaveMsg.TOKENID, TOKENID);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    String s = AES.desEncrypt(result.toString());
                    Log.d("s", s);
                    try {
                        JSONObject object2 = new JSONObject(s.toString());
                        int code = object2.getInt("ErrorCode");
                        if (code == 1){
                            Toast.makeText(WorkChooseManActivity1.this,object2.getString("ErrorMsg")+"",Toast.LENGTH_SHORT).show();
                        }else {
                            intent = new Intent();
                            setResult(RESULT_OK);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
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
        application = (SwsApplication) getApplication();
        UserID = application.getFUSERID();
        sp = WorkChooseManActivity1.this.getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE);
        editor = sp.edit();
        TOKENID = sp.getString(SaveMsg.TOKENID, "");
        SaveType = "Add";
        list = new ArrayList<Work2Info>();
        dialog = new ShowDialog(WorkChooseManActivity1.this,R.style.MyDialog,getResources().getString(R.string.upmsg));
        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAnim();
            }
        });
        choose_man = (TextView) findViewById(R.id.choose_man);
        choose_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popw1 = new SelectMan1Window(WorkChooseManActivity1.this, itemsOnClick, UserID, TOKENID);
                //显示窗口
                popw1.showAtLocation(v, Gravity.RIGHT, 0, 0);
                //设置layout在PopupWindow中显示的位置
                geimsg();
            }
        });

        man_list = (ListView) findViewById(R.id.man_list);
        adapter = new BaseAdapter() {
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
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;
                if (convertView == null){
                    convertView = LayoutInflater.from(WorkChooseManActivity1.this).inflate(R.layout.choose_manitem,null);
                    holder = new ViewHolder();
                    holder.link_name = (TextView) convertView.findViewById(R.id.link_name);
                    holder.link_type = (TextView) convertView.findViewById(R.id.link_type);
                    holder.del_man = (ImageView) convertView.findViewById(R.id.del_man);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                Work2Info work2Info = list.get(position);
                holder.link_name.setText(work2Info.getF_USERNAME());
                holder.link_type.setText(work2Info.getF_DEPARTMENTNAME() + "-" + work2Info.getF_POSITIONNAME());
                holder.del_man.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                return convertView;
            }
        };
        man_list.setAdapter(adapter);
    }

    private void geimsg() {
        if (popw1 == null)
            return;
        popw1.setOnData(new SelectMan1Window.OnGetData() {
            @Override
            public void onDataCallBack(List<Work2Info> getlist) {
                Log.i("-------------", getlist.toString());
                list.clear();
                list = getlist;
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void geimsg2() {
        if (popw2 == null)
            return;
        popw2.setOnData(new SelectMan2Window.OnGetData() {
            @Override
            public void onDataCallBack(List<Work2Info> getlist) {
                Log.i("-------------", getlist.toString());
                list.clear();
                list = getlist;
                adapter.notifyDataSetChanged();
            }
        });
    }
    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener(){

        public void onClick(View v) {
            switch (v.getId()){
                case R.id.selectman_sx:
                    LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view=inflater.inflate(R.layout.work_man1, null);
                    popw2 = new SelectMan2Window(WorkChooseManActivity1.this,itemsOnClick2,UserID,TOKENID);
                    //显示窗口
                    popw2.showAtLocation(view, Gravity.RIGHT, 0, 0);
                    //设置layout在PopupWindow中显示的位置
                    popw1.dismiss();
                    geimsg2();
                    break;
                case R.id.selectman_search:
                    break;
                case R.id.btn_sub:
                    break;
            }
        }
    };
    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick2 = new View.OnClickListener(){

        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_sub:
                    break;
            }
        }
    };
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
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
    static class ViewHolder{
        TextView link_name,link_type;
        ImageView del_man;
    }
}
