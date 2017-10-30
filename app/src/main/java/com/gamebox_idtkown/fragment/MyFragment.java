package com.gamebox_idtkown.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.activitys.EarnPointAcitivty;
import com.gamebox_idtkown.activitys.GoodTypeActivity;
import com.gamebox_idtkown.activitys.MyGameActivity;
import com.gamebox_idtkown.activitys.MyGiftActivity;
import com.gamebox_idtkown.activitys.PayActivity;
import com.gamebox_idtkown.activitys.SettingActivity;
import com.gamebox_idtkown.activitys.UserInfoActivity;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.DbUtil;
import com.gamebox_idtkown.core.db.greendao.UserInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.SignEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.CheckUtil;
import com.gamebox_idtkown.utils.CircleTransform;
import com.gamebox_idtkown.utils.LoadingUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.ScreenUtil;
import com.gamebox_idtkown.utils.StateUtil;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.widgets.GBActionBar;
import com.gamebox_idtkown.views.widgets.GBMyItemView;
import com.gamebox_idtkown.views.widgets.GBScrollView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/9/22.
 */
public class MyFragment extends BaseActionBarFragment<GBActionBar> {

    @BindView(R.id.scrollView)
    GBScrollView scrollView;

    @BindView(R.id.banner)
    RelativeLayout banner;

    @BindView(R.id.avatar)
    ImageView ivAvatar;

    @BindView(R.id.nickname)
    TextView tvNickName;

    @BindView(R.id.phone)
    TextView tvPhone;

    @BindView(R.id.my_point)
    GBMyItemView myPoint;

    @BindView(R.id.earn_point)
    GBMyItemView eranPoint;

    @BindView(R.id.mall_point)
    GBMyItemView mallPoint;

    @BindView(R.id.my_game)
    GBMyItemView myGame;

    @BindView(R.id.pu_balance)
    GBMyItemView puBalance;


    @BindView(R.id.my_gifts)
    GBMyItemView myGifts;

    @BindView(R.id.setting)
    GBMyItemView setting;


    @BindView(R.id.item)
    RelativeLayout rlItem;

    @BindView(R.id.content)
    RelativeLayout rlContent;

    @BindView(R.id.blank)
    View blankView;

    @Inject
    public MyFragment() {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_my;
    }

    private int height = 0;


    private void reInit() {
        banner.setBackgroundColor(GoagalInfo.getInItInfo().androidColor);
        myPoint.setButton("签到赚积分");
        myPoint.showNumber();

        puBalance.setButton("充值");
        puBalance.showNumber();

        eranPoint.setDesc("做任务，赚积分");
        mallPoint.setDesc("积分当钱花");
    }

