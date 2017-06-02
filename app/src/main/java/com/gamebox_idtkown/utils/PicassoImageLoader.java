package com.gamebox_idtkown.utils;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by zhangkai on 17/1/13.
 */

public class PicassoImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Picasso.with(context).load((String)path).into(imageView);
    }
}
