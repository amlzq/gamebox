package com.gamebox_idtkown.activitys;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.cache.GoodListCache;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GoodList;
import com.gamebox_idtkown.core.db.greendao.GoodType;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.di.dagger2.components.DaggerEnginComponent;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.GoodConvertEngin;
import com.gamebox_idtkown.engin.GoodListEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.security.Base64;
import com.gamebox_idtkown.utils.DialogGiftUtil;
import com.gamebox_idtkown.utils.LoadingUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.TaskUtil;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.adpaters.GBGoodListAdapter;
import com.gamebox_idtkown.views.widgets.GBActionBar5;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/11/11.
 */
public class GoodListActivity extends BaseGameListActivity<GoodList, GBActionBar5> {
    @BindView(R.id.listView)
    ListView listView;

    @Inject
    GoodListEngin goodListEngin;


    GBGoodListAdapter adapter;

    GoodType goodType;

    @Override
    public void initVars() {
        super.initVars();
        Intent intent = this.getIntent();
        if (intent != null) {
            goodType = (GoodType) intent.getSerializableExtra("good_type");
        }

        DaggerEnginComponent.create().injectGoodList(this);
    }

    @Override
    public boolean ERROR() {
        if (goodType == null) {
            return true;
        }
        return false;
    }

//    @Override
//    public boolean isNeedLogin() {
//        return true;
//    }

    @Override
    public void initViews() {
        super.initViews();
        actionBar.setTitle(goodType.getTitle());
        setBackListener();
        actionBar.showMenuItem();

        actionBar.setOnItemClickListener(new GBActionBar5.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                Intent intent = new Intent(getBaseContext(), GoodChangeActivity.class);
                intent.putExtra("game_id", goodType.getGame_id());
                startActivity(intent);
            }

            @Override
            public void onClose(View view) {

            }
        });

        adapter = new GBGoodListAdapter(this);
        addHeaderAndFooter(listView, true);
        listView.setAdapter(adapter);
        removeFooterView();

        loadMoreView.setItemHeight(80);

        adapter.setOnItemClickListener(new GBGoodListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view) {
                GoodList goodList = (GoodList) view.getTag();
                Intent intent = new Intent(getBaseContext(), GiftDetailActivity.class);
                intent.putExtra("good_list", goodList);
                startActivity(intent);
            }
        });

        addScrollListener(listView);
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
                        List<GoodList> goodLists = GoodListCache.getCache(getBaseContext(), goodType.getTypeId()
                                + "_" + goodType.getGame_id());
                        readCache(adapter, goodLists);
                    }
                });
                getList();
            }
        });
    }

    private void getList() {
        goodListEngin.getList(page, limit, goodType.getTypeId(), goodType.getGame_id(), new Callback<List<GoodList>>() {
            @Override
            public void onSuccess(final ResultInfo<List<GoodList>> resultInfo) {
                success(resultInfo, adapter, new Runnable() {
                    @Override
                    public void run() {
                        GoodListCache.setCache(getBaseContext(), resultInfo.data, goodType.getTypeId());
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
    public int getLayoutID() {
        return R.layout.activity_good_list;
    }

    @Override
    public void removeFooterView() {
        if (listView.getFooterViewsCount() > 0) {
            listView.removeFooterView(footerView);
        }
    }

    @Override
    public void addFooterView() {
        if (listView.getFooterViewsCount() <= 0) {
            listView.addFooterView(footerView);
        }
    }

    @Override
    public void update(DownloadInfo downloadInfo) {

    }
}