package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.core.db.greendao.GameInfo;

/**
 * Created by zhangkai on 16/9/28.
 */
public class GBTGridView extends GBGameListView<GameInfo, GBTitleView2, GBGridView> {

    public GBTGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_tgrid_view;
    }

    public void setColumns(int n){
        gamelist.setNumColumns(n);
    }

    private int dH = 40;
    public void setHeight(int height){
        dH = height;
    }
}
