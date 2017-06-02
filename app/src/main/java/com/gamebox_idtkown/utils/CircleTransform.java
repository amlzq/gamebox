package com.gamebox_idtkown.utils;

/*
 * Copyright 2014 Julian Shen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

/**
 * Created by julian on 13/6/21.
 */
public class CircleTransform implements Transformation {

    private int mBorderWidth = 4;  //边框宽度
    private int mBorderColor = Color.TRANSPARENT;  //边框颜色

    public CircleTransform(){

    }

    public CircleTransform(int mBorderWidth, int mBorderColor){
        this.mBorderColor = mBorderColor;
        this.mBorderWidth = mBorderWidth;

    }

    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig() != null
                ? source.getConfig() : Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        //绘制圆形
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        //绘制边框
        Paint mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeCap(Paint.Cap.ROUND);
        mBorderPaint.setAntiAlias(true);

        //将边框和圆形画到canvas上
        float r = size / 2f;
        float r1 = (size-2*mBorderWidth) / 2f;
        canvas.drawCircle(r, r, r1, paint);
        canvas.drawCircle(r, r, r1, mBorderPaint);

        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "Circle";
    }
}