package com.jokeep.swsmep.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.filechooser.FileChooserList;
import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.BaseActivity;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.MyData;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.base.SwsApplication;
import com.jokeep.swsmep.model.SuggestionInfo;
import com.jokeep.swsmep.model.WorkTable;
import com.jokeep.swsmep.utls.FileUtils;
import com.jokeep.swsmep.view.IdeaMsg1Window;
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
 * Created by wbq501 on 2016-3-3 18:51.
 * SWSMEP
 */
public class WorkReturnIdea2Activity extends BaseActivity{
    public static final String action = "com.swsmep.suggestion";
    private ImageButton back;
    private Button me_msg,btn_sub;
    private EditText add_context;
    private ImageView add_file;
    private ListView files_list;
    IdeaMsg1Window popw;
    Intent intent;
    List<WorkTable> workTables;
    List<Integer> listchange;
    BaseAdapter adapter;
    String suggestions;
    private ShowDialog dialog;
    private SwsApplication application;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    String TOKENID;
    List<SuggestionInfo> listsuggestionInfo;
    String Title,BusinessCode,MainID;
    private View view_file;
    private RelativeLayout mRelativeLayout;
    private int reply;
    private MyData mMyData = MyData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workreturn_idea2);
        init();
        initdata();
    }

    private void initdata() {

    }

    private void init() {
        listsuggestionInfo = new ArrayList<SuggestionInfo>();
        workTables = new ArrayList<WorkTable>();
        listchange = new ArrayList<Integer>();
        application = (SwsApplication) getApplication();
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_file);
        view_file = findViewById(R.id.view_file);
        listsuggestionInfo = (List<SuggestionInfo>) getIntent().getSerializableExtra("suggestionInfo");
        reply = getIntent().getIntExtra("reply", 2);
        if(reply==1){
            MainID = getIntent().getStringExtra("MainID");
            mRelativeLayout.setVisibility(View.GONE);
        }else {
            MainID =mMyData.getmRECEIVEDBILLID();
            view_file.setVisibility(View.VISIBLE);
            mRelativeLayout.setVisibility(View.VISIBLE);
        }

        Title = getIntent().getStringExtra("Title");
        BusinessCode = getIntent().getStringExtra("BusinessCode");

        sp = WorkReturnIdea2Activity.this.getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE);
        editor = sp.edit();
        TOKENID = sp.getString(SaveMsg.TOKENID, "");

        dialog = new ShowDialog(WorkReturnIdea2Activity.this,R.style.MyDialog,getResources().getString(R.string.upmsg));
        add_context = (EditText) findViewById(R.id.add_context);
        me_msg = (Button) findViewById(R.id.me_msg);
        btn_sub = (Button) findViewById(R.id.btn_sub);
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggestions = add_context.getText().toString();
                requesthttp();
            }
        });
        add_file = (ImageView) findViewById(R.id.add_file);
        add_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        files_list = (ListView) findViewById(R.id.files_list);
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
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;
                if (convertView == null){
                    convertView = LayoutInflater.from(WorkReturnIdea2Activity.this).inflate(R.layout.add_work2_file,null);
                    holder = new ViewHolder();
                    holder.file_name = (TextView) convertView.findViewById(R.id.file_name);
                    holder.del_file = (ImageView) convertView.findViewById(R.id.del_file);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                boolean isup = workTables.get(position).isUp();
                if (isup){
                    holder.file_name.setText(workTables.get(position).getF_FILENAME());
                }else {
                    holder.file_name.setText(FileUtils.getFileName(workTables.get(position).getF_STORAGEPATH()));
                }
                holder.del_file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        workTables.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                return convertView;
            }
        };
        files_list.setAdapter(adapter);
        final View top_line = findViewById(R.id.top_line);
        me_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popw = new IdeaMsg1Window(WorkReturnIdea2Activity.this,itemsOnClick);
                //显示窗口
