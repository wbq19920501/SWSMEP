package com.jokeep.swsmep.utls;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by wbq501 on 2016-2-29 09:57.
 * SWSMEP
 */
public class FileUtils {
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
}
