package com.gamebox_idtkown.views.widgets;

import android.content.Context;

import android.util.AttributeSet;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.domain.GoagalInfo;


/**
 * Created by zhangkai on 16/9/28.
 */
public class GBTitleView2 extends GBBaseTitleView {
    public GBTitleView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        ivIcon.setBackgroundColor(GoagalInfo.getInItInfo().androidColor);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_title2;
    }
}
