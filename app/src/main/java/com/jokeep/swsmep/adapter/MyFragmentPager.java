package com.jokeep.swsmep.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by wbq501 on 2016-1-25 17:22.
 * SWSMEP
 */
public class MyFragmentPager extends FragmentPagerAdapter {
    ArrayList<Fragment> list;
    public MyFragmentPager(FragmentManager fm, ArrayList<Fragment> fragmentList) {
        super(fm);
        this.list = fragmentList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }
}
