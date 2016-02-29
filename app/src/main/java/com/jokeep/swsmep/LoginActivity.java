package com.jokeep.swsmep;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.activity.ActivityList;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.Client;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.MyTools;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.db.RequestDb;
import com.jokeep.swsmep.model.UserInfo;
import com.jokeep.swsmep.utls.Utils;

import org.json.JSONObject;

import java.util.Random;
import java.util.UUID;

/**
 * Created by wbq501 on 2016-1-7 09:59.
 * SWSMEP
 */
public class LoginActivity extends Activity implements View.OnClickListener{
    private EditText usename,usepsd,login_yzm;
    private Button uselogin;
    private TextView yzm_img;
    private String name,psd;
    private Random random;
    private int randomx;
    private ImageView clean_usename,clean_psd;
    private LinearLayout login_onclick;
    private CheckBox save_psd;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String res = "";

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ActivityList.getInstance().addActivity(this);
        init();
        initdata();
    }

    private void initdata() {
        random = new Random();
        randomx = random.nextInt(9999-1000+1)+1000;
        yzm_img.setText(randomx + "");
        usename.setOnTouchListener(new View.OnTouchListener() {
            int i = 0;
            long fistclick, twoclick;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    i++;
                    if (i == 1) {
                        fistclick = System.currentTimeMillis();
                    } else if (i == 2) {
                        twoclick = System.currentTimeMillis();
                        if (twoclick - fistclick > 1000) {

                        } else {
                            clean_usename.setVisibility(View.VISIBLE);
                        }
                        i = 0;
                    }
                    return false;
                }
                return false;
            }
        });
        usepsd.setOnTouchListener(new View.OnTouchListener() {
            int i = 0;
            long fistclick, twoclick;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    i++;
                    if (i == 1) {
                        fistclick = System.currentTimeMillis();
                    } else if (i == 2) {
                        twoclick = System.currentTimeMillis();
                        if (twoclick - fistclick > 1000) {

                        } else {
                            clean_psd.setVisibility(View.VISIBLE);
                        }
                        i = 0;
                    }
                    return false;
                }
                return false;
            }
        });
    }

    private void init() {
        dialog = new Dialog(LoginActivity.this,R.style.MyDialog);
        dialog.setContentView(R.layout.dialog);
        usename = (EditText) findViewById(R.id.usename);
        usepsd = (EditText) findViewById(R.id.usepsd);
        uselogin = (Button) findViewById(R.id.uselogin);
        uselogin.setOnClickListener(this);

        yzm_img = (TextView) findViewById(R.id.login_yzm_img);
        yzm_img.setOnClickListener(this);

        login_yzm = (EditText) findViewById(R.id.login_yzm);
        clean_usename = (ImageView) findViewById(R.id.clean_usename);
        clean_psd = (ImageView) findViewById(R.id.clean_psd);
        clean_usename.setOnClickListener(this);
        clean_psd.setOnClickListener(this);
        login_onclick = (LinearLayout) findViewById(R.id.login_onclick);
        login_onclick.setOnClickListener(this);
        sp = getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE);
        editor = sp.edit();

        save_psd = (CheckBox) findViewById(R.id.save_psd);
        boolean cb = sp.getBoolean("CheckBox",false);
        if (cb){
            name = sp.getString("UserName","");
            psd = sp.getString("UserPsd","");
            usename.setText(name);
            usepsd.setText(psd);
            save_psd.setChecked(true);
        }else {
            name = sp.getString("UserName","");
            usename.setText(name);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_yzm_img:
                randomx = random.nextInt(9999 - 1000 + 1) + 1000;
                yzm_img.setText(randomx + "");
                break;
            case R.id.uselogin:
                name = usename.getText().toString().trim();
                psd = usepsd.getText().toString().trim();
                String yzm = login_yzm.getText().toString().toString().trim();

                if (name.equals("")||psd.equals("")||name==null||psd==null){
                    Toast.makeText(LoginActivity.this,"用户或密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!yzm_img.getText().toString().trim().equals(yzm) || yzm.equals("")) {
                    Toast.makeText(LoginActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean save = save_psd.isChecked();
                if (save){
                    editor.putString("UserName",name);
                    editor.putString("UserPsd",psd);
                    editor.putBoolean("CheckBox",true);
                    editor.commit();
                }else {
                    editor.putString("UserName",name);
                    editor.putString("UserPsd",psd);
                    editor.putBoolean("CheckBox",false);
                    editor.commit();
                }
//                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                startActivity(intent);
//                LoginActivity.this.finish();
                httpRequest();
                break;
            case R.id.login_onclick:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
            case R.id.clean_usename:
                usename.setText("");
                clean_usename.setVisibility(View.GONE);
                break;
            case R.id.clean_psd:
                usepsd.setText("");
                clean_psd.setVisibility(View.GONE);
                break;
        }
    }

    private void httpRequest() {
        dialog.show();
        final String uuid = MyTools.toUpperCase(UUID.randomUUID().toString().replaceAll("-", ""));
        editor.putString(SaveMsg.TOKENID, uuid);
        editor.commit();
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                Client client = new Client(HttpIP.ACTION_KEY,uuid);
                client.callKey();
                res = client.getResponseString();
                if (res.equals("1")||res=="1"){
//                    randomx = random.nextInt(9999 - 1000 + 1) + 1000;
//                    yzm_img.setText(randomx + "");
                    Toast.makeText(LoginActivity.this,"当前无网络",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else {
                    AES.key = res;
                    AES.iv = AES.key;
                    editor.putString("AES", AES.key);
                    editor.commit();
                    loginuser(uuid);
                }
                Looper.loop();
            }
        }.start();
    }
    private void loginuser(String uuid) {
        JSONObject object = new JSONObject();
        try {
            object.put("UserCode",name);
            object.put("PassWord", psd);
            object.put("IP",MyTools.getIP(LoginActivity.this));
            String mac = MyTools.getMac(LoginActivity.this);
            if (mac == null || "".equals(mac)) {
                object.put("Mac", "");
            } else {
                object.put("Mac", MyTools.getMac(LoginActivity.this));
            }
            boolean isTablet = Utils.getInstance().isTablet(LoginActivity.this);
            if (isTablet) {
                object.put("F_LoginType", 5);
            } else {
                object.put("F_LoginType", 2);
            }
            object.put("F_EncryptKey", AES.key);
            object.put("ClientVersion", getVersion(LoginActivity.this));
            String mac2 = MyTools.getMac(LoginActivity.this);
            object.put("F_DEVICEID",mac2.replace(":", "")
                    .trim().toUpperCase());
            Client client = new Client(HttpIP.ACTION_KEY,object.toString(),uuid);
            client.calllogin();
            res = client.getResponseString();
            if (res.equals("1")||res=="1"){
                Toast.makeText(LoginActivity.this,"登录错误",Toast.LENGTH_SHORT).show();
                randomx = random.nextInt(9999 - 1000 + 1) + 1000;
                yzm_img.setText(randomx + "");
                dialog.dismiss();
            }else {
                UserInfo.ResultEntity.UserInfoEntity userInfoEntity = new UserInfo.ResultEntity.UserInfoEntity();
//                DbManager db = x.getDb(((SwsApplication) getApplicationContext()).getDaoConfig());

                int i = RequestDb.UserInfo(res);
                Intent intent = null;
                switch (i){
                    case SaveMsg.successCode:
                        intent = new Intent(LoginActivity.this,MainActivity.class);
                        String s = RequestDb.ResultInfo().get(0).getF_USERHEADURI();
                        userInfoEntity = RequestDb.ResultInfo().get(0);
                        intent.putExtra("result", userInfoEntity);
                        startActivity(intent);
                        LoginActivity.this.finish();
                        break;
                    case SaveMsg.errorCode:
                        String errormsg = RequestDb.ErrorMsg();
                        Toast.makeText(LoginActivity.this,errormsg,Toast.LENGTH_SHORT).show();
                        randomx = random.nextInt(9999 - 1000 + 1) + 1000;
                        yzm_img.setText(randomx + "");
                        break;
                    case SaveMsg.UPDATEF:
                        break;
                    case SaveMsg.UPDATECODE:
                        break;
                }
                dialog.dismiss();
            }
        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
        }
    }
    public String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }
}