//                popw.showAtLocation(v, Gravity.RIGHT, 0, 0);
                popw.showAsDropDown(top_line,0,0);
                //设置layout在PopupWindow中显示的位置
                getmsg();
            }
        });
        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAnim();
            }
        });
    }

    private void requesthttp() {
        dialog.show();
        RequestParams params = new RequestParams(HttpIP.UploadFiles);
        for (int i=0;i<workTables.size();i++){
            boolean isup = workTables.get(i).isUp();
            if (isup){

            }else {
                listchange.add(i);
                params.addBodyParameter("FileContext"+i,new File(workTables.get(i).getF_STORAGEPATH()));
            }
        }
        if (listchange.size()>0){
            params.addBodyParameter("SavaDir","UploadFiles");
            params.setAsJsonContent(true);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject object2 = new JSONObject(result.toString());
                        int code = object2.getInt("ErrorCode");
                        if (code == 1){
                            dialog.dismiss();
                            Toast.makeText(WorkReturnIdea2Activity.this,object2.getString("ErrorMsg")+"",Toast.LENGTH_SHORT).show();
                        }else if (code == 0){
                            JSONArray array = new JSONArray(object2.getString("Result").toString());
                            for (int i=0;i<array.length();i++){
                                JSONObject object = (JSONObject) array.get(i);
                                workTables.get(Integer.valueOf(listchange.get(i))).setIsUp(true);
                                workTables.get(Integer.valueOf(listchange.get(i))).setF_STORAGEPATH(object.getString("SaveFilePath"));
                            }
                            upmsg(HttpIP.OpinionReply_Insert);
                        }
                    } catch (JSONException e) {
                        dialog.dismiss();
                        Toast.makeText(WorkReturnIdea2Activity.this,"上传文件失败",Toast.LENGTH_SHORT).show();
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
        }else {
            upmsg(HttpIP.OpinionReply_Insert);
        }
    }

    private void upmsg(String upmsg) {
        dialog.show();
        RequestParams params = new RequestParams(HttpIP.MainService+upmsg);
        JSONArray arrayfile = new JSONArray();
        JSONObject object = new JSONObject();
        try {
            for (int i=0;i<workTables.size();i++){
                JSONObject objectfile = new JSONObject();
                WorkTable table = workTables.get(i);
                objectfile.put("F_FILENAME",table.getF_FILENAME());
                objectfile.put("F_FILESIZE",table.getF_FILESIZE());
                objectfile.put("F_STORAGEPATH",table.getF_STORAGEPATH());
                objectfile.put("F_FILETYPE",table.getF_FILETYPE());
                arrayfile.put(objectfile);
            }
            SuggestionInfo suggestionInfo = listsuggestionInfo.get(0);
            object.put("ExcutMainID",suggestionInfo.getF_EXECUTMAINID());
            object.put("OpinionID",suggestionInfo.getF_OPINIONID());
            object.put("UnitID",application.getF_MAINUNITID());
            object.put("DepartmentID",application.getF_MAINDEPARTID());
            object.put("UserID",application.getFUSERID());
            object.put("UserName",application.getF_USERNAME());
            object.put("Opinion",suggestions);
            object.put("Attachment",arrayfile);
            object.put("OriginalID",suggestionInfo.getF_HANDLEID());
            object.put("OriginalName",suggestionInfo.getF_HANDLENAME());
            object.put("MainID",MainID);
            object.put("Title",Title);
            object.put("BusinessCode",BusinessCode);

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
                        if (code == 1) {
                            Toast.makeText(WorkReturnIdea2Activity.this, object2.getString("ErrorMsg") + "", Toast.LENGTH_SHORT).show();
                        } else {
//                            intent = new Intent(action);
                            setResult(RESULT_OK);
//                            sendBroadcast(intent);
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

    private void showFileChooser() {
        intent = new Intent(this, FileChooserList.class);
        startActivityForResult(intent, 100);
//        intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        try {
//            startActivityForResult( Intent.createChooser(intent, "Select a File to Upload"), 1);
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
//        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String filepath = FileUtils.getPath(this, uri);
                    upfile(filepath);
                    adapter.notifyDataSetChanged();
                }
                break;
            case 2:
                if (resultCode == RESULT_OK)
                    finish();
                break;
            case 100:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    Uri uri = data.getData();
                    String filepath = bundle.getString("FileURI", "");
                    upfile(filepath);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void upfile(String listpath) {
        InputStream is;
        try {
            File myfile = new File(listpath);
            String filetype = myfile.getName().substring(myfile.getName().lastIndexOf(".") + 1);
            String filename = FileUtils.getFileName(listpath);
            is = new FileInputStream(listpath);
            int filesize = is.available();
            WorkTable workTable = new WorkTable();
            workTable.setF_FILENAME(filename);
            workTable.setF_STORAGEPATH(listpath);
            workTable.setF_FILESIZE(filesize);
            workTable.setF_FILETYPE(filetype);
            workTable.setIsUp(false);
            workTables.add(workTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class ViewHolder{
        TextView file_name;
        ImageView del_file;
    }
    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener(){

        public void onClick(View v) {
            switch (v.getId()){

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

    public void getmsg() {
        if (popw == null)
            return;
        popw.setOnData(new IdeaMsg1Window.OnGetData() {
            @Override
            public void onDataCallBack(List<String> listmsg) {
                add_context.setText(listmsg.get(0).toString());
            }
        });
    }
}
