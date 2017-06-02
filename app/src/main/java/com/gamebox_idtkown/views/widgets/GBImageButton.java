package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.gamebox_idtkown.R;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/9/22.
 */
public class GBImageButton extends BaseView {

    @BindView(R.id.icon)
    ImageView ivIcon;

    public GBImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.tab_item);
        Drawable iconSrc = a.getDrawable(R.styleable.tab_item_src);
        if (iconSrc != null) {
            ivIcon.setImageDrawable(iconSrc);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_image_button;
    }

    public void setIcon(Drawable drawable){
        ivIcon.setImageDrawable(drawable);
    }

    public void setIcon(Bitmap bitmap){
        if(bitmap != null) {
            ivIcon.setImageBitmap(bitmap);
        }
    }
}
