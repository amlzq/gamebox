package com.gamebox_idtkown.activitys;

import android.widget.ListView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.di.dagger2.components.DaggerEnginComponent;
import com.gamebox_idtkown.domain.PayRecordInfo;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.PayRecordEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.views.adpaters.PayRecordAdpater;
import com.gamebox_idtkown.views.widgets.GBActionBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/2/6.
 */

public class PayRecordActivity extends BaseGameListActivity<PayRecordInfo, GBActionBar> {

    @BindView(R.id.gamelist)
    ListView gamelist;

    @Inject
    PayRecordEngin payRecordEngin;

    PayRecordAdpater adapter;

    @Override
    public int getLayoutID() {
        return R.layout.activity_pay_record;
    }

    @Override
    public void initVars() {
        super.initVars();

        DaggerEnginComponent.create().injectPayRecord(this);
    }

    @Override
    public void initViews() {
        super.initViews();
        setBackListener();
        actionBar.setTitle("充值记录");
        actionBar.hideMenuItem();

        adapter = new PayRecordAdpater(this);
        adapter.setListView(gamelist);
        loadMoreView.setItemHeight(108);
        addHeaderAndFooter(gamelist, true);
        gamelist.setAdapter(adapter);
        removeFooterView();
        addScrollListener(gamelist);

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
        getPayRecord();
    }

    private void getPayRecord() {
        payRecordEngin.getPayRecord(page, limit, new Callback<List<PayRecordInfo>>() {
            @Override
            public void onSuccess(final ResultInfo<List<PayRecordInfo>> resultInfo) {
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
