package com.jokeep.swsmep.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.base.WebSeting;
import com.jokeep.swsmep.model.Work1Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbq501 on 2016-3-3 14:50.
 * SWSMEP
 */
public class WorkFlowFragmet extends Fragment{
    View fragment;
    private WebView webview;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    String TOKENID;
    String F_EXECUTMAINID;
    List<Work1Info> work1Infos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragment == null){
            fragment = inflater.inflate(R.layout.work_flow,container,false);
        }else {
            // 缓存的rootView需要判断是否已经被加过parent
            // 如果有parent需要从parent删除
            ViewGroup parent = (ViewGroup) fragment.getParent();
            if (parent != null) {
                parent.removeView(fragment);
            }
        }
        init();
        return fragment;
    }
    private void openwebview() {
        String url = HttpIP.FlowPreview+"?F_ExecutMainID="+F_EXECUTMAINID+"&TokenID="+TOKENID+"&Ismobile=1";
        WebSeting.openweb(webview);
        //WebView加载web资源
        webview.loadUrl(url);
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
        work1Infos = new ArrayList<Work1Info>();
        work1Infos = (List<Work1Info>) getActivity().getIntent().getSerializableExtra("work1Infos");
//        final int position = getActivity().getIntent().getIntExtra("intposition", 0);

        sp = getActivity().getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE);
        editor = sp.edit();
        TOKENID = sp.getString(SaveMsg.TOKENID, "");

        F_EXECUTMAINID = work1Infos.get(0).getF_EXECUTMAINID();


        webview = (WebView) fragment.findViewById(R.id.webview);
        openwebview();
    }
}
