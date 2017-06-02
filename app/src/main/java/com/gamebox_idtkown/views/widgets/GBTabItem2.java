package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.domain.GoagalInfo;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/11/3.
 */
public class GBTabItem2 extends BaseView {
    @BindView(R.id.title)
    TextView titleView;

    @BindView(R.id.indicator)
    View indicator;

    public GBTabItem2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTitle(String title) {
        titleView.setText(title);

    }

    public void selected() {
        indicator.setBackgroundColor(GoagalInfo.getInItInfo().androidColor);
        indicator.setVisibility(View.VISIBLE);
    }

    public void cancel() {
        indicator.setVisibility(View.GONE);

    }

    @Override
    public int getLayoutId() {
        return R.layout.view_tab_tem2;
    }
}
