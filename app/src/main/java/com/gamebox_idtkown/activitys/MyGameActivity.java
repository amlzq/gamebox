package com.gamebox_idtkown.activitys;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.cache.CacheConfig;
import com.gamebox_idtkown.cache.GameInfoCache;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.MyGameEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.TaskUtil;
import com.gamebox_idtkown.views.adpaters.MyGameAdpater;
import com.gamebox_idtkown.views.widgets.GBActionBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/9/30.
 */
public class MyGameActivity extends BaseGameListActivity<GameInfo, GBActionBar> {
    @BindView(R.id.gamelist)
    ListView gamelist;

    MyGameAdpater adapter = null;

    @Override
    public int getLayoutID() {
        return R.layout.activity_mygame;
    }

    @Override
    public boolean isNeedLogin() {
        return true;
    }

    @Override
    public void initViews() {
        super.initViews();

        setBackListener();
        actionBar.setTitle("我的游戏");
        actionBar.hideMenuItem();

        adapter = new MyGameAdpater(this);
        adapter.setListView(gamelist);
        addHeaderAndFooter(gamelist, true);
        gamelist.setAdapter(adapter);
        removeFooterView();

        adapter.setOnItemClickListener(new MyGameAdpater.OnItemClickListener() {
            @Override
            public void onDetail(View view) {
                GameInfo gameInfo = (GameInfo) view.getTag();
                startGameDetailActivity(gameInfo);
            }

            @Override
            public void onDownload(TextView view) {
                final GameInfo gameInfo = (GameInfo) view.getTag();
                ApkStatusUtil.actionByStatus(MyGameActivity.this, gameInfo, view, new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        });

        addScrollListener(gamelist);

    }

    @Override
    public void loadData() {
        super.loadData();
        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                bindCache(adapter, new Runnable() {
                    @Override
                    public void run() {
                        List<GameInfo> newGameInfos = GameInfoCache.getCache(getBaseContext(), CacheConfig.GAME_INFO_MYGAME);
                        readCache(adapter, newGameInfos);
                    }
                });
                getMyGame();
            }
        });
    }

    private void getMyGame() {
        MyGameEngin.getImpl(this).getMyGame(page, limit, new Callback<List<GameInfo>>() {
            @Override
            public void onSuccess(final ResultInfo<List<GameInfo>> resultInfo) {
                success(resultInfo, adapter, new Runnable() {
                    @Override
                    public void run() {
                        GameInfoCache.setCache(getBaseContext(), resultInfo.data, CacheConfig.GAME_INFO_MYGAME);
                    }
                });
            }

            @Override
            public void onFailure(Response response) {
                fail(adapter.dataInfos == null, getMessage(response.body, DescConstans.NET_ERROR));
            }
        });
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
    public void update(DownloadInfo downloadInfo) {
        adapter.updateView(downloadInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer type) {
        if (type == EventBusMessage.REFRESH_INFO) {
            adapter.notifyDataSetChanged();
        }
    }
}
