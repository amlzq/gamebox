package com.gamebox_idtkown.activitys;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.cache.GiftListCache;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GiftDetail;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.MyGiftEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.DialogGiftUtil;
import com.gamebox_idtkown.utils.TaskUtil;
import com.gamebox_idtkown.views.adpaters.MyGiftAdapter;
import com.gamebox_idtkown.views.widgets.GBActionBar;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/10/25.
 */
public class MyGiftActivity extends BaseGameListActivity<GiftDetail, GBActionBar> {

    @BindView(R.id.gift_view)
    ListView giftListView;


    private MyGiftAdapter adapter;

    private String game_id;

    @Override
    public int getLayoutID() {
        return R.layout.activity_mygift;
    }

    @Override
    public void initVars() {
        super.initVars();
        Intent intent = this.getIntent();
        if (intent != null) {
            game_id = intent.getStringExtra("game_id");
        }
    }

    @Override
    public boolean isNeedLogin() {
        return true;
    }

    @Override
    public void initViews() {
        super.initViews();

        setBackListener();
        actionBar.setTitle("我的礼包");
        actionBar.hideMenuItem();


        adapter = new MyGiftAdapter(this);
        addHeaderAndFooter(giftListView, true);
        giftListView.setAdapter(adapter);
        removeFooterView();


        loadMoreView.setItemHeight(80);

        adapter.setOnItemClickListener(new MyGiftAdapter.OnItemClickListener() {
            @Override
            public void onDetail(View view) {
                GiftDetail giftDetail = (GiftDetail) view.getTag();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("礼包码", giftDetail.getCode());
                clipboard.setPrimaryClip(clip);
                DialogGiftUtil.show2(MyGiftActivity.this, giftDetail.getCode());
            }

            @Override
            public void onClick(View view) {
                GiftDetail giftDetail = (GiftDetail) view.getTag();

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("礼包码", giftDetail.getCode());
                clipboard.setPrimaryClip(clip);
                DialogGiftUtil.show2(MyGiftActivity.this, giftDetail.getCode());
            }
        });

        addScrollListener(giftListView);
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

    @Override
    public void loadData() {
        super.loadData();

        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
//                bindCache(adapter, new Runnable() {
//                    @Override
//                    public void run() {
//                        List<GiftDetail> giftDetails = GiftListCache.getCache(getBaseContext(), "-1");
//                        readCache(adapter, giftDetails);
//                    }
//                });
                getMyGift();
            }
        });


    }

    private void getMyGift() {
        MyGiftEngin.getImpl(this).getMyGift(page, limit, game_id, new Callback<List<GiftDetail>>() {
            @Override
            public void onSuccess(final ResultInfo<List<GiftDetail>> resultInfo) {
                success(resultInfo, adapter, new
                        Runnable() {
                            @Override
                            public void run() {
                                GiftListCache.setCache(getBaseContext(), resultInfo.data, "-1");
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
    public void update(DownloadInfo downloadInfo) {

    }
}
