package com.jokeep.swsmep;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.activity.ActivityList;
import com.jokeep.swsmep.activity.ChangePassdActivity;
import com.jokeep.swsmep.adapter.MyFragmentPager;
import com.jokeep.swsmep.base.Client;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.ToastMSG;
import com.jokeep.swsmep.fragment.NewsFragment;
import com.jokeep.swsmep.fragment.PhoneFragment;
import com.jokeep.swsmep.fragment.ScheduleFragment;
import com.jokeep.swsmep.fragment.WorkFragment;
import com.jokeep.swsmep.model.UserInfo;
import com.jokeep.swsmep.view.CustomImageView;
import com.jokeep.swsmep.view.RoundImageView;
import com.jokeep.swsmep.view.SelectPicPopupWindow;
import com.jokeep.swsmep.view.ShowDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends FragmentActivity implements View.OnClickListener{
    private LinearLayout main_date;
    private ImageView menu_left,menu_right;
    private LinearLayout menuleft;
    private DrawerLayout drawerLayout;

    private RelativeLayout tab1,tab2,tab3,tab4;
    private TextView main_tab1,main_tab2,main_tab3,main_tab4,main_tabnum1,main_tabnum3;
    private View tab_view1,tab_view2,tab_view3,tab_view4;
    private ViewPager pager;
    private ArrayList<Fragment> fragmentList;
    WorkFragment workfragment;
    NewsFragment newsfragment;
    ScheduleFragment schedulefragment;
    PhoneFragment phonefragment;

    private RoundImageView use_img;
    private Button upload_img,change_psd,chang_number,use_exit;
    private TextView use_name,use_text;

    //自定义的弹出框类
    private SelectPicPopupWindow menuWindow;
    private Intent intent;
    private Context context;
    UserInfo.ResultEntity.UserInfoEntity userInfoEntity;
    String FUSERID = null;

    private ShowDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        ActivityList.getInstance().addActivity(this);
        init();
        initdata();
    }

    private void init() {
        dialog = new ShowDialog(MainActivity.this,R.style.MyDialog,"正在加载...");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        menu_left = (ImageView) findViewById(R.id.mian_menuleft);
        menuleft = (LinearLayout) findViewById(R.id.menu_left);
        menu_right = (ImageView) findViewById(R.id.main_menuright);
        main_date = (LinearLayout) findViewById(R.id.main_date);

        menu_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(menuleft);
            }
        });
        initmenu();

        tab1 = (RelativeLayout) findViewById(R.id.tab1);
        tab1.setOnClickListener(this);
        tab2 = (RelativeLayout) findViewById(R.id.tab2);
        tab2.setOnClickListener(this);
        tab3 = (RelativeLayout) findViewById(R.id.tab3);
        tab3.setOnClickListener(this);
        tab4 = (RelativeLayout) findViewById(R.id.tab4);
        tab4.setOnClickListener(this);
        main_tab1 = (TextView) findViewById(R.id.main_tab1);
        main_tab2 = (TextView) findViewById(R.id.main_tab2);
        main_tab3 = (TextView) findViewById(R.id.main_tab3);
        main_tab4 = (TextView) findViewById(R.id.main_tab4);
        main_tabnum1 = (TextView) findViewById(R.id.main_tabnum1);
        main_tabnum3 = (TextView) findViewById(R.id.main_tabnum3);
        tab_view1 = findViewById(R.id.tab_view1);
        tab_view2 = findViewById(R.id.tab_view2);
        tab_view3 = findViewById(R.id.tab_view3);
        tab_view4 = findViewById(R.id.tab_view4);

        pager = (ViewPager) findViewById(R.id.main_viewpager);
        fragmentList = new ArrayList<Fragment>();
        workfragment = new WorkFragment();
        newsfragment = new NewsFragment();
        schedulefragment = new ScheduleFragment();
        phonefragment = new PhoneFragment();
        fragmentList.add(workfragment);
        fragmentList.add(newsfragment);
        fragmentList.add(schedulefragment);
        fragmentList.add(phonefragment);

        pager.setAdapter(new MyFragmentPager(getSupportFragmentManager(), fragmentList));
        pager.setOffscreenPageLimit(4);
        pager.setCurrentItem(0);
        pager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    private void initmenu() {
        use_img = (RoundImageView) findViewById(R.id.use_img);
        use_name = (TextView) findViewById(R.id.use_name);
        use_text = (TextView) findViewById(R.id.use_text);
        upload_img = (Button) findViewById(R.id.upload_img);
        change_psd = (Button) findViewById(R.id.change_psd);
        chang_number = (Button) findViewById(R.id.chang_number);
        use_exit = (Button) findViewById(R.id.use_exit);
        initmenudata();
    }
    private void initdata() {
        userInfoEntity = new UserInfo.ResultEntity.UserInfoEntity();
        intent = getIntent();
        userInfoEntity = (UserInfo.ResultEntity.UserInfoEntity) intent.getSerializableExtra("result");
        use_name.setText(userInfoEntity.getF_USERNAME() + "(" + userInfoEntity.getF_POSITIONNAME() + ")");
        use_text.setText(userInfoEntity.getF_DEPARTMENTNAME());
        FUSERID = userInfoEntity.getF_USERID();
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setRadius(DensityUtil.dip2px(5))//ImageView圆角半径
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.mipmap.ic_launcher)
                .setFailureDrawableId(R.mipmap.ic_launcher)
                .build();
        x.image().bind(use_img, userInfoEntity.getF_USERHEADURI(), imageOptions);
    }
    private void initmenudata() {
        use_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityList.getInstance().exit();
            }
        });
        chang_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context,LoginActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
