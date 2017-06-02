package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.domain.GoagalInfo;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/9/22.
 */
public class GBTabBarItem extends BaseView {
    @BindView(R.id.icon)
    ImageView ivIcon;

    @BindView(R.id.title)
    TextView tvTitle;

    public GBTabBarItem(Context context, AttributeSet attrs) {
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



    @Override
    public int getLayoutId() {
        return R.layout.view_tabbar_item;
    }

    public void selected(Drawable src){
        ivIcon.setImageDrawable(src);
        ivIcon.setBackgroundColor(GoagalInfo.getInItInfo().androidColor);
        tvTitle.setTextColor(GoagalInfo.getInItInfo().androidColor);
    }

    public void normal(Drawable src){
        ivIcon.setImageDrawable(src);
        ivIcon.setBackgroundColor(Color.parseColor("#00000000"));
        tvTitle.setTextColor(Color.parseColor("#999999"));
    }

}
