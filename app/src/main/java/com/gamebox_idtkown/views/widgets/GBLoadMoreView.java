package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.utils.AnimationUtil;
import com.gamebox_idtkown.utils.ScreenUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/9/23.
 */
public class GBLoadMoreView extends BaseView {
    @BindView(R.id.load)
    RelativeLayout rlLoad;

    @BindView(R.id.icon)
    ImageView ivIcon;

    @BindView(R.id.title)
    TextView tvTitle;

    public static int LOADING = 0;
    public static int LOADED = 1;
    public static int MORE = 2;
    public int state = 1;

    public GBLoadMoreView(Context context) {
        super(context);
    }

    public static GBLoadMoreView getInstance(Context context, View view) {
        GBLoadMoreView loadMoreView = new GBLoadMoreView(context);
        ButterKnife.bind(loadMoreView, view);
        loadMoreView.tvTitle.setTextColor(GoagalInfo.getInItInfo().androidColor);
        loadMoreView.ivIcon.setVisibility(View.GONE);
        return loadMoreView;
    }

    public GBLoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ivIcon.setVisibility(View.GONE);
        tvTitle.setTextColor(GoagalInfo.getInItInfo().androidColor);
    }

    public void loading() {
        tvTitle.setText("");
        ivIcon.setVisibility(View.VISIBLE);
        ivIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.list_loading));
        ivIcon.startAnimation(AnimationUtil.rotaAnimation());
        rlLoad.setClickable(false);
        rlLoad.setBackground(null);
        state = LOADING;
    }

    public void more() {
        tvTitle.setTextColor(GoagalInfo.getInItInfo().androidColor);
        tvTitle.setText(getResources().getString(R.string.more2));
        ivIcon.setVisibility(View.GONE);
        ivIcon.clearAnimation();
        rlLoad.setClickable(true);
        rlLoad.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_bg_selector));
        state = MORE;
    }

    public void loaded() {
        tvTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_light));
        tvTitle.setText(getResources().getString(R.string.loaded));
        ivIcon.setVisibility(View.GONE);
        ivIcon.clearAnimation();
        rlLoad.setClickable(false);
        rlLoad.setBackground(null);
        state = LOADED;
    }

    public boolean isHiden(List datas, int page) {
        boolean flag = false;
        if (datas != null && page == 1) {
            if ((ScreenUtil.dip2px(getContext(), (datas.size() * itemHeight) + otherHeight) + statusBarHeight) <
                    ScreenUtil.getHeight
                            (getContext())) {
                flag = true;
                state = LOADED;
            }
        }
        return flag;
    }

    private int statusBarHeight = 0;
    private int otherHeight = 0;
    private int itemHeight = 95;

    public void setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
    }

    public void setOtherHeight(int otherHeight) {
        this.otherHeight = otherHeight;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_load_more;
    }

    @OnClick(R.id.load)
    public void onClick(View view) {
        if (onLoadingListener == null) throw new NullPointerException("onLoadingListener == null");
        this.loading();
        this.onLoadingListener.onLoading(view);
    }

    private OnLoadingListener onLoadingListener;

    public void setOnLoadingListener(OnLoadingListener onLoadingListener) {
        this.onLoadingListener = onLoadingListener;
    }

    public interface OnLoadingListener {
        void onLoading(View view);
    }
}
