package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamebox_idtkown.R;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/9/28.
 */
public abstract class GBBaseTitleView extends BaseView{
    @BindView(R.id.icon)
    ImageView ivIcon;

    @BindView(R.id.title)
    TextView tvTitle;

    public GBBaseTitleView(Context context){
        super(context);

    }

    public GBBaseTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.tab_item);
        CharSequence title = a.getText(R.styleable.tab_item_text);
        if(title != null){
            setTitle(title.toString());
        }
    }

    public void setColor(String color){
        tvTitle.setTextColor(Color.parseColor(color));
    }
    public void setTitle(String title){
        tvTitle.setText(title);
    }

    public void setIcon(Drawable iconSrc){
        ivIcon.setImageDrawable(iconSrc);
    }
}
