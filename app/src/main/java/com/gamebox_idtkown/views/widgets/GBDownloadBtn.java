package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.utils.ApkStatusUtil;

/**
 * Created by zhangkai on 16/9/29.
 */
public class GBDownloadBtn extends TextView {
    public GBDownloadBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        this.setGravity(Gravity.CENTER);
        ApkStatusUtil.enableButtonState(context, this, GoagalInfo.getInItInfo().androidColor);
    }
}


