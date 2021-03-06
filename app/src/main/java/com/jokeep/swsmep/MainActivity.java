package com.jokeep.swsmep;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.activity.ActivityList;
import com.jokeep.swsmep.activity.ChangePassdActivity;
import com.jokeep.swsmep.activity.SearchManActivity;
import com.jokeep.swsmep.adapter.MyFragmentPager;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.CalendarUtil;
import com.jokeep.swsmep.base.Client;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.MyData;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.base.SwsApplication;
import com.jokeep.swsmep.fragment.NewsFragment;
import com.jokeep.swsmep.fragment.Phone3Fragment;
import com.jokeep.swsmep.fragment.ScheduleFragment;
import com.jokeep.swsmep.fragment.WorkFragment;
import com.jokeep.swsmep.model.UserInfo;
import com.jokeep.swsmep.model.WorkNumber;
import com.jokeep.swsmep.photo.Crop;
import com.jokeep.swsmep.photo.FileUtils;
import com.jokeep.swsmep.view.RoundImageView;
import com.jokeep.swsmep.view.SelectPicPopupWindow;
import com.jokeep.swsmep.view.ShowDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


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
    Phone3Fragment phonefragment;

    private RoundImageView use_img;
    private Button upload_img,change_psd,chang_number,use_exit;
    private TextView use_name,use_text;

    //自定义的弹出框类
    private SelectPicPopupWindow menuWindow;
    private Intent intent;
    private Context context;
    private List<WorkNumber> NumberList;

    UserInfo.ResultEntity.UserInfoEntity userInfoEntity;
    String FUSERID = null;

    private ShowDialog dialog;
    private File mTempDir;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1458;
    private String mCurrentPhotoPath;
    private SwsApplication application;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String TOKENID;
    private MyData mMyData = MyData.getInstance();

    private TextView main_year,main_month;
    private RelativeLayout years,months;
    PopupWindow popupWindow;
    PopupWindow popupWindow2;
    BaseAdapter adapteryear;
    BaseAdapter adaptermonth;
    List<Integer> listyear;
    List<Integer> listymonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        ActivityList.getInstance().addActivity(this);
        application = (SwsApplication) getApplication();
        init();
        initdata();
        //connectToServerByPostMain();
    }

    private void init() {
        NumberList = new ArrayList<>();
        sp = getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE);
        TOKENID = sp.getString(SaveMsg.TOKENID, "");

        dialog = new ShowDialog(MainActivity.this,R.style.MyDialog,"正在加载...");
        dialog.setCancelable(false);
        mTempDir = new File( Environment.getExternalStorageDirectory(),"Temp");
        if(!mTempDir.exists()){
            mTempDir.mkdirs();
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }
            @Override
            public void onDrawerOpened(View drawerView) {
                drawerView.setClickable(true);
            }
            @Override
            public void onDrawerClosed(View drawerView) {

            }
            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
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
        menu_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, SearchManActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        initSchedule();
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
        mMyData.setmTabnum1(main_tabnum1);
        tab_view1 = findViewById(R.id.tab_view1);
        tab_view2 = findViewById(R.id.tab_view2);
        tab_view3 = findViewById(R.id.tab_view3);
        tab_view4 = findViewById(R.id.tab_view4);

        pager = (ViewPager) findViewById(R.id.main_viewpager);
        fragmentList = new ArrayList<Fragment>();
        workfragment = new WorkFragment();
        newsfragment = new NewsFragment();
        schedulefragment = new ScheduleFragment();
        phonefragment = new Phone3Fragment();
        fragmentList.add(workfragment);
        fragmentList.add(newsfragment);
        fragmentList.add(schedulefragment);
        fragmentList.add(phonefragment);

        pager.setAdapter(new MyFragmentPager(getSupportFragmentManager(), fragmentList));
        pager.setOffscreenPageLimit(4);
        pager.setCurrentItem(0);
        pager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    private void initSchedule() {
        listyear = new ArrayList<>();
        listymonth = new ArrayList<>();
        final int year = CalendarUtil.getYear();
        final int month = CalendarUtil.getMonth();
        years = (RelativeLayout) findViewById(R.id.years);
        months = (RelativeLayout) findViewById(R.id.months);
        main_year = (TextView) findViewById(R.id.main_year);
        main_month = (TextView) findViewById(R.id.main_month);
        main_year.setText(year+"年");
        main_month.setText(month + "月");
        for (int i=1;i<=12;i++){
            listymonth.add(i);
        }
        for (int i=0;i<30;i++){
            listyear.add(year-10+i);
        }
        years.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int yearswidth = v.getWidth();
                int yearsheight = v.getHeight();
                showPopupWindow(v, yearswidth, yearsheight, year);
            }
        });
        months.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int monthswidth = v.getWidth();
                int monthheight = v.getHeight();
                showPopupWindow2(v, monthswidth, monthheight, month);
            }
        });
        adapteryear = new BaseAdapter() {
            @Override
            public int getCount() {
                return listyear.size();
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;
                if (convertView == null){
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.window_schedule_item,null);
                    holder.calendar = (TextView) convertView.findViewById(R.id.calend_item);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.calendar.setText(listyear.get(position)+"年");
                return convertView;
            }
        };
        adaptermonth = new BaseAdapter() {
            @Override
            public int getCount() {
                return listymonth.size();
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;
                if (convertView == null){
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.window_schedule_item,null);
                    holder.calendar = (TextView) convertView.findViewById(R.id.calend_item);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.calendar.setText(listymonth.get(position)+"月");
                return convertView;
            }
        };
    }
    private void showPopupWindow2(View parent,int width,int height,int month) {
        if (popupWindow2 == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.window_schedule2, null);
            ListView list = (ListView) view.findViewById(R.id.window_schedule_list);
            TextView main_month = (TextView) view.findViewById(R.id.main_month);
            main_month.setText(month+"月");
//            list.setAdapter(adaptermonth);
            popupWindow2 = new PopupWindow(view, width, height*5);
        }
        popupWindow2.setBackgroundDrawable(new BitmapDrawable());
        popupWindow2.setOutsideTouchable(true);
        popupWindow2.setFocusable(true);

        popupWindow2.showAsDropDown(parent, 0, -height);
    }
    private void showPopupWindow(View parent,int width,int height,int year) {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.window_schedule, null);
            ListView list = (ListView) view.findViewById(R.id.window_schedule2_list);
            TextView main_year = (TextView) view.findViewById(R.id.main_year);
            main_year.setText(year+"年");
