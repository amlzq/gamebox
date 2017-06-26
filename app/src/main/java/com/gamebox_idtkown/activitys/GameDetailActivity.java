package com.gamebox_idtkown.activitys;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.ApkStatus;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GameDetail;
import com.gamebox_idtkown.core.db.greendao.GameImage;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.di.dagger2.components.DaggerEnginComponent;
import com.gamebox_idtkown.domain.GameOpenServiceInfo;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.GameInfoEngin;
import com.gamebox_idtkown.engin.GameOpenServiceEngin;
import com.gamebox_idtkown.fragment.GameDetailFragment;
import com.gamebox_idtkown.fragment.GiftListFragment;
import com.gamebox_idtkown.fragment.OpenServiceFragment;
import com.gamebox_idtkown.net.contains.HttpConfig;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.security.Base64;
import com.gamebox_idtkown.services.DownloadManagerService;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.CheckUtil;
import com.gamebox_idtkown.utils.DialogShareUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.ScreenUtil;
import com.gamebox_idtkown.utils.ShareUtil;
import com.gamebox_idtkown.utils.StateUtil;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.widgets.GBActionBar;
import com.gamebox_idtkown.views.widgets.GBDownloadBtn;
import com.gamebox_idtkown.views.widgets.GBTabItem2;
import com.gxz.library.StickyNavLayout;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/9/26.
 */
public class GameDetailActivity extends BaseActionBarActivity<GBActionBar> {

    @BindView(R.id.tab0)
    GBTabItem2 tab0;

    @BindView(R.id.tab1)
    GBTabItem2 tab1;

    @BindView(R.id.tab2)
    GBTabItem2 tab2;

    @BindView(R.id.id_stickynavlayout_viewpager)
    ViewPager viewPager;


    @BindView(R.id.icon2)
    ImageView ivIcon2;

    @BindView(R.id.title2)
    TextView tvTitle2;

    @BindView(R.id.size)
    TextView tvSize;

    @BindView(R.id.type)
    TextView tvType;

    @BindView(R.id.has_gift)
    TextView tvGift;

    @BindView(R.id.desc)
    TextView tvDesc;

    @BindView(R.id.download3)
    GBDownloadBtn btnDownload3;

    @BindView(R.id.download2)
    GBDownloadBtn downView;

    @BindView(R.id.download4)
    TextView downView2;

    @BindView(R.id.process)
    GBDownloadBtn processView;

    @BindView(R.id.main_pay)
    TextView tvMainPay;

    @BindView(R.id.section5)
    RelativeLayout rlSection5;

    @BindView(R.id.rl_wraper)
    StickyNavLayout stickyNavLayout;

    public GameInfo gameInfo;

    public String shareUrl;
    public static List<GameImage> gameImages;

    FragmentAdapter fragmentAdapter;
    int currentIndex = 0;

    @Inject
    GameInfoEngin gameInfoEngin;

    @Override
    public int getLayoutID() {
        return R.layout.activity_game_detail;
    }

    @Override
    public void initVars() {
        super.initVars();
        DaggerEnginComponent.create().injectGameInfo(this);
        Intent intent = this.getIntent();
        if (intent != null) {
            gameInfo = (GameInfo) intent.getSerializableExtra("game_info");
            if (gameInfo == null) {
                String data = intent.getStringExtra("data");
                if (data != null) {
                    try {
                        data = new String(Base64.decode(data));
                        HashMap map = JSON.parseObject(data, HashMap.class);
                        String game_id = map.get("id") + "";
                        String type = map.get("type") + "";
                        getGameInfo(game_id, type);
                    } catch (Exception e) {
                        LogUtil.msg("data->解析异常" + e);
                    }
                }
            } else {
                if (gameInfo.getUrl() == null || gameInfo.getUrl().isEmpty()) {
                    getGameInfo(gameInfo.getGameId(), gameInfo.getType());
                }
            }
        }
    }

    private void getGameInfo(String game_id, String type) {
        gameInfoEngin.getGameInfo(game_id, type, new Callback<String>() {
            @Override
            public void onSuccess(ResultInfo<String> resultInfo) {
                try {
                    if (resultInfo.code == 1) {
                        gameInfo = JSON.parseObject(resultInfo.data, GameInfo.class);
                        List<GameInfo> gameInfos = new ArrayList<GameInfo>();
                        gameInfos.add(gameInfo);
                        ApkStatusUtil.getStatuss(getBaseContext(), gameInfos);
                        bindView(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    showInfo();
                                } catch (Exception e) {
                                    LogUtil.msg("bindView->异常" + e);
                                }
                            }
                        });
                    } else {
                        ToastUtil.toast2(GameDetailActivity.this, getMessage(resultInfo.message, "游戏不存在"));
                    }
                } catch (Exception e) {
                    LogUtil.msg("gameInfo->解析异常" + e);
                }
            }

