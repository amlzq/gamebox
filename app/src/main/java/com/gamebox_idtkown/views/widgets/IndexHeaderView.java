package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.utils.StateUtil;
import com.youth.banner.Banner;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/10/15.
 */
public class IndexHeaderView extends BaseView {
    @BindView(R.id.banner)
    public Banner banner;

    @BindView(R.id.category)
    GBButton categoryBtn;

    @BindView(R.id.newgame)
    GBButton newgameBtn;

    @BindView(R.id.btgame)
    GBButton btgameBtn;

    @BindView(R.id.gamerank)
    GBButton gamerankBtn;

    @BindView(R.id.eran_point)
    GBButton eranpointBtn;

    @BindView(R.id.menu)
    public LinearLayout llMenu;

    public IndexHeaderView(Context context){
        super(context);
    }

    public static IndexHeaderView getInstance(Context context, View view) {
        IndexHeaderView indexHeaderView = new IndexHeaderView(context);
        ButterKnife.bind(indexHeaderView, view);
        indexHeaderView.categoryBtn.setTag(0);
        indexHeaderView.newgameBtn.setTag(1);
        indexHeaderView.btgameBtn.setTag(2);
        indexHeaderView.gamerankBtn.setTag(3);
        indexHeaderView.eranpointBtn.setTag(4);


        StateUtil.setRipple(indexHeaderView.categoryBtn);
        StateUtil.setRipple(indexHeaderView.newgameBtn);
        StateUtil.setRipple(indexHeaderView.btgameBtn);
        StateUtil.setRipple(indexHeaderView.gamerankBtn);
        StateUtil.setRipple(indexHeaderView.eranpointBtn);
        return indexHeaderView;
    }


    public IndexHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_index_header;
    }

    @OnClick({R.id.category,R.id.btgame, R.id.eran_point, R.id.newgame, R.id.gamerank})
    public void onClick(View view){
        onItemClickListener.onClick(view);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    private IndexHeaderView.OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onClick(View view);
    }
}
