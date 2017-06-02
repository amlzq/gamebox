package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by zhangkai on 2017/2/18.
 */

public class MarqueeTextView extends TextView {
    
    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused(){
        return true;
    }

}