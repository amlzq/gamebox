package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.gamebox_idtkown.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangkai on 16/10/17.
 */
public class NewGameHeaderView extends BaseView {

    @BindView(R.id.hot_game)
    public GBVGameListView gameListView;

    public NewGameHeaderView(Context context){
        super(context);
    }

    public static NewGameHeaderView getInstance(Context context, View view) {
        NewGameHeaderView newGameHeader = new NewGameHeaderView(context);
        ButterKnife.bind(newGameHeader, view);
        return newGameHeader ;
    }


    public NewGameHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_new_game_header;
    }
}
