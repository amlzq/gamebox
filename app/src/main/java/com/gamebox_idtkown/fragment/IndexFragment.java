package com.gamebox_idtkown.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.activitys.EarnPointAcitivty;
import com.gamebox_idtkown.activitys.GameCategoryActivity;
import com.gamebox_idtkown.activitys.GameCategoryDetailActivity;
import com.gamebox_idtkown.activitys.GameOpenServiceActivity;
import com.gamebox_idtkown.activitys.GamePreOpenServiceActivity;
import com.gamebox_idtkown.activitys.GameRankActivity;
import com.gamebox_idtkown.activitys.NewGameActivity;
import com.gamebox_idtkown.cache.CacheConfig;
import com.gamebox_idtkown.cache.GameInfoCache;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.core.db.greendao.GameType;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.di.dagger2.components.DaggerEnginComponent;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.domain.SlideInfo;
import com.gamebox_idtkown.engin.GameListEngin;
import com.gamebox_idtkown.engin.SlideEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.LoadingUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.PicassoImageLoader;
import com.gamebox_idtkown.utils.ScreenUtil;
import com.gamebox_idtkown.utils.StateUtil;
import com.gamebox_idtkown.utils.TaskUtil;
import com.gamebox_idtkown.views.adpaters.GBHGameListAdapter;
import com.gamebox_idtkown.views.widgets.GBActionBar;
import com.gamebox_idtkown.views.widgets.GBButton;
import com.gamebox_idtkown.views.widgets.IndexHeaderView;
import com.youth.banner.listener.OnBannerClickListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by zhangkai on 16/9/22.
 */
public class IndexFragment extends BaseGameListFragment<GameInfo, GBActionBar> {
    @BindView(R.id.new_game)
    ListView newGameListView;

    @BindView(R.id.menu)
    RelativeLayout llMenu;

    @BindView(R.id.category)
    GBButton categoryBtn;

    @BindView(R.id.newgame)
    GBButton newgameBtn;

    @BindView(R.id.gamerank)
    GBButton gamerankBtn;

    @BindView(R.id.btgame)
    GBButton btgameBtn;

    @BindView(R.id.eran_point)
    GBButton eranpointBtn;

    @Inject
    SlideEngin slideEngin;

    @Inject
    public IndexFragment() {
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragement_index;
    }

    private GBHGameListAdapter newAdapter = null;

    private IndexHeaderView indexHeaderView;

    @Override
    public void initVars() {
        super.initVars();
        DaggerEnginComponent.create().injectSlide(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventInit2(Integer type) {
        if (type == EventBusMessage.RE_INIT) {
            setActionBar();
            actionBar.setTitle(GoagalInfo.getInItInfo().title);
            newAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        actionBar.setTitle(GoagalInfo.getInItInfo().title);
        actionBar.setLogoWH(this);

        headerView = getActivity().getLayoutInflater().inflate(R.layout
                .view_index_header, null, false);
        indexHeaderView = IndexHeaderView.getInstance(getContext(), headerView);
        indexHeaderView.banner.setImageLoader(new PicassoImageLoader());
        indexHeaderView.banner.setDelayTime(3000);
        indexHeaderView.banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                try {
                    SlideInfo slideInfo = slideInfos.get(position - 1);
                    if (slideInfo.getType().equals("0")) {
                        GameInfo gameInfo = new GameInfo();
                        gameInfo.setGameId(slideInfo.getGameId());
                        gameInfo.setType(slideInfo.getGameType());
                        startGameDetailActivity(gameInfo);
                    } else if (slideInfo.getType().equals("1")) {
                        String url = slideInfo.getTypeValue();
                        if (!url.contains("http://")) {
                            url = "http://" + url;
                        }
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    LogUtil.msg("banner click->" + e);
                }
            }
        });


        newAdapter = new GBHGameListAdapter(getActivity());
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
                ApkStatusUtil.actionByStatus(getActivity(), gameInfo, view, new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });

        addHeaderAndFooter(newGameListView, true);
        newGameListView.setAdapter(newAdapter);
        removeFooterView();
        newGameListView.removeHeaderView(headerView);

        loadMoreView.setOtherHeight(100 + 290);

        categoryBtn.setTag(0);
        newgameBtn.setTag(1);
        btgameBtn.setTag(2);
        gamerankBtn.setTag(3);
        eranpointBtn.setTag(4);

        StateUtil.setRipple(categoryBtn);
        StateUtil.setRipple(newgameBtn);
        StateUtil.setRipple(btgameBtn);
        StateUtil.setRipple(gamerankBtn);
        StateUtil.setRipple(eranpointBtn);

        //IndexHeaderView 事件监听
        indexHeaderView.setOnItemClickListener(new IndexHeaderView.OnItemClickListener() {
            @Override
            public void onClick(View view) {
                menuClick(view);
            }
        });


        final int height = ScreenUtil.dip2px(getContext(), 210);

        newGameListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {

                    View childView = newGameListView.getChildAt(0);
                    if (childView == null) {
                        return;
                    }
                    int y = childView.getTop();
                    if (-y >= height) {
                        indexHeaderView.llMenu.setAlpha(0);
                        llMenu.setVisibility(View.VISIBLE);
                    } else {
                        indexHeaderView.llMenu.setAlpha(1);
                        llMenu.setVisibility(View.GONE);
                    }
                } else {
                    indexHeaderView.llMenu.setAlpha(0);
                    llMenu.setVisibility(View.VISIBLE);
                }

                if (view.getLastVisiblePosition() == view.getCount() - 1 && totalItemCount > visibleItemCount) {
                    if (loadMoreView.state == loadMoreView.MORE) {
                        page++;
                        loadMoreView.loading();
                        loadData();
                    }
                }
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Bitmap bitmap) {
        actionBar.setLogoWH(this);
    }

