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
import com.gamebox_idtkown.engin.GameListEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.TaskUtil;
import com.gamebox_idtkown.views.adpaters.GBHGameListAdapter;
import com.gamebox_idtkown.views.widgets.GBActionBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/9/26.
 */
public class GameRankActivity extends BaseGameListActivity<GameInfo, GBActionBar> {
    @BindView(R.id.new_game)
    ListView newGameListView;

    private GBHGameListAdapter newAdapter = null;


    @Override
    public int getLayoutID() {
        return R.layout.activity_game_rank;
    }

    @Override
    public void initViews() {
        super.initViews();
        actionBar.setTitle("游戏排行");
        setBackListener();

        newAdapter = new GBHGameListAdapter(getBaseContext());
        newAdapter.setListView(newGameListView);
        newAdapter.setOnItemClickListener(new GBHGameListAdapter.OnItemClickListener() {
            @Override
            public void onDetail(View view) {
                GameInfo gameInfo = (GameInfo) view.getTag();
                startGameDetailActivity(gameInfo);
            }

            @Override
            public void onDownload(TextView view) {
                final GameInfo gameInfo = (GameInfo) view.getTag();
                ApkStatusUtil.actionByStatus(getBaseContext(), gameInfo, view, new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });

        addHeaderAndFooter(newGameListView, true);
        newGameListView.setAdapter(newAdapter);
        removeFooterView();

        addScrollListener(newGameListView);

    }


    @Override
    public void update(DownloadInfo downloadInfo) {
        newAdapter.updateView(downloadInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer type) {
        if(type == EventBusMessage.REFRESH_INFO) {
            newAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadData() {
        super.loadData();

        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                bindCache(newAdapter, new Runnable() {
                    @Override
                    public void run() {
                        List<GameInfo> newGameInfos = GameInfoCache.getCache(getBaseContext(), CacheConfig.GAME_INFO_RANK);
                        readCache(newAdapter, newGameInfos);
                    }
                });
                getTypeInfo();
            }
        });
    }

    private void getTypeInfo() {
        GameListEngin.getImpl(this).getTypeInfo(page, limit, "rank", new Callback<List<GameInfo>>() {
            @Override
            public void onSuccess(final ResultInfo<List<GameInfo>> resultInfo) {
                success(resultInfo, newAdapter, new Runnable() {
                    @Override
                    public void run() {
                        GameInfoCache.setCache(getBaseContext(), resultInfo.data, CacheConfig.GAME_INFO_RANK);
                    }
                });
            }

            @Override
            public void onFailure(Response response) {
                fail(newAdapter.dataInfos == null, getMessage(response.body, DescConstans.NET_ERROR));
            }
        });
    }

    @Override
    public void removeFooterView() {
        if (newGameListView.getFooterViewsCount() > 0) {
            newGameListView.removeFooterView(footerView);
        }
    }

    @Override
    public void addFooterView() {
        if (newGameListView.getFooterViewsCount() <= 0) {
            newGameListView.addFooterView(footerView);
        }
    }


}
