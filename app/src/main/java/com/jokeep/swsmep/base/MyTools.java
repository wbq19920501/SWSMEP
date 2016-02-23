package com.jokeep.swsmep.base;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import com.jokeep.swsmep.utls.Utils;

public class MyTools {
	// 判断是否有网络
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static String toUpperCase(String dataString) {
		byte[] data = dataString.getBytes();
		int start = 0;
		int len = data.length;
		int end = start + len;
		int dist = 'A' - 'a';
		for (int i = start; i < end; i++) {
			if (data[i] >= 'a' && data[i] <= 'z') {
				data[i] += dist;
			}
		}
		return new String(data);
	}

	public static String toLowerCase(String dataString) {
		byte[] data = dataString.getBytes();
		int start = 0;
		int len = data.length;
		int end = start + len;
		int dist = 'a' - 'A';
		for (int i = start; i < end; i++) {
			if (data[i] >= 'A' && data[i] <= 'Z') {
				data[i] += dist;
			}
		}
		return new String(data);
	}

	/**
	 * 获取手机mac地址<br/>
	 * 错误返回12个0
	 */
	public static String getMac(Context context) {
		// 获取mac地址：
		String macAddress = "000000000000";
		try {
			WifiManager wifiMgr = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = (null == wifiMgr ? null : wifiMgr
					.getConnectionInfo());
			if (null != info) {
				if (!TextUtils.isEmpty(info.getMacAddress()))
					macAddress = info.getMacAddress().replace(":", "");
				else
					macAddress = Utils.getInstance().getImeiNum(context);
				return macAddress;
			}
		} catch (Exception e) {

			macAddress = Utils.getInstance().getImeiNum(context);

			e.printStackTrace();
			return macAddress;
		}
		return macAddress;
	}

	// public static String getMac(Context context) {
	// WifiManager wifi = (WifiManager) context
	// .getSystemService(Context.WIFI_SERVICE);
	// WifiInfo info = wifi.getConnectionInfo();
	// String macAdress = info.getMacAddress(); // 获取mac地址
	// return macAdress;
	// }

	public static String getIP(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		int ipAddress = info.getIpAddress(); // 获取ip地址
		String ip = intToIp(ipAddress);
		return ip;
	}

	/**
	 * 获取本机外网IP地址
	 * 
	 * @param context
	 * @return IP地址
	 */
	public static String getNetIP(Context context) {
		String IP = "";
		try {
			String address = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
			URL url = new URL(address);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setUseCaches(false);

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream in = connection.getInputStream();

				// 将流转化为字符串
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));

				String tmpString = "";
				StringBuilder retJSON = new StringBuilder();
				while ((tmpString = reader.readLine()) != null) {
					retJSON.append(tmpString + "\n");
				}

				JSONObject jsonObject = new JSONObject(retJSON.toString());
				String code = jsonObject.getString("code");
				if (code.equals("0")) {
					JSONObject data = jsonObject.getJSONObject("data");
					IP = data.getString("ip") + "(" + data.getString("country")
							+ data.getString("area") + "区"
							+ data.getString("region") + data.getString("city")
							+ data.getString("isp") + ")";
				} else {
					IP = "";
					Log.e("提示", "IP接口异常，无法获取IP地址！");
				}
			} else {
				IP = "";
				Log.e("提示", "网络连接异常，无法获取IP地址！");
			}
		} catch (Exception e) {
			IP = "";
			Log.e("提示", "获取IP地址时出现异常，异常信息是：" + e.toString());
		}
		return IP;
	}

	/**
	 * 
	 * @Description:
	 * @param context
	 * @param uri
	 * @return 安装返回true
	 * @authe：杨海
	 */
	public static boolean isAppInstalled(Context context, String uri) {
		PackageManager pm = context.getPackageManager();
		boolean installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			installed = false;
		}
		return installed;
	}

	public static String intToIp(int i) {
		// return ((i >> 24) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
		// +((i >> 8 ) & 0xFF) + "." + ( i & 0xFF) ;
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + ((i >> 24) & 0xFF);
	}

//	public static int[] switchID2Resource(String MenuID) {
//		int position = MenuResource.searchPosition(MenuID);
//		if (position == -1)
//			return null;
//		else {
//			return MenuResource.getIcons(position);
//		}
//	}

