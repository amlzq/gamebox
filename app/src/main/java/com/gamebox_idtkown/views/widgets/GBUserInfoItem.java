package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamebox_idtkown.R;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/10/27.
 */
public class GBUserInfoItem extends BaseView {
    @BindView(R.id.title)
    TextView tvTitle;

    @BindView(R.id.other)
    public TextView tvOther;

    @BindView(R.id.icon)
    public ImageView ivIcon;

    @BindView(R.id.origration)
    ImageView arrow;

    public GBUserInfoItem(Context context) {
        super(context);

    }

    public GBUserInfoItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.tab_item);
        CharSequence title = a.getText(R.styleable.tab_item_text);
        if (title != null) {
            tvTitle.setText(title);
        }
        Drawable iconSrc = a.getDrawable(R.styleable.tab_item_src);
        if (iconSrc != null) {
            ivIcon.setImageDrawable(iconSrc);
        }
    }


    public void setTitle(String title) {
        tvTitle.setText(title);
    }
    public void setDesc(String title) {
        tvOther.setText(title);
    }

    public void hideArrow() {
        arrow.setVisibility(View.GONE);
    }

    public void showIcon(){
        ivIcon.setVisibility(View.VISIBLE);
        tvOther.setVisibility(View.GONE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_userinfo_item;
    }
}