package com.gamebox_idtkown.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.core.db.greendao.GiftDetail;
import com.gamebox_idtkown.core.db.greendao.GoodList;
import com.gamebox_idtkown.core.db.greendao.GoodType;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.di.dagger2.components.DaggerEnginComponent;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.GETGiftEngin;
import com.gamebox_idtkown.engin.GameInfoEngin;
import com.gamebox_idtkown.engin.GiftDetailEngin;
import com.gamebox_idtkown.engin.GoodConvertEngin;
import com.gamebox_idtkown.engin.GoodDetailEngin;
import com.gamebox_idtkown.net.contains.HttpConfig;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.Blur;
import com.gamebox_idtkown.utils.CheckUtil;
import com.gamebox_idtkown.utils.CircleTransform;
import com.gamebox_idtkown.utils.DialogGiftUtil;
import com.gamebox_idtkown.utils.LoadingUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.ScreenUtil;
import com.gamebox_idtkown.utils.StateUtil;
import com.gamebox_idtkown.utils.StatusBarUtil;
import com.gamebox_idtkown.utils.TaskUtil;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.widgets.GBActionBar;
import com.gamebox_idtkown.views.widgets.GBDownloadBtn;
import com.gamebox_idtkown.views.widgets.GBImageButton;
import com.gamebox_idtkown.views.widgets.GBScrollView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/10/24.
 */
public class GiftDetailActivity extends BaseActionBarActivity<GBActionBar> {

    @BindView(R.id.icon2)
    ImageView ivIcon2;

    @BindView(R.id.icon3)
    ImageView ivIcon3;

    @BindView(R.id.gift_get)
    GBDownloadBtn btnGiftGet;

    @BindView(R.id.title2)
    TextView tvTitle2;

    @BindView(R.id.back)
    GBImageButton backImageButton;

    @BindView(R.id.surplusNum)
    TextView tvSurplusNum;

    @BindView(R.id.access_date)
    TextView tvAccessDate;

    @BindView(R.id.gift_content)
    TextView tvContext;

    @BindView(R.id.gift_method)
    TextView tvMethod;

    @BindView(R.id.gift_channel)
    TextView tvChannel;

    @BindView(R.id.bg_actionbar)
    RelativeLayout bgActionbar;

    @BindView(R.id.rl_actionbar)
    RelativeLayout rlActionbar;


    @BindView(R.id.gift_icon)
    ImageView getIvIcon;

    @BindView(R.id.gift_icon3)
    ImageView getIvIcon2;

    @BindView(R.id.gift_icon4)
    ImageView getIvIcon3;

    @BindView(R.id.scrollView)
    GBScrollView scrollView;

    private GiftDetail giftDetail;
    private GoodList goodList;


    @Inject
    GiftDetailEngin giftDetailEngin;

    @BindView(R.id.gameInfo)
    RelativeLayout gameInfoRl;

    @BindView(R.id.app_icon)
    ImageView appIcon;

    @BindView(R.id.app_title)
    TextView appTitle;

    @BindView(R.id.size)
    TextView tvSize;

    @BindView(R.id.type)
    TextView tvType;

    @BindView(R.id.gift_title2)
    TextView tvGiftTitle;

    @BindView(R.id.has_gift)
    TextView tvGift;

    @BindView(R.id.desc)
    TextView tvDesc;

    @BindView(R.id.download2)
    GBDownloadBtn btnDownload;

    @BindView(R.id.num_price)
    TextView tvNumPrice;

    @BindView(R.id.section4)
    RelativeLayout rlSection4;

    @Inject
    GoodConvertEngin goodConvertEngin;

    @Inject
    GoodDetailEngin goodDetailEngin;

    @BindView(R.id.score)
    TextView tvScore;

    @Override
    public int getLayoutID() {
        return R.layout.activity_gift_detail;
    }

