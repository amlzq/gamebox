package com.gamebox_idtkown.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.activitys.LoginActivity;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.utils.AnimationUtil;
import com.gamebox_idtkown.utils.CheckUtil;
import com.gamebox_idtkown.utils.LoadingUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.widgets.GBNoView;
import com.umeng.analytics.game.UMGameAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangkai on 16/9/12.
 */
public abstract class BaseFragment extends Fragment {
    @Nullable
    @BindView(R.id.refresh)
    protected SwipeRefreshLayout refreshLayout;

    public int loaded = 0;

    private View view;

    public void initVars() {
    }

    public void initViews() {
        ButterKnife.bind(this, view);
        setRefreshLayout();
    }

    public void setRefreshLayout(){
        if (refreshLayout != null) {
            refreshLayout.setColorSchemeColors(GoagalInfo.getInItInfo().androidColor, GoagalInfo.getInItInfo().androidColor);
        }
    }

    public void loadData() {
        disableRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), getLayoutID(), null);
            try {
                initVars();
                initViews();
                loadData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    public abstract int getLayoutID();

    public void bindView(Runnable runnable) {
        try {
            getActivity().runOnUiThread(runnable);
        } catch (Exception e) {
            LogUtil.msg("bindView->" + e);
        }
    }



    public void bindViewDelay(Runnable runnable, int second) {
        view.postDelayed(runnable, second);
    }

    public void stopRefreshing(int n) {
        if (refreshLayout != null && refreshLayout.isRefreshing() && loaded >= n) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        UMGameAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        UMGameAgent.onPageEnd(this.getClass().getSimpleName());
    }

    private GBNoView noView = null;
    private View noDataView = null;

    public void showNoDataView() {
        removeNoView();
        noDataView = getActivity().getLayoutInflater().inflate(R.layout
                .view_no, null, false);
        noView = GBNoView.getInstance(getActivity(), noDataView);
        noView.setNoDataView("暂无数据");
        ((ViewGroup) view).addView(noDataView);
        noView.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewGroup) view).removeView(noDataView);
                loadData();
                noDataView = null;
                noView = null;
            }
        });
    }

    public void showNoDataView2() {
        removeNoView();
        noDataView = getActivity().getLayoutInflater().inflate(R.layout
                .view_no2, null, false);
        noView = GBNoView.getInstance(getActivity(), noDataView);
        noView.setNoDataView("暂无数据");
        ((ViewGroup) view).addView(noDataView);
        noView.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewGroup) view).removeView(noDataView);
                loadData();
                noDataView = null;
                noView = null;
            }
        });
    }

    public View processView = null;

    public void removeProcessView() {
        if (processView != null) {
            ImageView loading = (ImageView) processView.findViewById(R.id.loading);
            loading.clearAnimation();
            ((ViewGroup) view).removeView(processView);
            processView = null;
        }
    }

    public void showProcessView() {
        processView = getActivity().getLayoutInflater().inflate(R.layout
                .view_process, null, false);
        ImageView loading = (ImageView) processView.findViewById(R.id.loading);
        loading.startAnimation(AnimationUtil.rotaAnimation());
        ((ViewGroup) view).addView(processView);
    }

    public void showNoDataView(String data) {
        removeNoView();
        noDataView = getActivity().getLayoutInflater().inflate(R.layout
                .view_no, null, false);
        noView = GBNoView.getInstance(getActivity(), noDataView);
        noView.setNoDataView(data);
        ((ViewGroup) view).addView(noDataView);
        noView.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewGroup) view).removeView(noDataView);
                loadData();
                noDataView = null;
                noView = null;
            }
        });
    }


    public void showNoNetView(boolean flag, String msg) {
        removeNoView();
        if (!flag || !CheckUtil.isNetworkConnected(getActivity()))  {
            if(GBApplication.is_swich){
                return;
            }
            ToastUtil.toast(getActivity(), msg);
            return;
        }
        noDataView = getActivity().getLayoutInflater().inflate(R.layout
                .view_no2, null, false);
        noView = GBNoView.getInstance(getActivity(), noDataView);
        noView.setNoNetView(msg);
        ((ViewGroup) view).addView(noDataView);
        noView.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewGroup) view).removeView(noDataView);
                noDataView = null;
                loadData();
                noView = null;
            }
        });
    }

    public void showNoNetView2(boolean flag, String msg) {
        removeNoView();
        if (!flag || !CheckUtil.isNetworkConnected(getActivity())) {
            if(GBApplication.is_swich){
                return;
            }
            ToastUtil.toast(getActivity(), msg);
            return;
        }
        noDataView = getActivity().getLayoutInflater().inflate(R.layout
                .view_no, null, false);
        noView = GBNoView.getInstance(getActivity(), noDataView);
        noView.setNoNetView(msg);
        ((ViewGroup) view).addView(noDataView);
        noView.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewGroup) view).removeView(noDataView);
                noDataView = null;
                loadData();
                noView = null;
            }
        });
    }

    public void removeNoView() {
        try {
            if (noDataView != null) {
                ((ViewGroup) view).removeView(noDataView);
                noDataView = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop(final int n) {
        bindView(new Runnable() {
            @Override
            public void run() {
                loaded++;
                if (loaded >= n) {
                    enableRefresh();
                    stopRefreshing(n);
                    removeProcessView();
                }
            }
        });
    }

    public void stop() {
        stop(1);
    }

    public void disableRefresh() {
        if (refreshLayout != null) {
            refreshLayout.setEnabled(false);
        }
    }

    public void enableRefresh() {
        if (refreshLayout != null) {
            refreshLayout.setEnabled(true);
        }
    }

    public String getMessage(String msg, String replaceMsg) {
        return msg != null && !msg.isEmpty() ? msg : replaceMsg;
    }

    public void error() {
        bindView(new Runnable() {
            @Override
            public void run() {
                LoadingUtil.dismiss();
                ToastUtil.toast2(getContext(), DescConstans.SERVICE_ERROR);
            }
        });
    }

    public boolean startLoginActivity() {
        if (!GBApplication.isLogin()) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