//	public static int switchId2Icon(String MenuID) {
//		int position = MenuResource.searchPosition(MenuID);
//		if (position == -1)
//			return 0;
//		else {
//			return MenuResource.getIcon(position);
//		}
//	}

	/*
	 * searchFile 查找文件并加入到ArrayList 当中去
	 * 
	 * @String keyword 查找的关键词
	 * 
	 * @File filepath 查找的目录
	 */
	public static int searchFile(String keyword, File filepath) {
		List<Map<String, Object>> bookList = new ArrayList<Map<String, Object>>();
		Map<String, Object> rowItem;
		int index = 0;
		// 判断SD卡是否存在
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			if (!filepath.exists()) {
				filepath.mkdir();
			}

			File[] files = filepath.listFiles();

			if (files.length > 0) {
				for (File file : files) {
					if (file.isDirectory()) {
						// 如果目录可读就执行（一定要加，不然会挂掉）
						if (file.canRead()) {
							searchFile(keyword, file); // 如果是目录，递归查找
						}
					} else {
						// 判断是文件，则进行文件名判断
						try {
							if (file.getName().indexOf(keyword) > -1
									|| file.getName().indexOf(
											keyword.toUpperCase()) > -1) {
								rowItem = new HashMap<String, Object>();
								rowItem.put("number", index); // 加入序列号
								rowItem.put("bookName", file.getName());// 加入名称
								rowItem.put("path", file.getPath()); // 加入路径
								rowItem.put("size", file.length()); // 加入文件大小
								bookList.add(rowItem);
								index++;
							} else {
								// if(file.exists() && file.isFile())
								// file.delete();
							}
						} catch (Exception e) {
							// Toast.makeText(this,"查找发生错误",
							// Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
		}
		return index;
	}

	public static Bitmap readBitmap(String url) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Config.RGB_565;
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		InputStream is = null;
		try {
			is = new FileInputStream(url);
			return BitmapFactory.decodeStream(is, null, opts);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap readBitmap(Context context, int resId) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Config.RGB_565;
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opts);
	}

	public static InputStream readStream(Context context, int resId) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Config.RGB_565;
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		return context.getResources().openRawResource(resId);
	}

	/**
	 * 将InputStream转换成byte数组
	 * 
	 * @param in
	 *            InputStream
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] InputStreamTOByte(InputStream in) throws IOException {
		int BUFFER_SIZE = 1024;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);

		data = null;
		return outStream.toByteArray();
	}

	public static boolean haveKey() {
		if (AES.key.equals(""))
			return false;
		if (AES.iv.equals(""))
			return false;
		return true;
	}

	public static int keyLevelJudge(String key) {
		if (key.length() == 0)
			return -2;
		if (key.length() < 6)
			return -1;

		int haveNumber = 0;
		int haveBigLetter = 0;
		int haveSmallLetter = 0;
		int haveSpecial = 0;
		int chr;

		for (int i = 0; i < key.length(); i++) {
			chr = key.charAt(i);
			if (isNumber(chr)) {
				haveNumber++;
			} else if (isBigLetter(chr)) {
				haveBigLetter++;
			} else if (isSmallLetter(chr)) {
				haveSmallLetter++;
			} else {
				haveSpecial++;
			}
		}

		if (haveNumber > 0 && haveBigLetter > 0 && haveSmallLetter > 0
				&& haveSpecial > 0)
			return 2;
		if (haveNumber > 0 && (haveBigLetter + haveSmallLetter > 0))
			return 1;
		return 0;
	}

	public static boolean isNumber(int chr) {
		if (chr < 48 || chr > 57)
			return false;
		return true;
	}

	public static boolean isBigLetter(int chr) {
		if (chr < 65 || chr > 90)
			return false;
		return true;
	}

	public static boolean isSmallLetter(int chr) {
		if (chr < 97 || chr > 122)
			return false;
		return true;
	}

	public static boolean isLine(int chr) {
		if (chr == 45)
			return true;
		return false;
	}

	@SuppressWarnings("unused")
	public static String parseOfficeTel(String tel) {
		String returnTel = "";
		byte[] bytes = tel.getBytes();
		for (int i = 0; i < tel.length(); i++) {
			if (isNumber(tel.charAt(i)) || isLine(tel.charAt(i)))
				returnTel += tel.substring(i, i + 1);
		}
		return returnTel;
	}

	public final static String PATH = getSDCardPath() + "/Jokeep";

	/**
	 * Get image from newwork
	 * 
	 * @param path
	 *            The path of image
	 * @return byte[]
	 * @throws Exception
	 */
	// public static InputStream getImage(String path) throws Exception{
	// URL url = new URL(path);
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// conn.setConnectTimeout(5 * 1000);
	// conn.setRequestMethod("GET");
	// InputStream inStream = conn.getInputStream();
	// // if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
	// // return readStream(inStream);
	// // }
	// // return null;
	// return inStream;
	// }

	/**
	 * Get image from newwork
	 * 
	 * @param path
	 *            The path of image
	 * @return InputStream
	 * @throws Exception
	 */
	public static URLConnection getInputStream(String path) throws Exception {
		// URL url = new URL(path);
		// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// conn.setConnectTimeout(5 * 1000);
		// conn.setRequestMethod("GET");
		// if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
		// return conn.getInputStream();
		// }
		// return null;
		String[] strs = path.split("/");
		path = "";
		for (int i = 0; i < strs.length; i++) {
			if (i < strs.length - 1) {
				path += strs[i] + "/";
			} else {
				path += URLEncoder.encode(strs[i], "utf-8");
			}
		}
		URL url = new URL(path);
		// 打开连接
		// URLConnection con = url.openConnection();
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestProperty("Accept-Encoding", "identity");
		con.connect();

		return con;
	}

	/**
	 * Get data from stream
	 * 
	 * @param inStream
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	/**
	 * 保存文件
	 * 
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public static void saveFile(Bitmap bm, String fileName) throws IOException {
		File dirFile = new File(PATH);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(PATH + "/" + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}

	public static String getSDCardPath() {
		File sdcardDir = null;
		boolean sdcardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (sdcardExist) {
			sdcardDir = Environment.getExternalStorageDirectory();
		}
		return sdcardDir.toString();
	}

	public static String getFomatTime(String time, int type) {
		SimpleDateFormat fm = null;

		switch (type) {
		case 0:
			fm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				Date date = fm.parse(time);
				return String.format("%02d", date.getHours()) + ":"
						+ String.format("%02d", date.getMinutes());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		case 1:
			fm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				Date date = fm.parse(time);
				return "--" + String.format("%02d", date.getHours()) + ":"
						+ String.format("%02d", date.getMinutes());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		case 2:
			fm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				Date date = fm.parse(time);
				return (date.getYear() + 1900) + "-" + (date.getMonth() + 1)
						+ "-" + date.getDate() + " "
						+ String.format("%02d", date.getHours()) + ":"
						+ String.format("%02d", date.getMinutes());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		}
		return "";
	}

	public static Date getDate(String time, int type) {
		SimpleDateFormat fm = null;

		switch (type) {
		case 0:
			fm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			break;
		case 1:
			fm = new SimpleDateFormat("yyyy-MM-dd");
			break;
		}
		try {
			// Date date = fm.parse(time);
			return fm.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getDayOfWeek(int day) {
		switch (day) {
		case 0:
			return "星期天";
		case 1:
			return "星期一";
		case 2:
			return "星期二";
		case 3:
			return "星期三";
		case 4:
			return "星期四";
		case 5:
			return "星期五";
		case 6:
			return "星期六";
		default:
			return "";
		}
	}

	public static Calendar addHeadDays(Calendar c) {
		Calendar reCalendar = Calendar.getInstance();
		switch (c.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			reCalendar = c;
			break;
		case Calendar.MONDAY:
			reCalendar.setTimeInMillis(c.getTimeInMillis() - 1 * 24 * 3600
					* 1000);
			break;
		case Calendar.TUESDAY:
			reCalendar.setTimeInMillis(c.getTimeInMillis() - 2 * 24 * 3600
					* 1000);
			break;
		case Calendar.WEDNESDAY:
			reCalendar.setTimeInMillis(c.getTimeInMillis() - 3 * 24 * 3600
					* 1000);
			break;
		case Calendar.THURSDAY:
			reCalendar.setTimeInMillis(c.getTimeInMillis() - 4 * 24 * 3600
					* 1000);
			break;
		case Calendar.FRIDAY:
			reCalendar.setTimeInMillis(c.getTimeInMillis() - 5 * 24 * 3600
					* 1000);
			break;
		case Calendar.SATURDAY:
			reCalendar.setTimeInMillis(c.getTimeInMillis() - 6 * 24 * 3600
					* 1000);
			break;
		}

		return reCalendar;
	}

	public static Calendar addTailDays(Calendar c) {
		Calendar reCalendar = Calendar.getInstance();
		switch (c.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			reCalendar.setTimeInMillis(c.getTimeInMillis() + 6 * 24 * 3600
					* 1000);
			break;
		case Calendar.MONDAY:
			reCalendar.setTimeInMillis(c.getTimeInMillis() + 5 * 24 * 3600
					* 1000);
			break;
		case Calendar.TUESDAY:
			reCalendar.setTimeInMillis(c.getTimeInMillis() + 4 * 24 * 3600
					* 1000);
			break;
		case Calendar.WEDNESDAY:
			reCalendar.setTimeInMillis(c.getTimeInMillis() + 3 * 24 * 3600
					* 1000);
			break;
		case Calendar.THURSDAY:
			reCalendar.setTimeInMillis(c.getTimeInMillis() + 2 * 24 * 3600
					* 1000);
			break;
		case Calendar.FRIDAY:
			reCalendar.setTimeInMillis(c.getTimeInMillis() + 1 * 24 * 3600
					* 1000);
			break;
		case Calendar.SATURDAY:
			reCalendar = c;
			break;
		}

		return reCalendar;
	}

	public static String parseTime(String time, int type) {
		try {
			Calendar calendar = Calendar.getInstance();
			Date date = MyTools.getDate(time, 0);
			calendar.setTime(date);
			// String tail = date.getHours() + ":" + date.getMinutes();
			String tail = String.format("%02d", date.getHours()) + ":"
					+ String.format("%02d", date.getMinutes());
			
			// String tail = date;
			// if (date.getHours() > 12) {
			// tail = "下午    " + String.format("%02d", (date.getHours() - 12))
			// + ":" + String.format("%02d", date.getMinutes());
			// } else {
			// tail = "上午    " + String.format("%02d", date.getHours()) + ":"
			// + String.format("%02d", date.getMinutes());
			// }
			
			
			switch (type) {
			case 0:
				
				String mthString = String.format("%02d",(date.getMonth() + 1));
				String mdayString = String.format("%02d",(date.getDate()));
				
				return (date.getYear() + 1900) + "年" +mthString
						+ "月" + mdayString + "日" + "  "
						+ getDayOfWeek(date.getDay());
			case 1:
				return tail;
			case 2:
				return (date.getMonth() + 1) + "月" + date.getDate() + "日"
						+ "   " + tail;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String parseHour(int hour) {
		System.out.println("parse hour in");
		String tail;
		if (hour > 12) {
			tail = "下午    " + String.format("%02d", (hour - 12));
		} else {
			tail = "上午    " + String.format("%02d", hour);
		}
		System.out.println("tail=" + tail);
		return tail;
	}

	public static boolean TimeEqual(Calendar c, String time) {
		String str = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1)
				+ "-" + c.get(Calendar.DATE);
		if (str.equals(time)) {
			return true;
		}
		return false;
	}

	/**
	 * JSON字符串特殊字符处理，比如：“\A1;1300”
	 * 
	 * @param s
	 * @return String
	 */
	public static String string2Json(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '\"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '/':
				sb.append("\\/");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			default:
				sb.append(c);
			}
		}
		return sb.toString();
	}

	// 封装字节数组与参数
	public static byte[] getPacket(String json, byte[] image) {
		byte[] jsonb = json.getBytes();
		int length = image.length + jsonb.length;
		System.out.println(image.length + "    " + jsonb.length);
		byte[] bytes = new byte[length + 1];
		byte[] lengthb = InttoByteArray(jsonb.length, 1);
		System.arraycopy(lengthb, 0, bytes, 0, 1);
		System.arraycopy(jsonb, 0, bytes, 1, jsonb.length);
		System.arraycopy(image, 0, bytes, 1 + jsonb.length, image.length);
		return bytes;
	}

	// 将int转换为字节数组
	public static byte[] InttoByteArray(int iSource, int iArrayLen) {
		byte[] bLocalArr = new byte[iArrayLen];
		for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
			bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);
		}
		return bLocalArr;
	}

	// 将byte数组bRefArr转为一个整数,字节数组的低位是整型的低字节位
	public static int BytestoInt(byte[] bRefArr) {
		int iOutcome = 0;
		byte bLoop;
		for (int i = 0; i < bRefArr.length; i++) {
			bLoop = bRefArr[i];
			iOutcome += (bLoop & 0xFF) << (8 * i);
		}
		return iOutcome;
	}

	/**
	 * 初始化话获取pdf文件的intent
	 * 
	 * @param id
	 * @param filepath
	 * @param showFile
	 * @param
	 * @return
	 */
