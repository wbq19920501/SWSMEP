package com.jokeep.swsmep.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.kinggrid.pdfservice.PageViewMode;

import java.io.File;

/**
 * Created by wbq501 on 2016-1-19 14:02.
 * SWSMEP
 */
public class PDFTools {

    /**
     * 打开pdf传值
     * @param filepath
     * @param admin
     * @return
     */
    public static Intent getBillPdfIntent(String filepath, String admin) {
        File file = new File(filepath);
        Uri uri = Uri.fromFile(file);

        final Intent intent = new Intent("android.intent.action.VIEW", uri);
        intent.setClassName("com.jokeep.swsmep", "com.jokeep.swsmep.base.PdfShowerActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sUrl",
                "http://192.168.1.111:8888/iSignaturePDF/DefaulServlet");
        intent.putExtra("userName", admin);//用户名
        intent.putExtra("signatureField1", "Signature1");//盖章域(仅供测试)
        intent.putExtra("signatureField2", "Signature2");//签名域
        intent.putExtra("copyRight", sureCopyRight());//授权码
        intent.putExtra("isCanField_edit", false);//是否能进行域编辑
        intent.putExtra("isVectorSign", true);//手写签批是否矢量化
        intent.putExtra("isSupportEbenT7Mode", false);//手写模式
        intent.putExtra("loadPDFReaderCache", true);//是否载入上次阅读的缓存记录
        intent.putExtra("saveVector", false);//是否将矢量数据保存到文档中，默认为true；
        Bundle bundle = new Bundle();
        bundle.putParcelable("pageViewMode", PageViewMode.VSCROLL);//阅读翻页模式
        intent.putExtras(bundle);
        intent.putExtra("local_flag", 1);//是否本地文件
        intent.putExtra("bookRotation", ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        return intent;
    }
    // 金格的验证码
    public static String sureCopyRight() {
        String copyRight = "";
        copyRight = "SxD/phFsuhBWZSmMVtSjKZmm/c/3zSMrkV2Bbj5tznSkEVZmTwJv0wwMmH/+p6wL0S2OsP0xHHj1V5/y+UOyVyjsxu8s2q0ybCINp+Xd7AQhHybF2I+6S2t+TQ5f7Hn5jp2HrKwufOY7Drg3h8cpGXSHQJlRzo7luBZo5/DMZmRzDF3Q9bvFKXgnwIhBwsOEyOawfupUcPzMGmZFsFkdbJ7CHHwcDBnMVj8djMthFaapMFm/i6swvGEQ2JoygFU3qdVAR8bAZbXs39bf8Up7YGJ4C9pL9rTcXXb4rsAQi4ugSOimMBZXAWJyoNec+zKV1unaolABhIxXa+WPvNqslmbOGDsTXe358SjOA3eCpX9fjgZng3BAkQ0sSjCWwWV9xbHZI6NwWs3BVP74BcUFE6ZJhr2fg9mMpqlUrMXb+X7rywsFAbnsWEzwGSrto8BWOZRJxka2ugzP6V+ZgWsc+G9XmCmwlZlxKnkAgp1jgbY=";
        return copyRight.trim();
    }
}
