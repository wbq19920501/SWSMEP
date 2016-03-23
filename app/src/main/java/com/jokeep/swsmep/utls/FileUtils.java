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
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wbq501 on 2016-2-29 09:57.
 * SWSMEP
 */
public class FileUtils {

    //下载文件进度条
    private DownloadProgressDialog mProgressbar;
    private Context ct;

    //获得文件路径
    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    //获得文件名
    public static String getFileName(String pathandname) {
        int length = pathandname.length();
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, length);
        } else {
            return null;
        }
    }

    /**
     * 下载准备工作，获取SD卡路径、开启线程
     *
     * @param mContext
     * @param url
     * @param filename
     */
    public void downfile(Context mContext, String url, String filename) {
        ct = mContext;
        mProgressbar = new DownloadProgressDialog(mContext);
        // 获取SD卡路径
        String path = Environment.getExternalStorageDirectory()
                + "/SWSMEP/";
        File file = new File(path);
        // 如果SD卡目录不存在创建
        if (!file.exists()) {
            file.mkdir();
        }

        String saveFilePath = path + "/" + filename;
        file = new File(saveFilePath);
        if (file.exists() == true) {//直接打开
            FileType.openfile(saveFilePath, ct);
        } else {//文件存在不进行下载
            mProgressbar.show();
            mProgressbar.setCancelable(false);
            // 设置progressBar初始化
            mProgressbar.setProgress(0);
            int threadNum = 5;
            String filepath = path + filename;
            DownloadTask task = new DownloadTask(url, threadNum, filepath);
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
            mProgressbar.setProgress((s * 100 / filesize));
            if (s == filesize) {
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
     * @author yangxiaolong
     * @2014-8-7
     */
    class DownloadTask extends Thread {
        private String downloadUrl;// 下载链接地址
        private int threadNum;// 开启的线程数
        private String filePath;// 保存文件路径地址
        private int blockSize;// 每一个线程的下载量

        public DownloadTask(String downloadUrl, int threadNum, String fileptah) {
            this.downloadUrl = downloadUrl;
            this.threadNum = threadNum;
            this.filePath = fileptah;
        }

        @Override
        public void run() {

            FileDownloadThread[] threads = new FileDownloadThread[threadNum];
            HttpURLConnection conn = null;
            try {
                URL url = new URL(downloadUrl);

                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
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

                    //创建相同大小的空文件
                    File file = new File(filePath);
                    RandomAccessFile raFile = new RandomAccessFile(file, "rw");
                    raFile.setLength(fileSize);//设置文件大小
                    int startIndex = 0;// i*blockSize
                    int endIndex = 0;// (i+1)*blockSize-1,last filesize

                    for (int i = 0; i < threads.length; i++) {
                        // 启动线程，分别下载每个线程需要下载的部分
                        startIndex = (i - 1) * blockSize;
                        endIndex = i * blockSize - 1;
                        //最后一个线程下载文件剩下的大小
                        if (i == threads.length - 1) {
                            endIndex = fileSize;
                        }

                        threads[i] = new FileDownloadThread(url, file, startIndex,
                                endIndex);
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
                        msg.getData().putInt("filesize", fileSize);
                        msg.getData().putString("filepath", filePath);
                        mHandler.sendMessage(msg);
                        Thread.sleep(500);// 休息1秒后再读取下载进度
                    }

                } else {
                    throw new Exception("网络请求失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                    conn = null;
                }
            }
        }
    }
}
