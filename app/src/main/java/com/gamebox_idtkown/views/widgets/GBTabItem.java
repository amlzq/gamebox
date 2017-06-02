package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.domain.GoagalInfo;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/9/29.
 */
public class GBTabItem extends BaseView {
    @BindView(R.id.title)
    TextView titleView;

    @BindView(R.id.indicator)
    View indicator;

    public GBTabItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTitle(String title) {
        titleView.setText(title);

    }

    public void selected() {
        titleView.setTextColor(GoagalInfo.getInItInfo().androidColor);
        indicator.setBackgroundColor(GoagalInfo.getInItInfo().androidColor);
        indicator.setVisibility(View.VISIBLE);
    }

    public void cancel() {
        titleView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        indicator.setVisibility(View.GONE);

    }

    @Override
    public int getLayoutId() {
        return R.layout.view_tab_tem;
    }
}
