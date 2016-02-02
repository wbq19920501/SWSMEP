package com.jokeep.swsmep.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by wbq501 on 2016-1-25 16:42.
 * SWSMEP
 */
public abstract class BaseFragment extends Fragment {

    public Activity context;
    public View fragment;
//    public FinalDb db;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
//        db = FinalDb.create(activity);
        init();
    }

    @Override
    public void onDestroy() {
        recycle();
        super.onDestroy();
    }

    protected void recycle() {
        context = null;
        fragment = null;
        System.gc();
    }

    public void doRecycle() {
        recycle();
    }

    /**
     * 实现fragment部分参数初始化
     */
    protected abstract void init();
}
