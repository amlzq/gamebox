package com.gamebox_idtkown.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.GameOpenServiceInfo;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.GameOpenServiceEngin;
import com.gamebox_idtkown.engin.SearchEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.adpaters.GBGameOpenServiceAdapter;
import com.gamebox_idtkown.views.adpaters.GBHGameListAdapter;
import com.gamebox_idtkown.views.widgets.GBActionBar2;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * Created by zhangkai on 2017/3/15.
 */

public class GameOpenServiceActivity extends BaseGameListActivity<GameOpenServiceInfo, GBActionBar2> {
    @BindView(R.id.gamelist)
    ListView gamelist;

    public String keyword = "";
    public String time = "";
    private GBGameOpenServiceAdapter adapter = null;

    @Override
    public void initVars() {
        super.initVars();
        Intent intent = this.getIntent();
        if (intent != null) {
            keyword = intent.getStringExtra("keyword");
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        actionBar.setKeyWord(keyword);
        setBackListener();
        actionBar.setOnSearchListener(new GBActionBar2.OnSearchListener() {
            @Override
            public void onSearch(View view) {

                if (keyword.equals(actionBar.getKeyWord())) {
                    return;
                }

                keyword = actionBar.getKeyWord();

                if (keyword == null || keyword.isEmpty()) {
                    ToastUtil.toast(getBaseContext(), "关键字不能为空");
                    return;
                }

                view.setClickable(false);

                hideKeyboard();
                adapter.dataInfos = null;
                loadData();
            }
        });

        adapter = new GBGameOpenServiceAdapter(this);
        adapter.setListView(gamelist);
        addHeaderAndFooter(gamelist, true);
        gamelist.setAdapter(adapter);
        removeFooterView();

        loadMoreView.setItemHeight(50);

        adapter.setOnItemClickListener(new GBGameOpenServiceAdapter.OnItemClickListener() {
            @Override
            public void onSearch(View view) {
                final GameOpenServiceInfo gameOpenServiceInfo = (GameOpenServiceInfo) view.getTag();
                if (gameOpenServiceInfo.getGame_name().equals(actionBar.getKeyWord())) {
                    return;
                }
                Intent intent = new Intent(getBaseContext(), GameOpenServiceActivity.class);
                intent.putExtra("keyword", gameOpenServiceInfo.getGame_name());
                startActivity(intent);
                finish();
            }

            @Override
            public void onDownload(TextView view) {
                final GameOpenServiceInfo gameOpenServiceInfo = (GameOpenServiceInfo) view.getTag();
                GameInfo gameInfo = new GameInfo();
                gameInfo.setGameId(gameOpenServiceInfo.getGame_id());
                gameInfo.setType(gameOpenServiceInfo.getType() + "");
                startGameDetailActivity(gameInfo);
            }

            @Override
            public void OnClikeTime(TextView view) {
                page = 1;
                datePicker = new DatePicker(GameOpenServiceActivity.this);
                datePicker.setTextColor(GoagalInfo.getInItInfo().androidColor);
                datePicker.setCancelTextColor(Color.RED);
                datePicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        time = year + month + day;
                        search();
                    }
                });
                datePicker.setRangeStart(2013, 1, 1);
                datePicker.setRangeEnd(2050, 12, 31);
                Calendar calendar = Calendar.getInstance();
                datePicker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get
                        (Calendar.DAY_OF_MONTH));
                datePicker.show();
            }
        });

        addScrollListener(gamelist);

    }

    private DatePicker datePicker;


    @Override
    public void loadData() {
        super.loadData();
        if (keyword == null || keyword.isEmpty()) {
            keyword = "";
        }
        search();
    }

    public void search() {
        GameOpenServiceEngin.getImpl(this).getOpenService(page, limit, keyword, time, new
                Callback<List<GameOpenServiceInfo>>() {
                    @Override
                    public void onSuccess(final ResultInfo<List<GameOpenServiceInfo>> resultInfo) {
                        success(resultInfo, adapter);
                        setClickable(true);
                    }

                    @Override
                    public void onFailure(Response response) {
                        fail(adapter.dataInfos == null, getMessage(response.body, DescConstans.NET_ERROR));
                        setClickable(true);
                    }
                });
    }

    public void setClickable(final boolean flag) {
        bindView(new Runnable() {
            @Override
            public void run() {
                actionBar.setClickable(flag);
            }
        });
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_game_open_service;
    }

    @Override
    public void removeFooterView() {
        if (gamelist.getFooterViewsCount() > 0) {
            gamelist.removeFooterView(footerView);
        }
    }

    @Override
    public void addFooterView() {
        if (gamelist.getFooterViewsCount() <= 0) {
            gamelist.addFooterView(footerView);
        }
    }

    @Override
    public void update(DownloadInfo downloadInfo) {

    }
}
