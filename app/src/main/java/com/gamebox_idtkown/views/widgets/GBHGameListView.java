package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.core.db.greendao.GameInfo;

/**
 * Created by zhangkai on 16/9/22.
 */
public class GBHGameListView extends GBGameListView<GameInfo, GBTitleView, GBListView> {
    public GBHGameListView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public int getLayoutId() {
        return R.layout.view_h_gamelist;
    }

}
