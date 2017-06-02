package com.gamebox_idtkown.activitys;

import android.content.Intent;
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
import com.gamebox_idtkown.core.db.greendao.GameType;
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
 * Created by zhangkai on 16/9/28.
 */
public class GameCategoryDetailActivity extends BaseGameListActivity<GameInfo, GBActionBar> {

    @BindView(R.id.gamelist)
    ListView gamelist;

    private GameType gameType = null;

    @Override
    public int getLayoutID() {
        return R.layout.activity_game_category_detail;
    }

    private GBHGameListAdapter adapter = null;

    @Override
    public void initVars() {
        super.initVars();
        Intent intent = this.getIntent();
        if (intent != null) {
            gameType = (GameType) intent.getSerializableExtra("game_type");

        }
    }

    @Override
    public boolean ERROR() {
        if (gameType == null) {
            return true;
        }
        return false;
    }

    @Override
    public void initViews() {
        super.initViews();

        setBackListener();
        actionBar.setTitle(gameType.getName());
        adapter = new GBHGameListAdapter(this);
        adapter.setListView(gamelist);

        addHeaderAndFooter(gamelist, true);
        gamelist.setAdapter(adapter);
        removeFooterView();

        adapter.setOnItemClickListener(new GBHGameListAdapter.OnItemClickListener() {
            @Override
            public void onDetail(View view) {
                GameInfo gameInfo = (GameInfo) view.getTag();
                startGameDetailActivity(gameInfo);
            }

            @Override
            public void onDownload(TextView view) {
                GameInfo gameInfo = (GameInfo) view.getTag();
                ApkStatusUtil.actionByStatus(GameCategoryDetailActivity.this, gameInfo, (TextView) view, new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });

        addScrollListener(gamelist);
    }

    @Override
    public void update(DownloadInfo downloadInfo) {
        adapter.updateView(downloadInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer type) {
        if(type == EventBusMessage.REFRESH_INFO) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadData() {
        super.loadData();
        final int tid = Integer.parseInt(gameType.getTid());

        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                bindCache(adapter, new Runnable() {
                    @Override
                    public void run() {
                        List<GameInfo> gameInfos = GameInfoCache.getCache(getBaseContext(), CacheConfig.GAME_INFO_CATEGORY + tid);
                        readCache(adapter, gameInfos);
                    }
                });
                getCateInfo(tid);
            }
        });
    }

    private void getCateInfo(final int tid) {
        GameListEngin.getImpl(this).getCateInfo(page, limit, tid, new Callback<List<GameInfo>>() {
            @Override
            public void onSuccess(final ResultInfo<List<GameInfo>> resultInfo) {
                success(resultInfo, adapter, new Runnable() {
                    @Override
                    public void run() {
                        GameInfoCache.setCache(getBaseContext(), resultInfo.data, CacheConfig.GAME_INFO_CATEGORY + tid);
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
}