//	public static Intent getPdfIntent(int id, String filepath,
//			boolean showFile) {
//		File file = new File(filepath);
//		final Uri uri = Uri.fromFile(file);
//		final Intent intent = new Intent("android.intent.action.VIEW", uri);
//		if (id == 1) {
//			intent.setClassName("com.jokeep.swsmep",
//					"com.jokeep.swsmep.base.BillPdfShowerActivity");
//		} else {
//			intent.setClassName("com.jokeep.swsmep",
//					"com.jokeep.portal.custom.PdfShowerActivity2");
//		}
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//		intent.putExtra("sUrl",
//				"http://192.168.1.111:8888/iSignaturePDF/DefaulServlet");
//		// 用户id
//		intent.putExtra("userName", "admin");
//		intent.putExtra("signatureField1", "Signature1");
//		intent.putExtra("signatureField2", "Signature2");
//		intent.putExtra("copyRight", sureCopyRight());
//		intent.putExtra("path", filepath);
//		/*
//		 * intent.putExtra("usedKinggridSign",
//		 * chv_usedKinggridSign.isChecked());
//		 */
//		intent.putExtra("loadPDFReaderCache", false);
//		intent.putExtra("onlySaveAnnots", false);
//		intent.putExtra("isCanField_edit", false);
//		intent.putExtra("isSupportEbenT7Mode", false);
//		intent.putExtra("annotType", 1);
//		Bundle bundle = new Bundle();
//		bundle.putParcelable("pageViewMode", PageViewMode.VSCROLL);
//		intent.putExtras(bundle);
//		intent.putExtra("local_flag", 1);
//		intent.putExtra("isVectorSign", true);// 手写签批是否矢量化
//
//		intent.putExtras(bundle);
//		intent.putExtra("bookRotation",
//				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		/* startActivityForResult(intent, 1); */
//		intent.putExtra("showFile", showFile);
//		return intent;
//	}

	/**
	 * 初始化领导签字pdf的intent
	 * 
	 * @param
	 * @param filepath
	 * @param showFile
	 * @param
	 * @return
	 */
