package com.gamebox_idtkown.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.activitys.GameDetailActivity;
import com.gamebox_idtkown.activitys.GameDetailGalleryActivity;
import com.gamebox_idtkown.cache.GameDetailCache;
import com.gamebox_idtkown.cache.GameImageCache;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GameDetail;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.GameDetailEngin;
import com.gamebox_idtkown.engin.GameListEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.CheckUtil;
import com.gamebox_idtkown.utils.ScreenUtil;
import com.gamebox_idtkown.utils.TaskUtil;
import com.gamebox_idtkown.views.adpaters.GBHGameListAdapter;
import com.gamebox_idtkown.views.adpaters.GameDetailPPTAdapter;
import com.gamebox_idtkown.views.widgets.GBBaseActionBar;
import com.gamebox_idtkown.views.widgets.GBGameDetailFragmentHeader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/11/3.
 */
public class GameDetailFragment extends BaseGameListFragment<GameInfo, GBBaseActionBar> {
    @BindView(R.id.id_stickynavlayout_innerscrollview)
    ListView gamelist;

    private GameDetailPPTAdapter adapter = null;
    private GBHGameListAdapter listAdapter = null;
    private GBGameDetailFragmentHeader gbGameDetailFragmentHeader;
    private GameInfo gameInfo;
    private WeakReference<GameDetailActivity> weakGameDetailActivity;

    @Override
    public void initVars() {
        super.initVars();
        weakGameDetailActivity =new WeakReference<GameDetailActivity>((GameDetailActivity)getActivity());
        gameInfo = weakGameDetailActivity.get().gameInfo;
    }

