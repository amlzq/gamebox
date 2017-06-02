package com.gamebox_idtkown.fragment;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.ApkUtil;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.GameUpdateEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.views.adpaters.GBGameUpdateAdpater;
import com.gamebox_idtkown.views.widgets.GBActionBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/9/29.
 */
public class UpdateFragment extends BaseGameListFragment<GameInfo, GBActionBar> {

    @BindView(R.id.gamelist)
    ListView gameList;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_update;
    }

    private GBGameUpdateAdpater adpater = null;

    @Override
    public void initViews() {
        super.initViews();

        View headerView = getActivity().getLayoutInflater().inflate(R.layout
                .view_blank2, null, false);
        adpater = new GBGameUpdateAdpater(getContext());
        adpater.setListView(gameList);
        gameList.addHeaderView(headerView);
        gameList.setAdapter(adpater);

        adpater.setOnItemClickListener(new GBGameUpdateAdpater.OnItemClickListener() {
            @Override
            public void onDetail(View view) {
                GameInfo gameInfo = (GameInfo) view.getTag();
                startGameDetailActivity(gameInfo);
            }

            @Override
            public void onDownload(TextView view) {
                final GameInfo gameInfo = (GameInfo) view.getTag();
                ApkStatusUtil.actionByStatus(getActivity(), gameInfo, view, new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
    }


    private String get6071Games(Context context) {
        String json = "[";
        int length = ApkUtil.packageNames.size();
        for (int i = 0; i < length; i++) {
            String packname = ApkUtil.packageNames.get(i);
            if (packname.contains("6071")) {
                String version = ApkUtil.getApkVersion(context, packname);
                json += "{\"package\":\"" + packname + "\",\"version\":\"" + version + "\"},";
            }
            if (i == length - 1 && json.length() > 1) {
                json = json.substring(0, json.length() - 1);
            }
        }
        json += "]";
        LogUtil.msg("UpdateFragment json->" + json);
        return json;
    }

    @Override
    public void loadData() {
        super.loadData();
        String json = get6071Games(getContext());
        GameUpdateEngin.getImpl(getContext()).getUpdaetGames(json, new Callback<List<GameInfo>>() {
            @Override
            public void onSuccess(final ResultInfo<List<GameInfo>> resultInfo) {
                bindView(new Runnable() {
                    @Override
                    public void run() {
                        stop();
                        if (resultInfo != null && resultInfo.data != null && resultInfo.data.size() > 0 && resultInfo.code
                                == 1) {
                            adpater.dataInfos = resultInfo.data;
                            adpater.notifyDataSetChanged();
                        } else {
                            showNoDataView2();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Response response) {
                fail(adpater.dataInfos == null, getMessage(response.body, DescConstans.NET_ERROR));
            }
        });
    }

    @Override
    public void update(DownloadInfo downloadInfo) {
        adpater.updateView(downloadInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer type) {
        if (type == EventBusMessage.REFRESH_INFO) {
            adpater.notifyDataSetChanged();
        }
    }

    @Override
    public void removeFooterView() {

    }

    @Override
    public void addFooterView() {

    }
}
