package com.jokeep.swsmep.fragment;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.AES;
import com.jokeep.swsmep.base.FileDownloadThread;
import com.jokeep.swsmep.base.FileType;
import com.jokeep.swsmep.base.HttpIP;
import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.model.WorkTable;
import com.jokeep.swsmep.view.DownloadProgressDialog;
import com.jokeep.swsmep.view.ShowDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbq501 on 2016-3-3 16:26.
 * SWSMEP
 */
public class WorkAnnexFragment extends Fragment{
    View fragment;
    ListView list;
    BaseAdapter adapter;
    String TOKENID,f_jointid;
    LinearLayout nomsg;
    List<WorkTable> workTables;
    private ShowDialog dialog;
    DownloadProgressDialog mProgressbar;
    int filesize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragment == null){
            fragment = inflater.inflate(R.layout.workannex,container,false);
        }else {
            // 缓存的rootView需要判断是否已经被加过parent
            // 如果有parent需要从parent删除
            ViewGroup parent = (ViewGroup) fragment.getParent();
            if (parent != null) {
                parent.removeView(fragment);
            }
        }
        init();
        initdata();
        return fragment;
    }

    private void initdata() {
        dialog.show();
        RequestParams params = new RequestParams(HttpIP.MainService+HttpIP.JointAttByID);
        JSONObject object = new JSONObject();
        try {
            object.put("JointID", f_jointid);
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
                            String Result = object2.getString("Result");
                            JSONArray array0 = new JSONArray(Result);
                            JSONArray arrayTable = new JSONArray(((JSONObject)array0.get(0)).getString("Table"));
                            JSONArray arrayTable1 = new JSONArray(((JSONObject)array0.get(0)).getString("Table1"));
                            JSONObject object3 = (JSONObject) arrayTable.get(0);
                            f_jointid = object3.getString("F_JOINTID");
                            for (int i=0;i<arrayTable1.length();i++){
                                JSONObject jsonObject = (JSONObject) arrayTable1.get(i);
                                WorkTable workTable = new WorkTable();
                                workTable.setF_FILENAME(jsonObject.getString("F_FILENAME"));
                                workTable.setF_FILETYPE(jsonObject.getString("F_FILETYPE"));
                                workTable.setF_FILESIZE(jsonObject.getInt("F_FILESIZE"));
                                workTable.setF_STORAGEPATH(jsonObject.getString("F_STORAGEPATH"));
                                workTable.setIsUp(true);
                                workTables.add(workTable);
                            }
                            if (workTables.size() == 0){
                                nomsg.setVisibility(View.VISIBLE);
                                list.setVisibility(View.GONE);
                            }else {
                                list.setVisibility(View.VISIBLE);
                                nomsg.setVisibility(View.GONE);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    dialog.dismiss();
                    Log.d("ex", ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        workTables = new ArrayList<WorkTable>();
        Bundle data = getArguments();
        TOKENID = data.getString("TOKENID");
        f_jointid = data.getString("JointID");

        dialog = new ShowDialog(getActivity(),R.style.MyDialog,getResources().getString(R.string.dialogmsg));
        mProgressbar = new DownloadProgressDialog(getActivity());

        list = (ListView) fragment.findViewById(R.id.listwork_annex);
        nomsg = (LinearLayout) fragment.findViewById(R.id.nomsg);
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return workTables.size();
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
                if (convertView==null){
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.look_fileitem,null);
                    holder = new ViewHolder();
                    holder.filename = (TextView) convertView.findViewById(R.id.file_name);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                final WorkTable workTable = workTables.get(position);
                holder.filename.setText(workTable.getF_FILENAME());
                holder.filename.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downfile(v,workTable);
                    }
                });
                return convertView;
            }
        };
        list.setAdapter(adapter);
    }

    private void downfile(View v, WorkTable workTable) {
        filesize = workTable.getF_FILESIZE();
        doDownload(HttpIP.Web+"/"+workTable.getF_STORAGEPATH(),workTable.getF_FILENAME());
    }
    /**
     * 下载准备工作，获取SD卡路径、开启线程
     * @param url
     * @param f_filename
     */
    private void doDownload(String url, String f_filename) {
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

        // 简单起见，我先把URL和文件名称写死，其实这些都可以通过HttpHeader获取到
//        String downloadUrl = "http://gdown.baidu.com/data/wisegame/91319a5a1dfae322/baidu_16785426.apk";
//        String fileName = "baidu_16785426.apk";
        int threadNum = 5;
        String filepath = path + f_filename;
        downloadTask task = new downloadTask(url, threadNum, filepath);
        task.start();
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
            if (s==filesize) {
                Toast.makeText(getActivity(), "下载完成！", Toast.LENGTH_SHORT).show();
                mProgressbar.dismiss();
                String filepath = msg.getData().getString("filepath");
                FileType.openfile(filepath, getActivity());
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
    class ViewHolder{
        TextView filename;
    }
}
