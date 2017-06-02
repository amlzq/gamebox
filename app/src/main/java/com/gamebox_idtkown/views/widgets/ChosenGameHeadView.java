package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.core.db.greendao.ChosenInfo;
import com.gamebox_idtkown.utils.CheckUtil;
import com.gamebox_idtkown.utils.RoundedTransformation;
import com.gamebox_idtkown.utils.ScreenUtil;
import com.gamebox_idtkown.utils.StateUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangkai on 16/10/18.
 */
public class ChosenGameHeadView extends BaseView {

    @BindView(R.id.icon)
    ImageView ivIcon;

    @BindView(R.id.desc)
    TextView tvDesc;

    public ChosenGameHeadView(Context context) {
        super(context);
    }

    public static ChosenGameHeadView getInstance(Context context, View view) {
        ChosenGameHeadView chosenGameHeadView = new ChosenGameHeadView(context);
        ButterKnife.bind(chosenGameHeadView, view);
        StateUtil.setDrawable(context, chosenGameHeadView.ivIcon, ScreenUtil.dip2px(context, 5), ContextCompat.getColor(context, R.color.gray));
        return chosenGameHeadView;
    }

    public void setHeaderInfo(ChosenInfo chosenInfo) {
        tvDesc.setText(CheckUtil.checkDesc(chosenInfo.getDesp()));
        Picasso.with(getContext()).load(chosenInfo.getImg()).transform(new RoundedTransformation(ScreenUtil.dip2px
                (getContext(), 5), 0)).into(ivIcon);
    }

    public ChosenGameHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_chosen_game_heaer;
    }
}


