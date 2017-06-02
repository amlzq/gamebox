package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.utils.ScreenUtil;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/9/30.
 */
public class GBImageTitleButton extends BaseView {

    @BindView(R.id.icon)
    ImageView ivIcon;

    @BindView(R.id.title)
    TextView tvTitle;

    public GBImageTitleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_image_title_button;
    }

    public void setIcon(Drawable drawable){
        ivIcon.setImageDrawable(drawable);
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }

    public void setColor(String color){
        Context context = getContext();

        StateListDrawable backgroundDrawable = new StateListDrawable();

        GradientDrawable drawable = new GradientDrawable();
        drawable.setStroke(ScreenUtil.dip2px(context, 1), Color.parseColor(color));
        drawable.setCornerRadius(ScreenUtil.dip2px(context, 12));

        GradientDrawable pressDrawable = new GradientDrawable();
        pressDrawable.setColor(ContextCompat.getColor(getContext(), R.color.translucent));
        pressDrawable.setCornerRadius(ScreenUtil.dip2px(context, 12));
        backgroundDrawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressDrawable);
        backgroundDrawable.addState(new int[]{android.R.attr.state_enabled}, drawable);
        this.setBackground(backgroundDrawable);
        tvTitle.setTextColor(Color.parseColor(color));
    }

}
