package com.jokeep.swsmep.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.BaseActivity;
import com.jokeep.swsmep.utls.FileUtils;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_work);
        init();
        initdata();
    }

    private void initdata() {

    }

    private void init() {
        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAnim();
            }
        });
        file_list = (LinearLayout) findViewById(R.id.file_list);
        LayoutInflater inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.add_work2_file, null);
        choose_file = (RelativeLayout) findViewById(R.id.choose_file);
        file_name = (TextView) choose_file.findViewById(R.id.file_name);
        add_file = (ImageView) choose_file.findViewById(R.id.add_file);
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
                intent = new Intent(AddWorkActivity.this,WorkChooseManActivity1.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
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
                    filename = FileUtils.getFileName(filepath);
                    file_name.setText(filename);
                    file_name.setTextColor(getResources().getColor(R.color.file_color));
                    add_file.setBackground(getResources().getDrawable(R.mipmap.del_file));
                    file_list.addView(view);
                }
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
