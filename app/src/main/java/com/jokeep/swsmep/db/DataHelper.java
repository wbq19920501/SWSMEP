package com.jokeep.swsmep.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jokeep.swsmep.model.UserBook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbq501 on 2016-2-22 11:08.
 * SWSMEP
 */
public class DataHelper {
    // 数据库名称
    private static String DB_NAME = "phoneman.db";
    // 数据库版本
    private static int DB_VERSION = 1;
    private SQLiteDatabase db;
    private SqliteHelper dbHelper;

    public DataHelper(Context context) {
        dbHelper = new SqliteHelper(context, DB_NAME, null, DB_VERSION );
        db = dbHelper.getWritableDatabase();
    }

    public void Close() {
        db.close();
        dbHelper.close();
    }

    // 获取users表中的UserID、Access Token、Access Secret的记录
    public List<UserBook> GetUserList() {
        List<UserBook> userList = new ArrayList<UserBook>();
        Cursor cursor = db.query(SqliteHelper. TB_NAME, null, null , null, null,
                null, UserBook. ID + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null )) {
            UserBook user = new UserBook();
            user.setId(Integer.parseInt(cursor.getString(0)));
            user.setF_POSITIONNAME(cursor.getString(1));
            user.setF_CALLPHONETYPE(Integer.parseInt(cursor.getString(2)));
            user.setF_USERID(cursor.getString(3));
            user.setF_CALLPHONE(cursor.getString(4));
            user.setF_USERNAME(cursor.getString(5));
            user.setF_DEPARTMENTNAME(cursor.getString(6));
            userList.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return userList;
    }

    // 判断users表中的是否包含某个UserID的记录
    public Boolean HaveUserInfo(String UserId) {
        Boolean b = false;
        Cursor cursor = db.query(SqliteHelper. TB_NAME, null, UserBook.USERID
                + "=?", new String[]{UserId}, null, null, null );
        b = cursor.moveToFirst();
        cursor.close();
        return b;
    }

    // 更新users表的记录
    public int UpdateUserInfo(UserBook user) {
        ContentValues values = new ContentValues();
        values.put(UserBook.PHONENAME,user.getF_POSITIONNAME());
        values.put(UserBook.USERTYPE,user.getF_CALLPHONETYPE());
        values.put(UserBook.USERID,user.getF_USERID());
        values.put(UserBook.CALLPHONE,user.getF_CALLPHONE());
        values.put(UserBook.USERNAME,user.getF_USERNAME());
        values.put(UserBook.USERTYPENAME,user.getF_DEPARTMENTNAME());
        int id = db.update(SqliteHelper.TB_NAME, values, UserBook.USERID + "="
                + user.getF_USERID(), null);
        return id;
    }

    // 添加users表的记录
    public Long SaveUserInfo(UserBook user) {
        ContentValues values = new ContentValues();
        values.put(UserBook.PHONENAME,user.getF_POSITIONNAME());
        values.put(UserBook.USERTYPE,user.getF_CALLPHONETYPE());
        values.put(UserBook.USERID,user.getF_USERID());
        values.put(UserBook.CALLPHONE,user.getF_CALLPHONE());
        values.put(UserBook.USERNAME,user.getF_USERNAME());
        values.put(UserBook.USERTYPENAME,user.getF_DEPARTMENTNAME());
        Long uid = db.insert(SqliteHelper. TB_NAME, UserBook.ID, values);
        return uid;
    }
    // 删除users表的记录
    public int DelUserInfo(String UserId) {
        int id = db.delete(SqliteHelper. TB_NAME,
                UserBook. USERID + "=?", new String[]{UserId});
        return id;
    }

    public static UserBook getUserByName(String userName,List<UserBook> userList){
        UserBook userInfo = null;
        int size = userList.size();
        for( int i=0;i<size;i++){
            if(userName.equals(userList.get(i).getF_USERNAME())){
                userInfo = userList.get(i);
                break;
            }
        }
        return userInfo;
    }
}
