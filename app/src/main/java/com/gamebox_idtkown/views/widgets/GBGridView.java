package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.gamebox_idtkown.utils.ScreenUtil;

import java.lang.reflect.Field;

/**
 * Created by zhangkai on 16/9/23.
 */
public class GBGridView extends GridView {

    private int height = 0;

    public GBGridView(Context context) {
        super(context);
    }

    public GBGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GBGridView(Context context, AttributeSet attrs,
                      int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setHeight(int h) {
        ListAdapter listAdapter = getAdapter();
        if (listAdapter == null) {
            return;
        }

        int columns = 4;
        int totalHeight = 0;
        int count = listAdapter.getCount();
        Class<?> clazz = GridView.class;
        try {
            //利用反射，取得每行显示的个数
            Field column = clazz.getDeclaredField("mRequestedNumColumns");
            column.setAccessible(true);
            columns = (Integer) column.get(this);

            //利用反射，取得横向分割线高度
            Field horizontalSpacing = clazz.getDeclaredField("mRequestedHorizontalSpacing");
            horizontalSpacing.setAccessible(true);
            totalHeight += (Integer) horizontalSpacing.get(this);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        if (count > 0 && height == 0) {
            View listItem = listAdapter.getView(0, null, this);

            try {
                listItem.measure(0, 0);
                height = listItem.getMeasuredHeight();
            } catch (Exception e) {
                height = ScreenUtil.dip2px(getContext(), h);
                e.printStackTrace();
            }
        }
        totalHeight += height * (count / columns + (count % columns > 0 ? 1 : 0)) + ScreenUtil.dip2px(getContext(),
                this.getVerticalSpacing());

        ViewGroup.LayoutParams params = this.getLayoutParams() ;


        params.height = totalHeight;

        this.setLayoutParams(params);

    }

}
