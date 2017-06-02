package com.gamebox_idtkown.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.activitys.BaseActivity;
import com.gamebox_idtkown.activitys.GameDetailActivity;
import com.gamebox_idtkown.cache.BaseCache;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.views.adpaters.GBBaseAdapter;
import com.gamebox_idtkown.views.widgets.GBBaseActionBar;
import com.gamebox_idtkown.views.widgets.GBLoadMoreView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by zhangkai on 16/10/18.
 */
public abstract class BaseGameListFragment<D, T extends GBBaseActionBar> extends BaseActionBarFragment<T> {

    protected GBLoadMoreView loadMoreView;
    protected int page = 1;
    protected int limit = 20;
    protected boolean isFooterAndHeaderViewAdd = false;
    protected View headerView;
    protected View footerView;

    @Override
    public void initViews() {
        super.initViews();
        Activity activity = getActivity();
        footerView = activity.getLayoutInflater().inflate(R.layout
                .view_load_more, null, false);
        loadMoreView = GBLoadMoreView.getInstance(activity, footerView);

        //刷新
        if (refreshLayout != null) {
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    page = 1;
                    loadData();
                    loadMoreView.more();
                }
            });
        }

        //加载更多
        loadMoreView.setOnLoadingListener(new GBLoadMoreView.OnLoadingListener() {
            @Override
            public void onLoading(View view) {
                page++;
                loadData();
            }
        });

        loadMoreView.setStatusBarHeight(((BaseActivity) getActivity()).statusBarHeight);

        EventBus.getDefault().register(this);
    }

    @Override
    public void loadData() {
        super.loadData();
        removeNoView();
        if (page == 1 && !cache) {
            showProcessView();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DownloadInfo downloadInfo) {
        update(downloadInfo);
    }

    public void fail(final int n, final boolean flag, final String msg) {
        stop(n);
        bindView(new Runnable() {
            @Override
            public void run() {
                if (page > 1) {
                    page--;
                    loadMoreView.more();
                }
                if (loaded >= n) {
                    showNoNetView(flag, msg);
                    loaded = 0;
                }
            }
        });
    }

    public void fail2(final boolean flag, final String msg) {
        stop(1);
        bindView(new Runnable() {
            @Override
            public void run() {
                if (page > 1) {
                    page--;
                    loadMoreView.more();
                }
                if (loaded >= 1) {
                    showNoNetView2(flag, msg);
                    loaded = 0;
                }
            }
        });
    }


    public void readCache(final GBBaseAdapter adapter, final List<D> dataInfos) {
        if (dataInfos != null && dataInfos.size() > 0) {
            bindView(new Runnable() {
                @Override
                public void run() {
                    removeNoView();
                    removeProcessView();
                    adapter.dataInfos = dataInfos;
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }


    public void addHeaderAndFooter(ListView listView, boolean hasFooterView) {
        if (headerView != null) {
            listView.addHeaderView(headerView);
        }
        if (!isFooterAndHeaderViewAdd && hasFooterView) {
            listView.addFooterView(footerView);
            isFooterAndHeaderViewAdd = true;
        }
    }

    public void bindCache(GBBaseAdapter adapter, Runnable runnable) {
        if (!cache && page == 1 && adapter.dataInfos == null) {
            runnable.run();
            LogUtil.msg(this.getClass().getSimpleName() + "缓存方法getCache已运行");
        }
    }

    public void addScrollListener(ListView listView) {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
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

    public boolean cache = false;

    public List<D> getCache(Class clazz) {
        BaseCache<D> baseCache = new BaseCache<D>();
        List<D> list = baseCache.getCache(getContext(), clazz);
        return list;
    }

    public <U> List<U> getCache2(Class clazz) {
        BaseCache<U> baseCache = new BaseCache<U>();
        List<U> list = baseCache.getCache(getContext(), clazz);
        return list;
    }

    public void setCache(List<D> list, Class clazz) {
        if (list == null || list.size() <= 0) {
            return;
        }
        BaseCache<D> baseCache = new BaseCache<D>();
        baseCache.setCache(getContext(), list, clazz);
    }

    public <U> void setCache2(List<U> list, Class clazz) {
        if (list == null || list.size() <= 0) {
            return;
        }
        BaseCache<U> baseCache = new BaseCache<U>();
        baseCache.setCache(getContext(), list, clazz);
    }


    public void fail(final boolean flag, String msg) {
        fail(1, flag, msg);
    }


    public abstract void update(DownloadInfo downloadInfo);


    public void success(final ResultInfo<List<D>> resultInfo, final GBBaseAdapter adapter, Runnable runnable) {
        success(resultInfo, adapter, 1, runnable, false);
    }

    public void success(final ResultInfo<List<D>> resultInfo, final GBBaseAdapter adapter) {
        success(resultInfo, adapter, 1, null, false);
    }

    public void success(final ResultInfo<List<D>> resultInfo, final GBBaseAdapter adapter, int n, Runnable runnable) {
        success(resultInfo, adapter, n, runnable, false);
    }

    public void success(final ResultInfo<List<D>> resultInfo, final GBBaseAdapter adapter, int n, Runnable runnable,
                        final boolean nodata) {
        bindView(new Runnable() {
            @Override
            public void run() {
                if (page == 1) {
                    if (showStateView(resultInfo, adapter, page, limit, nodata)) {
                        adapter.dataInfos = resultInfo.data;
                    }
                } else {
                    if (showStateView(resultInfo, adapter, page, limit, nodata)) {
                        adapter.dataInfos.addAll(resultInfo.data);
                    }
                }
                adapter.notifyDataSetChanged();

            }
        });
        stop(n);
        if (loaded >= n) {
            loaded = 0;
        }

        if (page == 1 && resultInfo != null && resultInfo.data != null && resultInfo.data.size() > 0 && runnable !=
                null) {
            if (!cache) {
                cache = true;
                runnable.run();
                LogUtil.msg(this.getClass().getSimpleName() + "缓存方法setCache已运行");
            }
        }
    }

    public abstract void removeFooterView();

    public abstract void addFooterView();

    public boolean showStateView(ResultInfo<List<D>> resultInfo, GBBaseAdapter adapter, int page, int limit) {
        return showStateView(resultInfo, adapter, page, limit, false);
    }


    public boolean showStateView(ResultInfo<List<D>> resultInfo, GBBaseAdapter adapter, int page, int limit, boolean
            nodata) {
        boolean flag;
        if (resultInfo != null && resultInfo.data != null && resultInfo.data.size() > 0) {
            List<D> datas = resultInfo.data;
            if (datas.size() < limit) {
                if (page == 1 && loadMoreView.isHiden(datas, page)) {
                    removeFooterView();
                } else {
                    addFooterView();
                    loadMoreView.loaded();
                }
            } else {
                addFooterView();
                loadMoreView.more();
            }
            flag = true;
        } else {
            if (page == 1) {
                if (nodata || (adapter.dataInfos == null || adapter.dataInfos.size() == 0) && headerView == null) {
                    showNoDataView();
                }
                if(nodata){
                    adapter.dataInfos = null;
                }
                removeFooterView();
            } else {
                addFooterView();
                loadMoreView.loaded();
            }
            flag = false;
        }
        return flag;
    }


    public void startGameDetailActivity(GameInfo gameInfo) {
        Intent intent = new Intent(getContext(), GameDetailActivity.class);
        intent.putExtra("game_info", gameInfo);
        startActivity(intent);
    }
}
