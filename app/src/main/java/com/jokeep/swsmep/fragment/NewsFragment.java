package com.jokeep.swsmep.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.BaseFragment;

/**
 * Created by wbq501 on 2016-1-25 17:01.
 * SWSMEP
 */
public class NewsFragment extends BaseFragment{
    View fragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragment == null){
            fragment = inflater.inflate(R.layout.news_fragment,container,false);
        }else {
            // 缓存的rootView需要判断是否已经被加过parent
            // 如果有parent需要从parent删除
            ViewGroup parent = (ViewGroup) fragment.getParent();
            if (parent != null) {
                parent.removeView(fragment);
            }
        }
        return fragment;
    }

    @Override
    protected void init() {

    }
}
