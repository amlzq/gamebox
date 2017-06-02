package com.gamebox_idtkown.fragment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.activitys.DownloadActivity;
import com.gamebox_idtkown.activitys.SearchActivity;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.utils.StatusBarUtil;
import com.gamebox_idtkown.views.widgets.GBBaseActionBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/10/8.
 */
public abstract class BaseActionBarFragment<T extends GBBaseActionBar> extends BaseFragment {
    @Nullable
    @BindView(R.id.actionbar)
    public T actionBar;

    @Override
    public void initViews() {
        super.initViews();
        setActionBar();
    }

    public void setActionBar() {
        if (actionBar != null) {
            //actionBar.setLogo(GoagalInfo.getInItInfo().logoBitmp);
            actionBar.setBackgroundColor(GoagalInfo.getInItInfo().androidColor);
            actionBar.setOnActionBarItemClickListener(new GBBaseActionBar.OnActionBarItemClickListener() {
                @Override
                public void onSearchClick(View view) {
                    Intent intent = new Intent(getContext(), SearchActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onDownloadClick(View view) {
                    Intent intent = new Intent(getContext(), DownloadActivity.class);
                    startActivity(intent);
                }
            });
        }
        StatusBarUtil.setColor(getActivity(), GoagalInfo.getInItInfo().androidColor);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventInit(Integer type) {
        if (type == EventBusMessage.RE_INIT){
            setActionBar();
            setRefreshLayout();
            loadData();
        }
    }
}
