package com.zx.lockscreen.popmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * Created by liuyuchuan on 2018/8/20.
 */

public class PopMenuMoreListView extends ListView {

    public PopMenuMoreListView(Context context) {
        super(context);
    }

    public PopMenuMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopMenuMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), heightMeasureSpec);
            int w = child.getMeasuredWidth();
            if (w > width) width = w;
        }

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width + getPaddingLeft() + getPaddingRight(), MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
