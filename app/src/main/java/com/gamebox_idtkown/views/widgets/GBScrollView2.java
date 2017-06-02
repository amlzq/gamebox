package com.gamebox_idtkown.views.widgets;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.gamebox_idtkown.utils.ScreenUtil;

/**
 * Created by zhangkai on 16/11/7.
 */
public class GBScrollView2 extends ScrollView {
    public GBScrollView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        detailHeight = ScreenUtil.dip2px(context, 95);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
    }


    private int detailHeight = 0;
    public int y = 0;
    public int y2 = 0;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int scrollY = this.getScrollY();
        y = this.getScrollY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                break;
            default:
                if (scrollY < detailHeight) {
                    if (y2 == 0) {
                        scroolToTop(detailHeight);
                        y2 = detailHeight;
                        y = detailHeight;
                    } else {
                        scroolToTop(0);
                        y2 = 0;
                        y = 0;
                    }
                } else {
                    y2 = detailHeight;
                }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }


    public void scroolToTop(final int ty) {
        ObjectAnimator yTranslate = ObjectAnimator.ofInt(this, "scrollY", ty);
        AnimatorSet animators = new AnimatorSet();
        animators.setDuration(300L);
        animators.play(yTranslate);
        animators.start();
    }
}