//	public static Intent getSignPdfIntent(String filepath, boolean showFile) {
//		File file = new File(filepath);
//		final Uri uri = Uri.fromFile(file);
//		final Intent intent = new Intent("android.intent.action.VIEW", uri);
//		intent.setClassName("com.jokeep.swsmep",
//				"com.jokeep.swsmep.base.BillPdfShowerActivity");
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//		intent.putExtra("sUrl",
//				"http://192.168.1.111:8888/iSignaturePDF/DefaulServlet");
//		// 用户id
//		intent.putExtra("userName", "admin");
//		intent.putExtra("signatureField1", "Signature1");
//		intent.putExtra("signatureField2", "Signature2");
//		intent.putExtra("copyRight", sureCopyRight());
//		intent.putExtra("path", filepath);
//		/*
//		 * intent.putExtra("usedKinggridSign",
//		 * chv_usedKinggridSign.isChecked());
//		 */
//		intent.putExtra("loadPDFReaderCache", false);
//		intent.putExtra("onlySaveAnnots", false);
//		intent.putExtra("isCanField_edit", false);
//		intent.putExtra("isSupportEbenT7Mode", false);
//		intent.putExtra("annotType", 1);
//		Bundle bundle = new Bundle();
//		bundle.putParcelable("pageViewMode", PageViewMode.VSCROLL);
//		intent.putExtras(bundle);
//		intent.putExtra("local_flag", 1);
//		intent.putExtra("isVectorSign", true);// 手写签批是否矢量化
//		intent.putExtra("bookRotation",
//				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		intent.putExtras(bundle);
//
//		/* startActivityForResult(intent, 1); */
//		intent.putExtra("showFile", showFile);
//		return intent;
//	}

