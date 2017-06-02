package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamebox_idtkown.R;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/9/28.
 */
public class GBActionBar3 extends GBBaseActionBar {

    @BindView(R.id.mygame)
    public RelativeLayout myGameBtn;

    @BindView(R.id.title)
    TextView tvTitle;



    public GBActionBar3(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_actionbar3;
    }


}