    @Override
    public void initVars() {
        super.initVars();
        DaggerEnginComponent.create().injectGiftDetail(this);
        Intent intent = this.getIntent();
        if (intent != null) {
            String good_id = intent.getStringExtra("good_id");
            giftDetail = (GiftDetail) intent.getSerializableExtra("game_detail");
            goodList = (GoodList) intent.getSerializableExtra("good_list");
            if (giftDetail == null) {
                String data = intent.getStringExtra("data");
                if (data != null) {
                    giftDetailEngin.getGiftInfo(data, new Callback<String>() {
                        @Override
                        public void onSuccess(ResultInfo<String> resultInfo) {
                            try {
                                giftDetail = JSON.parseObject(resultInfo.data, GiftDetail.class);
                                bindView(new Runnable() {
                                    @Override
                                    public void run() {
                                        showInfo();
                                    }
                                });
                            } catch (Exception e) {
                                LogUtil.msg("giftDetail->解析异常" + e);
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
            }

            if (good_id != null) {
                goodDetailEngin.getList(good_id, new Callback<GoodList>() {
                    @Override
                    public void onSuccess(ResultInfo<GoodList> resultInfo) {
                        if (resultInfo.code == HttpConfig.STATUS_OK) {
                            goodList = resultInfo.data;
                            bindView(new Runnable() {
                                @Override
                                public void run() {
                                    showInfo();
                                }
                            });
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
        }
    }


    @Override
    public void initViews() {
        StatusBarUtil.setColor(this, Color.TRANSPARENT);
        StateUtil.softKey(getWindow());
        full = true;
        super.initViews();
        reinit();
        bgActionbar.setAlpha(0.0f);
        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                try {
                    if (GBApplication.icon == null) {
                        Bitmap icon = Picasso.with(getBaseContext()).load(R.mipmap.gift_detail_bg).get();
                        GBApplication.icon = Blur.fastblur(getBaseContext(), icon, 20);
                    }
                    bindView(new Runnable() {
                        @Override
                        public void run() {
                            ivIcon2.setImageBitmap(GBApplication.icon);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.msg("Picasso get->" + e.getMessage());
                }
            }
        });
        setBackListener();
        showInfo();

        EventBus.getDefault().register(this);
    }

    private void reinit() {
        bgActionbar.setBackgroundColor(GoagalInfo.getInItInfo().androidColor);
        getIvIcon.setBackgroundColor(GoagalInfo.getInItInfo().androidColor);
        getIvIcon2.setBackgroundColor(GoagalInfo.getInItInfo().androidColor);
        getIvIcon3.setBackgroundColor(GoagalInfo.getInItInfo().androidColor);
        StateUtil.setDrawable(getBaseContext(), btnGiftGet, 1.5f, GoagalInfo.getInItInfo().androidColor);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventInit3(final Integer type) {
        if (type == EventBusMessage.RE_INIT) {
            reinit();
        }
    }


    private void showInfo() {
        if (goodList != null) {
            tvScore.setVisibility(View.VISIBLE);
            rlSection4.setVisibility(View.GONE);

            actionBar.setTitle(goodList.getName());
            tvSurplusNum.setText(goodList.getPrice());
            String time = goodList.getUc_end_time();
            if (time.equals("0") || time.equals("1970-01-01")) {
                time = "-";
            }
            tvAccessDate.setText(time);
            tvTitle2.setText(goodList.getName());

            tvNumPrice.setText("积分");
            btnGiftGet.setText("兑换");
            tvGiftTitle.setText("兑换内容");
            String score = "当前积分：<font color=#ff0000>" + GBApplication.userInfo.getPoint() + "</font>";
            tvScore.setText(Html.fromHtml(score));
            String desc = goodList.getName() + "<br/>";
            if (goodList.getType_id().equals("2")) {
                desc = goodList.getDesp();
            } else if (goodList.getType_id().equals("1")) {
                desc += "<font color=#ff0000>满" + goodList.getUc_money() + "元可用</font><br/>价值<font color=#ff0000>" + goodList.getType_val() + "元</font>";
            } else {
                desc += "价值<font color=#ff0000>" + goodList.getType_val() + "元</font>";
            }

            tvContext.setText(Html.fromHtml(desc));
            tvMethod.setText(goodList.getUse_method());
            Picasso.with(this).load(goodList.getImg()).placeholder(R.mipmap.icon_default).transform(new CircleTransform()).into
                    (ivIcon3);

            btnGiftGet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    change(goodList);
                }
            });

        }
        if (giftDetail != null)

        {
            tvScore.setVisibility(View.GONE);
            actionBar.setTitle(giftDetail.getName());
            actionBar.hideMenuItem();
            tvTitle2.setText(giftDetail.getName());
            backImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            tvAccessDate.setText(giftDetail.getAccess_date());
            tvSurplusNum.setText(giftDetail.getSurplusNum());

            tvContext.setText(Html.fromHtml(giftDetail.getContent()));
            tvMethod.setText(Html.fromHtml(giftDetail.getChange_note()));
            tvChannel.setText(Html.fromHtml(giftDetail.getDesp()));

            Picasso.with(this).load(giftDetail.getImgUrl()).placeholder(R.mipmap.icon_default).transform(new CircleTransform()).into
                    (ivIcon3);

            btnGiftGet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (startLoginActivity()) {
                        LoadingUtil.show(GiftDetailActivity.this, "领取中...");
                        GETGiftEngin.getImpl(getBaseContext()).getGift(giftDetail.getGiftId(), new Callback<HashMap>() {
                            @Override
                            public void onSuccess(final ResultInfo<HashMap> resultInfo) {
                                bindView(new Runnable() {
                                    @Override
                                    public void run() {
                                        LoadingUtil.dismiss();
                                        if (resultInfo == null) {
                                            ToastUtil.toast2(getBaseContext(), DescConstans.NET_ERROR);
                                            return;
                                        }

                                        if (resultInfo.code == 1 || resultInfo.code == 2) {
                                            String title = "已成功领取";
                                            int surplusNum = Integer.parseInt(giftDetail.getSurplusNum());
                                            if (resultInfo.code == 1) {
                                                surplusNum = Integer.parseInt(giftDetail.getSurplusNum()) - 1;
                                                giftDetail.setSurplusNum(surplusNum + "");
                                                tvSurplusNum.setText(giftDetail.getSurplusNum());
                                                EventBus.getDefault().post(giftDetail);
                                            } else {
                                                title = "已领取";
                                            }
                                            String code = resultInfo.data.get("code") + "";
                                            DialogGiftUtil.show(GiftDetailActivity.this, code, title);
                                            if (surplusNum == 0) {
                                                btnGiftGet.setEnabled(false);
                                                btnGiftGet.setClickable(false);
                                                StateUtil.setDrawable(getBaseContext(), btnGiftGet, 1.5f, Color.parseColor
                                                        ("#999999"));
                                            }
                                            return;
                                        } else {
                                            getGameInfo(giftDetail.getGameId(), "1");
                                            ToastUtil.toast2(getBaseContext(), getMessage(resultInfo.message, "领取失败"));
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Response response) {
                                error();
                            }
                        });
                    }
                }
            });
        }


        final int actionBarHeight = ScreenUtil.dip2px(this, 50);
        scrollView.getViewTreeObserver().

                addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        int y = scrollView.getScrollY();
                        if (y > 0) {
                            rlActionbar.setAlpha(0);
                        } else if (y <= 0) {
                            rlActionbar.setAlpha(1);
                        }
                        bgActionbar.setAlpha((float) y / actionBarHeight);
                    }
                });

    }


    private void change(final GoodList goodList) {
        if (startLoginActivity()) {
            goodConvertEngin.change(goodList.getGoodId(), GBApplication.userInfo.getUserId(), new Callback<String>() {
                @Override
                public void onSuccess(final ResultInfo<String> resultInfo) {
                    dimiss();
                    bindView(new Runnable() {
                        @Override
                        public void run() {
                            if (resultInfo != null && resultInfo.code == 1) {
                                if (!goodList.getType_id().equals("2")) {
                                    DialogGiftUtil.show3(GiftDetailActivity.this, "价值" + goodList.getType_val() + "元" +
                                                    goodList
                                                            .getName(), "兑换成功",
                                            "确定");
                                } else {
                                    if (resultInfo.data != null) {
                                        String code = resultInfo.data.replace("\"", "").replace("[", "").replace("]", "");
                                        DialogGiftUtil.show(GiftDetailActivity.this, code, "兑换成功");
                                    }
                                }
                                EventBus.getDefault().post(EventBusMessage.UPDATE_USER_INFO);
                            } else {
                                ToastUtil.toast2(getBaseContext(), getMessage(resultInfo.message, "兑换失败"));
                            }
                        }
                    });
                }

                @Override
                public void onFailure(Response response) {
                    dimiss();
                    ToastUtil.toast2(getBaseContext(), getMessage(response.body, DescConstans.NET_ERROR));
                }
            });
        } else {
            dimiss();
        }
    }

    private void dimiss() {
        bindView(new Runnable() {
            @Override
            public void run() {
                LoadingUtil.dismiss();
            }
        });
    }


    private GameInfo gameInfo;

    private void setInfo() {
        if (gameInfo == null) {
            return;
        }

        List<GameInfo> gameInfos = new ArrayList<GameInfo>();
        gameInfos.add(gameInfo);
        ApkStatusUtil.getStatuss(getBaseContext(), gameInfos);
        gameInfoRl.setVisibility(View.VISIBLE);
        gameInfoRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameInfo.isdownload = true;
                startGameDetailActivity(gameInfo);
            }
        });
        ApkStatusUtil.setButtonStatus(this, btnDownload, gameInfo.status);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApkStatusUtil.actionByStatus(GiftDetailActivity.this, gameInfo, btnDownload, new Runnable() {
                    @Override
                    public void run() {
                        startGameDetailActivity(gameInfo);
                    }
                });
            }
        });

        if (gameInfo.getCateName().isEmpty()) {
            tvType.setVisibility(View.GONE);
        } else {
            tvType.setVisibility(View.VISIBLE);
            tvType.setText(gameInfo.getCateName());
        }
        appTitle.setText(gameInfo.getName());
        tvSize.setText(gameInfo.getDownloadTimes() + "次下载  " + gameInfo.getSize_text());
        tvDesc.setText("版本: " + CheckUtil.checkStr(gameInfo.getVersion(), "未知") + "  更新时间: " + CheckUtil.checkStr
                (gameInfo.getUpdateTime(), "未知"));
        StateUtil.setDrawable(getBaseContext(), tvGift, 1.5f, Color.parseColor("#ffc000"));
        StateUtil.setDrawable(getBaseContext(), tvType, 1.5f, Color.parseColor("#ff5555"));
        Picasso.with(this).load(gameInfo.getIconUrl()).placeholder(R.mipmap.icon_default).into(appIcon);

        if (gameInfo.getHasGift() > 0) {
            tvGift.setVisibility(View.VISIBLE);
        } else {
            tvGift.setVisibility(View.GONE);
        }
    }

    private void getGameInfo(String game_id, String type) {
        new GameInfoEngin().getGameInfo(game_id, type, new Callback<String>() {
            @Override
            public void onSuccess(ResultInfo<String> resultInfo) {
                try {
                    if (resultInfo.code == 1) {
                        gameInfo = JSON.parseObject(resultInfo.data, GameInfo.class);
                        bindView(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    setInfo();
                                } catch (Exception e) {
                                    LogUtil.msg("bindView->异常" + e);
                                }
                            }
                        });
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DownloadInfo downloadInfo) {
        if (downloadInfo.url.equals(gameInfo.getUrl())) {
            gameInfo.setStatus(downloadInfo.getStatus());
            ApkStatusUtil.setButtonStatus(getBaseContext(), btnDownload, gameInfo.getStatus());
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
