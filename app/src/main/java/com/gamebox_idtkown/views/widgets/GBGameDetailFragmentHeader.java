package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.gamebox_idtkown.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangkai on 16/11/3.
 */
public class GBGameDetailFragmentHeader extends BaseView {
    @BindView(R.id.game_detail_ppt)
    public RecyclerView mRecyclerView;

    @BindView(R.id.detail)
    public TextView tvDetail;

    @BindView(R.id.version)
    public TextView tvVersion;

    @BindView(R.id.time)
    public TextView tvTime;

    @BindView(R.id.open)
    public TextView btnOpen;

    public GBGameDetailFragmentHeader(Context context){
        super(context);
    }
    public static GBGameDetailFragmentHeader getInstance(Context context, View view) {
        GBGameDetailFragmentHeader gbGameDetailFragmentHeader = new GBGameDetailFragmentHeader(context);
        ButterKnife.bind(gbGameDetailFragmentHeader, view);

        return gbGameDetailFragmentHeader;
    }

    public GBGameDetailFragmentHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_game_detail_fragment_header;
    }

}