            @Override
            public void onFailure(final Response response) {
                bindView(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.toast2(getBaseContext(), getMessage(response.body, DescConstans.NET_ERROR));
                    }
                });
            }
        });
    }

    @Override
    public boolean ERROR() {
//        if (gameInfo == null) {
//            return true;
//        }
        return false;
    }

    @Override
    public void initViews() {
        super.initViews();
        actionBar.setTitle("游戏详情");
        actionBar.hideMenuItem();
        setBackListener();
        width = ScreenUtil.dip2px(this, 250);

        EventBus.getDefault().register(this);

        StateUtil.setStorke(this, tvMainPay, 3);
        StateUtil.setColor(tvMainPay);

        showInfo();
    }

    private void showInfo() {
        if (gameInfo == null || gameInfo.getUrl() == null || gameInfo.getUrl().isEmpty()) {
            return;
        }

        actionBar.setOnShareClickLister(new GBActionBar.OnShareClickLister() {
            @Override
            public void onClick(View v) {
                DialogShareUtil.show(GameDetailActivity.this, 0);
                DialogShareUtil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogShareUtil.dismiss();
                        int type = Integer.parseInt(v.getTag() + "");
                        if (type == 2) {
                            ClipboardManager clipboard = (ClipboardManager) getBaseContext().getSystemService(Context
                                    .CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("下载链接", gameInfo.getDown_url());
                            clipboard.setPrimaryClip(clip);
                            ToastUtil.toast2(GameDetailActivity.this, "复制成功");
                            return;
                        } else if (type == 1) {
                            ShareUtil.openWXShareWithImage(GameDetailActivity.this, gameInfo.getName() + shareUrl, gameImages,
                                    type);
                        } else {
                            ShareUtil.OpenWxShareText(GameDetailActivity.this, gameInfo.getName() + shareUrl);
                        }
                    }
                });
            }
        });
        setInfo();



        if (gameInfo.isdownload && (gameInfo.status == ApkStatus.UNDOWNLOAD || gameInfo.status == ApkStatus.WAITING
                || gameInfo.status == ApkStatus.WAIT_LISTENER)) {
            download();
        }
    }

    private void initFragments(){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
            }
        });

    }

    @OnClick({R.id.process, R.id.download2, R.id.download3})
    public void onClick2(View view) {
        download();
    }

    public void download() {
        setDownloadButtonInfo();
        ApkStatusUtil.actionByStatus(GameDetailActivity.this, gameInfo, null, new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public void setHeight(int height) {
        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) viewPager.getLayoutParams();
        layoutParams.height = height;
        viewPager.setLayoutParams(layoutParams);
        viewPager.requestLayout();
    }

    private void setInfo() {
        setDownloadButtonInfo();
        if (gameInfo.getCateName().isEmpty()) {
            tvType.setVisibility(View.GONE);
        } else {
            tvType.setVisibility(View.VISIBLE);
            tvType.setText(gameInfo.getCateName());
        }
        tvTitle2.setText(gameInfo.getName());
        tvSize.setText(gameInfo.getDownloadTimes() + "次下载  " + gameInfo.getSize_text());
        tvDesc.setText("版本: " + CheckUtil.checkStr(gameInfo.getVersion(), "未知") + "  更新时间: " + CheckUtil.checkStr
                (gameInfo.getUpdateTime(), "未知"));
        StateUtil.setDrawable(getBaseContext(), tvGift, 1.5f, Color.parseColor("#ffc000"));
        StateUtil.setDrawable(getBaseContext(), tvType, 1.5f, Color.parseColor("#ff5555"));
        Picasso.with(this).load(gameInfo.getIconUrl()).placeholder(R.mipmap.icon_default).into(ivIcon2);

        tab0.setTag(0);
        tab0.setTitle("简介");
        tab0.selected();

        tab1.setTag(1);
        tab1.setTitle("礼包");

        tab2.setTag(2);
        tab2.setTitle("开服");

        processView.setAlpha(0.95f);
        StateUtil.setDrawable(this, processView, 1.5f);


        if (gameInfo.getHasGift() > 0) {
            fcount++;
            tab1.setVisibility(View.VISIBLE);
            tvGift.setVisibility(View.VISIBLE);
        } else {
            tab1.setVisibility(View.GONE);
            tvGift.setVisibility(View.GONE);
        }

        search();
    }

    private int fcount = 1;
    OpenServiceFragment openServiceFragment;

    class FragmentAdapter extends FragmentStatePagerAdapter {

        GameDetailFragment gameDetailFragment;
        GiftListFragment giftListFragment;


        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (gameDetailFragment == null) {
                    gameDetailFragment = new GameDetailFragment();
                }
                return gameDetailFragment;
            } else if (position == 1) {
                if (giftListFragment == null && tab1.getVisibility() == View.VISIBLE) {
                    giftListFragment = new GiftListFragment();
                }

                if(tab1.getVisibility() == View.VISIBLE){
                    return giftListFragment;
                }

                if (openServiceFragment == null && tab2.getVisibility() == View.VISIBLE) {
                    openServiceFragment = new OpenServiceFragment();
                    openServiceFragment.datainfos = datainfos;
                }

                if(tab2.getVisibility() == View.VISIBLE){
                    return openServiceFragment;
                }


            } else if (position == 2) {
                if (openServiceFragment == null) {
                    openServiceFragment = new OpenServiceFragment();
                    openServiceFragment.datainfos = datainfos;
                }
                return openServiceFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return fcount;
        }
    }


    public ResultInfo<List<GameOpenServiceInfo>> datainfos;

    public void search() {
        GameOpenServiceEngin.getImpl(this).getOpenService(1, 100, gameInfo.getName(), "", new
                Callback<List<GameOpenServiceInfo>>() {
                    @Override
                    public void onSuccess(final ResultInfo<List<GameOpenServiceInfo>> resultInfo) {
                        if (resultInfo.code == HttpConfig.STATUS_OK && resultInfo.data != null && resultInfo.data
                                .size() > 0) {
                            fcount++;
                            datainfos = resultInfo;
                            GameDetailActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (openServiceFragment != null) {
                                        openServiceFragment.notifyDataSetChanged(datainfos);
                                    }
                                }
                            });
                        } else {
                            GameDetailActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tab2.setVisibility(View.GONE);
                                }
                            });
                        }
                        initFragments();
                    }

                    @Override
                    public void onFailure(Response response) {
                        GameDetailActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tab2.setVisibility(View.GONE);
                            }
                        });
                        initFragments();
                    }
                });
    }

    private void setDownloadButtonInfo() {
        DownloadInfo downloadInfo = DownloadManagerService.getDownloadInfo(gameInfo);
        if (downloadInfo == null) {
            ApkStatusUtil.setButtonStatus(getBaseContext(), downView, gameInfo.getStatus());
            ApkStatusUtil.setButtonStatus(getBaseContext(), btnDownload3, gameInfo.getStatus());
        } else {
            ApkStatusUtil.setButtonStatus(getBaseContext(), downView, gameInfo.getStatus());
            ApkStatusUtil.setButtonStatus(getBaseContext(), btnDownload3, gameInfo.getStatus());
            showProcess(downloadInfo);
        }
    }

    private void showProcess(DownloadInfo downloadInfo) {
        FrameLayout.LayoutParams l = (FrameLayout.LayoutParams) processView.getLayoutParams();
        if (downloadInfo.getStatus() != ApkStatus.INSTALLED && downloadInfo.getStatus() != ApkStatus.DOWNLOADED) {
            if (downloadInfo.getPrecent() > 0.01) {
                l.width = (int) (width * downloadInfo.getPrecent());
            }
            if (downloadInfo.getPrecent() > 0) {

                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                //构造方法的字符格式这里如果小数不足2位,会以0补足.
                downView2.setText(downView.getText() + "(" + (decimalFormat.format(downloadInfo.getPrecent() * 100)) +
                        "%)");
                downView.setText("");
                StateUtil.setDrawable(getBaseContext(), downView, 2.5f, Color.parseColor("#999999"));
            }
        } else {
            l.width = 0;
            downView2.setText("");
        }
        processView.setLayoutParams(l);
    }

    private int width;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DownloadInfo downloadInfo) {
        if (downloadInfo.url.equals(gameInfo.getUrl())) {
            gameInfo.setStatus(downloadInfo.getStatus());
            ApkStatusUtil.setButtonStatus(getBaseContext(), downView, gameInfo.getStatus());
            ApkStatusUtil.setButtonStatus(getBaseContext(), btnDownload3, gameInfo.getStatus());
            showProcess(downloadInfo);
            if (downloadInfo.getStatus() == ApkStatus.INSTALLED) {
                gameInfo.setPackageName(downloadInfo.packageName);
            }
        }
    }

    @OnClick({R.id.tab0, R.id.tab1, R.id.tab2})
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
            tab2.cancel();
        } else if (idx == 1) {
            if(tab1.getVisibility() == View.VISIBLE){
                tab1.selected();
                tab2.cancel();
            }
            else if(tab2.getVisibility() == View.VISIBLE){
                tab2.selected();
            }
            tab0.cancel();
        } else if (idx == 2) {
            tab2.selected();
            tab0.cancel();
            tab1.cancel();
        }
    }

    @Override
    public void loadData() {
        super.loadData();
        showProcessView();
    }

    public void show() {
        bindView(new Runnable() {
            @Override
            public void run() {
                actionBar.showShare();
                stickyNavLayout.setVisibility(View.VISIBLE);
                rlSection5.setVisibility(View.VISIBLE);
                removeProcessView();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
