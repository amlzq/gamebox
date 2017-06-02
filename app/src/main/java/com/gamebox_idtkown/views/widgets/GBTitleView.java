package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.domain.GoagalInfo;

/**
 * Created by zhangkai on 16/9/23.
 */
public class GBTitleView extends GBBaseTitleView {

    public GBTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ivIcon.setBackgroundColor(GoagalInfo.getInItInfo().androidColor);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.tab_item);
        CharSequence title = a.getText(R.styleable.tab_item_text);
        if(title != null){
            tvTitle.setText(title.toString());
        }
        Drawable iconSrc = a.getDrawable(R.styleable.tab_item_src);
        if (iconSrc != null) {
            ivIcon.setImageDrawable(iconSrc);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_title;

    }

}
