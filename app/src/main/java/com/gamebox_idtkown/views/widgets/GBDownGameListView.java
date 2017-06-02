package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;

/**
 * Created by zhangkai on 16/9/29.
 */
public class GBDownGameListView extends GBGameListView<DownloadInfo, GBTitleView2,
        GBGeneralListView> {
    public GBDownGameListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_down_game_list;
    }
}
