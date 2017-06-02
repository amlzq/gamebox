package com.gamebox_idtkown.activitys;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.views.widgets.GBActionBar;
import com.gamebox_idtkown.views.widgets.GBBaseActionBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/10/8.
 */
public abstract class BaseActionBarActivity<T extends GBBaseActionBar> extends BaseActivity {
    @Nullable
    @BindView(R.id.actionbar)
    public T actionBar;

    public void setBackListener() {
        actionBar.setBackListener(new GBActionBar.OnBackListener() {
            @Override
            public void onBack(View view) {
                finish();
            }
        });
    }

    @Override
    public void initViews() {
        super.initViews();
        setActionBar();
    }

    public void setActionBar() {
        //actionBar.setLogo(GoagalInfo.getInItInfo().logoBitmp);
        actionBar.setBackgroundColor(GoagalInfo.getInItInfo().androidColor);
        actionBar.setOnActionBarItemClickListener(new GBBaseActionBar.OnActionBarItemClickListener() {
            @Override
            public void onSearchClick(View view) {
                Intent intent = new Intent(getBaseContext(), SearchActivity.class);
                startActivity(intent);
            }

            @Override
            public void onDownloadClick(View view) {
                Intent intent = new Intent(getBaseContext(), DownloadActivity.class);
                startActivity(intent);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventInit222(Integer type) {
        if (type == EventBusMessage.RE_INIT){
            setActionBar();
            setRefreshLayout();
            loadData();
        }
    }
}