//            list.setAdapter(adapteryear);
            popupWindow = new PopupWindow(view, width, height*5);
        }
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAsDropDown(parent, 0, -height);
    }
    class ViewHolder{
        TextView calendar;
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
        dialog.show();
        userInfoEntity = new UserInfo.ResultEntity.UserInfoEntity();
        intent = getIntent();
        userInfoEntity = (UserInfo.ResultEntity.UserInfoEntity) intent.getSerializableExtra("result");
        use_name.setText(userInfoEntity.getF_USERNAME() + "(" + userInfoEntity.getF_POSITIONNAME() + ")");
        use_text.setText(userInfoEntity.getF_DEPARTMENTNAME());
        FUSERID = userInfoEntity.getF_USERID();
        application.setFUSERID(FUSERID);
        application.setF_USERNAME(userInfoEntity.getF_USERNAME());
        application.setF_MAINDEPARTID(userInfoEntity.getF_MAINDEPARTID());
        application.setF_MAINUNITID(userInfoEntity.getF_MAINUNITID());
        application.setF_POSITIONNAME(userInfoEntity.getF_POSITIONNAME());
        application.setF_DEPARTMENTNAME(userInfoEntity.getF_DEPARTMENTNAME());
        application.setF_INTERVAL(userInfoEntity.getF_INTERVAL());


        ImageOptions imageOptions = new ImageOptions.Builder()
                .setRadius(DensityUtil.dip2px(5))//ImageView圆角半径
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.mipmap.ic_launcher)
                .setFailureDrawableId(R.mipmap.ic_launcher)
                .build();
        x.image().bind(use_img, userInfoEntity.getF_USERHEADURI(), imageOptions);
        dialog.dismiss();
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
//                sp = getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE);
//                editor = sp.edit();
//                editor.clear();
//                editor.commit();
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
        dialog = new ShowDialog(MainActivity.this,R.style.MyDialog,"修改头像中...");
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
                    getImageFromCamera();
                    break;
                case R.id.btn_pick_photo:
                    Crop.pickImage(MainActivity.this);
                    break;
                default:
                    break;
            }
            menuWindow.dismiss();
        }
    };
    protected void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = "Temp_camera" + String.valueOf( System.currentTimeMillis());
        File cropFile = new File( mTempDir, fileName);
        Uri fileUri = Uri.fromFile( cropFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name
        mCurrentPhotoPath = fileUri.getPath();
        // start the image capture Intent
        startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTempDir.exists()){
            FileUtils.deleteFile(mTempDir);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK || data != null){
            if(requestCode == Crop.REQUEST_PICK) {
                beginCrop(data.getData());
            } else if(requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode,data);
            } else if(requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
                if(mCurrentPhotoPath != null) {
                    beginCrop(Uri.fromFile( new File( mCurrentPhotoPath)));
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            dialog.show();
            upload(Crop.getOutput(result), Crop.getOutput(result).getPath());
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(MainActivity.this, Crop.getError(result).getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void beginCrop(Uri source) {
        String fileName = "Temp_" + String.valueOf( System.currentTimeMillis());
        File cropFile = new File( mTempDir, fileName);
        Uri outputUri = Uri.fromFile( cropFile);
        new Crop(source).output(outputUri).setCropType(true).start(MainActivity.this);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ExitDialog(MainActivity.this).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private Dialog ExitDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("退出");
        builder.setMessage("确认退出程序吗？");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity.this.finish();
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
        return builder.create();
    }
    private void upload(final Uri bitmap,String path) {

        byte[] bytes = null;
        InputStream is;
        File myfile = new File(path);
        String imgtype = ".png";
//        String imgtype = myfile.getName().substring(myfile.getName().lastIndexOf(".")+1);
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
            Toast.makeText(MainActivity.this,"修改头像失败",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void requesthttp(byte[] updata,final Uri bitmap) {
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
//                        use_img.setImageBitmap(bitmap);
                        use_img.setImageURI(bitmap);
                        Toast.makeText(MainActivity.this,"修改头像成功",Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this,"修改头像失败",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
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

        int widthSample = (int) (imageWidth/5);
        int heightSample = (int) (imageHeight/5);
        // 计算缩放比例
        options.inSampleSize = widthSample < heightSample ? heightSample
                : widthSample;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imgPath);
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
