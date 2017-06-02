package com.gamebox_idtkown.activitys;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.cache.BaseCache;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
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
public abstract class BaseGameListActivity<D, T extends GBBaseActionBar> extends BaseActionBarActivity<T> {
    protected GBLoadMoreView loadMoreView;
    protected int page = 1;
    protected int limit = 20;
    protected boolean isFooterAndHeaderViewAdd = false;
    protected View headerView;
    protected View footerView;

    @Override
    public void initViews() {
        super.initViews();

        footerView = this.getLayoutInflater().inflate(R.layout
                .view_load_more, null, false);
        loadMoreView = GBLoadMoreView.getInstance(this, footerView);

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

        loadMoreView.setStatusBarHeight(statusBarHeight);

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final DownloadInfo downloadInfo) {
        update(downloadInfo);
    }

    public void fail(final int n, final boolean flag, final String msg) {
        stop(n);
        bindView(new Runnable() {
            @Override
            public void run() {
                if (loaded >= n) {
                    showNoNetView(flag, msg);
                    loaded = 0;
                }
                if (page > 1) {
                    page--;
                    loadMoreView.more();
                }

                if (page == 0 && flag) {
                    removeFooterView();
                }
            }
        });
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

    @Override
    public void loadData() {
        super.loadData();
        removeNoView();
        if (page == 1 && !cache) {
            showProcessView();
        }
    }

    public abstract void removeFooterView();

    public abstract void addFooterView();


    public boolean showStateView(ResultInfo<List<D>> resultInfo, GBBaseAdapter adapter, int page, int limit) {
        boolean flag = false;
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
                if ((adapter.dataInfos == null || adapter.dataInfos.size() == 0) && headerView == null) {
                    showNoDataView();
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
        List<D> list = baseCache.getCache(this, clazz);
        return list;
    }

    public void setCache(List<D> list, Class clazz) {
        if (cache || list == null || list.size() <= 0) {
            return;
        }
        cache = true;
        BaseCache<D> baseCache = new BaseCache<D>();
        baseCache.setCache(this, list, clazz);
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

    public void bindCache(GBBaseAdapter adapter, Runnable runnable) {
        if (!cache && page == 1 && adapter.dataInfos == null) {
            runnable.run();
            LogUtil.msg(this.getClass().getSimpleName() + "缓存方法getCache已运行");
        }
    }

    public void fail(boolean flag, String msg) {
        fail(1, flag, msg);
    }

    public void success(final ResultInfo<List<D>> resultInfo, final GBBaseAdapter adapter, Runnable runnable) {
        success(resultInfo, adapter, 1, runnable);
    }

    public void success(final ResultInfo<List<D>> resultInfo, final GBBaseAdapter adapter) {
        success(resultInfo, adapter, 1, null);
    }

    public void success(final ResultInfo<List<D>> resultInfo, final GBBaseAdapter adapter, int n, Runnable runnable) {
        bindView(new Runnable() {
            @Override
            public void run() {
                if (page == 1) {
                    if (showStateView(resultInfo, adapter, page, limit)) {
                        adapter.dataInfos = resultInfo.data;
                    }
                } else {
                    if (showStateView(resultInfo, adapter, page, limit)) {
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
                runnable.run();
                cache = true;
                LogUtil.msg(this.getClass().getSimpleName() + "缓存方法setCache已运行");
            }
        }
    }

    public abstract void update(DownloadInfo downloadInfo);

}
