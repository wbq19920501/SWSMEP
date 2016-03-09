package com.jokeep.swsmep.base;

import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by wbq501 on 2016-3-8 15:05.
 * SWSMEP
 */
public class WebSeting {
    public static void openweb(WebView webview){
        //启用支持javascript
        WebSettings settings = webview.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);//支持缩放
        settings.setSupportZoom(true);
        settings.setDisplayZoomControls(false);//去掉缩放控件
        webview.requestFocusFromTouch();//支持焦点
    }
}