//                ActivityList.getInstance().exit();
            }
        });
        use_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popw(v);
            }
        });
        upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popw(v);
            }
        });
        change_psd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, ChangePassdActivity.class);
                intent.putExtra("FUSERID", FUSERID);
                startActivity(intent);
            }
        });

    }

    private void popw(View v) {
        drawerLayout.closeDrawer(menuleft);
        //实例化SelectPicPopupWindow
        menuWindow = new SelectPicPopupWindow(MainActivity.this, itemsOnClick);
        //显示窗口
        menuWindow.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //设置layout在PopupWindow中显示的位置
    }
    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener(){

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    Toast.makeText(MainActivity.this,"1111",Toast.LENGTH_SHORT).show();
                    takephoto();
                    break;
                case R.id.btn_pick_photo:
                    Toast.makeText(MainActivity.this,"2222",Toast.LENGTH_SHORT).show();
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent,2);
                    break;
                default:
                    break;
            }
            menuWindow.dismiss();
        }
    };

    private void takephoto() {
        //执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if(SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
            startActivityForResult(intent, 1);
        }else{
            Toast.makeText(this,"内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK || data != null){
            Uri uri = data.getData();
            Bitmap bitmap = null;
            String imgPath = null;
            if (null != uri) {
                ContentResolver resolver = this.getContentResolver();
                String[] columns = { MediaStore.Images.Media.DATA };
                Cursor cursor = null;
                cursor = resolver.query(uri, columns, null, null, null);
                if (Build.VERSION.SDK_INT > 18)// 4.4以后文件选择发生变化，判断处理
                {
                    // http://blog.csdn.net/tempersitu/article/details/20557383
                    if (requestCode == 2)// 选择图片
                    {
                        imgPath = uri.getPath();
                        if (!TextUtils.isEmpty(imgPath)
                                && imgPath.contains(":"))
                        {
                            String imgIndex = imgPath.split(":")[1];
                            cursor = resolver.query(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    columns, "_id=?", new String[] { imgIndex },
                                    null);
                        }
                    }
                }
                if (null != cursor && cursor.moveToFirst())
                {
                    int columnIndex = cursor.getColumnIndex(columns[0]);
                    imgPath = cursor.getString(columnIndex);
                    cursor.close();
                }
                if (!TextUtils.isEmpty(imgPath))
                {
                    bitmap = genBitmap(imgPath);
                }
            }
            else if (requestCode == 1)// 拍照
            {
                // 拍照时，注意小米手机不会保存图片到本地，只可以从intent中取出bitmap, 要特殊处理
                Object object = data.getExtras().get("data");
                if (null != object && object instanceof Bitmap)
                {
                    bitmap = (Bitmap) object;
                }
            }
            if (null != bitmap)
            {
                dialog.show();
                upload(bitmap, imgPath);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void upload(final Bitmap bitmap,String path) {

        byte[] bytes = null;
        InputStream is;
        File myfile = new File(path);
        String imgtype = myfile.getName().substring(myfile.getName().lastIndexOf(".")+1);
        JSONObject object = new JSONObject();
        try {
            is = new FileInputStream(path);
            bytes = new byte[(int) myfile.length()];
            int len = 0;
            int curLen = 0;
            while ((len = is.read(bytes)) != -1) {
                curLen += len;
                is.read(bytes);
            }
            is.close();
            object.put("UserId", FUSERID);
            object.put("ImageType",imgtype);
            byte[] updata = Client.getPacket(object.toString(), bytes);// 参数与文件封装成单个数据
            requesthttp(updata, bitmap);
        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
        }
    }

    private void requesthttp(byte[] updata,final Bitmap bitmap) {
        RequestParams params = new RequestParams(HttpIP.UpImage);
        params.addBodyParameter("ImageContext",updata,null);
        params.setAsJsonContent(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object2 = new JSONObject(result.toString());
                    int code = object2.getInt("ErrorCode");
                    if (code == 1){
                        Toast.makeText(MainActivity.this,object2.getString("ErrorMsg")+"",Toast.LENGTH_SHORT).show();
                    }else if (code == 0){
                        use_img.setImageBitmap(bitmap);
                        Toast.makeText(MainActivity.this,"修改头像成功",Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }
                Log.i("result",result.toString());
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog.dismiss();
                Log.i("Throwable", ex.getMessage());
            }
            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });
    }

    /**通过给定的图片路径生成对应的bitmap*/
    private Bitmap genBitmap(String imgPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;

        int widthSample = (int) (imageWidth / 60);
        int heightSample = (int) (imageHeight / 60);
        // 计算缩放比例
        options.inSampleSize = widthSample < heightSample ? heightSample
                : widthSample;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imgPath, options);
    }
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            changepage(position+1);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tab1:
                changepage(1);
                pager.setCurrentItem(0);
                break;
            case R.id.tab2:
                changepage(2);
                pager.setCurrentItem(1);
                break;
            case R.id.tab3:
                changepage(3);
                pager.setCurrentItem(2);
                break;
            case R.id.tab4:
                changepage(4);
                pager.setCurrentItem(3);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ){

        }
        return false;
    }

    private void changepage(int i) {
        switch (i){
            case 1:
                main_tab1.setTextColor(getResources().getColor(R.color.white));
                main_tab2.setTextColor(getResources().getColor(R.color.white2));
                main_tab3.setTextColor(getResources().getColor(R.color.white2));
                main_tab4.setTextColor(getResources().getColor(R.color.white2));
                tab_view1.setVisibility(View.VISIBLE);
                tab_view2.setVisibility(View.GONE);
                tab_view3.setVisibility(View.GONE);
                tab_view4.setVisibility(View.GONE);
                menu_right.setVisibility(View.GONE);
                main_date.setVisibility(View.GONE);
                break;
            case 2:
                main_tab1.setTextColor(getResources().getColor(R.color.white2));
                main_tab2.setTextColor(getResources().getColor(R.color.white));
                main_tab3.setTextColor(getResources().getColor(R.color.white2));
                main_tab4.setTextColor(getResources().getColor(R.color.white2));
                tab_view1.setVisibility(View.GONE);
                tab_view2.setVisibility(View.VISIBLE);
                tab_view3.setVisibility(View.GONE);
                tab_view4.setVisibility(View.GONE);
                menu_right.setVisibility(View.GONE);
                main_date.setVisibility(View.GONE);
                break;
            case 3:
                main_tab1.setTextColor(getResources().getColor(R.color.white2));
                main_tab2.setTextColor(getResources().getColor(R.color.white2));
                main_tab3.setTextColor(getResources().getColor(R.color.white));
                main_tab4.setTextColor(getResources().getColor(R.color.white2));
                tab_view1.setVisibility(View.GONE);
                tab_view2.setVisibility(View.GONE);
                tab_view3.setVisibility(View.VISIBLE);
                tab_view4.setVisibility(View.GONE);
                menu_right.setVisibility(View.GONE);
                main_date.setVisibility(View.VISIBLE);
                break;
            case 4:
                main_tab1.setTextColor(getResources().getColor(R.color.white2));
                main_tab2.setTextColor(getResources().getColor(R.color.white2));
                main_tab3.setTextColor(getResources().getColor(R.color.white2));
                main_tab4.setTextColor(getResources().getColor(R.color.white));
                tab_view1.setVisibility(View.GONE);
                tab_view2.setVisibility(View.GONE);
                tab_view3.setVisibility(View.GONE);
                tab_view4.setVisibility(View.VISIBLE);
                menu_right.setVisibility(View.VISIBLE);
                main_date.setVisibility(View.GONE);
                break;
        }
    }
}