    @OnClick({R.id.category, R.id.btgame, R.id.eran_point, R.id.newgame, R.id.gamerank})
    public void onClick(View view) {
        menuClick(view);
    }

    private void menuClick(View view) {
        Intent intent = null;
        int tag = (int) view.getTag();
        switch (tag) {
            case 0:
                intent = new Intent(getActivity(), GameCategoryActivity.class);
                break;
            case 1:
                intent = new Intent(getActivity(), NewGameActivity.class);
                break;
            case 2:
                GameType gameType = new GameType();
                gameType.setName("变态版");
                gameType.setTid("21");
                intent = new Intent(getActivity(), GameCategoryDetailActivity.class);
                intent.putExtra("game_type", gameType);
                break;
            case 3:
                intent = new Intent(getActivity(), GamePreOpenServiceActivity.class);
                break;
            case 4:
                if (startLoginActivity()) {
                    intent = new Intent(getContext(), EarnPointAcitivty.class);
                    startActivity(intent);
                }
                return;
            default:
                intent = new Intent(getActivity(), GameCategoryActivity.class);
                break;
        }
        getActivity().startActivity(intent);
    }

    @Override
    public void update(DownloadInfo downloadInfo) {
        newAdapter.updateView(downloadInfo);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer type) {
        if (type == EventBusMessage.REFRESH_INFO) {
            newAdapter.notifyDataSetChanged();
        }
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

    @Override
    public void loadData() {
        super.loadData();

        getSlide();

        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                bindCache(newAdapter, new Runnable() {
                    @Override
                    public void run() {
                        final List<GameInfo> newGameInfos = GameInfoCache.getCache(getContext(), CacheConfig
                                .GAME_INFO_INDEX);
                        readCache(newAdapter, newGameInfos);
                        bindView(new Runnable() {
                            @Override
                            public void run() {
                                if (newGameListView.getHeaderViewsCount() <= 0 && newGameInfos != null &&
                                        newGameInfos.size() > 0) {
                                    newGameListView.addHeaderView(headerView);
                                }
                            }
                        });
                    }
                });
                getTypeInfo();
            }
        });
    }

    private void getSlide() {

        slideEngin.getSlide(new Callback<List<SlideInfo>>() {
            @Override
            public void onSuccess(final ResultInfo<List<SlideInfo>> resultInfo) {
                bindView(new Runnable() {
                    @Override
                    public void run() {
                        List<String> urls = new ArrayList<>();
                        if (resultInfo != null && resultInfo.data != null && resultInfo.data.size() > 0) {
                            for (SlideInfo slideInfo : resultInfo.data) {
                                urls.add(slideInfo.getImg());
                            }
                            indexHeaderView.banner.setImages(urls);
                            indexHeaderView.banner.start();
                            slideInfos = resultInfo.data;
                            //setCache2(slideInfos, SlideInfo.class);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Response response) {

            }
        });
    }

    private List<SlideInfo> slideInfos;

    private void getTypeInfo() {
        GameListEngin.getImpl(getContext()).getTypeInfo(page, limit, "hots", new Callback<List<GameInfo>>() {
            @Override
            public void onSuccess(final ResultInfo<List<GameInfo>> resultInfo) {
                success(resultInfo, newAdapter, new Runnable() {
                    @Override
                    public void run() {
                        GameInfoCache.setCache(getContext(), resultInfo.data, CacheConfig.GAME_INFO_INDEX);
                    }
                });
                bindView(new Runnable() {
                    @Override
                    public void run() {
                        if (newGameListView.getHeaderViewsCount() <= 0) {
                            newGameListView.addHeaderView(headerView);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Response response) {

                fail2(newAdapter.dataInfos == null, getMessage(response.body, DescConstans.NET_ERROR));
            }
        });
    }

    public void startap() {
        if(indexHeaderView!=null && indexHeaderView.banner != null) {
            indexHeaderView.banner.startAutoPlay();
        }
    }

    public void stopap() {
        if(indexHeaderView!=null && indexHeaderView.banner != null) {
            indexHeaderView.banner.stopAutoPlay();
        }
    }


}
