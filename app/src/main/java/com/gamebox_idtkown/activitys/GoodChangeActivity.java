package com.gamebox_idtkown.activitys;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GoodChange;
import com.gamebox_idtkown.core.db.greendao.GoodType;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.di.dagger2.components.DaggerEnginComponent;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.GoodChangeEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.security.Base64;
import com.gamebox_idtkown.utils.DialogGiftUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.views.adpaters.GBGoodChangeAdapter;
import com.gamebox_idtkown.views.widgets.GBActionBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/11/11.
 */
public class GoodChangeActivity extends BaseGameListActivity<GoodChange, GBActionBar> {
    @BindView(R.id.listView)
    ListView listView;

    @Inject
    GoodChangeEngin goodChangeEngin;

    GBGoodChangeAdapter adapter;

    private String game_id;

    GoodType goodType;

    @Override
    public void initVars() {
        super.initVars();
        DaggerEnginComponent.create().injectGoodChange(this);

        Intent intent = this.getIntent();
        if (intent != null) {
            goodType = (GoodType) intent.getSerializableExtra("good_type");
            if (goodType == null) {
                try {
                    String data = intent.getStringExtra("data");
                    data = new String(Base64.decode(data));
                    goodType = JSON.parseObject(data, GoodType.class);
                    LogUtil.msg("goodType data->" + data);
                } catch (Exception e) {
                    LogUtil.msg("goodType data->解析异常" + e);
                }
            }
        }

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
        actionBar.setTitle("兑换记录");
        setBackListener();
        actionBar.hideMenuItem();

        adapter = new GBGoodChangeAdapter(this);
        addHeaderAndFooter(listView, true);
        listView.setAdapter(adapter);
        removeFooterView();
        addScrollListener(listView);

        adapter.setOnItemClickListener(new GBGoodChangeAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view) {
                GoodChange goodChange = (GoodChange) view.getTag();
                if (goodChange.getGift_code() != null && !goodChange.getGift_code().isEmpty()) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("礼包码", goodChange.getGift_code());
                    clipboard.setPrimaryClip(clip);
                    DialogGiftUtil.show2(GoodChangeActivity.this, goodChange.getGift_code());
                } else {
                    LogUtil.msg("getGift_code -> null");
                }
            }
        });
    }

    @Override
    public void loadData() {
        super.loadData();
        goodChangeEngin.getList(page, limit, GBApplication.userInfo.getUserId(), game_id, new Callback<List<GoodChange>>() {
            @Override
            public void onSuccess(ResultInfo<List<GoodChange>> resultInfo) {
                success(resultInfo, adapter);
            }

            @Override
            public void onFailure(Response response) {
                fail(adapter.dataInfos == null, getMessage(response.body, DescConstans.NET_ERROR));
            }
        });
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

    @Override
    public int getLayoutID() {
        return R.layout.activity_good_change;
    }
}
