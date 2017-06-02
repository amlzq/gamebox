package com.gamebox_idtkown.activitys;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.SearchEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.adpaters.GBHGameListAdapter;
import com.gamebox_idtkown.views.widgets.GBActionBar2;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/9/28.
 */
public class Search2Activity extends BaseGameListActivity<GameInfo, GBActionBar2> {

    @BindView(R.id.gamelist)
    ListView gamelist;

    public String keyword = null;

    @Override
    public int getLayoutID() {
        return R.layout.activity_search2;
    }

    @Override
    public void initVars() {
        super.initVars();
        Intent intent = this.getIntent();
        if (intent != null) {
            keyword = intent.getStringExtra("keyword");
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        actionBar.setKeyWord(keyword);
        setBackListener();
        actionBar.setOnSearchListener(new GBActionBar2.OnSearchListener() {
            @Override
            public void onSearch(View view) {

                if (keyword.equals(actionBar.getKeyWord())) {
                    return;
                }

                keyword = actionBar.getKeyWord();

                if (keyword == null || keyword.isEmpty()) {
                    ToastUtil.toast(getBaseContext(), "关键字不能为空");
                    return;
                }

                view.setClickable(false);

                hideKeyboard();
                adapter.dataInfos = null;
                loadData();
            }
        });

        adapter = new GBHGameListAdapter(this);
        adapter.setListView(gamelist);
        addHeaderAndFooter(gamelist, true);
        gamelist.setAdapter(adapter);
        removeFooterView();

        adapter.setOnItemClickListener(new GBHGameListAdapter.OnItemClickListener() {
            @Override
            public void onDetail(View view) {
                final GameInfo gameInfo = (GameInfo) view.getTag();
                startGameDetailActivity(gameInfo);
            }

            @Override
            public void onDownload(TextView view) {
                final GameInfo gameInfo = (GameInfo) view.getTag();
                ApkStatusUtil.actionByStatus(Search2Activity.this, gameInfo, view, new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });

        addScrollListener(gamelist);

    }

    private GBHGameListAdapter adapter = null;

    @Override
    public void loadData() {
        super.loadData();
        SearchEngin.getImpl(this).getInfosByKeyword(page, limit, keyword, new Callback<List<GameInfo>>() {
            @Override
            public void onSuccess(final ResultInfo<List<GameInfo>> resultInfo) {
                success(resultInfo, adapter);
                setClickable(true);
            }

            @Override
            public void onFailure(Response response) {
                fail(adapter.dataInfos == null, getMessage(response.body, DescConstans.NET_ERROR));
                setClickable(true);
            }
        });
    }

    public void setClickable(final boolean flag) {
        bindView(new Runnable() {
            @Override
            public void run() {
                actionBar.setClickable(flag);
            }
        });
    }

    @Override
    public void update(DownloadInfo downloadInfo) {
        adapter.updateView(downloadInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer type) {
        if (type == EventBusMessage.REFRESH_INFO) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean ERROR() {
        if (keyword == null) {
            return true;
        }
        return false;
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
}
