package com.jokeep.swsmep.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.BaseActivity;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.view.ShowDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by wbq501 on 2016-1-28 11:32.
 * SWSMEP
 */
public class ChangePassdActivity extends BaseActivity{
    private LinearLayout passd_back,passd_edittext;
    private EditText old_passd,new_passd,agree_passd;
    private Button agree;
    private String passd1,passd2,passd3;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Intent intent;
    String FUSERID = null;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chang_passd);
        ActivityList.getInstance().addActivity(this);
        intent = getIntent();
        FUSERID = intent.getStringExtra("FUSERID").toString();
        init();
    }

    private void init() {
        dialog = new ShowDialog(ChangePassdActivity.this,R.style.MyDialog,"修改密码中...");
        passd_edittext = (LinearLayout) findViewById(R.id.passd_edittext);
        passd_back = (LinearLayout) findViewById(R.id.passd_back);
        old_passd = (EditText) findViewById(R.id.old_passd);
        new_passd = (EditText) findViewById(R.id.new_passd);
        agree_passd = (EditText) findViewById(R.id.agree_passd);
        agree = (Button) findViewById(R.id.change_psd_agree);

        sp = getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE);
        editor = sp.edit();

        initdata();
    }

    private void initdata() {
        passd_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        passd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassdActivity.this.finish();
            }
        });
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passd1 = old_passd.getText().toString().trim();
                passd2 = new_passd.getText().toString().trim();
                passd3 = agree_passd.getText().toString().trim();
                String psd = sp.getString("UserPsd", "");
                if (!psd.equals(passd1) || passd1==null || passd1.equals("")) {
                    Toast.makeText(ChangePassdActivity.this, "请输入旧密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passd2.length() < 6 || passd2.equals("") || passd2 == null) {
                    Toast.makeText(ChangePassdActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passd3.length() < 6 || passd3.equals("") || passd3 == null) {
                    Toast.makeText(ChangePassdActivity.this, "请输入确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!passd2.equals(passd3)) {
                    Toast.makeText(ChangePassdActivity.this, "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                httprequest();
            }
        });
    }

    private void httprequest() {
        dialog.show();
        final JSONObject object = new JSONObject();
        try {
            object.put("FUSERPWD", passd2);
            object.put("FUSERID", FUSERID);

//            String Ip = HttpIP.MainService + HttpIP.ResetPassword;
//            XutlsBase xutlsbase = new XutlsBase();
//            List<String> list = xutlsbase.post(Ip, object.toString(), sp.getString(SaveMsg.TOKENID, ""));
//            List<String> list1 = list;
//            String s = list.get(0);
//            String s2 = list.get(1);
            RequestParams params = new RequestParams(HttpIP.MainService+HttpIP.ResetPassword);
            params.addBodyParameter("parameter", AES.encrypt(object.toString()));
            params.setAsJsonContent(true);
            params.addBodyParameter(SaveMsg.TOKENID, sp.getString(SaveMsg.TOKENID, ""));
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    String s = AES.desEncrypt(result.toString());
                    Log.i("result", "result--->" + result.toString());
                    dialog.dismiss();
                    try {
                        JSONObject object2 = new JSONObject(s);
                        int code = object2.getInt("ErrorCode");
                        if (code==1){
                            Toast.makeText(ChangePassdActivity.this,object2.getString("ErrorMsg").toString(),Toast.LENGTH_SHORT).show();
                        }else if (code==0){
                            Toast.makeText(ChangePassdActivity.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                            editor.putString("UserPsd", passd2);
                            editor.commit();
                            ChangePassdActivity.this.finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.i("Throwable", "Throwable--->" + ex.getMessage());
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
            Toast.makeText(ChangePassdActivity.this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
