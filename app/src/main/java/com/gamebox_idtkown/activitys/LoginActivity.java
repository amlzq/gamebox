package com.gamebox_idtkown.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.db.greendao.UserInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.CheckCodeEngin;
import com.gamebox_idtkown.engin.LoginEngin;
import com.gamebox_idtkown.engin.RegisterEngin;
import com.gamebox_idtkown.engin.SendCodeEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.GameBox2SDKUtil;
import com.gamebox_idtkown.utils.LoadingUtil;
import com.gamebox_idtkown.utils.ScreenUtil;
import com.gamebox_idtkown.utils.StateUtil;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.widgets.GBActionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/10/25.
 */
public class LoginActivity extends BaseActionBarActivity<GBActionBar> {

    @BindView(R.id.banner)
    View ivBanner;

    @BindView(R.id.tab)
    LinearLayout rlTab;

    @BindView(R.id.tab1)
    TextView tvTab1;

    @BindView(R.id.tab2)
    TextView tvTab2;

    @BindView(R.id.rl_checkcode)
    RelativeLayout rlCheckCode;

    @BindView(R.id.login_register)
    TextView btnLoingRegister;

    @BindView(R.id.sendcode)
    TextView btnSendCode;

    @BindView(R.id.forgot)
    TextView btnForgot;

    @BindView(R.id.et_phone)
    EditText etPhone;

    @BindView(R.id.tv_phone)
    TextView tvPhone;

    @BindView(R.id.et_checkcode)
    EditText etCheckCode;

    @BindView(R.id.et_password)
    EditText etPassword;

    @Override
    public int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    public void initViews() {
        super.initViews();
        setBackListener();
        actionBar.setTitle("登录");
        actionBar.hideMenuItem();
        ivBanner.setBackgroundColor(GoagalInfo.getInItInfo().androidColor);
        setStroke(this, rlTab, r);
        setTabLeft(this, tvTab1, r);
        tvTab2.setTextColor(GoagalInfo.getInItInfo().androidColor);
        tvTab1.setClickable(false);

        tvPhone.setText("用户名");
        etPhone.setHint("输入手机号或游戏帐号");
        StateUtil.setDrawable(this, btnLoingRegister, r);

        StateUtil.setDrawable(this, btnSendCode, r);
        StateUtil.setCursorDrawableColor(getBaseContext(), etPhone, GoagalInfo.getInItInfo().androidColor);
        StateUtil.setCursorDrawableColor(getBaseContext(), etCheckCode, GoagalInfo.getInItInfo().androidColor);
        StateUtil.setCursorDrawableColor(getBaseContext(), etPassword, GoagalInfo.getInItInfo().androidColor);

        EventBus.getDefault().register(this);
    }

    final Handler handler = new Handler();
    private int secondes = 60;

    private void codeRefresh() {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final Integer type) {
        if (type == EventBusMessage.FINISH) {
            finish();
        }
    }

