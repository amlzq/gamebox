package com.gamebox_idtkown.fragment;

import android.widget.GridView;
import android.widget.ListView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.domain.GameOpenServiceInfo;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.views.adpaters.OpenServiceListAdapter;
import com.gamebox_idtkown.views.widgets.GBBaseActionBar;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/4/27.
 */

public class OpenServiceFragment extends BaseGameListFragment<GameOpenServiceInfo, GBBaseActionBar> {

    @BindView(R.id.id_stickynavlayout_innerscrollview)
    ListView gridView;

    OpenServiceListAdapter adapter;
    public  ResultInfo<List<GameOpenServiceInfo>> datainfos;


    @Override
    public int getLayoutID() {
        return R.layout.fragment_open_service;
    }

    @Override
    public void initViews() {
        super.initViews();
        adapter = new OpenServiceListAdapter(getActivity());
        footerView = getActivity().getLayoutInflater().inflate(R.layout
                .view_blank_footer, null, false);
        gridView.addFooterView(footerView);
        gridView.setAdapter(adapter);
        notifyDataSetChanged(datainfos);
    }

    public void notifyDataSetChanged(ResultInfo<List<GameOpenServiceInfo>> datainfos){
        success(datainfos, adapter);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void update(DownloadInfo downloadInfo) {

    }

    @Override
    public void removeFooterView() {

    }

    @Override
    public void addFooterView() {

    }


}
