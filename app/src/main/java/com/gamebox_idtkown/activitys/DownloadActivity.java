package com.gamebox_idtkown.activitys;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.fragment.DownloadFragment;
import com.gamebox_idtkown.fragment.UpdateFragment;
import com.gamebox_idtkown.security.Base64;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.views.widgets.GBActionBar3;
import com.gamebox_idtkown.views.widgets.GBTabItem;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URLDecoder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/9/26.
 */
public class DownloadActivity extends BaseActionBarActivity<GBActionBar3> {

    @BindView(R.id.tab0)
    GBTabItem tab0;

    @BindView(R.id.tab1)
    GBTabItem tab1;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    FragmentAdapter fragmentAdapter;
    int currentIndex = 0;

    private String from;

    @Override
    public int getLayoutID() {
        return R.layout.activity_download;
    }

    @Override
    public void initVars() {
        super.initVars();
        Intent intent = getIntent();
        if (intent != null) {
            String data = intent.getStringExtra("data");
            String title = intent.getStringExtra("title");
            if (data != null) {
                try {
                    data = new String(Base64.decode(data));
                    LogUtil.msg("data=" + data);
                    DownloadInfo downloadInfo = JSON.parseObject(data, DownloadInfo.class);
                    if(title != null) {
                        downloadInfo.name = URLDecoder.decode(title, "utf-8");
                    }
                    if (downloadInfo.name != null && downloadInfo.url != null) {
                        ApkStatusUtil.downloadByDownloadInfo(this, downloadInfo);
                    }
                } catch (Exception e) {
                    LogUtil.msg("数据解析错误data=" + e);
                }
            }
            from = intent.getStringExtra("from");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventInit222222(Integer type) {
        if (type == EventBusMessage.RE_INIT){
                tab(currentIndex);
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        setBackListener();
        actionBar.setTitle("下载管理");

        tab0.setTag(0);
        tab0.setTitle("下载中");
        tab0.selected();

        tab1.setTag(1);
        tab1.setTitle("可升级");


        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());

        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (currentIndex == position) {
                    return;
                }
                currentIndex = position;
                tab(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        actionBar.myGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startLoginActivity()) {
                    Intent intent = new Intent(getBaseContext(), MyGameActivity.class);
                    startActivity(intent);
                }
            }
        });

        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.tab0, R.id.tab1})
    public void onClick(View view) {
        int idx = (int) view.getTag();
        if (currentIndex == idx) {
            return;
        }
        viewPager.setCurrentItem(idx);
    }

    public void tab(int idx) {
        if (idx == 0) {
            tab0.selected();
            tab1.cancel();
        } else if (idx == 1) {
            tab1.selected();
            tab0.cancel();
        }
    }

    class FragmentAdapter extends FragmentPagerAdapter {
        private DownloadFragment downloadFragment;
        private UpdateFragment updateFragment;

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (downloadFragment == null) {
                    downloadFragment = new DownloadFragment();
                }
                return downloadFragment;
            }

            if (position == 1) {
                if (updateFragment == null) {
                    updateFragment = new UpdateFragment();
                }
                return updateFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (from != null) {
            JumpActivity.disableActivity(getBaseContext());
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
