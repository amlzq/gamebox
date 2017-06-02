package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.gamebox_idtkown.utils.LogUtil;

import butterknife.ButterKnife;


/**
 * Created by zhangkai on 16/9/14.
 */
public abstract class BaseView extends RelativeLayout {
    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public void initViews(Context context){
        try {
            inflate(context, getLayoutId(), this);
            ButterKnife.bind(this);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.msg("baseView初始化失败->"+e);
        }
    }

    public abstract int getLayoutId();
}
