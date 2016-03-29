package com.jokeep.swsmep.utls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.jokeep.swsmep.base.FileDownloadThread;
import com.jokeep.swsmep.base.FileType;
import com.jokeep.swsmep.view.DownloadProgressDialog;


import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by wbq501 on 2016-2-29 09:57.
 * SWSMEP
 */
public class FileUtils {

    //下载文件进度条
    private  DownloadProgressDialog mProgressbar;
    private  Context ct;

    //获得文件路径
    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection,null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        }else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    //获得文件名
    public static String getFileName(String pathandname){
        int length = pathandname.length();
        int start=pathandname.lastIndexOf("/");
        int end=pathandname.lastIndexOf(".");
        if(start!=-1 && end!=-1){
            return pathandname.substring(start+1,length);
        }else{
            return null;
        }
    }

    /**
     * 下载准备工作，获取SD卡路径、开启线程
     * @param mContext
     * @param url
     * @param filename
     */
    public  void downfile(Context mContext, String url,String filename) {
        ct=mContext;
        mProgressbar = new DownloadProgressDialog(mContext);
        // 获取SD卡路径
        String path = Environment.getExternalStorageDirectory()
                + "/SWSMEP/";
        File file = new File(path);
        // 如果SD卡目录不存在创建
        if (!file.exists()) {
            file.mkdirs();
        }
        String saveFilePath=path+filename;
        file = new File(saveFilePath);
        if(file.exists()==true)
        {//直接打开
            FileType.openfile(saveFilePath, ct);
        }
        else {//文件存在不进行下载
            mProgressbar.show();
            mProgressbar.setCancelable(false);
            // 设置progressBar初始化
            mProgressbar.setProgress(0);
            int threadNum = 5;
            String filepath = path + filename;
            downloadTask task = new downloadTask(url, threadNum, filepath);
            task.start();
        }
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
            mProgressbar.setProgress((s*100/filesize));
            if (s == filesize){
                Toast.makeText(ct, "下载完成！", Toast.LENGTH_SHORT).show();
                mProgressbar.dismiss();
                String filepath = msg.getData().getString("filepath");
                FileType.openfile(filepath, ct);
            }
        }
    };

    /**
     * 多线程文件下载
     *
     * @author zhangxp
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
                //URLConnection conn = url.openConnection();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(60000);

                if(conn.getResponseCode()==404)
                {//请求失败直接关闭
                    Toast.makeText(ct, "文件不存在", Toast.LENGTH_SHORT).show();
                    mProgressbar.dismiss();
                    return;
                }
                // 读取下载文件总大小
                int fileSize = conn.getContentLength();
                if (fileSize <= 0) {
                    mProgressbar.dismiss();
                    System.out.println("读取文件失败");
                    return;
                }
                // 设置ProgressBar最大的长度为文件Size
                //mProgressbar.setMax(fileSize);

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
                mProgressbar.dismiss();
                e.printStackTrace();
            }
        }
    }
}
