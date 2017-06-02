package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.gamebox_idtkown.utils.ScreenUtil;

/**
 * Created by zhangkai on 16/9/23.
 */
public class GBListView extends ListView {

    public int headHeight = 0;

    public int getTotalHeight() {
        ListAdapter listAdapter = getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        int count = listAdapter.getCount();

        for (int i = 0; i < count; i++) {
            int height = 0;

            View listItem = listAdapter.getView(i, null, this);
            try {
                listItem.measure(0, 0);
                height = listItem.getMeasuredHeight();
                if (i == 0) {
                    headHeight = height;
                }
            } catch (Exception e) {
                height = ScreenUtil.dip2px(this.getContext(), 95);
                e.printStackTrace();
            }
            totalHeight += height;
        }
        return totalHeight;
    }

    private static final int MAX_Y_OVERSCROLL_DISTANCE = 50;

    private Context mContext;
    private int mMaxYOverscrollDistance;

    public GBListView(Context context) {
        super(context);
        mContext = context;
        initBounceListView();
    }

    public GBListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initBounceListView();
    }

    public GBListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initBounceListView();
    }

    private void initBounceListView() {
        //get the density of the screen and do some maths with it on the max overscroll distance
        //variable so that you get similar behaviors no matter what the screen size

        final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        final float density = metrics.density;

        mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        //This is where the magic happens, we have replaced the incoming maxOverScrollY with our own custom variable mMaxYOverscrollDistance;
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);
    }

}
