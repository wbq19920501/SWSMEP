package com.jokeep.swsmep.base;

import android.content.SharedPreferences;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbq501 on 2016-1-28 18:54.
 * SWSMEP
 */
public class XutlsBase {
    private static int CodeBack = 1;
    List<String> list ;
    public List<String> post(String Ip,String parameter,String TOKENID){
        list = new ArrayList<String>();
        RequestParams params = new RequestParams(Ip);
        params.addBodyParameter("parameter", AES.encrypt(parameter));
        params.setAsJsonContent(true);
        params.addBodyParameter(SaveMsg.TOKENID, TOKENID);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CodeBack = 0;
                list.add(CodeBack+"");
                list.add(result);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                CodeBack = 1;
                list.add(CodeBack+"");
                list.add(ex.getMessage());
            }
            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });
        return list;
    }
    public List<String> get(String Ip,String parameter ,String TOKENID){
        RequestParams params = new RequestParams(Ip);
        params.addBodyParameter("parameter",AES.encrypt(parameter));
        params.setAsJsonContent(true);
        params.addBodyParameter(SaveMsg.TOKENID, TOKENID);
        final List<String> list = new ArrayList<String>();
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CodeBack = 0;
                list.add(CodeBack+"");
                list.add(result);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                CodeBack = 1;
                list.add(CodeBack+"");
                list.add(ex.getMessage());
            }
            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });
        return list;
    }
}