    public void setStroke(Context context, View view, float n) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setStroke(ScreenUtil.dip2px(context, 1), GoagalInfo.getInItInfo().androidColor);
        drawable.setCornerRadius(ScreenUtil.dip2px(context, n));
        view.setBackground(drawable);
    }

    public void setTabLeft(Context context, View view, float n) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(GoagalInfo.getInItInfo().androidColor);
        int r = ScreenUtil.dip2px(context, n);
        drawable.setCornerRadii(new float[]{r, r, 0, 0, 0, 0, r, r});
        view.setBackground(drawable);
    }

    public void setTabRight(Context context, View view, float n) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(GoagalInfo.getInItInfo().androidColor);
        int r = ScreenUtil.dip2px(context, n);
        drawable.setCornerRadii(new float[]{0, 0, r, r, r, r, 0, 0});
        view.setBackground(drawable);
    }

    public void login() {
        if (!checkPhone()) {
            return;
        }

        if (!checkPassword()) {
            return;
        }

        LoadingUtil.show(this, "正在登录...");


        LoginEngin.getImpl(this).login(phone, phone, password, new Callback<UserInfo>() {
            @Override
            public void onSuccess(final ResultInfo<UserInfo> resultInfo) {
                bindView(new Runnable() {
                    @Override
                    public void run() {
                        LoadingUtil.dismiss();
                        if (resultInfo == null) {
                            ToastUtil.toast2(getBaseContext(), DescConstans.SERVICE_ERROR);
                            return;
                        }
                        if (resultInfo.data != null) {
                            if (resultInfo.code == 1) {
                                ToastUtil.toast2(getBaseContext(), getMessage(resultInfo.message,
                                        "登录成功"));
                                GBApplication.userInfo = resultInfo.data;
                                GBApplication.reInit(LoginActivity.this);
                                GBApplication.setUserInfo(
                                        getBaseContext());
                                GBApplication.getUserBitmap(getBaseContext());
                                GBApplication.insertGameUserInfo(getBaseContext());
                                finish();
                                return;
                            }
                        }
                        ToastUtil.toast2(getBaseContext(), getMessage(resultInfo.message, "登录失败"));
                    }
                });
            }

            @Override
            public void onFailure(Response response) {
                error();
            }
        });
    }

    public void register() {
        if (!checkPhone()) {
            return;
        }

        if (!checkCode()) {
            return;
        }

        if (!checkPassword()) {
            return;
        }

        LoadingUtil.show(this, "正在验证...");
        CheckCodeEngin.getImpl(this).check(phone, phone, code, DescConstans.SEND_TYPE1, new Callback<String>() {
            @Override
            public void onSuccess(final ResultInfo<String> resultInfo) {
                bindView(new Runnable() {
                    @Override
                    public void run() {
                        LoadingUtil.dismiss();
                        if (resultInfo == null) {
                            ToastUtil.toast2(getBaseContext(), DescConstans.SERVICE_ERROR);
                            return;
                        }
                        if (resultInfo.code == 1) {
                            LoadingUtil.show(LoginActivity.this, "正在注册...");
                            RegisterEngin.getImpl(getBaseContext()).register(phone, phone, password, "0", new
                                    Callback<UserInfo>() {
                                        @Override
                                        public void onSuccess(final ResultInfo<UserInfo> resultInfo) {
                                            bindView(new Runnable() {
                                                @Override
                                                public void run() {
                                                    LoadingUtil.dismiss();
                                                    if (resultInfo == null) {
                                                        ToastUtil.toast2(getBaseContext(), DescConstans.SERVICE_ERROR);
                                                        return;
                                                    }
                                                    if (resultInfo.code == 1 && resultInfo.data != null) {
                                                        ToastUtil.toast2(getBaseContext(), getMessage(resultInfo.message,
                                                                "注册成功"));
                                                        GBApplication.userInfo = resultInfo.data;
                                                        GBApplication.userInfo.setSex("0");
                                                        GBApplication.userInfo.setSigned(false);
                                                        GBApplication.setUserInfo(getBaseContext());
                                                        GBApplication.insertGameUserInfo(getBaseContext());
                                                        GameBox2SDKUtil.UserInfo g2sUserInfo = GameBox2SDKUtil
                                                                .exchangeUserInfo(GBApplication.userInfo, 0);
                                                        GameBox2SDKUtil.insertUserInfo(getBaseContext(), g2sUserInfo);
                                                        finish();
                                                    } else {
                                                        ToastUtil.toast2(getBaseContext(), getMessage(resultInfo.message, "注册失败"));
                                                    }
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(Response response) {
                                            error();
                                        }
                                    });
                        } else {
                            ToastUtil.toast2(getBaseContext(), getMessage(resultInfo.message, "验证失败,请重新获取验证码"));
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

    public void sendCode() {
        if (checkPhone()) {
            LoadingUtil.show(this, "正在发送...");
            SendCodeEngin.getImpl(this).send(phone, phone, DescConstans.SEND_TYPE1, new Callback<String>() {
                @Override
                public void onSuccess(final ResultInfo<String> resultInfo) {
                    bindView(new Runnable() {
                        @Override
                        public void run() {
                            LoadingUtil.dismiss();
                            if (resultInfo == null) {
                                ToastUtil.toast2(getBaseContext(), DescConstans.SERVICE_ERROR);
                                return;
                            }
                            if (resultInfo.code == 1) {
                                codeRefresh();
                                ToastUtil.toast2(getBaseContext(), getMessage(resultInfo.message, "验证码发送成功,请注意查收"));
                            } else {
                                ToastUtil.toast2(getBaseContext(), getMessage(resultInfo.message, "验证码发送失败,请重试"));
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

    private String phone;
    private String password;
    private String code;

    private boolean checkPhone() {
        boolean flag = true;
        phone = etPhone.getText().toString();
        if (phone.isEmpty()) {
            flag = false;
            String msg = DescConstans.PHONE_EMPTY;
            if (rlCheckCode.getVisibility() != View.GONE) {
                msg = DescConstans.PHONE_EMPTY2;
            }
            ToastUtil.toast2(this, msg);
        }
        return flag;
    }

    private boolean checkCode() {
        boolean flag = true;
        code = etCheckCode.getText().toString();
        if (code.isEmpty()) {
            flag = false;
            ToastUtil.toast2(this, DescConstans.CODE_EMPTY);
        }
        return flag;
    }


    private boolean checkPassword() {
        boolean flag = true;
        password = etPassword.getText().toString();
        if (password.isEmpty() || password.length() < 6) {
            flag = false;
            ToastUtil.toast2(this, DescConstans.PASSWORD_EMPTY);
        }
        return flag;
    }

    @OnClick({R.id.tab1, R.id.tab2, R.id.sendcode, R.id.login_register, R.id.forgot})
    public void onClick(View view) {
        if (view.getId() == tvTab1.getId()) {
            tvTab1.setClickable(false);
            tvTab2.setClickable(true);
            tvTab1.setTextColor(Color.WHITE);
            tvTab2.setTextColor(GoagalInfo.getInItInfo().androidColor);
            tvTab2.setBackgroundColor(Color.parseColor("#00000000"));
            rlCheckCode.setVisibility(View.GONE);
            setTabLeft(this, tvTab1, r);
            actionBar.setTitle("登录");
            tvPhone.setText("用户名");
            etPhone.setHint("输入手机号或游戏帐号");
            btnLoingRegister.setText("立即登录");
            etPassword.setText("");
            return;
        }
        if (view.getId() == tvTab2.getId()) {
            tvTab2.setClickable(false);
            tvTab1.setClickable(true);
            tvTab1.setBackgroundColor(Color.parseColor("#00000000"));
            tvTab2.setTextColor(Color.WHITE);
            tvTab1.setTextColor(GoagalInfo.getInItInfo().androidColor);
            setTabRight(this, tvTab2, r);
            actionBar.setTitle("注册");
            etPhone.setHint("输入手机号");
            tvPhone.setText("手机号");
            rlCheckCode.setVisibility(View.VISIBLE);
            btnLoingRegister.setText("注册");
            etPassword.setText("");
            return;
        }

        if (view.getId() == btnSendCode.getId()) {
            sendCode();
            return;
        }

        if (view.getId() == btnLoingRegister.getId()) {
            if (rlCheckCode.getVisibility() == View.GONE) {
                login();
            } else {
                register();
            }
            return;
        }

        if (view.getId() == btnForgot.getId()) {
            ForgotActivity.loginActivity = this;
            Intent intent = new Intent(this, ForgotActivity.class);
            intent.putExtra("phone", etPhone.getText());
            startActivity(intent);
            return;
        }

    }

    @Override
    public void finish() {
        super.finish();
        ForgotActivity.loginActivity = null;
    }
}
