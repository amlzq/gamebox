package com.gamebox_idtkown.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.activitys.GiftListActivity;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GiftIndex;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.GiftIndexEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.TaskUtil;
import com.gamebox_idtkown.views.adpaters.GBGiftIndexAdapter;
import com.gamebox_idtkown.views.widgets.GBActionBar4;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/9/22.
 */
public class GiftsFragment extends BaseGameListFragment<GiftIndex, GBActionBar4> {

    @BindView(R.id.gift_view)
    ListView giftListView;

    private String keyword = "";
    private String game_id = "";

    @Inject
    public GiftsFragment() {

    }

    @Override
    public void initVars() {
        super.initVars();
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            game_id = intent.getStringExtra("data");
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_gifts;
    }

    private GBGiftIndexAdapter adapter;

    @Override
    public void initViews() {
        super.initViews();
        actionBar.setOnSearchListener(new GBActionBar4.OnSearchListener() {
            @Override
            public void onSearch(View view) {
                if (keyword.equals(actionBar.getKeyWord())) {
                    return;
                }
//                view.setClickable(false);
                keyword = actionBar.getKeyWord();
                hideKeyboard();
                page = 1;
                loadData();
            }
        });

        adapter = new GBGiftIndexAdapter(getActivity());

        addHeaderAndFooter(giftListView, true);
        giftListView.setAdapter(adapter);
        removeFooterView();
        loadMoreView.setOtherHeight(100);
        adapter.setOnItemClickListener(new GBGiftIndexAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view) {
                GiftIndex giftIndex = (GiftIndex) view.getTag();
                Intent intent = new Intent(getContext(), GiftListActivity.class);
                intent.putExtra("game_index", giftIndex);
                startActivity(intent);
            }
        });

        addScrollListener(giftListView);

    }

    public void setClickable(final boolean flag) {
//        bindView(new Runnable() {
//            @Override
//            public void run() {
//                actionBar.setClickable(flag);
//            }
//        });
    }


    @Override
    public void loadData() {
        super.loadData();
        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                if (keyword != null && keyword.length() > 0) {
                    bindCache(adapter, new Runnable() {
                        @Override
                        public void run() {
                            List<GiftIndex> giftIndexes = getCache(GiftIndex.class);
                            readCache(adapter, giftIndexes);
                        }
                    });
                }
                getGiftIndexInfo();
            }
        });
    }

    private void getGiftIndexInfo() {
        GiftIndexEngin.getImpl(getActivity()).getGiftIndexInfo(page, limit, keyword, game_id, new
                Callback<List<GiftIndex>>
                        () {
                    @Override
                    public void onSuccess(final ResultInfo<List<GiftIndex>> resultInfo) {
                        success(resultInfo, adapter, 1, new Runnable() {
                            @Override
                            public void run() {
                                setCache(resultInfo.data, GiftIndex.class);
                            }
                        }, true);
                        setClickable(true);
                    }

                    @Override
                    public void onFailure(Response response) {
                        fail2(adapter.dataInfos == null, getMessage(response.body, DescConstans.NET_ERROR));
                        setClickable(true);
                    }
                });
    }

    @Override
    public void update(DownloadInfo downloadInfo) {

    }

    @Override
    public void removeFooterView() {
        if (giftListView.getFooterViewsCount() > 0) {
            giftListView.removeFooterView(footerView);
        }
    }

    @Override
    public void addFooterView() {
        if (giftListView.getFooterViewsCount() <= 0) {
            giftListView.addFooterView(footerView);
        }
    }

}
