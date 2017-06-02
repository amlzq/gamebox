package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.gamebox_idtkown.utils.ScreenUtil;

/**
 * Created by zhangkai on 16/9/30.
 */
public class GBGeneralListView extends ListView {

    public GBGeneralListView(Context context) {
        super(context);
    }

    public GBGeneralListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GBGeneralListView(Context context, AttributeSet attrs,
                      int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setHeight() {
        ListAdapter listAdapter = getAdapter();
        if (listAdapter == null) {
            return;

        }
        int totalHeight = 0;
        int count = listAdapter.getCount();

        for(int i=0; i < count; i++){
            View listItem = listAdapter.getView(i, null, this);
            try {
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }catch (Exception e){
                totalHeight += ScreenUtil.dip2px(getContext(), 84);
            }
        }
        ViewGroup.LayoutParams params = this.getLayoutParams();

        params.height = totalHeight
                + (this.getDividerHeight() * (listAdapter.getCount() - 1));

        this.setLayoutParams(params);

    }
}
