package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.utils.ScreenUtil;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/10/8.
 */
public class GBMyItemView extends BaseView {

    @BindView(R.id.icon)
    ImageView ivIcon;

    @BindView(R.id.title)
    TextView tvTitle;

    @BindView(R.id.number)
    TextView tvNumber;

    @BindView(R.id.other)
    public TextView tvOther;

    @BindView(R.id.origration)
    ImageView arrow;

    public GBMyItemView(Context context){
        super(context);

    }

    public GBMyItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.tab_item);
        CharSequence title = a.getText(R.styleable.tab_item_text);
        if(title != null){
            tvTitle.setText(title);
        }
        Drawable iconSrc = a.getDrawable(R.styleable.tab_item_src);
        if (iconSrc != null) {
            ivIcon.setImageDrawable(iconSrc);
        }
    }

    public void setNumber(String number){
        tvNumber.setText(number);
    }

    public void setDesc(String title){
        tvOther.setText(title);
    }

    public void setButton(String title){
        hideArrow();
        Context context = getContext();

        StateListDrawable backgroundDrawable = new StateListDrawable();
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(GoagalInfo.getInItInfo().androidColor);
        drawable.setCornerRadius(ScreenUtil.dip2px(context, 1.5f));

        GradientDrawable pressDrawable = new GradientDrawable();
        pressDrawable.setColor(ContextCompat.getColor(context, R.color.btn_pressed));
        pressDrawable.setCornerRadius(ScreenUtil.dip2px(context, 1.5f));

        backgroundDrawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressDrawable);
        backgroundDrawable.addState(new int[]{android.R.attr.state_enabled}, drawable);
        tvOther.setBackground(backgroundDrawable);
        tvOther.setText(title);
        tvOther.setClickable(true);
        tvOther.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        tvOther.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tvOther.getLayoutParams();
        layoutParams.setMargins(0, 0, ScreenUtil.dip2px(context, 8), 0);
    }

    public void hideButton(){
        tvOther.setVisibility(View.GONE);
    }

    public void showNumber(){
        tvNumber.setVisibility(View.VISIBLE);
    }

    public void hideArrow(){
        arrow.setVisibility(View.GONE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_my_item;
    }
}
