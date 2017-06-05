package com.gamebox_idtkown.activitys;

import android.widget.ListView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.IntegralDetailInfo;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.IntegralDetailEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.views.adpaters.IntegralDetailAdapter;
import com.gamebox_idtkown.views.widgets.GBActionBar;
import com.gamebox_idtkown.views.widgets.GBActionBar5;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/6/5.
 */

public class IntegralDetailsActivity extends BaseGameListActivity<IntegralDetailInfo, GBActionBar> {
    @BindView(R.id.gamelist)
    ListView gamelist;

    IntegralDetailEngin integralDetailEngin;

    IntegralDetailAdapter adapter;

    @Override
    public int getLayoutID() {
        return R.layout.activity_integral_detail;
    }

    @Override
    public void initVars() {
        super.initVars();
        integralDetailEngin = new IntegralDetailEngin();
    }

    @Override
    public void initViews() {
        super.initViews();
        setBackListener();
        actionBar.setTitle("积分明细");
        actionBar.hideMenuItem();

        adapter = new IntegralDetailAdapter(this);
        adapter.setListView(gamelist);
        addHeaderAndFooter(gamelist, true);
        gamelist.setAdapter(adapter);
        removeFooterView();

        loadMoreView.setItemHeight(80);
    }

    @Override
    public void removeFooterView() {
        if (gamelist.getFooterViewsCount() > 0) {
            gamelist.removeFooterView(footerView);
        }
    }

    @Override
    public void addFooterView() {
        if (gamelist.getFooterViewsCount() <= 0) {
            gamelist.addFooterView(footerView);
        }
    }

    @Override
    public void loadData() {
        super.loadData();
        getIntegralDetail();
    }

    private void getIntegralDetail() {
        integralDetailEngin.getList(page, limit, new Callback<List<IntegralDetailInfo>>() {
            @Override
            public void onSuccess(final ResultInfo<List<IntegralDetailInfo>> resultInfo) {
                success(resultInfo, adapter);
            }

            @Override
            public void onFailure(Response response) {
                fail(adapter.dataInfos == null, getMessage(response.body, DescConstans.NET_ERROR));
            }
        });
    }

    @Override
    public void update(DownloadInfo downloadInfo) {

    }
}
