package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamebox_idtkown.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangkai on 16/10/18.
 */
public class GBNoView extends BaseView {
    @BindView(R.id.icon)
    ImageView ivIcon;

    @BindView(R.id.title)
    TextView tvTitle;

    @BindView(R.id.rlItem)
    public RelativeLayout rlItem;

    public GBNoView(Context context){
        super(context);
    }

    public static GBNoView getInstance(Context context, View view) {
        GBNoView gbnoView = new GBNoView(context);
        ButterKnife.bind(gbnoView, view);
        return gbnoView ;
    }

    public void setNoDataView(String data){
        ivIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_empty));
        tvTitle.setText(data);
    }

    public void setNoNetView(String data){
        ivIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_no_network));
        tvTitle.setText(data);
    }

    public GBNoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_no;
    }
}
