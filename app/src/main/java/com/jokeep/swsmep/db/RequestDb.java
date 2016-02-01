package com.jokeep.swsmep.db;

import com.jokeep.swsmep.base.SaveMsg;
import com.jokeep.swsmep.model.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbq501 on 2016-1-27 11:23.
 * SWSMEP
 */
public class RequestDb {
    public static List<UserInfo> userInfoList = new ArrayList<UserInfo>();
    public static List<UserInfo.ResultEntity.UserInfoEntity> resultInfo = new ArrayList<UserInfo.ResultEntity.UserInfoEntity>();
    public static String errormsg;
    public static int UserInfo(String res, DbManager db){
        JSONObject user;
        JSONArray prime;
        JSONObject json;
        JSONObject object;
        int errorCode = 1;
        UserInfo.ResultEntity.UserInfoEntity userInfoEntity = null;
        UserInfo userInfo = new UserInfo();
        resultInfo.clear();
        resultInfo = new ArrayList<UserInfo.ResultEntity.UserInfoEntity>();
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("user").setDbVersion(1).setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                    }
                });
        db = x.getDb(daoConfig);
        try {
            json = new JSONObject(res);
            errorCode = Integer.parseInt(json.getString("ErrorCode"));
            switch (errorCode){
                case SaveMsg.successCode:
                    prime = (new JSONObject(json.getString("Result"))).getJSONArray("UserInfo");
                    for (int i=0;i<prime.length();i++){
                        object = prime.getJSONObject(i);
                        userInfoEntity = new UserInfo.ResultEntity.UserInfoEntity();
                        userInfoEntity.setF_USERID(object.getString("F_USERID").toString());
                        userInfoEntity.setF_USERNAME(object.getString("F_USERNAME").toString());
                        userInfoEntity.setF_MAINDEPARTID(object.getString("F_MAINDEPARTID").toString());
                        userInfoEntity.setF_MAINUNITID(object.getString("F_MAINUNITID").toString());
                        userInfoEntity.setF_POSITIONNAME(object.getString("F_POSITIONNAME").toString());
                        userInfoEntity.setF_DEPARTMENTNAME(object.getString("F_DEPARTMENTNAME").toString());
                        userInfoEntity.setF_USERHEADURI(object.getString("F_USERHEADURI").toString());
                        userInfoEntity.setF_INTERVAL(object.getString("F_INTERVAL").toString());
                        userInfoEntity.setF_PSDISPASSTIME(object.getInt("F_PSDISPASSTIME"));
//                        db.save(userInfoEntity);
                    }
                    resultInfo.add(userInfoEntity);
                    break;
                case SaveMsg.errorCode:
                    errormsg = json.getString("ErrorMsg").toString();
                    break;
                case SaveMsg.UPDATECODE:
                    break;
                case SaveMsg.UPDATEF:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return errorCode;
    };
    public static String ErrorMsg(){
        return errormsg;
    }
    public static List<UserInfo.ResultEntity.UserInfoEntity> ResultInfo(){
        return resultInfo;
    }
}
