package com.jokeep.swsmep.base;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.utls.Utils;
import com.kinggrid.iapppdf.ui.viewer.IAppPDFActivity;
import com.kinggrid.iapppdf.ui.viewer.ViewerActivityController.LoadPageFinishedListener;
import com.kinggrid.pdfservice.Field;

/**
 * 发文、收文批阅签
 * 
 * @author chenyu
 * 
 */
public class BillPdfShowerActivity extends IAppPDFActivity implements
		OnClickListener {

	private static final String TAG = "BookShower";
	public FrameLayout frameLayout;
	private static Context context;
	final static int KEY_DOCUMENT_SAVE = 0;
	final static int KEY_SINGER = 1;
	final static int KEY_SINGER_DEL = 2;
	final static int KEY_FULL_SINGER = 3;
	final static int KEY_DEL_FULL_SINGER = 4;// 删除全文批注
	final static int KEY_TEXT_NOTE = 5;
	final static int KEY_DEL_TEXT_NOTE = 6;// 删除文字注释
	final static int KEY_SOUND_NOTE = 7;// 语音批注
	final static int KEY_DEL_SOUND_NOTE = 8;// 删除语音批注
	final static int KEY_NOTE_LIST = 9; //
	final static int KEY_TEXT_LIST = 10;// 注释列表
	final static int KEY_SOUND_LIST = 11; // 语音批注列表
	final static int KEY_BOOKMARK_LIST = 12;// 大纲
	final static int KEY_CAMERA = 13;// 证件拍照
	final static int KEY_ABOUT = 14; // 关于界面
	final static int KEY_DIGITAL_SIGNATURE = 15;// 数字签名
	final static int KEY_VERIFY = 16;// 验证
	final static int KEY_SEARCH = 17;// 搜索

	/* final static int TYPE_ANNOT_HANDWRITE = 1; */
	final static int TYPE_ANNOT_STAMP = 1;
	final static int TYPE_ANNOT_TEXT = 2;
	final static int TYPE_ANNOT_SIGNATURE = 3;
	final static int TYPE_ANNOT_SOUND = 4;

	private Intent intent;
	private int templateId;
	private boolean loadField = true;
	private ArrayList<Field> fieldList;
	private int annotType;

	private Uri systemPhotoUri; // 照片在媒体库中的路径
	private String imagePath; // 照片文件在文件系统中的路径

	private boolean hasLoaded = false;

	private final static String url = "http://192.168.1.124:8888/iSignaturePDF/DefaulServlet";

	// private String url;
	private final static String signatruePdfUrl = "pdf/isignature/template.pdf";
	private final static String verifyPdfUrl = "pdf/verify/template.pdf";

	private final static String IMAGE_PATH = Environment
			.getExternalStorageDirectory().getPath().toString()
			+ "/testSignature.jpg";

	private boolean showFile = false;
	private boolean showButton = true;

	private boolean isload = false;

	@Override
	protected void onCreateImpl(Bundle savedInstanceState) {
		super.onCreateImpl(savedInstanceState);
		context = this;
		intent = getIntent();
		// if (!isload) {
		// isload = true;
		// refreshPage(getCurrentPageNo());
		// }

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_pdf);
		ActivityCollector.addActivity(this);
//		SharedPreferences sharedPreferences = this.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
//		String share_username = sharedPreferences.getString("login_name", "");
		userName = intent.getStringExtra("userName");
//		userName = share_username;
		frameLayout = (FrameLayout) findViewById(R.id.book_frame);
		topMenu = findViewById(R.id.layout_buttons);

		findViewById(R.id.layout_annotation_add).setOnClickListener(this);
		findViewById(R.id.layout_annotation_delete).setOnClickListener(this);
		// findViewById(R.id.bottom).setVisibility(View.GONE);
		initPDFView(frameLayout);
		// initViewMap();
		// initToolBar();

		showFile = intent.getBooleanExtra("showFile", false);
		showButton = intent.getBooleanExtra("showButton", false);
		if (showFile) {
			findViewById(R.id.layout_file).setVisibility(View.VISIBLE);
			findViewById(R.id.layout_file).setOnClickListener(this);
		}
		if (showButton) {
			topMenu.setVisibility(View.VISIBLE);
		} else {
			topMenu.setVisibility(View.GONE);
		}
		boolean isTablet = Utils.getInstance().isTablet(this);
		// 判断能否拨打电话
		boolean isPhoneType = Utils.getInstance().isCall(this);
		// istablet true为平板，false为手机；
		if (isTablet) {
			setPenInfo(12, Color.BLACK, 0);

		} else {
			setPenInfo(4f, Color.BLACK, 0);
		}
		clearPDFReadSettings();
		getController().setLoadPageFinishedListener(
				new LoadPageFinishedListener() {

					@Override
					public void onLoadPageFinished() {
						// if (!hasLoaded) {
						// // loadFieldTemplates();
						// // setScreenPort();
						// refreshPage(0);
						// hasLoaded = true;
						// }
					}
				});

		super.setOnKinggridSignListener(new OnKinggridSignListener() {

			@Override
			public void onSaveSign() {
				Log.i(TAG, "onSaveSign");
			}

			@Override
			public void onCloseSign() {
				Log.i(TAG, "onCloseSign");
			}

			@Override
			public void onClearSign() {
				Log.i(TAG, "onClearSign");
			}
		});

		super.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageChange(String currentPage) {
				Log.i(TAG, "onPageChange:" + currentPage);
			}
		});
	}

	@Override
	protected void loadFieldTemplates() {
		super.loadFieldTemplates();

		setAllAnnotationsVisible(true);
		refreshDocument();

		if (intent.hasExtra("template")) {
			templateId = intent.getExtras().getInt("template");
			Thread thread = new Thread(new LoadAnnots());
			thread.start();
		}
	}

	@Override
	protected void onDestroyImpl(boolean finishing) {
		topMenu = null;
		System.exit(0);
		ActivityCollector.removeActivity(this);
		super.onDestroyImpl(finishing);

	}

	public class LoadAnnots implements Runnable {

		/*
		 * （非 Javadoc）
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			if (loadField) {
				if (fieldList.size() > 0) {
					for (int i = 0; i < fieldList.size(); i++) {
						setFieldContent(fieldList.get(i).getFieldName(),
								fieldList.get(i).getFieldContent());
					}
				}
				loadField = false;
				refreshDocument();
			}
		}

	}

	private void createDialog(final int type) {
		final Builder builder = new Builder(context);
		switch (type) {
		case TYPE_ANNOT_STAMP:
			builder.setMessage(getString(R.string.dialog_message_annot_hardwrite));
			break;
		}
		builder.setTitle(getString(R.string.dialog_title));
		builder.setPositiveButton(getString(R.string.ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						deleteAllAnnotations(type);
					}
				});
		builder.setNegativeButton(getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		final Dialog dialog1 = builder.create();
		dialog1.setCancelable(false);
		dialog1.show();
	}

	// 拍照
	private void takePicture() {
		systemPhotoUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		imagePath = FILEDIR_PATH + "/tempphotos/cameraphoto"
				+ System.currentTimeMillis() + ".jpg";
		final ContentResolver resolver = getContentResolver();
		final Cursor cursor = resolver.query(systemPhotoUri, null, null, null,
				null);
		String lastPhotoPath;
		if (!cursor.moveToFirst()) {
			cursor.moveToLast();
			lastPhotoPath = cursor.getString(1);
		} else {
			lastPhotoPath = "";
		}
		final SharedPreferences sPreferences = getSharedPreferences(
				"photo_info", MODE_PRIVATE);
		sPreferences.edit().putString("photoPath", lastPhotoPath).commit();
		cursor.close();
		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		final File imageFile = new File(imagePath);
		final Uri fileUri = Uri.fromFile(imageFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(intent, REQUESTCODE_PHOTOS_TAKE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		systemPhotoUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		final ContentResolver resolver = getContentResolver();
		if (requestCode == REQUESTCODE_PHOTOS_TAKE) {
			if (resultCode == RESULT_OK) {
				final Cursor cursor = resolver.query(systemPhotoUri, null,
						null, null, null);
				String lastPhotoPath;
				String lastPhotoPath_old;
				if (!cursor.moveToFirst()) {
					cursor.moveToLast();
					lastPhotoPath = cursor.getString(1);
					final SharedPreferences sPreferences = getSharedPreferences(
							"photo_info", MODE_PRIVATE);
					lastPhotoPath_old = sPreferences.getString("photoPath", "");
					if (!lastPhotoPath.equals(lastPhotoPath_old)) {
						final File file = new File(cursor.getString(1));
						if (file.exists()) {
							file.delete();
						}
					}
				}
				cursor.close();
				insertPhotoIntoPDF(imagePath, photo);
			}
		}
	}

	private void getVectorData() {
		// 取得矢量数据属于 耗时操作，需要放到线程
		ArrayList<String> list = getVectorData(userName);
		if (list != null && list.size() > 0) {
			Log.v("tbz", "list isn't null");
			for (int i = 0; i < list.size(); i++) {
				String[] annotinfo = list.get(i).split(";");
				Log.d("debug", "annotinfo " + i + " : " + annotinfo[4]);
				// saveString2File(annotinfo[4], FILEDIR_PATH + "/vector_info("
				// + i + ").txt");
			}
		}
	}

	@SuppressWarnings("unused")
	private void saveString2File(String info, String filePath) {
		try {
			File saveFile = new File(filePath);
			if (saveFile.exists()) {
				saveFile.delete();
			}
			File dir = new File(saveFile.getParent());
			dir.mkdirs();
			saveFile.createNewFile();

			FileOutputStream outStream = new FileOutputStream(saveFile);
			outStream.write(info.getBytes());
			outStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String val = data.getString("value");
			/*
			 * if(msg.obj.equals(99)){ showPopupWindow(val); return; }
			 */
			if (val.equals("iSignature Successed")) {
				insertSignatureInField(IMAGE_PATH, (float) 1.0);
				return;
			}
			Toast.makeText(getApplicationContext(), val, Toast.LENGTH_LONG)
					.show();
		}
	};

	private void sendMsgToHandler(String message, boolean isShowVerify) {
		Message msg = new Message();
		Bundle data = new Bundle();
		if (message.equals("-1")) {
			data.putString("value", getString(R.string.result_no_exist_sign));
		} else if (message.equals("3")) {
			data.putString("value", getString(R.string.result_sign_unusual));
		} else {
			data.putString("value", message);
		}

		msg.setData(data);
		handler.sendMessage(msg);
	}

	protected String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int bytesOut = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytesOut = is.read(bufferOut)) != -1) {
			baos.write(bufferOut, 0, bytesOut);
		}
		String resstrString = new String();
		resstrString = baos.toString("UTF-8");
		baos.flush();
		baos.close();
		return resstrString;
	}

	private String getJsonData(List<SignPosition> positions) {
		JSONObject data = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			for (int i = 0; i < positions.size(); i++) {
				JSONObject jo = new JSONObject();
				jo.put("pageno", positions.get(i).pageno);
				jo.put("x",
						(positions.get(i).rect[0] + positions.get(i).rect[2]) / 2);
				jo.put("y", positions.get(i).height
						- (positions.get(i).rect[1] + positions.get(i).rect[3])
						/ 2);

				array.put(jo);
			}

			data.put("positions", array);
		} catch (JSONException joe) {
			joe.printStackTrace();
		}

		return data.toString();
	}

	private void upload(String url, String imagePath, int type, String name) {
		HttpURLConnection conn = null;
		OutputStream out = null;
		try {
			URL serviceUrl = new URL(url);
			conn = (HttpURLConnection) serviceUrl.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			/*
			 * conn.setRequestProperty("enctype", "multipart/form-data");
			 * conn.setRequestProperty("contentType", "charset=UTF-8");
			 */
			conn.setRequestProperty("connection", "close");
			conn.setRequestMethod("POST");

			DataInputStream disImage = null;
			String imageOrder = null;
			String json = null;
			BitmapFactory.Options options = new BitmapFactory.Options();
			if (type == 1) {
				File image = new File(imagePath);
				long imageSize = image.length();
				Log.v("tbz", "imageSize = " + imageSize);
				disImage = new DataInputStream(new FileInputStream(image));
				imageOrder = "fileSize=" + imageSize + ",fileName=IMAGE";
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(imagePath, options);
				options.inJustDecodeBounds = false;

				// List<SignPosition> positions = getSignPositionInField(name);
				List<SignPosition> positions = getSignPositionInText("盖章");
				json = getJsonData(positions);
			}

			out = conn.getOutputStream();
			int bytesOut = 0;
			byte[] bufferOut = new byte[1024 * 4];
			Log.v("tbz", "outputStream start transform");
			if (type == 1) {
				out.write("type=1".getBytes("UTF-8"));
				out.write("\r\n".getBytes("UTF-8"));
				out.write("debug=0".getBytes("UTF-8"));
				out.write("\r\n".getBytes("UTF-8"));
				/*
				 * out.write("pageno=1".getBytes("UTF-8"));
				 * out.write("\r\n".getBytes("UTF-8"));
				 * out.write("x=100".getBytes("UTF-8"));
				 * out.write("\r\n".getBytes("UTF-8"));
				 * out.write("y=100".getBytes("UTF-8"));
				 */
				out.write(("position=" + json).getBytes("UTF-8"));
				out.write("\r\n".getBytes("UTF-8"));
				out.write(("w=" + options.outWidth).getBytes("UTF-8"));
				out.write("\r\n".getBytes("UTF-8"));
				out.write(("h=" + options.outHeight).getBytes("UTF-8"));
				out.write("\r\n".getBytes("UTF-8"));
				out.write("imagetype=jpg".getBytes("UTF-8"));
				out.write("\r\n".getBytes("UTF-8"));
				out.write(("pdfPath=" + signatruePdfUrl).getBytes("UTF-8"));
				out.write("\r\n".getBytes("UTF-8"));
				out.write("\r\n".getBytes("UTF-8"));
				out.write(imageOrder.getBytes("UTF-8"));
				out.write("\r\n".getBytes("UTF-8"));
				while (((bytesOut = disImage.read(bufferOut, 0, 1024)) != -1)) {
					out.write(bufferOut, 0, bytesOut);
				}

			} else if (type == 2) {
				out.write("type=2".getBytes("UTF-8"));
				out.write("\r\n".getBytes("UTF-8"));
				out.write("debug=0".getBytes("UTF-8"));
				out.write("\r\n".getBytes("UTF-8"));
				out.write(("pdfPath=" + verifyPdfUrl).getBytes("UTF-8"));
				out.write("\r\n".getBytes("UTF-8"));
			}

			out.flush();
			out.close();
			Log.v("tbz", "outputStream transform end");

			InputStream isResult = conn.getInputStream();
			String resultString = inputStream2String(isResult);
			if (type == 1) {
				sendMsgToHandler(resultString, false);
			} else if (type == 2) {

				sendMsgToHandler(resultString, false);
			}

		} catch (Exception e) {
			Log.v("tbz", "exception found");
			sendMsgToHandler(getString(R.string.exception_tip), false);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				Log.v("tbz", "final step");
				conn.disconnect();
				conn = null;
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_annotation_add:
			update();
			openHandwriteAnnotation();
			break;
		case R.id.layout_annotation_delete:

			if (!isDocumentModified()) {

				Toast.makeText(this, "未签批", Toast.LENGTH_SHORT).show();
			} else {
				createDialog(TYPE_ANNOT_STAMP);
			}
			break;
//		case R.id.layout_file:
//			startActivity(new Intent(this, FileActivity.class));
//			break;
		}
	}

	private int tag = 1;
	public static View topMenu = null;

	public boolean isDocumentModified() {
		boolean boo = super.isDocumentModified();

		return boo;
	}

	@Override
	protected void onStopImpl(boolean finishing) {
		// TODO Auto-generated method stub
		super.onStopImpl(finishing);
	}

	public static BillPdfShowerActivity getInstance() {
		return (BillPdfShowerActivity) context;
	}

	@Override
	public void onResumeImpl() {
		topMenu.requestFocus();
		super.onResumeImpl();
	}

	@Override
	public void refreshPage(int pageNo) {

		super.refreshPage(pageNo);
	}

	@Override
	public void refreshDocument() {
		// TODO Auto-generated method stub
		super.refreshDocument();
	}

	/**
	 * @param fieldName 参数是插入的位置
	 * @param content
	 *            插入的内容
	 */
	public void setFieldcontent(String fieldName, String content) {
		setFieldContent(fieldName, content);
	}

	/**
	 *  获取某个域的内容
	 * @param fieldName
	 */
	public String getFieldcontent(String fieldName) {
		return getFieldContent(fieldName);
	}
	
	/**
	 * 隐藏顶部按钮
	 * 
	 * @Description: TODO
	 * @auther 修改作者：杨海
	 */
	public void GONELayoutButton() {
		findViewById(R.id.layout_buttons).setVisibility(View.GONE);
	}

	@Override
	public int getCurrentPageNo() {
		// TODO Auto-generated method stub
		return super.getCurrentPageNo();
	}

}
