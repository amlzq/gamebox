package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.kyleduo.switchbutton.SwitchButton;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/10/27.
 */
public class GBSettingItem extends BaseView {
    @BindView(R.id.g4_switch)
    public SwitchButton switchButton;

    @BindView(R.id.title)
    TextView tvTitle;

    @BindView(R.id.other)
    public TextView tvOther;

    @BindView(R.id.origration)
    ImageView arrow;

    public GBSettingItem(Context context) {
        super(context);

    }

    public GBSettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.tab_item);
        CharSequence title = a.getText(R.styleable.tab_item_text);
        if (title != null) {
            tvTitle.setText(title);
        }
    }


    public void setDesc(String title) {
        tvOther.setText(title);
    }

    public void hideArrow() {
        arrow.setVisibility(View.GONE);
    }


    public void showSwitch() {
        SpannableString ss = new SpannableString("");
        switchButton.setText(ss, "");
        switchButton.setTintColor(GoagalInfo.getInItInfo().androidColor);
        switchButton.setVisibility(View.VISIBLE);
        tvOther.setVisibility(View.GONE);
    }


    @Override
    public int getLayoutId() {
        return R.layout.view_setting_item;
    }
}
