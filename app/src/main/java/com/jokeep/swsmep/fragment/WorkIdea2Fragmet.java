package com.jokeep.swsmep.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.activity.WorkReturnIdea2Activity;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.FileDownloadThread;
import com.jokeep.swsmep.base.FileType;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.model.SuggestionFilesInfo;
import com.jokeep.swsmep.model.SuggestionInfo;
import com.jokeep.swsmep.model.SuggestionsInfo;
import com.jokeep.swsmep.model.Work1Info;
import com.jokeep.swsmep.view.DownloadProgressDialog;
import com.jokeep.swsmep.view.RoundImageView;
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
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbq501 on 2016-3-3 14:50.
 * SWSMEP  意见
 */
public class WorkIdea2Fragmet extends Fragment{
    public static final String action = "com.swsmep.suggestion";
    View fragment;
    private ListView idea_list;
    LinearLayout no_msg;
    BaseAdapter adapter;
    List<SuggestionInfo> suggestionInfos;
    List<Work1Info> work1Infos;
    List<SuggestionsInfo> suggestionsInfos;
    List<SuggestionFilesInfo> suggestionFilesInfos;
    String F_EXECUTMAINID,TOKENID;
    private ShowDialog dialog;
    Intent intent;
    String Title,BusinessCode,MainID;
    DownloadProgressDialog mProgressbar;
    int typeopen,state;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragment == null){
            fragment = inflater.inflate(R.layout.work_idea2,container,false);
        }else {
            // 缓存的rootView需要判断是否已经被加过parent
            // 如果有parent需要从parent删除
            ViewGroup parent = (ViewGroup) fragment.getParent();
            if (parent != null) {
                parent.removeView(fragment);
            }
        }
        IntentFilter filter = new IntentFilter(action);
        getActivity().registerReceiver(broadcastReceiver, filter);
        init();
        initdata();
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            suggestionInfos.clear();
            initdata();
        }
    };
    private void initdata() {
        RequestParams params = new RequestParams(HttpIP.MainService+HttpIP.ToDo_Opinion_Filter);
        JSONObject object = new JSONObject();
        try {
            object.put("ExcutMainID",F_EXECUTMAINID);
            params.addBodyParameter("parameter", AES.encrypt(object.toString()));
            params.setAsJsonContent(true);
            params.addBodyParameter(SaveMsg.TOKENID, TOKENID);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    String s = AES.desEncrypt(result.toString());
                    Log.d("s", s);
                    dialog.dismiss();
                    try {
                        JSONObject object2 = new JSONObject(s);
                        int code = object2.getInt("ErrorCode");
                        if (code==1){
                            Toast.makeText(getActivity(), object2.getString("ErrorMsg").toString(), Toast.LENGTH_SHORT).show();
                        }else if (code==0){
                            JSONArray jsonArray = new JSONArray(object2.getString("Result"));
                            JSONObject object3 = (JSONObject) jsonArray.get(0);
                            JSONArray array = new JSONArray(object3.getString("Table"));
                            for (int i=0;i<array.length();i++){
                                JSONObject jsonObject = (JSONObject) array.get(i);
                                SuggestionInfo suggestionInfo = new SuggestionInfo();
                                suggestionInfo.setF_OPINIONID(jsonObject.getString("F_OPINIONID"));
                                suggestionInfo.setF_EXECUTMAINID(jsonObject.getString("F_EXECUTMAINID"));
                                suggestionInfo.setF_NODEID(jsonObject.getString("F_NODEID"));
                                suggestionInfo.setF_OPINION(jsonObject.getString("F_OPINION"));
                                suggestionInfo.setF_CONCLUSION(jsonObject.getString("F_CONCLUSION"));
                                suggestionInfo.setF_HANDLETIME(jsonObject.getString("F_HANDLETIME"));
                                suggestionInfo.setF_HANDLEID(jsonObject.getString("F_HANDLEID"));
                                suggestionInfo.setF_HANDLENAME(jsonObject.getString("F_HANDLENAME"));
                                suggestionInfo.setF_DEPARTMENTNAME(jsonObject.getString("F_DEPARTMENTNAME"));
                                suggestionInfo.setF_POSITIONNAME(jsonObject.getString("F_POSITIONNAME"));
                                suggestionInfo.setF_USERHEADURI(jsonObject.getString("F_USERHEADURI"));
                                suggestionInfo.setSuggestionFilesInfos(files(jsonObject.getString("ATTACHMENT")));
                                suggestionInfo.setSuggestionsInfos(suggestionlist(jsonObject.getString("OPINIONREPLY")));
                                suggestionInfo.setATTACHMENT(jsonObject.getString("ATTACHMENT"));
                                suggestionInfo.setOPINIONREPLY(jsonObject.getString("OPINIONREPLY"));
                                suggestionInfos.add(suggestionInfo);
                            }
                            if (array.length()==0){
                                no_msg.setVisibility(View.VISIBLE);
                                idea_list.setVisibility(View.GONE);
                            }else {
                                no_msg.setVisibility(View.GONE);
                                idea_list.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    dialog.dismiss();
//                    Log.d("ex", ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        } catch (JSONException e) {
            dialog.dismiss();
            e.printStackTrace();
        }
    }

    private List<SuggestionsInfo> suggestionlist(String OPINIONREPLY) throws JSONException {
        JSONArray array = new JSONArray(OPINIONREPLY);
        for (int i=0;i<array.length();i++){
            JSONObject object1 = (JSONObject) array.get(i);
            SuggestionsInfo suggestionsInfo = new SuggestionsInfo();
            suggestionsInfo.setF_OPINIONREPLYID(object1.getString("F_OPINIONREPLYID"));
            suggestionsInfo.setF_OPINIONID(object1.getString("F_OPINIONID"));
            suggestionsInfo.setF_OPINION(object1.getString("F_OPINION"));
            suggestionsInfo.setF_USERID(object1.getString("F_USERID"));
            suggestionsInfo.setF_USERNAME(object1.getString("F_USERNAME"));
            suggestionsInfo.setF_REPLYTIME(object1.getString("F_REPLYTIME"));
            suggestionsInfo.setF_REPLYTIMEFULL(object1.getString("F_REPLYTIMEFULL"));
            suggestionsInfo.setF_DEPARTMENTNAME(object1.getString("F_DEPARTMENTNAME"));
            suggestionsInfo.setF_POSITIONNAME(object1.getString("F_POSITIONNAME"));
            suggestionsInfo.setSuggestionFilesInfos(childFiles(object1.getString("ATTACHMENT")));
            suggestionsInfos.add(suggestionsInfo);
        }
        return suggestionsInfos;
    }

    private List<SuggestionFilesInfo> childFiles(String ATTACHMENT) throws JSONException {
        suggestionFilesInfos = new ArrayList<SuggestionFilesInfo>();
        JSONArray array = new JSONArray(ATTACHMENT);
        for (int i=0;i<array.length();i++){
            JSONObject object1 = (JSONObject) array.get(i);
            SuggestionFilesInfo suggestionFilesInfo = new SuggestionFilesInfo();
            suggestionFilesInfo.setF_ATTACHMENTID(object1.getString("F_ATTACHMENTID"));
            suggestionFilesInfo.setF_DATAID(object1.getString("F_DATAID"));
            suggestionFilesInfo.setF_FILENAME(object1.getString("F_FILENAME"));
            suggestionFilesInfo.setF_STORAGEPATH(object1.getString("F_STORAGEPATH"));
            suggestionFilesInfos.add(suggestionFilesInfo);
        }
        return suggestionFilesInfos;
    }

    private List<SuggestionFilesInfo> files(String ATTACHMENT) throws JSONException {
        JSONArray array = new JSONArray(ATTACHMENT);
        for (int i=0;i<array.length();i++){
            JSONObject object1 = (JSONObject) array.get(i);
            SuggestionFilesInfo suggestionFilesInfo = new SuggestionFilesInfo();
            suggestionFilesInfo.setF_ATTACHMENTID(object1.getString("F_ATTACHMENTID"));
            suggestionFilesInfo.setF_DATAID(object1.getString("F_DATAID"));
            suggestionFilesInfo.setF_FILENAME(object1.getString("F_FILENAME"));
            suggestionFilesInfo.setF_STORAGEPATH(object1.getString("F_STORAGEPATH"));
            suggestionFilesInfos.add(suggestionFilesInfo);
        }
        return suggestionFilesInfos;
    }
    private void init() {
        typeopen = getActivity().getIntent().getIntExtra("typeopen", 1);
        dialog = new ShowDialog(getActivity(),R.style.MyDialog,getResources().getString(R.string.dialogmsg));
        mProgressbar = new DownloadProgressDialog(getActivity());
        suggestionInfos = new ArrayList<SuggestionInfo>();
        suggestionsInfos = new ArrayList<SuggestionsInfo>();
        suggestionFilesInfos = new ArrayList<SuggestionFilesInfo>();
        work1Infos = new ArrayList<Work1Info>();
        work1Infos = (List<Work1Info>) getActivity().getIntent().getSerializableExtra("work1Infos");
        int position = getActivity().getIntent().getIntExtra("intposition", 0);
        if (typeopen == 4){
            state = work1Infos.get(position).getF_STATE();
        }
        F_EXECUTMAINID = work1Infos.get(position).getF_EXECUTMAINID();
        Title = work1Infos.get(position).getF_TITLE();
        BusinessCode = work1Infos.get(position).getF_BUSINESSCODE();
        MainID = work1Infos.get(position).getF_JOINTID();
        TOKENID = getActivity().getIntent().getStringExtra("TOKENID");

        no_msg = (LinearLayout) fragment.findViewById(R.id.no_msg);
        idea_list = (ListView) fragment.findViewById(R.id.idea_list);
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return suggestionInfos.size();
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
                ViewGroipHolder holder = null;
                if (convertView == null){
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.wrok_idea_item1, null);
                    holder = new ViewGroipHolder();
                    holder.man_name = (TextView) convertView.findViewById(R.id.man_name);
                    holder.man_type = (TextView) convertView.findViewById(R.id.man_type);
                    holder.man_time = (TextView) convertView.findViewById(R.id.man_time);
                    holder.man_img = (RoundImageView) convertView.findViewById(R.id.man_img);
                    holder.man_context = (TextView) convertView.findViewById(R.id.man_context);
                    holder.files = (LinearLayout) convertView.findViewById(R.id.files);
                    holder.man_imgs = (ImageView) convertView.findViewById(R.id.man_imgs);
                    holder.addview = (LinearLayout) convertView.findViewById(R.id.addview);
                    holder.return_msg = (Button) convertView.findViewById(R.id.return_msg);
                    holder.add_ideamsg = (LinearLayout) convertView.findViewById(R.id.add_ideamsg);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewGroipHolder) convertView.getTag();
                }
                if (typeopen == 3){
                    holder.return_msg.setVisibility(View.GONE);
                }else if (typeopen == 4){
                    if (state==10){
                        holder.return_msg.setVisibility(View.VISIBLE);
                    }else {
                        holder.return_msg.setVisibility(View.GONE);
                    }
                }
                final SuggestionInfo suggestionInfo = suggestionInfos.get(position);
                final List<SuggestionFilesInfo> suggestionFilesInfos = suggestionInfo.getSuggestionFilesInfos();
                holder.man_name.setText(suggestionInfo.getF_HANDLENAME());
                holder.man_type.setText(suggestionInfo.getF_DEPARTMENTNAME()+"-"+suggestionInfo.getF_POSITIONNAME());
                holder.man_time.setText(suggestionInfo.getF_HANDLETIME());
                holder.man_context.setText(suggestionInfo.getF_OPINION());
                if (suggestionFilesInfos.size()==0){
                    holder.files.setVisibility(View.GONE);
                }
                for (int i=0;i<suggestionFilesInfos.size();i++){
                    TextView textView = new TextView(getActivity());
                    textView.setText(suggestionFilesInfos.get(i).getF_FILENAME());
                    textView.setTextColor(Color.parseColor("#21ac69"));
                    holder.addview.addView(textView);
                    final int finalI = i;
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            downfile(HttpIP.Web+"/"+suggestionFilesInfos.get(finalI).getF_STORAGEPATH(),
                                    suggestionFilesInfos.get(finalI).getF_FILENAME());
                        }
                    });
                }
                List<SuggestionsInfo> suggestionsInfos = suggestionInfo.getSuggestionsInfos();
                for (int i=0;i<suggestionsInfos.size();i++){
                    SuggestionsInfo suggestionsInfo = suggestionsInfos.get(i);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    View view = inflater.inflate(R.layout.wrok_idea_item2, null);
                    TextView man_name = (TextView) view.findViewById(R.id.man_name);
                    man_name.setText(suggestionsInfo.getF_USERNAME());

                    TextView man_type = (TextView) view.findViewById(R.id.man_type);
                    man_type.setText(suggestionsInfo.getF_DEPARTMENTNAME()+"-"+suggestionsInfo.getF_POSITIONNAME());

                    TextView man_context = (TextView) view.findViewById(R.id.man_context);
                    man_context.setText(suggestionsInfo.getF_OPINION());

                    LinearLayout files = (LinearLayout) view.findViewById(R.id.files);
                    LinearLayout addview = (LinearLayout) view.findViewById(R.id.addview);
                    final List<SuggestionFilesInfo> suggestionFilesInfos1 = suggestionsInfo.getSuggestionFilesInfos();
                    if (suggestionFilesInfos1.size()==0){
                        files.setVisibility(View.GONE);
                    }else {
                        for (int j=0;j<suggestionFilesInfos1.size();j++){
                            TextView textView = new TextView(getActivity());
                            textView.setText(suggestionFilesInfos1.get(j).getF_FILENAME());
                            textView.setTextColor(Color.parseColor("#21ac69"));
                            addview.addView(textView);
                            final int finalJ = j;
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    downfile(HttpIP.Web+"/"+suggestionFilesInfos1.get(finalJ).getF_STORAGEPATH(),
                                            suggestionFilesInfos1.get(finalJ).getF_FILENAME());
                                }
                            });
                        }
                    }

                    TextView man_time = (TextView) view.findViewById(R.id.man_time);
                    man_time.setText(suggestionsInfo.getF_REPLYTIME());

                    view.setLayoutParams(lp);
                    holder.add_ideamsg.addView(view);
                }
                ImageOptions imageOptions = new ImageOptions.Builder()
                        .setRadius(DensityUtil.dip2px(5))//ImageView圆角半径
                        .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                        .setLoadingDrawableId(R.mipmap.ic_launcher)
                        .setFailureDrawableId(R.mipmap.ic_launcher)
                        .build();
                x.image().bind(holder.man_img, suggestionInfo.getF_USERHEADURI(), imageOptions);
                holder.return_msg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(getActivity(), WorkReturnIdea2Activity.class);
                        List<SuggestionInfo> listsuggestionInfo = new ArrayList<SuggestionInfo>();
                        listsuggestionInfo.add(suggestionInfo);
                        intent.putExtra("suggestionInfo", (Serializable) listsuggestionInfo);
                        intent.putExtra("MainID",MainID);
                        intent.putExtra("Title",Title);
                        intent.putExtra("BusinessCode",BusinessCode);
                        startActivityForResult(intent, 2);
                        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                });
                return convertView;
            }
        };
        idea_list.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                if (resultCode == getActivity().RESULT_OK){
                    idea_list.clearFocus();
                    suggestionInfos.clear();
                    suggestionsInfos.clear();
                    suggestionFilesInfos.clear();
                    initdata();
                }
                break;
        }
    }

    /**
     * 下载准备工作，获取SD卡路径、开启线程
     * @param url
     * @param filename
     */
    private void downfile(String url,String filename) {
        // 获取SD卡路径
        String path = Environment.getExternalStorageDirectory()
                + "/SWSMEP/";
        File file = new File(path);
        // 如果SD卡目录不存在创建
        if (!file.exists()) {
            file.mkdir();
        }
        mProgressbar.show();
        // 设置progressBar初始化
        mProgressbar.setProgress(0);
        int threadNum = 5;
        String filepath = path + filename;
        downloadTask task = new downloadTask(url, threadNum, filepath);
        task.start();
    }

    class ViewGroipHolder{
        RoundImageView man_img;
        TextView man_name,man_type,man_time;
        TextView man_context;
        ImageView man_imgs;
        LinearLayout files;
        Button return_msg;
        LinearLayout addview;
        LinearLayout add_ideamsg;
    }
    /**
     * 使用Handler更新UI界面信息
     */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            int s = msg.getData().getInt("size");
            int filesize = msg.getData().getInt("filesize");

            mProgressbar.setProgress((s*100)/filesize);
            if (s == filesize){
                Toast.makeText(getActivity(), "下载完成！", Toast.LENGTH_SHORT).show();
                mProgressbar.dismiss();
                String filepath = msg.getData().getString("filepath");
                FileType.openfile(filepath,getActivity());
            }
        }
    };
    /**
     * 多线程文件下载
     *
     * @author yangxiaolong
     * @2014-8-7
     */
    class downloadTask extends Thread {
        private String downloadUrl;// 下载链接地址
        private int threadNum;// 开启的线程数
        private String filePath;// 保存文件路径地址
        private int blockSize;// 每一个线程的下载量

        public downloadTask(String downloadUrl, int threadNum, String fileptah) {
            this.downloadUrl = downloadUrl;
            this.threadNum = threadNum;
            this.filePath = fileptah;
        }

        @Override
        public void run() {

            FileDownloadThread[] threads = new FileDownloadThread[threadNum];
            try {
                URL url = new URL(downloadUrl);
                URLConnection conn = url.openConnection();
                // 读取下载文件总大小
                int fileSize = conn.getContentLength();
                if (fileSize <= 0) {
                    System.out.println("读取文件失败");
                    return;
                }
                // 设置ProgressBar最大的长度为文件Size
//                mProgressbar.setMax(fileSize);

                // 计算每条线程下载的数据长度
                blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum
                        : fileSize / threadNum + 1;
                File file = new File(filePath);
                for (int i = 0; i < threads.length; i++) {
                    // 启动线程，分别下载每个线程需要下载的部分
                    threads[i] = new FileDownloadThread(url, file, blockSize,
                            (i + 1));
                    threads[i].setName("Thread:" + i);
                    threads[i].start();
                }

                boolean isfinished = false;
                int downloadedAllSize = 0;
                while (!isfinished) {
                    isfinished = true;
                    // 当前所有线程下载总量
                    downloadedAllSize = 0;
                    for (int i = 0; i < threads.length; i++) {
                        downloadedAllSize += threads[i].getDownloadLength();
                        if (!threads[i].isCompleted()) {
                            isfinished = false;
                        }
                    }
                    // 通知handler去更新视图组件
                    Message msg = new Message();
                    msg.getData().putInt("size", downloadedAllSize);
                    msg.getData().putInt("filesize",fileSize);
                    msg.getData().putString("filepath",filePath);
                    mHandler.sendMessage(msg);
                    Thread.sleep(500);// 休息1秒后再读取下载进度
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