//	public static Intent getJointPdfIntent(int id, String filepath,
//			boolean showFile) {
//		File file = new File(filepath);
//		final Uri uri = Uri.fromFile(file);
//		/*
//		 * final Intent intent = new Intent(this,BookShower.class);
//		 * intent.setData(uri);
//		 */
//		final Intent intent = new Intent("android.intent.action.VIEW", uri);
//		if (id == 1) {
//			intent.setClassName("com.jokeep.swsmep",
//					"com.jokeep.portal.activity.joint.JointZhengWenPdfShowerActivity");
//		} else if (id == 2) {
//			intent.setClassName("com.jokeep.swsmep",
//					"com.jokeep.portal.activity.joint.JointZhengWenPdfShowerActivity");
//
//		}
//
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		// if (userName.getText().toString() == null || sureCopyRight() == null)
//		// {
//		// return;
//		// }
//
//		intent.putExtra("sUrl",
//				"http://192.168.1.111:8888/iSignaturePDF/DefaulServlet");
//		intent.putExtra("userName", "admin");
//		intent.putExtra("signatureField1", "Signature1");
//		intent.putExtra("signatureField2", "Signature2");
//		intent.putExtra("copyRight", MyTools.sureCopyRight());
//		intent.putExtra("path", filepath);
//		intent.putExtra("bookRotation",
//				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//		intent.putExtra("onlySaveAnnots", false);
//		intent.putExtra("isCanField_edit", false);
//		intent.putExtra("isVectorSign", false);
//		intent.putExtra("isSupportEbenT7Mode", false);
//		intent.putExtra("annotType", 1);
//		Bundle bundle = new Bundle();
//		bundle.putParcelable("pageViewMode", PageViewMode.VSCROLL);
//		intent.putExtras(bundle);
//		intent.putExtra("local_flag", 1);
//		// if (chv_fillTemplate.isChecked()) {
//		// intent.putExtra("template", sureFillTemplate());
//		// }
//		// if (chv_gaininfo.isChecked()) {
//		// intent.putExtra("annotType", sureAnnotType());
//		// }
//		intent.putExtras(bundle);
//
//		/* startActivityForResult(intent, 1); */
//		intent.putExtra("showFile", showFile);
//		return intent;
//	}

	/**
	 * 初始化话获取pdf文件的intent
	 * 
	 * @param id
	 * @param filepath
	 * @param showFile
	 * @param
	 * @return
	 */
