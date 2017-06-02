package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by zhangkai on 16/9/23.
 */
public class GBScrollView extends ScrollView {

    public GBScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
    }


    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }
}
