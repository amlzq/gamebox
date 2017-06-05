package com.gamebox_idtkown.activitys;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.cache.GiftListCache;
import com.gamebox_idtkown.constans.DescConstans;

import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GiftDetail;
import com.gamebox_idtkown.core.db.greendao.GiftIndex;
import com.gamebox_idtkown.core.db.greendao.GoodType;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.ResultInfo;

import com.gamebox_idtkown.engin.GiftListEngin;
import com.gamebox_idtkown.net.entry.Response;

import com.gamebox_idtkown.security.Base64;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.TaskUtil;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.adpaters.GBGiftListAdapter;
import com.gamebox_idtkown.views.widgets.GBActionBar;
import com.gamebox_idtkown.views.widgets.GBActionBar5;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/10/24.
 */
public class GiftListActivity extends BaseGameListActivity<GiftDetail, GBActionBar5> {
    @BindView(R.id.gift_view)
    ListView giftListView;

    private GBGiftListAdapter adapter;
    private GiftIndex giftIndex;
    private String game_id= "";
    @Override
    public void initVars() {
        super.initVars();
        Intent intent = this.getIntent();
        if (intent != null) {
            giftIndex = (GiftIndex) intent.getSerializableExtra("game_index");
            if (giftIndex == null) {
                String data = intent.getStringExtra("data");
                if (data != null) {
                    try {
                        data = new String(Base64.decode(data));
                        giftIndex = JSON.parseObject(data, GiftIndex.class);
                        game_id =  giftIndex.getGameId();
                        LogUtil.msg("GiftListActivity data->" + data);
                    } catch (Exception e) {
                        LogUtil.msg("GiftListActivity data->" + e);
                    }
                }
            }
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        setBackListener();
        actionBar.setTitle(giftIndex.getGameName() + "礼包");
        actionBar.showMenuItem("我的礼包");
        adapter = new GBGiftListAdapter(this);
        addHeaderAndFooter(giftListView, true);
        giftListView.setAdapter(adapter);
        removeFooterView();

        actionBar.setOnItemClickListener(new GBActionBar5.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                if(startLoginActivity()) {
                    Intent intent = new Intent(getBaseContext(), MyGiftActivity.class);
                    intent.putExtra("game_id", game_id);
                    startActivity(intent);
                }
            }

            @Override
            public void onClose(View view) {

            }
        });

        adapter.setOnItemClickListener(new GBGiftListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view) {
                GiftDetail giftDetail = (GiftDetail) view.getTag();
                startDetailActivity(GiftListActivity.this, giftDetail);
            }
        });
        addScrollListener(giftListView);
    }

    public static void startDetailActivity(BaseActivity context, GiftDetail giftDetail) {
        try {
            LogUtil.msg("fixisPay->" + giftDetail.getGoods_type_id());
            if (giftDetail == null || giftDetail.fixisPay.equals("0")) {
                Intent intent = new Intent(context, GiftDetailActivity.class);
                intent.putExtra("game_detail", giftDetail);
                context.startActivity(intent);
            } else {
                if (context.startLoginActivity()) {
                    if (giftDetail.getGoods_id() != null) {
                        Intent intent = new Intent(context, GiftDetailActivity.class);
                        intent.putExtra("good_id", giftDetail.getGoods_id());
                        context.startActivity(intent);
                    } else {
                        ToastUtil.toast2(context, DescConstans.SERVICE_ERROR2);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean ERROR() {
        if (giftIndex == null) {
            return true;
        }
        return false;
    }

    @Override
    public void loadData() {
        super.loadData();
        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                bindCache(adapter, new Runnable() {
                    @Override
                    public void run() {
                        List<GiftDetail> giftDetails = GiftListCache.getCache(getBaseContext(), giftIndex.getGameId());
                        readCache(adapter, giftDetails);
                    }
                });
                getGiftListInfo();
            }
        });
    }

    private void getGiftListInfo() {
        GiftListEngin.getImpl(this).getGiftListInfo(page, limit, giftIndex.getGameId(), new Callback<List<GiftDetail>>() {
            @Override
            public void onSuccess(final ResultInfo<List<GiftDetail>> resultInfo) {
                success(resultInfo, adapter, new Runnable() {
                    @Override
                    public void run() {
                        GiftListCache.setCache(getBaseContext(), resultInfo.data, giftIndex.getGameId());
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GiftDetail giftDetail) {
        if (adapter.dataInfos != null) {
            for (GiftDetail _giftDetail : adapter.dataInfos) {
                if (giftDetail.getGiftId().equals(_giftDetail.getGiftId())) {
                    int surplusNum = Integer.parseInt(giftDetail.getSurplusNum());
                    if (surplusNum == 0) {
                        adapter.dataInfos.remove(_giftDetail);
                        break;
                    }
                    _giftDetail.setSurplusNum(surplusNum + "");
                }
            }
        }
        adapter.notifyDataSetChanged();
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
    public int getLayoutID() {
        return R.layout.activity_gift_list;
    }
}


