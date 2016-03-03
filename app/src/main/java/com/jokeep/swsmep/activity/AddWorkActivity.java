package com.jokeep.swsmep.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.BaseActivity;
import com.jokeep.swsmep.utls.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbq501 on 2016-2-26 16:54.
 * SWSMEP
 */
public class AddWorkActivity extends BaseActivity{
    LinearLayout back;
    private LinearLayout file_list;
    private RelativeLayout choose_file;
    private TextView file_name;
    private ImageView add_file;
    Intent intent;
    String filepath,filename;
    View view;
    Button btn_next;
    ListView files_list;
    List<String> listpath;
    BaseAdapter adapter;
    EditText add_work_title,add_context;
    String title,context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_work);
        init();
        initdata();
    }

    private void initdata() {
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return listpath.size();
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
                    convertView = LayoutInflater.from(AddWorkActivity.this).inflate(R.layout.add_work2_file,null);
                    holder = new ViewHolder();
                    holder.file_name = (TextView) convertView.findViewById(R.id.file_name);
                    holder.del_file = (ImageView) convertView.findViewById(R.id.del_file);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.file_name.setText(FileUtils.getFileName(listpath.get(position)));
                holder.del_file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listpath.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                return convertView;
            }
        };
        files_list.setAdapter(adapter);
    }
    class ViewHolder{
        TextView file_name;
        ImageView del_file;
    }

    private void init() {
        listpath = new ArrayList<String>();
        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAnim();
            }
        });
        add_work_title = (EditText) findViewById(R.id.add_work_title);
        add_context = (EditText) findViewById(R.id.add_context);
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
                if (title.equals("")||title==null){
                    Toast.makeText(AddWorkActivity.this,"请输入标题",Toast.LENGTH_SHORT).show();
                }else {
                    intent = new Intent(AddWorkActivity.this,WorkChooseManActivity1.class);
                    intent.putExtra("title",title);
                    intent.putExtra("context",context);
                    intent.putStringArrayListExtra("filespath", (ArrayList<String>) listpath);
                    startActivityForResult(intent,2);
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                }
            }
        });
    }
    private void showFileChooser() {
        intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult( Intent.createChooser(intent, "Select a File to Upload"), 1);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    filepath = FileUtils.getPath(this, uri);
//                    filename = FileUtils.getFileName(filepath);
                    listpath.add(filepath);
                    adapter.notifyDataSetChanged();
                }
                break;
            case 2:
                if (resultCode == RESULT_OK)
                finish();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
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
}
