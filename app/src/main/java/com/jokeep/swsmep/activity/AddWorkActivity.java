package com.jokeep.swsmep.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.model.Work1Info;
import com.jokeep.swsmep.model.WorkTable;
import com.jokeep.swsmep.utls.FileUtils;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbq501 on 2016-2-26 16:54.
 * SWSMEP
 */
public class AddWorkActivity extends BaseActivity {
    ImageButton back;
    private LinearLayout file_list;
    private RelativeLayout choose_file;
    private TextView file_name;
    private ImageView add_file;
    Intent intent;
    String filepath, filename;
    View view;
    Button btn_next;
    ListView files_list;
    BaseAdapter adapter;
    EditText add_work_title;
    EditText add_context;
    String title, context;
    List<Work1Info> work1Infos;
    String f_jointid;
    private ShowDialog dialog;
    String TOKENID;
    int typeopen;
    List<WorkTable> workTables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_work);
        init();
        if (typeopen == 0) {
            f_jointid = "";
        } else {
            work1Infos = (List<Work1Info>) getIntent().getSerializableExtra("work1Infos");
            int position = getIntent().getIntExtra("intposition", 0);
            TOKENID = getIntent().getStringExtra("TOKENID");
            Work1Info work1Info = work1Infos.get(position);
            f_jointid = work1Info.getF_JOINTID();
            initdata();
        }
    }

    private void initdata() {
        dialog.show();
        RequestParams params = new RequestParams(HttpIP.MainService + HttpIP.JointAttByID);
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
                        if (code == 1) {
                            Toast.makeText(AddWorkActivity.this, object2.getString("ErrorMsg").toString(), Toast.LENGTH_SHORT).show();
                        } else if (code == 0) {
                            String Result = object2.getString("Result");
                            JSONArray array0 = new JSONArray(Result);
                            JSONArray arrayTable = new JSONArray(((JSONObject) array0.get(0)).getString("Table"));
                            JSONArray arrayTable1 = new JSONArray(((JSONObject) array0.get(0)).getString("Table1"));
                            JSONObject object3 = (JSONObject) arrayTable.get(0);
                            add_work_title.setText(object3.getString("F_TITLE"));

//                            add_context.addJavascriptInterface(new CoustomJs2Java(AddWorkActivity.this), "android");
//                            add_context.getSettings().setJavaScriptEnabled(true);
//                            add_context.loadUrl(HttpIP.PATH_EDITOR);
//                            add_context.setWebViewClient(new EditorClient(object3.getString("F_CONTENT")));
//                            add_context.setText(object3.getString("F_CONTENT"));
                            add_context.setText(Html.fromHtml(object3.getString("F_CONTENT")));
                            f_jointid = object3.getString("F_JOINTID");
                            for (int i = 0; i < arrayTable1.length(); i++) {
                                JSONObject jsonObject = (JSONObject) arrayTable1.get(i);
                                WorkTable workTable = new WorkTable();
                                workTable.setF_FILENAME(jsonObject.getString("F_FILENAME"));
                                workTable.setF_FILETYPE(jsonObject.getString("F_FILETYPE"));
                                workTable.setF_FILESIZE(jsonObject.getInt("F_FILESIZE"));
                                workTable.setF_STORAGEPATH(jsonObject.getString("F_STORAGEPATH"));
                                workTable.setIsUp(true);
                                workTables.add(workTable);
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

    class ViewHolder {
        TextView file_name;
        ImageView del_file;
    }

    private void init() {
        work1Infos = new ArrayList<Work1Info>();
        workTables = new ArrayList<WorkTable>();
        typeopen = getIntent().getIntExtra("typeopen", 0);
        dialog = new ShowDialog(AddWorkActivity.this, R.style.MyDialog, getResources().getString(R.string.dialogmsg));
        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAnim();
            }
        });
        add_work_title = (EditText) findViewById(R.id.add_work_title);
        add_context = (EditText) findViewById(R.id.add_context);

//        add_context.addJavascriptInterface(new CoustomJs2Java(AddWorkActivity.this), "android");
//        add_context.getSettings().setJavaScriptEnabled(true);
//        add_context.loadUrl(HttpIP.PATH_EDITOR);
//        add_context.setWebViewClient(new EditorClient(null));

        file_list = (LinearLayout) findViewById(R.id.file_list);
        files_list = (ListView) findViewById(R.id.files_list);
        LayoutInflater inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.add_work2_file, null);
        choose_file = (RelativeLayout) findViewById(R.id.choose_file);
        file_name = (TextView) findViewById(R.id.file_name);
        add_file = (ImageView) findViewById(R.id.add_file);
        add_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = add_work_title.getText().toString().trim();
                context = add_context.getText().toString().trim();
                if (title.equals("") || title == null) {
                    Toast.makeText(AddWorkActivity.this, "请输入标题", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(AddWorkActivity.this, WorkChooseManActivity1.class);
                    intent.putExtra("title", title);
                    intent.putExtra("context", context);
                    intent.putExtra("typeopen", typeopen);
                    intent.putExtra("worktable", (Serializable) workTables);
                    intent.putExtra("f_jointid", f_jointid);
                    startActivityForResult(intent, 2);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            }
        });

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
                if (convertView == null) {
                    convertView = LayoutInflater.from(AddWorkActivity.this).inflate(R.layout.add_work2_file, null);
                    holder = new ViewHolder();
                    holder.file_name = (TextView) convertView.findViewById(R.id.file_name);
                    holder.del_file = (ImageView) convertView.findViewById(R.id.del_file);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                boolean isup = workTables.get(position).isUp();
                if (isup) {
                    holder.file_name.setText(workTables.get(position).getF_FILENAME());
                } else {
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
    }
    private class EditorClient extends WebViewClient {
        String f_content;

        public EditorClient(String f_content) {
            this.f_content = f_content;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (f_content==null||f_content.equals("")){

            }else {
                add_context.setText(f_content);
            }
        }
    }
    public class CoustomJs2Java {
        private Context context;

        public CoustomJs2Java(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void setHtmlContext(String strHtmlContext) {

//            add_context.strHtmlContext = strHtmlContext;
        }
    }
    private void showFileChooser() {
        intent = new Intent(this, FileChooserList.class);
        startActivityForResult(intent, 100);
//
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
                    filepath = FileUtils.getPath(this, uri);
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
                    filepath = bundle.getString("FileURI", "");
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitAnim();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitAnim() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}