    @Override
    public void initViews() {
        super.initViews();
        actionBar.setTitle("个人中心");
        actionBar.setAvatarOnClickListner(new Runnable() {
            @Override
            public void run() {
                if (startLoginActivity()) {
                    Intent intent = new Intent(getContext(), UserInfoActivity.class);
                    startActivity(intent);
                }
            }
        });
        reInit();


        rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startLoginActivity()) {
                    Intent intent = new Intent(getContext(), UserInfoActivity.class);
                    startActivity(intent);
                }
            }
        });

        eranPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startLoginActivity()) {
                    Intent intent = new Intent(getContext(), EarnPointAcitivty.class);
                    startActivity(intent);
                }
            }
        });

        mallPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startLoginActivity()) {
                    Intent intent = new Intent(getContext(), GoodTypeActivity.class);
                    startActivity(intent);
                }
            }
        });

        myGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startLoginActivity()) {
                    Intent intent = new Intent(getContext(), MyGameActivity.class);
                    startActivity(intent);
                }
            }
        });

        myGifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startLoginActivity()) {
                    Intent intent = new Intent(getContext(), MyGiftActivity.class);
                    startActivity(intent);
                }
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        myPoint.tvOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startLoginActivity()) {
                    LoadingUtil.show(getActivity(), "签到中...");
                    SignEngin.getImpl(getContext()).sign(new Callback<String>() {
                        @Override
                        public void onSuccess(final ResultInfo<String> resultInfo) {
                            bindView(new Runnable() {
                                @Override
                                public void run() {
                                    LoadingUtil.dismiss();
                                    if (resultInfo == null) {
                                        ToastUtil.toast2(getContext(), DescConstans.SERVICE_ERROR);
                                        return;
                                    }
                                    if (resultInfo.code == 1) {
                                        try {
                                            float point = Float.parseFloat(resultInfo.data);
                                            myPoint.tvOther.setText("已签到");
                                            myPoint.tvOther.setClickable(false);
                                            StateUtil.setDrawable(getContext(), myPoint.tvOther, 3, Color.GRAY);
                                            GBApplication.userInfo.setCheckTime(getTimeStr());
                                            GBApplication.userInfo.setPoint((Float.parseFloat(GBApplication.userInfo
                                                    .getPoint()) + point) + "");
                                            DbUtil.getSession(getContext()).update(GBApplication.userInfo);
                                            setInfo();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            LogUtil.msg("更新签到->" + e.getMessage());
                                            ToastUtil.toast2(getContext(), getMessage("",
                                                    DescConstans.SERVICE_ERROR3));
                                        }
                                    } else {
                                        ToastUtil.toast2(getContext(), getMessage(resultInfo.message, "签到失败,请重试"));
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

        puBalance.tvOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startLoginActivity()) {
                    Intent intent = new Intent(getContext(), PayActivity.class);
                    startActivity(intent);
                }
            }
        });
        setInfo();
        EventBus.getDefault().register(this);

        height = ScreenUtil.dip2px(getContext(), 200 + 46 * 7 + 40) + 4;
        final int dHeight = ScreenUtil.dip2px(getContext(), 150);
        int aHeight = ScreenUtil.getHeight(getContext()) + ScreenUtil.dip2px(getContext(), 100) - ScreenUtil
                .getStatusBarHeight(getActivity());
        final int navHeight = ScreenUtil.dip2px(getContext(), 90);
        if (height < aHeight) {
            ViewGroup.LayoutParams layoutParams = blankView.getLayoutParams();
            layoutParams.height += aHeight - height;
            blankView.setLayoutParams(layoutParams);
        }

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int y = scrollView.getScrollY();
                if (y >= dHeight) {
                    actionBar.setAlpha(1);
                } else if (y < navHeight) {
                    actionBar.setAlpha(0);
                } else {
                    actionBar.setAlpha((float) y / (float) dHeight);
                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventInit2(Integer type) {
        if (type == EventBusMessage.RE_INIT) {
            setActionBar();
            reInit();
        }
    }

    public String getTimeStr() {
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        return fDate;
    }


    public void setInfo() {
        if (GBApplication.isLogin()) {
            if (GBApplication.userInfo.getCheckTime() != null && GBApplication.userInfo.getCheckTime().equals
                    (getTimeStr())) {
                myPoint.tvOther.setText("已签到");
                myPoint.tvOther.setClickable(false);
                StateUtil.setDrawable(getContext(), myPoint.tvOther, 3, Color.GRAY);
            } else {
                if (GBApplication.userInfo.getSigned()) {
                    myPoint.tvOther.setText("已签到");
                    myPoint.tvOther.setClickable(false);
                    StateUtil.setDrawable(getContext(), myPoint.tvOther, 3, Color.GRAY);
                }
            }

            if (GBApplication.userInfo.getIs_vali_mobile()) {
                String phone = GBApplication.userInfo.getMobile();
                if (phone.length() >= 11) {
                    phone = phone.replace(phone.substring(3, 7), "****");
                }
                tvPhone.setText(phone);
            } else {
                tvPhone.setText(GBApplication.userInfo.getName());
            }
            String nickname = CheckUtil.checkStr(GBApplication.userInfo.getNick_name(), GBApplication.userInfo.getName());
            tvNickName.setText(CheckUtil.checkStr(nickname, DescConstans.NICKNAME));
            myPoint.setNumber(GBApplication.userInfo.getPoint() + "");
            puBalance.setNumber((int)Float.parseFloat(GBApplication.userInfo.getMoney()) + "");
            if (GBApplication.userInfo.getAvatar() != null && !GBApplication.userInfo.getAvatar().isEmpty()) {
                Picasso.with(getContext())
                        .load(GBApplication.userInfo.getAvatar()).placeholder(R.mipmap.avatar_default).transform(new
                        CircleTransform())
                        .into(ivAvatar);
            }
            if (!GBApplication.userInfo.sign_access) {
                myPoint.hideButton();
            }
            actionBar.setAvatar(GBApplication.userInfo.avatarBitmp);
        } else {
            myPoint.setButton("签到赚积分");
            tvNickName.setText("点击直接登录");
            tvPhone.setText("没有帐号? 手机号快速注册");
            myPoint.setNumber("0");
            puBalance.setNumber("0");
            ivAvatar.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.avatar_default));
            actionBar.setAvatar(null);

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserInfo userInfo) {
        setInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Bitmap bitmap) {
        actionBar.setLogoWH(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent2(Integer type) {
        if (type == EventBusMessage.UPDATE_USER_INFO)
            GBApplication.login(getContext(), GBApplication.userInfo.getMobile(), GBApplication.userInfo.getName(), GBApplication.userInfo.getPwd());
    }


}
