package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.gamebox_idtkown.R;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/11/1.
 */
public class GBActionBar5 extends GBBaseActionBar {
    @BindView(R.id.title)
    TextView tvTitle;

    @BindView(R.id.item)
    TextView tvItem;

    @BindView(R.id.close)
    TextView tvClose;


    public GBActionBar5(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_actionbar5;
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void hideMenuItem() {
        tvItem.setVisibility(View.GONE);
    }

    public void showMenuItem() {
        tvItem.setVisibility(View.VISIBLE);
    }

    public void showMenuItem(String title) {
        showMenuItem();
        tvItem.setText(title);
    }

    public void showClose() {
        tvClose.setVisibility(View.VISIBLE);
    }

    public void setOnItemClickListener(OnItemClickListener _onItemClickListener) {
        this.onItemClickListener = _onItemClickListener;
        tvItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v);
            }
        });
        tvClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClose(v);
            }
        });

    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view);
        void onClose(View view);
    }

}