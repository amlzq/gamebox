package com.gamebox_idtkown.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.activitys.GameDetailActivity;
import com.gamebox_idtkown.activitys.GiftDetailActivity;
import com.gamebox_idtkown.activitys.GoodListActivity;
import com.gamebox_idtkown.cache.GiftListCache;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.core.db.greendao.GiftDetail;
import com.gamebox_idtkown.core.db.greendao.GoodType;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.GiftListEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.TaskUtil;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.adpaters.GBGiftListAdapter;
import com.gamebox_idtkown.views.widgets.GBBaseActionBar;
import com.gamebox_idtkown.views.widgets.GBListView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/11/3.
 */
public class GiftListFragment extends BaseGameListFragment<GiftDetail, GBBaseActionBar> {
    @BindView(R.id.id_stickynavlayout_innerscrollview)
    GBListView giftListView;

    private GBGiftListAdapter adapter;
    private View footerView;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_gift_list;
    }

    @Override
    public void initViews() {
        super.initViews();
        final Context context = getContext();

        adapter = new GBGiftListAdapter(context);
        footerView = getActivity().getLayoutInflater().inflate(R.layout
                .view_blank_footer, null, false);
        giftListView.addFooterView(footerView);
        giftListView.setAdapter(adapter);

        adapter.setOnItemClickListener(new GBGiftListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view) {
                GiftDetail giftDetail = (GiftDetail) view.getTag();
                if (giftDetail == null || giftDetail.fixisPay.equals("0")) {
                    Intent intent = new Intent(context, GiftDetailActivity.class);
                    intent.putExtra("game_detail", giftDetail);
                    startActivity(intent);
                } else {
                    if (startLoginActivity()) {
                        if (giftDetail.getGoods_id() != null) {
                            Intent intent = new Intent(context, GiftDetailActivity.class);
                            intent.putExtra("good_id", giftDetail.getGoods_id());
                            context.startActivity(intent);
                        } else {
                            ToastUtil.toast2(context, DescConstans.SERVICE_ERROR2);
                        }
                    }
                }
            }
        });
    }

    boolean isVisible = false;
    boolean loaded = false;

    public void loadData() {
        super.loadData();
        final Context context = getContext();
        final GameInfo gameInfo = ((GameDetailActivity) getActivity()).gameInfo;
        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                bindCache(adapter, new Runnable() {
                    @Override
                    public void run() {
                        List<GiftDetail> giftDetails = GiftListCache.getCache(context, gameInfo.getGameId());
                        readCache(adapter, giftDetails);
                    }
                });
                getGiftListInfo(context, gameInfo);
            }
        });

    }

    private void getGiftListInfo(final Context context, final GameInfo gameInfo) {
        loaded = true;

        GiftListEngin.getImpl(context).getGiftListInfo(page, limit, gameInfo.getGameId(), new Callback<List<GiftDetail>>
                () {
            @Override
            public void onSuccess(final ResultInfo<List<GiftDetail>> resultInfo) {
                success(resultInfo, adapter, new Runnable() {
                    @Override
                    public void run() {
                        GiftListCache.setCache(context, resultInfo.data, gameInfo.getGameId());
                    }
                });
                setHeight();
            }

            @Override
            public void onFailure(Response response) {
                fail(true, getMessage(response.body, DescConstans.NET_ERROR));
                setHeight();
            }
        });


    }

    private void setHeight() {
//        bindView(new Runnable() {
//            @Override
//            public void run() {
//
//                GameDetailActivity gameDetailActivity = (GameDetailActivity) getActivity();
//                int defaultHeight = gameDetailActivity.defaultHeight;
//
//                ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) giftListView.getLayoutParams();
//                layoutParams.height = giftListView.getTotalHeight();
//                giftListView.setLayoutParams(layoutParams);
//
//                if (defaultHeight < layoutParams.height) {
//                    gameDetailActivity.tabHeight2 = layoutParams.height;
//                } else {
//                    giftListView.removeFooterView(footerView);
//                }
//                gameDetailActivity.setHeight(gameDetailActivity.tabHeight2);
//            }
//        });
    }


    @Override
    public void update(DownloadInfo downloadInfo) {

    }

    @Override
    public void removeFooterView() {

    }

    @Override
    public void addFooterView() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
        } else {
            isVisible = false;
        }

        if (isVisible && !loaded) {
            loadData();
        }
    }
}
