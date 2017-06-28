package com.gamebox_idtkown.activitys;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.di.dagger2.components.DaggerMainActivityComponent;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.domain.InItInfo;
import com.gamebox_idtkown.fragment.BaseFragment;
import com.gamebox_idtkown.fragment.ChosenFragment;
import com.gamebox_idtkown.fragment.GiftsFragment;
import com.gamebox_idtkown.fragment.IndexFragment;
import com.gamebox_idtkown.fragment.MyFragment;
import com.gamebox_idtkown.services.DownloadManagerService;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.DialogUpdateUtil;
import com.gamebox_idtkown.utils.LoadingUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.PreferenceUtil;
import com.gamebox_idtkown.utils.TaskUtil;
import com.gamebox_idtkown.views.widgets.GBTabBar;
import com.umeng.analytics.MobclickAgent;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;


public class MainActivity extends BaseActivity implements GBTabBar.OnTabSelectedListener {

    @BindView(R.id.tabbar)
    GBTabBar tabBar;

    @BindView(R.id.main_pay)
    RelativeLayout rlMainPay;

    @Inject
    IndexFragment indexFragment;
    @Inject
    ChosenFragment chosenFragment;
    @Inject
    GiftsFragment giftsFragment;
    @Inject
    MyFragment myFragment;

    int selectedIndex = -1;
    private FragmentManager fragmentManager;

    private static MainActivity mainActivity = null;

    public static MainActivity getImpl() {
        return mainActivity;
    }

    @Override
    public void initVars() {
        super.initVars();

        mainActivity = this;
        DaggerMainActivityComponent.create().inject(this);
        fragmentManager = getSupportFragmentManager();
        EventBus.getDefault().register(this);


    }

    private void preInstall() {
        try {
            if (PreferenceUtil.getImpl(this).getBoolean("preInstall", false)) {
                return;
            }
            PreferenceUtil.getImpl(this).putBoolean("preInstall", true);
            if (GoagalInfo.channelInfo != null && GoagalInfo.channelInfo.gameList != null && GoagalInfo.channelInfo
                    .gameList.size() > 0) {
                for (GameInfo gameInfo : GoagalInfo.channelInfo.gameList) {
                    ApkStatusUtil.downloadByGameInfo(MainActivity.this, gameInfo, null, new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
                Intent intent = new Intent(this, DownloadActivity.class);
                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.msg("预安装出错->" + e);
        }
    }

    private void update() {
        if (GoagalInfo.getInItInfo() != null && GoagalInfo.getInItInfo().is_strong) {
            DialogUpdateUtil.getImpl().show(this, "版本更新", true);
        } else {
            preInstall();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final Integer type) {
        if (type == EventBusMessage.TAB_CHOSEN) {
            tabBar.tab(type);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent2(final Integer type) {
        if (type == EventBusMessage.RE_INIT) {
            tabBar.tab(selectedIndex);
        }
    }


    @Override
    public void initViews() {
        super.initViews();
        tabBar.setOnTabSelectedListener(this);
        Intent intent = getIntent();
        int tab = 0;
        String jump = null;
        if (intent != null) {
            tab = intent.getIntExtra("tab", 0);
            jump = intent.getStringExtra("jump");
        }
        tabBar.tab(tab);
        update();
        ApkStatusUtil.createShortcuts(MainActivity.this);
        InItInfo inItInfo = GoagalInfo.getInItInfo();
        if (jump != null && !jump.isEmpty()) {
            if (inItInfo.getType().equals("0")) {
                GameInfo gameInfo = new GameInfo();
                gameInfo.setGameId(inItInfo.getGameId());
                gameInfo.setType(inItInfo.getGameType());
                startGameDetailActivity(gameInfo);
            } else if (inItInfo.getType().equals("1")) {
                String url = inItInfo.getTypeValue();
                if (!url.contains("http://")) {
                    url = "http://" + url;
                }
                Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse(url));
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
            }
        }

        rlMainPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BenActivity.class);
                startActivity(intent);
                MobclickAgent.onEvent(MainActivity.this, "ben", "点击首页充值返利按钮");
            }
        });

    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void onSelected(int idx) {
        if (idx == selectedIndex) return;

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        BaseFragment fragment = getFragment(idx);
        BaseFragment currentFragment = getFragment(selectedIndex);
        selectedIndex = idx;

        if (currentFragment == null) {
            transaction.add(R.id.content, fragment).commit();
            return;
        }

        if (fragment.isAdded()) {
            transaction.hide(currentFragment).show(fragment).commit();
        } else {
            transaction.hide(currentFragment).add(R.id.content, fragment).commit();
        }

    }

    @Override
    public void loadData() {
    }

    private BaseFragment getFragment(int idx) {
        switch (idx) {
            case 0:
                return indexFragment;
            case 1:
                return chosenFragment;
            case 2:
                return giftsFragment;
            case 3:
                return myFragment;
        }
        return null;
    }

    private long clickTime = 0;

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("data") != null) {
            super.onBackPressed();
            return;
        }
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            clickTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
        } else {
            DownloadManagerService.pauseAll();
            stopService(new Intent(this, DownloadManagerService.class));
            System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownloadManagerService.pauseAll();
        stopService(new Intent(this, DownloadManagerService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        if (indexFragment != null) {
            indexFragment.startap();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        if (indexFragment != null) {
            indexFragment.stopap();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GBApplication.is_swich = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        GBApplication.is_swich = false;
    }
}


