package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.gamebox_idtkown.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangkai on 16/10/18.
 */
public class RankGameHeaderView extends BaseView{
    @BindView(R.id.hot_game)
    public GBVGameListView gameListView;

    public RankGameHeaderView(Context context){
        super(context);
    }

    public static RankGameHeaderView getInstance(Context context, View view) {
        RankGameHeaderView rankGameHeaderView = new RankGameHeaderView(context);
        ButterKnife.bind(rankGameHeaderView, view);
        return rankGameHeaderView ;
    }


    public RankGameHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_rank_game_header;
    }
}
