package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.utils.ScreenUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/9/28.
 */
public abstract class GBBaseActionBar extends BaseView {
    @Nullable
    @BindView(R.id.logo)
    GBImageButton btnLogo;

    public GBBaseActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (btnLogo != null) {
            btnLogo.setTag(0);
        }
    }

    private OnBackListener onBackListener;

    public interface OnBackListener {
        void onBack(View view);

    }

    public void setBackListener(OnBackListener _onBackListener) {
        this.onBackListener = _onBackListener;
        setLogo(getDrawable(R.mipmap.back));
        if (btnLogo != null) {
            btnLogo.setClickable(true);
            btnLogo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackListener.onBack(v);
                }
            });
        }
    }

    protected Drawable getDrawable(int id) {
        return ContextCompat.getDrawable(getContext(), id);
    }

    public void setLogo(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        if (btnLogo != null) {
            btnLogo.setIcon(bitmap);
        }
    }

    public void setAvatarOnClickListner(final Runnable runnable) {
        btnLogo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                runnable.run();
            }
        });
    }



    public void setAvatar(Bitmap bitmap) {
        final int w = ScreenUtil.dip2px(getContext(), 30);

        if (bitmap == null) {
            Picasso.with(getContext()).load(R.mipmap.avatar_default).resize(w, w).into(btnLogo
                    .ivIcon);
            return;
        }

        btnLogo.ivIcon.setImageBitmap(bitmap);
    }

    public void setLogo(Drawable drawable) {
        if (btnLogo != null) {
            btnLogo.setIcon(drawable);
        }
    }

    protected OnActionBarItemClickListener onActionBarItemClickListener;

    public interface OnActionBarItemClickListener {
        void onSearchClick(View view);
        void onDownloadClick(View view);
    }

    public void setOnActionBarItemClickListener(OnActionBarItemClickListener _onActionBarItemClickListener) {

    }
}
