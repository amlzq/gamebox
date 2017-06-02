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
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.adpaters.GBGameOpenServiceAdapter;
import com.gamebox_idtkown.views.widgets.GBActionBar;
import com.gamebox_idtkown.views.widgets.GBActionBar2;
import com.gamebox_idtkown.views.widgets.GBBaseActionBar;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * Created by zhangkai on 2017/3/28.
 */

public class GamePreOpenServiceActivity extends BaseGameListActivity<GameOpenServiceInfo, GBActionBar> {
    @BindView(R.id.gamelist)
    ListView gamelist;

    public String keyword = "";
    public String time = "";

    private GBGameOpenServiceAdapter adapter = null;


    @Override
    public void initViews() {
        super.initViews();
        setBackListener();
        actionBar.setTitle("游戏区服列表");


        adapter = new GBGameOpenServiceAdapter(this);
        adapter.setListView(gamelist);
        addHeaderAndFooter(gamelist, true);
        gamelist.setAdapter(adapter);
        removeFooterView();

        loadMoreView.setItemHeight(50);


        actionBar.setOnActionBarItemClickListener(new GBBaseActionBar.OnActionBarItemClickListener() {
            @Override
            public void onSearchClick(View view) {
                Intent intent = new Intent(getBaseContext(), GameOpenServiceActivity.class);
                startActivity(intent);
            }

            @Override
            public void onDownloadClick(View view) {
                Intent intent = new Intent(getBaseContext(), DownloadActivity.class);
                startActivity(intent);
            }
        });

        adapter.setOnItemClickListener(new GBGameOpenServiceAdapter.OnItemClickListener() {
            @Override
            public void onSearch(View view) {
                final GameOpenServiceInfo gameOpenServiceInfo = (GameOpenServiceInfo) view.getTag();
                Intent intent = new Intent(getBaseContext(), GameOpenServiceActivity.class);
                intent.putExtra("keyword", gameOpenServiceInfo.getGame_name());
                startActivity(intent);
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
                datePicker = new DatePicker(GamePreOpenServiceActivity.this);
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
                datePicker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get
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
        return R.layout.activity_pre_game_open_service;
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