    @Override
    public void initViews() {

        super.initViews();

        final Context context = getContext();
        if (gameInfo == null) {
            getActivity().finish();
            return;
        }
        headerView = getActivity().getLayoutInflater().inflate(R.layout
                .view_game_detail_fragment_header, null, false);
        headerView.setVisibility(View.INVISIBLE);
        gbGameDetailFragmentHeader = GBGameDetailFragmentHeader.getInstance(getContext(), headerView);
        View footerView = getActivity().getLayoutInflater().inflate(R.layout
                .view_blank_footer, null, false);

        listAdapter = new GBHGameListAdapter(getActivity());
        listAdapter.setListView(gamelist);
        listAdapter.setOnItemClickListener(new GBHGameListAdapter.OnItemClickListener() {
            @Override
            public void onDetail(View view) {
                GameInfo gameInfo = (GameInfo) view.getTag();
                startGameDetailActivity(gameInfo);
                getActivity().finish();
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
        addHeaderAndFooter(gamelist, false);
        gamelist.addFooterView(footerView);
        gamelist.setAdapter(listAdapter);

//        gbGameDetailFragmentHeader.tvVersion.setText("版本: " + CheckUtil.checkStr(gameInfo.getVersion(), "未知"));
//        gbGameDetailFragmentHeader.tvTime.setText("更新时间: " + CheckUtil.checkStr(gameInfo.getUpdateTime(), "未知"));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        gbGameDetailFragmentHeader.mRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new GameDetailPPTAdapter(context);
        gbGameDetailFragmentHeader.mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GameDetailPPTAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view) {
                if (GameDetailActivity.gameImages == null || GameDetailActivity.
                        gameImages.size() == 0) {
                    GameDetailActivity.gameImages = adapter.dataInfos;
                }
                int position = Integer.parseInt(view.getTag() + "");
                Intent intent = new Intent(context, GameDetailGalleryActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        gbGameDetailFragmentHeader.btnOpen.setTag("close");
        gbGameDetailFragmentHeader.btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = v.getTag() + "";
                if (tag.equals("close")) {
                    v.setTag("open");
                    setBodyHeight(totalHeight);
//                    ((TextView) v).setText("");
                    v.setVisibility(View.GONE);
                    ((TextView) v).setText("关闭全文");
                } else {
                    v.setTag("close");
                    setBodyHeight(detailHeight);
                    ((TextView) v).setText("展开全文");
                }
                setHeight();
            }
        });

    }


    private void setHeight() {
//        bindView(new Runnable() {
//            @Override
//            public void run() {
//                GameDetailActivity gameDetailActivity = (GameDetailActivity) getActivity();
//                int defaultHeight = gameDetailActivity.defaultHeight;
//
//                ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) gamelist.getLayoutParams();
//                layoutParams.height = gamelist.getTotalHeight();
//                gamelist.setLayoutParams(layoutParams);
//
//                if (defaultHeight < layoutParams.height) {
//                    gameDetailActivity.tabHeight = layoutParams.height;
//                }
//                gameDetailActivity.headHeight = gamelist.headHeight;
//                gameDetailActivity.setHeight(gameDetailActivity.tabHeight);
//
//            }
//        });
        bindView(new Runnable() {
            @Override
            public void run() {
                headerView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void loadData() {
        //super.loadData();

        final Context context = getContext();
        if (gameInfo == null) {
            getActivity().finish();
            return;
        }

        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                List<GameDetail> gameDetails = GameDetailCache.getCache(context, gameInfo.getGameId());
                if (gameDetails != null && gameDetails.size() > 0) {
                    GameDetail gameDetail = gameDetails.get(0);
                    gameDetail.imgs = GameImageCache.getCache(context, gameInfo.getGameId());
                    showInfo(gameDetail);
                    weakGameDetailActivity.get().show();
                }
                getGameDetail(gameInfo);
            }
        });
    }

    private void getGameDetail(GameInfo gameInfo) {
        GameDetailEngin.getImpl(getContext()).getGameDetail(gameInfo.getType(), gameInfo.getGameId(), new Callback<GameDetail>
                () {
            @Override
            public void onSuccess(final ResultInfo<GameDetail> resultInfo) {
                bindView(new Runnable() {
                    @Override
                    public void run() {
                        stop(2);
                        if (resultInfo != null && resultInfo.data != null) {
                            showInfo(resultInfo.data);
                            otherGames(resultInfo.data);
                        } else {
                            weakGameDetailActivity.get().show();
                        }
                    }
                });
                if (resultInfo != null) {
                    setCache(resultInfo.data);
                }
            }

            @Override
            public void onFailure(Response response) {
                fail(1, true, getMessage(response.body, DescConstans.NET_ERROR));
                weakGameDetailActivity.get().show();
            }
        });
    }

    private void otherGames(final GameDetail gameDetail) {
        GameListEngin.getImpl(getContext()).getTypeInfo(1, 6, "otherDown", new Callback<List<GameInfo>>() {
            @Override
            public void onSuccess(ResultInfo<List<GameInfo>> resultInfo) {
                setDetailHeight(gameDetail);
                success(resultInfo, listAdapter, 2, new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                setHeight();
                weakGameDetailActivity.get().show();
            }

            @Override
            public void onFailure(Response response) {
                fail(2, true, getMessage(response.body, DescConstans.NET_ERROR));
                setHeight();
                weakGameDetailActivity.get().show();
            }
        });
    }

    private int detailHeight = 0;
    private int totalHeight = 0;

    private void showInfo(final GameDetail gameDetail) {
        ((GameDetailActivity) getActivity()).shareUrl = gameDetail.getShareUrl();
        GameDetailActivity.gameImages = gameDetail.imgs;
        bindView(new Runnable() {
            @Override
            public void run() {
                gbGameDetailFragmentHeader.tvDetail.setText(Html.fromHtml(CheckUtil.checkStr(gameDetail.getBody(), "暂无介绍")));
                adapter.dataInfos = gameDetail.imgs;
                if (gameDetail.imgs != null && gameDetail.imgs.size() > 0) {
                    adapter.notifyDataSetChanged();
                } else {
                    gbGameDetailFragmentHeader.mRecyclerView.setVisibility(View.GONE);
                }
            }
        });

    }

    private void setCache(final GameDetail gameDetail) {
        if (gameDetail != null) {
            final Context context = getContext();
            if (gameInfo != null) {
                GameDetailCache.setCache(context, gameDetail, gameInfo.getGameId());
                GameImageCache.setCache(context, gameDetail.imgs, gameInfo.getGameId());
            }
        }
    }

    private void setDetailHeight(final GameDetail gameDetail) {
        bindView(new Runnable() {
            @Override
            public void run() {
                if (totalHeight == 0) {
                    totalHeight = gbGameDetailFragmentHeader.tvDetail.getHeight();
                }
                detailHeight = gbGameDetailFragmentHeader.tvDetail.getLineHeight() * 4 + ScreenUtil.dip2px(getContext
                        (), 2);
                if (detailHeight > totalHeight) {
                    detailHeight = totalHeight;
                    gbGameDetailFragmentHeader.btnOpen.setVisibility(View.GONE);
                } else {
                    gbGameDetailFragmentHeader.btnOpen.setVisibility(View.VISIBLE);
                }
                totalHeight = gbGameDetailFragmentHeader.tvDetail.getHeight();
                setBodyHeight(detailHeight);
                gbGameDetailFragmentHeader.tvDetail.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setBodyHeight(int height) {
        ViewGroup.LayoutParams layoutParams = gbGameDetailFragmentHeader.tvDetail.getLayoutParams();
        layoutParams.height = height;
        gbGameDetailFragmentHeader.tvDetail.setLayoutParams(layoutParams);
    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_game_detial;
    }


    @Override
    public void update(DownloadInfo downloadInfo) {
        listAdapter.updateView(downloadInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer type) {
        if (type == EventBusMessage.REFRESH_INFO) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void removeFooterView() {
    }

    @Override
    public void addFooterView() {
    }
}
