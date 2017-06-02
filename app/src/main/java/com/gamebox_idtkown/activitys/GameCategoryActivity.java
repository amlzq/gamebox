package com.gamebox_idtkown.activitys;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.cache.GameTypeCache;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GameType;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.GameCateEnngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.TaskUtil;
import com.gamebox_idtkown.views.adpaters.GBGameCategoryAdpater;
import com.gamebox_idtkown.views.widgets.GBActionBar;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/9/26.
 */
public class GameCategoryActivity extends BaseGameListActivity<GameType, GBActionBar> {
    @BindView(R.id.gamecategory)
    ListView gameCateListView;

    @Override
    public int getLayoutID() {
        return R.layout.activity_game_category;
    }

    private GBGameCategoryAdpater adapter = null;

    @Override
    public void initViews() {
        super.initViews();
        actionBar.setTitle("游戏分类");
        setBackListener();

        adapter = new GBGameCategoryAdpater(this);
        adapter.setOnItemClickListener(new GBGameCategoryAdpater.OnItemClickListener() {
            @Override
            public void onClick(View view) {
                GameType gameType = (GameType) view.getTag();
                if (gameType != null) {
                    Intent intent = new Intent(getBaseContext(), GameCategoryDetailActivity.class);
                    intent.putExtra("game_type", gameType);
                    startActivity(intent);
                }
            }
        });
        addHeaderAndFooter(gameCateListView, true);
        gameCateListView.setAdapter(adapter);
        removeFooterView();

    }

    @Override
    public void update(DownloadInfo downloadInfo) {

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
                        List<GameType> gameTypes = GameTypeCache.getCache(getBaseContext());
                        readCache(adapter, gameTypes);
                    }
                });
                getCategoryList();
            }
        });
    }

    public void getCategoryList() {
        GameCateEnngin.getImpl(this).agetResultInfo(true, null, new Callback<List<GameType>>() {
            @Override
            public void onSuccess(final ResultInfo<List<GameType>> resultInfo) {
                bindView(new Runnable() {
                    @Override
                    public void run() {
                        success(resultInfo, adapter, new Runnable() {
                            @Override
                            public void run() {
                                GameTypeCache.setCache(getBaseContext(), resultInfo.data);
                            }
                        });
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
    public void removeFooterView() {
        if (gameCateListView.getFooterViewsCount() > 0) {
            gameCateListView.removeFooterView(footerView);
        }
    }

    @Override
    public void addFooterView() {
        if (gameCateListView.getFooterViewsCount() <= 0) {
            gameCateListView.addFooterView(footerView);
        }
    }

}
