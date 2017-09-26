package com.gamebox_idtkown.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.activitys.ChosenGamesActivity;
import com.gamebox_idtkown.cache.CacheConfig;
import com.gamebox_idtkown.cache.ChosenInfoCache;
import com.gamebox_idtkown.cache.GameInfoCache;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.db.greendao.ChosenInfo;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.ChosenEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.TaskUtil;
import com.gamebox_idtkown.views.adpaters.GBChosenAdapter;
import com.gamebox_idtkown.views.widgets.GBActionBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/9/22.
 * 精选
 */
public class ChosenFragment extends BaseGameListFragment<ChosenInfo, GBActionBar> {

    @BindView(R.id.gamelist)
    ListView gamelist;

    @Inject
    public ChosenFragment() {

    }

    GBChosenAdapter adapter = null;

    @Override
    public void loadData() {
        super.loadData();
        actionBar.setLogoWH(this);
        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                bindCache(adapter, new Runnable() {
                    @Override
                    public void run() {
                        final List<ChosenInfo> chosenInfos = ChosenInfoCache.getCache(getContext());
                        adapter.dataInfos = new ArrayList<GameInfo>();
                        bindView(new Runnable() {
                            @Override
                            public void run() {
                                for (ChosenInfo chosenInfo : chosenInfos) {
                                    GameInfo gameInfo = new GameInfo();
                                    gameInfo.chosenInfo = chosenInfo;
                                    adapter.dataInfos.add(gameInfo);
                                    chosenInfo.gameInfos = GameInfoCache.getCache(getContext(), CacheConfig
                                            .GAME_INFO_INDEX_CHOSEN + chosenInfo.getTid());
                                    if (chosenInfo.gameInfos == null) {
                                        continue;
                                    }
                                    for (int i = 0; i < chosenInfo.gameInfos.size(); i++) {
                                        if (i >= 6) {
                                            break;
                                        }
                                        GameInfo _gameInfo = chosenInfo.gameInfos.get(i);
                                        adapter.dataInfos.add(_gameInfo);
                                    }
                                }
                                notifyDataSetChanged();
                            }
                        });
                    }
                });
                getChosenList();
            }
        });
    }

    private void notifyDataSetChanged() {
        if (adapter.dataInfos.size() > 0) {
            removeNoView();
            removeProcessView();
            addFooterView();
            if (!loadMoreView.isHiden(adapter.dataInfos, page)) {
                loadMoreView.loaded();
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void getChosenList() {
        ChosenEngin.getImpl(getActivity()).getChosenList(new Callback<List<ChosenInfo>>() {
            @Override
            public void onSuccess(final ResultInfo<List<ChosenInfo>> resultInfo) {
                stop();
                bindView(new Runnable() {
                    @Override
                    public void run() {

                        if (resultInfo == null || resultInfo.data == null || resultInfo.data.size() == 0) {
                            showNoDataView();
                        } else {
                            adapter.dataInfos = new ArrayList<GameInfo>();
                            List<ChosenInfo> lists = resultInfo.data;
                            for (ChosenInfo chosenInfo : lists) {
                                GameInfo gameInfo = new GameInfo();
                                gameInfo.chosenInfo = chosenInfo;
                                adapter.dataInfos.add(gameInfo);
                                if (chosenInfo.gameInfos != null) {
                                    for (int i = 0; i < chosenInfo.gameInfos.size(); i++) {
                                        if (i >= 6) {
                                            break;
                                        }
                                        GameInfo _gameInfo = chosenInfo.gameInfos.get(i);
                                        adapter.dataInfos.add(_gameInfo);
                                    }
                                }
                            }
                            notifyDataSetChanged();
                        }
                    }
                });

                if (resultInfo != null && resultInfo.data != null && resultInfo.data.size() > 0) {
                    if (!cache) {
                        List<ChosenInfo> lists = resultInfo.data;
                        ChosenInfoCache.setCache(getContext(), lists);
                        for (ChosenInfo chosenInfo : lists) {
                            if (chosenInfo.gameInfos != null) {
                                GameInfoCache.setCache(getContext(), chosenInfo.gameInfos, CacheConfig
                                        .GAME_INFO_INDEX_CHOSEN +
                                        chosenInfo.getTid());
                            }
                        }
                        cache = true;
                        LogUtil.msg(ChosenFragment.this.getClass().getSimpleName() + "缓存方法setCache已运行");

                    }
                }

            }

            @Override
            public void onFailure(Response response) {
                fail2(adapter.dataInfos == null || adapter.dataInfos.size() == 0, getMessage(response.body, DescConstans
                        .NET_ERROR));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventInit2(Integer type) {
        if (type == EventBusMessage.RE_INIT) {
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_chosen;
    }

    @Override
    public void initViews() {
        super.initViews();
        actionBar.setTitle("精选游戏");
        adapter = new GBChosenAdapter(getContext());
        adapter.setListView(gamelist);
        adapter.setOnItemClickListener(new GBChosenAdapter.OnItemClickListener() {
            @Override
            public void onDetail(View view) {
                GameInfo gameInfo = (GameInfo) view.getTag();
                startGameDetailActivity(gameInfo);
            }

            @Override
            public void onDownload(TextView view) {
                final GameInfo gameInfo = (GameInfo) view.getTag();
                ApkStatusUtil.actionByStatus(getActivity(), gameInfo, view, new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onImage(View view) {
                ChosenInfo chosenInfo = (ChosenInfo) view.getTag();
                Intent intent = new Intent(getContext(), ChosenGamesActivity.class);
                intent.putExtra("chosen_info", chosenInfo);
                startActivity(intent);
            }


        });
        addHeaderAndFooter(gamelist, true);
        gamelist.setAdapter(adapter);
        removeFooterView();

        loadMoreView.setOtherHeight(100);


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer type) {
        if (type == EventBusMessage.REFRESH_INFO) {
            adapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Bitmap bitmap) {
        actionBar.setLogoWH(this);
    }

    @Override
    public void update(DownloadInfo downloadInfo) {
        adapter.updateView(downloadInfo);
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
