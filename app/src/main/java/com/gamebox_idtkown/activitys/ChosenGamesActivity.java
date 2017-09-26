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
import com.gamebox_idtkown.core.db.greendao.ChosenInfo;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.ChosenGameEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.TaskUtil;
import com.gamebox_idtkown.views.adpaters.GBHGameListAdapter;
import com.gamebox_idtkown.views.widgets.ChosenGameHeadView;
import com.gamebox_idtkown.views.widgets.GBActionBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/10/18.
 */
public class ChosenGamesActivity extends BaseGameListActivity<GameInfo, GBActionBar> {
    @BindView(R.id.gamelist)
    ListView gameListView;

    private ChosenGameHeadView chosenGameHeadView = null;
    private GBHGameListAdapter adapter = null;
    private ChosenInfo chosenInfo;

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
    public void initVars() {
        super.initVars();
        Intent intent = this.getIntent();
        if (intent != null) {
            chosenInfo = (ChosenInfo) intent.getSerializableExtra("chosen_info");
        }
    }

    @Override
    public boolean ERROR() {
        if (chosenInfo == null) {
            return true;
        }
        return false;
    }

    @Override
    public void initViews() {

        super.initViews();
        setBackListener();
        actionBar.setTitle(chosenInfo.getName());

        headerView = this.getLayoutInflater().inflate(R.layout
                .view_chosen_game_heaer, null, false);
        chosenGameHeadView = ChosenGameHeadView.getInstance(this, headerView);
        chosenGameHeadView.setHeaderInfo(chosenInfo);

        adapter = new GBHGameListAdapter(this);
        adapter.setListView(gameListView);
        addHeaderAndFooter(gameListView, true);
        gameListView.setAdapter(adapter);
        removeFooterView();
        gameListView.removeHeaderView(headerView);

        adapter.setOnItemClickListener(new GBHGameListAdapter.OnItemClickListener() {
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

        addScrollListener(gameListView);
    }

    @Override
    public void loadData() {
        super.loadData();
        final int id = Integer.parseInt(chosenInfo.getTid());
        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                bindCache(adapter, new Runnable() {
                    @Override
                    public void run() {
                        List<GameInfo> gameInfos = GameInfoCache.getCache(getBaseContext(), CacheConfig.GAME_INFO_CHOSEN + id);
                        readCache(adapter, gameInfos);
                        addHeaderView(gameInfos);
                    }
                });
                getChosenGames(id);
            }
        });

    }


    private void addHeaderView(final List infos) {
        if (infos != null && infos.size() > 0) {
            bindView(new Runnable() {
                @Override
                public void run() {
                    if (infos != null && infos.size() > 0) {
                        if (gameListView.getHeaderViewsCount() <= 0) {
                            gameListView.addHeaderView(headerView);
                        }
                    }
                }
            });
        }

    }

    private void getChosenGames(final int id) {
        ChosenGameEngin.getImpl(this).getChosenGames(id, page, limit, new Callback<List<GameInfo>>() {
            @Override
            public void onSuccess(final ResultInfo<List<GameInfo>> resultInfo) {
                success(resultInfo, adapter, new Runnable() {
                    @Override
                    public void run() {
                        GameInfoCache.setCache(getBaseContext(), resultInfo.data, CacheConfig.GAME_INFO_CHOSEN + id);
                    }
                });
                if (resultInfo != null) {
                    addHeaderView(resultInfo.data);
                }
            }

            @Override
            public void onFailure(Response response) {
                fail(adapter.dataInfos == null, getMessage(response.body, DescConstans.NET_ERROR));
            }
        });
    }


    @Override
    public int getLayoutID() {
        return R.layout.activity_chosen_game;
    }

    @Override
    public void removeFooterView() {
        if (gameListView.getFooterViewsCount() > 0) {
            gameListView.removeFooterView(footerView);
        }
    }

    @Override
    public void addFooterView() {
        if (gameListView.getFooterViewsCount() <= 0) {
            gameListView.addFooterView(footerView);
        }
    }
}
