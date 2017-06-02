package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.util.AttributeSet;
import android.view.View;

import com.gamebox_idtkown.domain.GoagalInfo;

/**
 * Created by zhangkai on 16/9/29.
 */
public class GBTriangleView extends View {
    private Context mContext = null;

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(0, getHeight());
        path.lineTo(getWidth()/2, getHeight()/2);
        path.lineTo(0, 0);
        ShapeDrawable circle = new ShapeDrawable(new PathShape(path, getWidth(), getHeight()));
        circle.getPaint().setColor(GoagalInfo.getInItInfo().androidColor);
        circle.setBounds(0, 0, getWidth(), getHeight());
        circle.draw(canvas);
        super.onDraw(canvas);
    }

    public GBTriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

}
