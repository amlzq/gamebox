package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.core.db.greendao.GameInfo;

import butterknife.OnClick;

/**
 * Created by zhangkai on 16/9/22.
 */
public class GBVGameListView extends GBGameListView<GameInfo, GBTitleView, GBGridView> {

    public GBVGameListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_v_gamelist;
    }

    @OnClick(R.id.more)
    public void onClick(View view){
        onMoreBtnClickListener.onClick(view);
    }

    private OnMoreBtnClickListener onMoreBtnClickListener;
    public void setOnMoreBtnClickListener(OnMoreBtnClickListener onMoreBtnClickListener){
        this.onMoreBtnClickListener = onMoreBtnClickListener;
    }
    public interface OnMoreBtnClickListener {
        void onClick(View view);
    }


}
