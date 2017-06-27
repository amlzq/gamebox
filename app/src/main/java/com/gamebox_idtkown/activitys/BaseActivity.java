package com.gamebox_idtkown.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.utils.AnimationUtil;
import com.gamebox_idtkown.utils.CheckUtil;
import com.gamebox_idtkown.utils.LoadingUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.ScreenUtil;
import com.gamebox_idtkown.utils.StatusBarUtil;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.widgets.GBNoView;
import com.umeng.analytics.game.UMGameAgent;


import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by zhangkai on 16/8/29.
 */
public abstract class BaseActivity extends FragmentActivity {
    @Nullable
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    public float r = 1.5f;
    protected boolean full = false;

    public int statusBarHeight = 0;

    public boolean ERROR() {
        return false;
    }

    public boolean isNeedLogin() {
        return false;
    }

    public void initVars() {
    }

    public void initViews() {
        try {
            ButterKnife.bind(this);
            setBackground();
            setRefreshLayout();
            statusBarHeight = ScreenUtil.getStatusBarHeight(this);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.msg("initViews->初始化失败");
        }
    }


    public void loadData() {
        disableRefresh();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(getLayoutID());
        initVars();
        if (ERROR()) {
            finish();
            return;
        }
        if (isNeedLogin() && !GBApplication.isLogin()) {
            finish();
            return;
        }
        initViews();
        loadData();
    }

    public abstract int getLayoutID();

    private GBNoView noView = null;
    private View noDataView = null;

    public void showNoDataView() {
        removeNoView();
        noDataView = this.getLayoutInflater().inflate(R.layout
                .view_no, null, false);
        noView = GBNoView.getInstance(this, noDataView);
        noView.setNoDataView("暂无数据");
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


    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showNoDataView(String data) {
        removeNoView();
        noDataView = this.getLayoutInflater().inflate(R.layout
                .view_no, null, false);
        noView = GBNoView.getInstance(this, noDataView);
        noView.setNoDataView(data);
        ((ViewGroup) view).addView(noDataView);
        noView.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewGroup) view).removeView(noDataView);
                noDataView = null;
                noView = null;
                loadData();
            }
        });
    }

    public void showNoNetView(boolean flag) {
        showNoNetView(flag, DescConstans.NET_ERROR);
    }

    public void showNoNetView(boolean flag, String msg) {
        removeNoView();
        if (!flag || !CheckUtil.isNetworkConnected(this)) {
            if(GBApplication.is_swich){
                return;
            }
            ToastUtil.toast(this, msg);
            return;
        }
        noDataView = this.getLayoutInflater().inflate(R.layout
                .view_no, null, false);
        noView = GBNoView.getInstance(this, noDataView);
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

    public void showNoNetView(String msg) {
        removeNoView();
        noDataView = this.getLayoutInflater().inflate(R.layout
                .view_no, null, false);
        noView = GBNoView.getInstance(this, noDataView);
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

    public View view;

    protected void setBackground() {
        view = findViewById(android.R.id.content);
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.bg));
        if (!full) {
            StatusBarUtil.setColor(this, GoagalInfo.getInItInfo().androidColor);
        }
    }


    public void setRefreshLayout() {
        if (refreshLayout != null) {
            refreshLayout.setColorSchemeColors(GoagalInfo.getInItInfo().androidColor, GoagalInfo.getInItInfo().androidColor);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshLayout.setRefreshing(false);
                }
            });
        }
    }

    public boolean active = false;

    @Override
    public void onResume() {
        super.onResume();
        // 集成基本统计分析,初始化 Session
        active = true;
        UMGameAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // //集成基本统计分析, 结束 Session
        active = false;
        UMGameAgent.onPause(this);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    public void bindView(Runnable runnable) {
        this.runOnUiThread(runnable);
    }

    public void bindViewDelay(Runnable runnable, int second) {
        view.postDelayed(runnable, second);
    }

    public int loaded = 0;

    public void stopRefreshing(int n) {
        if (refreshLayout != null && refreshLayout.isRefreshing() && loaded >= n) {
            refreshLayout.setRefreshing(false);
        }
    }

    public View processView = null;

    public void removeProcessView() {
        try {
            if (processView != null && view != null) {
                ImageView loading = (ImageView) processView.findViewById(R.id.loading);
                loading.clearAnimation();
                ((ViewGroup) view).removeView(processView);
                processView = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.msg("removeProcessView->" + e.getMessage());
        }
    }

    public void showProcessView() {
        processView = this.getLayoutInflater().inflate(R.layout
                .view_process, null, false);
        ImageView loading = (ImageView) processView.findViewById(R.id.loading);
        loading.startAnimation(AnimationUtil.rotaAnimation());
        if (view == null || processView == null) return;
        ((ViewGroup) view).addView(processView);
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

    public void success() {

    }

    public void startGameDetailActivity(GameInfo gameInfo) {
        Intent intent = new Intent(getBaseContext(), GameDetailActivity.class);
        intent.putExtra("game_info", gameInfo);
        startActivity(intent);
    }

    public boolean startLoginActivity() {
        if (!GBApplication.isLogin()) {
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

    public String getMessage(String msg, String replaceMsg) {
        return msg != null && !msg.isEmpty() ? msg : replaceMsg;
    }

    public void error() {
        bindView(new Runnable() {
            @Override
            public void run() {
                LoadingUtil.dismiss();
                ToastUtil.toast2(getBaseContext(), DescConstans.SERVICE_ERROR);
            }
        });
    }


    final Handler handler = new Handler();
    private int secondes = 60;

    public void codeRefresh(final TextView btnSendCode) {
        secondes = 60;
        btnSendCode.setClickable(false);
        btnSendCode.setText("重新发送(" + secondes + ")");
        btnSendCode.setBackgroundColor(Color.GRAY);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (secondes-- <= 0) {
                    btnSendCode.setClickable(true);
                    btnSendCode.setBackgroundColor(GoagalInfo.getInItInfo().androidColor);
                    btnSendCode.setText("发送验证码");
                    return;
                }
                btnSendCode.setClickable(false);
                btnSendCode.setText("重新发送(" + secondes + ")");
                btnSendCode.setBackgroundColor(Color.GRAY);
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }


}