//	public static Intent getBillPdfIntent(int id, String filepath,
//			boolean showFile) {
//		File file = new File(filepath);
//		final Uri uri = Uri.fromFile(file);
//		final Intent intent = new Intent("android.intent.action.VIEW", uri);
//		if (id == 1) {
//			intent.setClassName("com.jokeep.swsmep",
//					"com.jokeep.swsmep.base.BillPdfShowerActivity");
//		} else {
//			intent.setClassName("com.jokeep.swsmep",
//					"com.jokeep.portal.custom.BillPdfShowerActivity2");
//		}
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//		intent.putExtra("sUrl",
//				"http://192.168.1.111:8888/iSignaturePDF/DefaulServlet");
//		// 用户id
//		intent.putExtra("userName", "admin");
//		intent.putExtra("signatureField1", "Signature1");
//		intent.putExtra("signatureField2", "Signature2");
//		intent.putExtra("copyRight", sureCopyRight());
//		intent.putExtra("path", filepath);
//		intent.putExtra("loadPDFReaderCache", false);
//		intent.putExtra("onlySaveAnnots", false);
//		intent.putExtra("isCanField_edit", false);
//		intent.putExtra("isSupportEbenT7Mode", false);
//		intent.putExtra("annotType", 1);
//		Bundle bundle = new Bundle();
//		bundle.putParcelable("pageViewMode", PageViewMode.VSCROLL);
//		intent.putExtras(bundle);
//		intent.putExtra("local_flag", 1);
//		intent.putExtra("isVectorSign", true);// 手写签批是否矢量化
//		intent.putExtra("bookRotation",
//				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		intent.putExtras(bundle);
//
//		/* startActivityForResult(intent, 1); */
//		intent.putExtra("showFile", showFile);
//		return intent;
//	}

	// 金格的验证码
	public static String sureCopyRight() {
		String copyRight = "";
		// 试用码
//		copyRight = "SxD/phFsuhBWZSmMVtSjKZmm/c/3zSMrkV2Bbj5tznSkEVZmTwJv0wwMmH/+p6wLiUHbjadYueX9v51H9GgnjUhmNW1xPkB++KQqSv/VKLDsR8V6RvNmv0xyTLOrQoGzAT81iKFYb1SZ/Zera1cjGwQSq79AcI/N/6DgBIfpnlwiEiP2am/4w4+38lfUELaNFry8HbpbpTqV4sqXN1WpeJ7CHHwcDBnMVj8djMthFaapMFm/i6swvGEQ2JoygFU368sLBQG57FhM8Bkq7aPAVrvKeTdxzwi2Wk0Yn+WSxoXx6aD2yiupr6ji7hzsE6/QRx89Izb7etgW5cXVl5PwISjX+xEgRoNggvmgA8zkJXOVWEbHWAH22+t7LdPt+jENUl53rvJabZGBUtMVMHP2J32poSbszHQQyNDZrHtqZuuSgCNRP4FpYjl8hG/IVrYXSo6k2pEkUqzd5hh5kngQSOW8fXpxdRHfEuWC1PB9ruQ=";
//试用码	copyRight = "SxD/phFsuhBWZSmMVtSjKZmm/c/3zSMrkV2Bbj5tznSkEVZmTwJv0wwMmH/+p6wLiUHbjadYueX9v51H9GgnjUhmNW1xPkB++KQqSv/VKLDsR8V6RvNmv0xyTLOrQoGzAT81iKFYb1SZ/Zera1cjGwQSq79AcI/N/6DgBIfpnlwiEiP2am/4w4+38lfUELaNFry8HbpbpTqV4sqXN1WpeJ7CHHwcDBnMVj8djMthFaapMFm/i6swvGEQ2JoygFU3spKowEa/G/fvXKfXfS212NWB43A+NXRsevrx1DJOapLx6aD2yiupr6ji7hzsE6/QCZTwXZQwybuwQzu4N8GoHea+yCsj03ann8gIJ1+pcrGVWEbHWAH22+t7LdPt+jENITQ1ljdcKfJJ264wW1dt8lhvJY2l3E5bmkuxrOncCgeSgCNRP4FpYjl8hG/IVrYX9eNIGdyij/IxvdIf4qITLeW8fXpxdRHfEuWC1PB9ruQ=";
//		 正式版
//		copyRight = "SxD/phFsuhBWZSmMVtSjKZmm/c/3zSMrkV2Bbj5tznSkEVZmTwJv0wwMmH/+p6wL0S2OsP0xHHj1V5/y+UOyVyjsxu8s2q0ybCINp+Xd7AQhHybF2I+6S2t+TQ5f7Hn5jp2HrKwufOY7Drg3h8cpGXSHQJlRzo7luBZo5/DMZmQWA2maiQSA7FvD9H0R2Bi5EO9JXX9RXh4yZiTr6j4B5VQ9PW1waQ44Wyx97rcHxfBzMiyBpvVZHKe4Q26NX41FseqxJFres0pilHwpPTHMT9pv+jONaV8Q3B+ivjObv1icOjiv+Xi0enIo/R6xeyUgPEtr7r3OWayzKkMrgPNwj2zeDYUeCGH3mqNaobVYFotqHNYruYjuZbK+uGM3WQ73gkQLXJLGBuoP95zYF78d6Dc7qpKiQmpkMVMtYsbbuIRwsEc37kLLhjPBQkOztfMLnwGBxbb+c/6f8a64N4TtJA==";
		//手机验证   试用码
		copyRight = "SxD/phFsuhBWZSmMVtSjKZmm/c/3zSMrkV2Bbj5tznSkEVZmTwJv0wwMmH/+p6wL0S2OsP0xHHj1V5/y+UOyVyjsxu8s2q0ybCINp+Xd7AQhHybF2I+6S2t+TQ5f7Hn5jp2HrKwufOY7Drg3h8cpGXSHQJlRzo7luBZo5/DMZmRzDF3Q9bvFKXgnwIhBwsOEyOawfupUcPzMGmZFsFkdbJ7CHHwcDBnMVj8djMthFaapMFm/i6swvGEQ2JoygFU3qdVAR8bAZbXs39bf8Up7YGJ4C9pL9rTcXXb4rsAQi4ugSOimMBZXAWJyoNec+zKV1unaolABhIxXa+WPvNqslmbOGDsTXe358SjOA3eCpX9fjgZng3BAkQ0sSjCWwWV9xbHZI6NwWs3BVP74BcUFE6ZJhr2fg9mMpqlUrMXb+X7rywsFAbnsWEzwGSrto8BWOZRJxka2ugzP6V+ZgWsc+G9XmCmwlZlxKnkAgp1jgbY=";
		return copyRight.trim();
	}
}