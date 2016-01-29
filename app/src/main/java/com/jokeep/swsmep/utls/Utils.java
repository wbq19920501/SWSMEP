package com.jokeep.swsmep.utls;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.RenderPriority;

import com.jokeep.swsmep.model.DealURL;

public class Utils {

	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	private static String key;
	private static String value;
	private static final String APP_CACAHE_DIRNAME = "/webcache";
	private static Utils instance;

	/**
	 * 
	 * @Description: 获取设备的IMEI号
	 * @param context
	 * @return
	 * @authe：杨海
	 */
	public String getImeiNum(Context context) {
		String IMEI = "";

		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		IMEI = telephonyManager.getDeviceId();
		return IMEI;
	}

	public boolean isCall(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int phoneType = tm.getPhoneType();
		// 判断能否拨打电话
		boolean isPhoneType = phoneType == TelephonyManager.PHONE_TYPE_NONE;
		return isPhoneType;
	}

	/**
	 * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
	 * 
	 * @param context
	 * @return 平板返回 True，手机返回 False
	 */
	public boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static Utils getInstance() {
		if (instance == null) {
			instance = new Utils();
		}
		return instance;
	}

	// private Utils() {
	// }

	public void clearCache(Context context, WebView mWebView) {
		if (mWebView != null) {
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
			mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

			// 开启 DOM storage API 功能
			mWebView.getSettings().setDomStorageEnabled(true);
			// 开启 database storage API 功能
			mWebView.getSettings().setDatabaseEnabled(true);
			String cacheDirPath = context.getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
			// Log.i(TAG, "cacheDirPath="+cacheDirPath);
			// 设置数据库缓存路径
			mWebView.getSettings().setDatabasePath(cacheDirPath);
			// 设置 Application Caches 缓存目录
			mWebView.getSettings().setAppCachePath(cacheDirPath);
			// 开启 Application Caches 功能
			mWebView.getSettings().setAppCacheEnabled(true);
		}
	}

	/** * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param context */
	public static void cleanDatabases(Context context) {
		String filePath = "/data/data/" + context.getPackageName() + "/databases";
		deleteFilesByDirectory(new File(filePath));
	}

	private static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
		}
	}

	public static void isOfficelNo(String mobilePhone, String officeTel) {
		// if (mobilePhone.length() > 0
		// && (mobilePhone.length() < 11 || !(mobilePhone.substring(0, 2)
		// .equals("13")
		// || mobilePhone.substring(0, 2).equals("14")
		// || mobilePhone.substring(0, 2).equals("15") || mobilePhone
		// .substring(0, 2).equals("18")))) {
		// } else if (officeTel.contains("-")) {
		// String[] tel = officeTel.split("-");
		// if (tel[0].length() > 4 || tel[0].length() < 1
		// || tel[1].length() < 7 || tel[1].length() > 8
		// || tel.length > 2)
		// {
		// } else if ((officeTel.length() > 12 || officeTel.length() < 7)
		// && officeTel.length() > 0) {
		// }
	}

	public static String[] splitUrlParam(String param) {
		if (param != null) {
			return param.split(",");
		}
		return null;
	}

	/**
	 * 截取url后面携带的参数
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static DealURL splitUrlToGetParam(String url) throws Exception {

		String replace = url.replace("?", "&");
		DealURL billData = new DealURL();
		String[] split2 = replace.split("&");
		for (int i = 1; i < split2.length; i++) {
			String str = split2[i];
			String[] splits = str.split("=");
			if (splits.length > 1) {
				key = splits[0];
				value = splits[1];
				billData.setData(key, value);

			} else {
				key = splits[0];
				billData.setData(key, "");

			}

		}
		return billData;
	}

	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}

	/**
	 * 将整个json字符串解析，并放置到map<String,Object>中
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static Map<String, Object> getMapByJosn(String jsonStr) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (!TextUtils.isEmpty(jsonStr)) {
			JSONObject json = new JSONObject(jsonStr);
			Iterator<?> i = json.keys();
			while (i.hasNext()) {
				String key = (String) i.next();
				String value = json.getString(key);
				if (value.indexOf("{") == 0) {
					map.put(key.trim(), getMapByJosn(value));
				} else if (value.indexOf("[") == 0) {
					map.put(key.trim(), getList(value));
				} else {
					map.put(key.trim(), value.trim());
				}
			}
		}
		return map;
	}

	/**
	 * 将单个json数组字符串解析放在list中
	 * 
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getList(String jsonStr) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		JSONArray ja = new JSONArray(jsonStr);
		for (int j = 0; j < ja.length(); j++) {
			String jm = ja.get(j) + "";
			if (jm.indexOf("{") == 0) {
				Map<String, Object> m = getMapByJosn(jm);
				list.add(m);
			}
		}
		return list;
	}

	public static String getServicePath(String path) {
		return path.substring(0, path.lastIndexOf(".")) + "/";
	}

	// 得到文件名称
	public static String getFileName(String path) {
		return path.substring(path.lastIndexOf("/") + 1);
	}

	/**
	 * 去掉url中的路径，留下请求参数部分
	 * 
	 * @param strURL
	 *            url地址
	 * @return url请求参数部分
	 */
	private static String TruncateUrlPage(String strURL) {
		String strAllParam = null;
		String[] arrSplit = null;
		strURL = strURL.trim();
		arrSplit = strURL.split("[?]");
		if (strURL.length() > 1) {
			if (arrSplit.length > 1) {
				if (arrSplit[1] != null) {
					strAllParam = arrSplit[1];
				}
			}
		}
		return strAllParam;
	}

	/*
	 * 解析出url参数中的键值对*如"index.jsp?Action=del&id=123"，解析出Action:del,id:123 存入map中*
	 * 
	 * @param URL url地址*@return url请求参数部分
	 */

	public static Map<String, String> URLRequest(String URL) {
		Map<String, String> mapRequest = new HashMap<String, String>();

		String[] arrSplit = null;

		String strUrlParam = TruncateUrlPage(URL);
		if (strUrlParam == null) {
			return mapRequest;
		}

		// 每个键值为一组
		arrSplit = strUrlParam.split("[&]");
		for (String strSplit : arrSplit) {
			String[] arrSplitEqual = null;
			arrSplitEqual = strSplit.split("[=]");

			// 解析出键值
			if (arrSplitEqual.length > 1) {
				// 正确解析
				mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

			} else {
				if (arrSplitEqual[0] != "") {
					// 只有参数没有值，不加入
					mapRequest.put(arrSplitEqual[0], "");
				}
			}
		}
		return mapRequest;
	}

}
