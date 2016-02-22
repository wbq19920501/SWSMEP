package com.jokeep.swsmep.db;

import android.os.Environment;

import org.xutils.DbManager;

import java.io.File;

/**
 * Created by wbq501 on 2016-2-22 10:06.
 * SWSMEP
 */
public class MsgDb {
    static DbManager.DaoConfig daoConfig;
    public static DbManager.DaoConfig getDaoConfig(){
        if(daoConfig==null){
            daoConfig=new DbManager.DaoConfig()
                    .setDbName("Phoneman")
                    .setDbVersion(1)
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                        }
                    });
        }
        return daoConfig;
    }
}
