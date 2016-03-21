package com.jokeep.swsmep.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.base.WebSeting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by wbq501 on 2016-3-3 16:25.
 * SWSMEP
 */
public class WorkMainBodyFragment extends Fragment{
    View fragment;
    private WebView webview;
    String ExecutMainID,NodeID,NodeHandlerID,OriginalID,DataGuid,ToDoID;
    String F_LINKURL,JointID,TOKENID;
    int typeopen;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragment == null){
            fragment = inflater.inflate(R.layout.workman_body,container,false);
        }else {
            // 缓存的rootView需要判断是否已经被加过parent
            // 如果有parent需要从parent删除
            ViewGroup parent = (ViewGroup) fragment.getParent();
            if (parent != null) {
                parent.removeView(fragment);
            }
        }
        init();
        if (typeopen == 4){
            openwebview(F_LINKURL);
        }else {
            initdata();
        }
        return fragment;
    }

    private void initdata() {
        RequestParams params = new RequestParams(HttpIP.MainService+HttpIP.JointToDoInfo);
        JSONObject object = new JSONObject();
        try {
            object.put("JointID",JointID);
            object.put("ExecutMainID",ExecutMainID);
            object.put("NodeID",NodeID);
            object.put("ToDoID",ToDoID);
            object.put("NodeHandlerID",NodeHandlerID);
            params.addBodyParameter("parameter", AES.encrypt(object.toString()));
            params.setAsJsonContent(true);
            params.addBodyParameter(SaveMsg.TOKENID, TOKENID);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    String s = AES.desEncrypt(result.toString());
                    Log.d("s", s);
                    try {
                        JSONObject object2 = new JSONObject(s);
                        int code = object2.getInt("ErrorCode");
                        if (code==1){
                            Toast.makeText(getActivity(), object2.getString("ErrorMsg").toString(), Toast.LENGTH_SHORT).show();
                        }else if (code==0){
                            String Result = object2.getString("Result");
                            JSONArray array0 = new JSONArray(Result);
                            JSONArray arrayTable = new JSONArray(((JSONObject)array0.get(0)).getString("Table"));
                            JSONObject object3 = (JSONObject) arrayTable.get(0);
                            openwebview(object3.getString("F_FILEPATH"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
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

    private void openwebview(String f_filepath) {
        WebSeting.openweb(webview);
        //WebView加载web资源
        webview.loadUrl(HttpIP.Web+f_filepath);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                } else {
                    // 加载中
                }
            }
        });
    }

    private void init() {
        typeopen = getActivity().getIntent().getIntExtra("typeopen", 4);
        Bundle data = getArguments();
        F_LINKURL = data.getString("F_LINKURL");
        JointID = data.getString("JointID");
        TOKENID = data.getString("TOKENID");

        ExecutMainID = Uri.parse(F_LINKURL).getQueryParameter("ExecutMainID");
        NodeID = Uri.parse(F_LINKURL).getQueryParameter("NodeID");
        NodeHandlerID = Uri.parse(F_LINKURL).getQueryParameter("NodeHandlerID");
        OriginalID = Uri.parse(F_LINKURL).getQueryParameter("OriginalID");
        DataGuid = Uri.parse(F_LINKURL).getQueryParameter("DataGuid");
        ToDoID = Uri.parse(F_LINKURL).getQueryParameter("ToDoID");

        webview = (WebView) fragment.findViewById(R.id.webview);

        /**
         * 优先使用缓存
         */
//        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        /**
         * 不适用缓存
         */
//        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    }
}
