package com.jokeep.swsmep.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by wbq501 on 2016-2-3 09:35.
 * SWSMEP
 */
public class SideBarListView extends ListView{
    public SideBarListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBarListView(Context context) {
        super(context);
    }

    public SideBarListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
