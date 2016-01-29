package com.jokeep.swsmep.base;

import java.lang.ref.WeakReference;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class BaseTask extends AsyncTask<Void, Integer, Integer> {

	/**
	 * 网络是否通畅
	 */
	protected boolean netConnected = true;

	/**
	 * 请求用JSON数据
	 */
	protected JSONObject json;

	/**
	 * 请求用客户端
	 */
	protected Client client;

	/**
	 * 响应参数
	 */
	protected Object obj;

	protected String uuid;

	protected Context context;
	protected Message msg;
	protected Handler handler;
	protected SharedPreferences share;
	protected Editor editor;

	public BaseTask() {

	}

	protected WeakReference<Context> mTarget;

	protected void setBaseData(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	/**
	 * 初始化数据 执行网络请求前 检查AES加密key值
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		msg = new Message();
		json = new JSONObject();
		if (MyTools.isNetworkConnected(context)) {
			netConnected = true;
			share = context.getSharedPreferences("LoginInfo",
					Context.MODE_PRIVATE);
			editor = share.edit();
			if (!MyTools.haveKey()) {
				AES.key = share.getString("AES", "");
				AES.iv = AES.key;
			}
			uuid = share.getString(SaveMsg.TOKENID, "");
		} else {
			netConnected = false;
		}

	}

	@Override
	protected Integer doInBackground(Void... params) {
		return 0;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		msg.what = result;
		if (obj != null) {
			msg.obj = obj;
		}
		if (handler != null) {
			handler.sendMessage(msg);
		}
		recycle();
	}

	protected void recycle() {
		json = null;
		client = null;
		context = null;
		msg = null;
		handler = null;
		share = null;
		editor = null;
		uuid = null;
		obj = null;

		System.gc();
	}
}