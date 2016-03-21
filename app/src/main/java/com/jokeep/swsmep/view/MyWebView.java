package com.jokeep.swsmep.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MyWebView extends WebView {
	public String strHtmlContext = "";

	public MyWebView(Context context) {
		super(context);
	}

	public MyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public String stringByEvaluatingJavaScriptFromString(String script) {
		try {
			Object webViewObject;
			Class webViewClass = WebView.class;
			Field mp = webViewClass.getDeclaredField("mProvider");
			mp.setAccessible(true);
			webViewObject = mp.get(this);
			webViewClass = webViewObject.getClass();
			Field field_webviewcore = webViewClass
					.getDeclaredField("mWebViewCore");
			field_webviewcore.setAccessible(true);
			Object obj_webviewcore = field_webviewcore.get(webViewObject);
			Field field_BrowserFrame = obj_webviewcore.getClass()
					.getDeclaredField("mBrowserFrame");

			field_BrowserFrame.setAccessible(true);

			Object obj_frame = field_BrowserFrame.get(obj_webviewcore);

			Method method_stringByEvaluatingJavaScriptFromString = obj_frame
					.getClass().getMethod(
							"stringByEvaluatingJavaScriptFromString",
							String.class);

			Object obj_value = method_stringByEvaluatingJavaScriptFromString
					.invoke(obj_frame, script);

			return String.valueOf(obj_value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setText(String text) {
		strHtmlContext = text;
		this.loadUrl("javascript:SetHtml('" + text + "')");
	}

	public String getText() {
		return strHtmlContext;
	}

	/**
	 * 得到url上携带的参数 GetViewIndex
	 * 
	 * @return
	 */
	public void GetViewIndex() {
		// return this.stringByEvaluatingJavaScriptFromString("GetViewIndex()");

		this.loadUrl("javascript:GetViewIndex()");
	}

	/**
	 * 设置不能编辑
	 */
	public void setNotEdit() {
		this.loadUrl("javascript:SetDisabled()");
	}

	public void CreateOriginalEmail(String str) {
		this.loadUrl("javascript:CreateOriginalEmail('" + str + "')");
	}

	public String getPhoneText() {
		return this.stringByEvaluatingJavaScriptFromString("CallPhone()");
	}


}
