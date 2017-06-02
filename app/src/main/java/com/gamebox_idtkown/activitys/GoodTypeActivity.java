package com.gamebox_idtkown.activitys;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GoodType;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.di.dagger2.components.DaggerEnginComponent;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.GoodTypeEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.security.Base64;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.TaskUtil;
import com.gamebox_idtkown.views.adpaters.GBGoodTypeAdapter;
import com.gamebox_idtkown.views.widgets.GBActionBar5;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/11/11.
 */
public class GoodTypeActivity extends BaseGameListActivity<GoodType, GBActionBar5> {
    @BindView(R.id.listView)
    ListView listView;

    @Inject
    GoodTypeEngin goodTypeEngin;

    GBGoodTypeAdapter adapter;

    private String game_id;


    @Override
    public void initVars() {
        super.initVars();
        Intent intent = this.getIntent();
        if (intent != null) {
            game_id = intent.getStringExtra("data");
        }
        DaggerEnginComponent.create().injectGoodType(this);
    }

//    @Override
//    public boolean isNeedLogin() {
//        return true;
//    }

    @Override
    public void initViews() {
        super.initViews();
        actionBar.setTitle("积分商城");
        setBackListener();
        actionBar.showMenuItem();

        actionBar.setOnItemClickListener(new GBActionBar5.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                Intent intent = new Intent(getBaseContext(), GoodChangeActivity.class);
                intent.putExtra("game_id", game_id);
                startActivity(intent);
            }

            @Override
            public void onClose(View view) {

            }
        });


        adapter = new GBGoodTypeAdapter(this);
        listView.setAdapter(adapter);

        adapter.setOnItemClickListener(new GBGoodTypeAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view) {
                GoodType goodType = (GoodType) view.getTag();
                goodType.setGame_id(game_id);
                Intent intent = new Intent(getBaseContext(), GoodListActivity.class);
                intent.putExtra("good_type", goodType);
                startActivity(intent);
            }
        });
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
                        List<GoodType> giftIndexes = getCache(GoodType.class);
                        readCache(adapter, giftIndexes);
                    }
                });
                getType();
            }
        });
    }

    private void getType() {
        goodTypeEngin.getType(game_id, new Callback<List<GoodType>>() {
            @Override
            public void onSuccess(ResultInfo<List<GoodType>> resultInfo) {
                success(resultInfo, adapter);
            }

            @Override
            public void onFailure(Response response) {
                fail(adapter.dataInfos == null, getMessage(response.body, DescConstans.NET_ERROR));
            }
        });
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_good_type;
    }

    @Override
    public void removeFooterView() {

    }

    @Override
    public void addFooterView() {

    }

    @Override
    public void update(DownloadInfo downloadInfo) {

    }
}
