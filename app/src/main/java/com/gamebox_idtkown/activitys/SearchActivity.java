package com.gamebox_idtkown.activitys;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.GridView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.SearchTagEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.adpaters.GBTGridViewAdapter;
import com.gamebox_idtkown.views.widgets.GBActionBar2;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/9/26.
 */
public class SearchActivity extends BaseGameListActivity<String, GBActionBar2> {
    @BindView(R.id.gamelist)
    GridView gamelist;

    @Override
    public int getLayoutID() {
        return R.layout.activity_search;
    }

    @Override
    public void initViews() {
        super.initViews();
        setBackListener();

        actionBar.setOnSearchListener(new GBActionBar2.OnSearchListener() {
            @Override
            public void onSearch(View view) {
                String keyword = actionBar.getKeyWord();
                if (keyword == null || keyword.isEmpty()) {
                    ToastUtil.toast(getBaseContext(), "关键字不能为空");
                    return;
                }
                Intent intent = new Intent(getBaseContext(), Search2Activity.class);
                intent.putExtra("keyword", keyword);
                startActivity(intent);
                finish();
            }
        });

        adapter = new GBTGridViewAdapter(this);
        gamelist.setAdapter(adapter);

        //刷新
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        adapter.setOnItemClickListener(new GBTGridViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view) {
                String tag = (String) view.getTag();
                String keyword = tag;
                Intent intent = new Intent(getBaseContext(), Search2Activity.class);
                intent.putExtra("keyword", keyword);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void update(DownloadInfo downloadInfo) {

    }

    private GBTGridViewAdapter adapter = null;

    @Override
    public void loadData() {
        super.loadData();

        SearchTagEngin.getImpl(this).getTags(new Callback<List<String>>() {
            @Override
            public void onSuccess(final ResultInfo<List<String>> resultInfo) {
               success(resultInfo, adapter);
            }

            @Override
            public void onFailure(Response response) {
                fail(adapter.dataInfos == null, getMessage(response.body, DescConstans.NET_ERROR));
            }
        });
    }

    @Override
    public void removeFooterView() {}

    @Override
    public void addFooterView() {}
}
