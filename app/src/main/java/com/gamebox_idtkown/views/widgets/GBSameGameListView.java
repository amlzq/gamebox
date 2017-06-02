package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.core.db.greendao.GameInfo;

/**
 * Created by zhangkai on 16/10/8.
 */
public class GBSameGameListView extends GBGameListView<GameInfo, GBTitleView2, GBGridView>  {

    public GBSameGameListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_gamelist;
    }

}
