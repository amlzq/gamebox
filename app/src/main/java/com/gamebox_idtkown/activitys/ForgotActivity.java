package com.gamebox_idtkown.activitys;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.CheckCodeEngin;
import com.gamebox_idtkown.engin.ForgotEngin;
import com.gamebox_idtkown.engin.SendCodeEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.GameBox2SDKUtil;
import com.gamebox_idtkown.utils.LoadingUtil;
import com.gamebox_idtkown.utils.StateUtil;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.widgets.GBActionBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/10/25.
 */
public class ForgotActivity extends BaseActionBarActivity<GBActionBar> {
    @BindView(R.id.banner)
    View ivBanner;

    @BindView(R.id.forgot)
    TextView btnForgot;

    @BindView(R.id.sendcode)
    TextView btnSendCode;

    @BindView(R.id.et_phone)
    EditText etPhone;

    @BindView(R.id.et_checkcode)
    EditText etCheckCode;

    @BindView(R.id.et_password)
    EditText etPassword;

    @Override
    public int getLayoutID() {
        return R.layout.activity_forgot;
    }

    @Override
    public void initViews() {
        super.initViews();
        setBackListener();
        actionBar.setTitle("忘记密码");
        actionBar.hideMenuItem();
        ivBanner.setBackgroundColor(GoagalInfo.getInItInfo().androidColor);

        StateUtil.setDrawable(this, btnForgot, r);
        StateUtil.setDrawable(this, btnSendCode, r);

        StateUtil.setCursorDrawableColor(getBaseContext(),etPhone, GoagalInfo.getInItInfo().androidColor);
        StateUtil.setCursorDrawableColor(getBaseContext(),etCheckCode, GoagalInfo.getInItInfo().androidColor);
        StateUtil.setCursorDrawableColor(getBaseContext(),etPassword, GoagalInfo.getInItInfo().androidColor);
    }

    private String phone;
    private String password;
    private String code;

    private boolean checkPhone() {
        boolean flag = true;
        phone = etPhone.getText().toString();
        if (phone.isEmpty()) {
            flag = false;
            ToastUtil.toast2(this, DescConstans.PHONE_EMPTY2);
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

    public void sendCode() {
        if (checkPhone()) {
            LoadingUtil.show(this, "正在发送...");
            SendCodeEngin.getImpl(this).send(phone, phone, DescConstans.SEND_TYPE2, new Callback<String>() {
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
                                codeRefresh(btnSendCode);
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

    private boolean checkPassword() {
        boolean flag = true;
        password = etPassword.getText().toString();
        if (password.isEmpty() || password.length() < 6) {
            flag = false;
            ToastUtil.toast2(this, DescConstans.PASSWORD_EMPTY);
        }
        return flag;
    }

    @OnClick({R.id.sendcode, R.id.forgot})
    public void onClick(View view) {
        if (view.getId() == btnSendCode.getId()) {
            sendCode();
            return;
        }

        if (view.getId() == btnForgot.getId()) {
            forgot();
            return;
        }
    }

    private void forgot() {
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
        CheckCodeEngin.getImpl(this).check(phone, phone, code, DescConstans.SEND_TYPE2, new Callback<String>() {
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
                            LoadingUtil.show(ForgotActivity.this, "正在修改...");
                            ForgotEngin.getImpl(getBaseContext()).forgot(phone, password, new
                                    Callback<String>() {
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
                                                        GameBox2SDKUtil.UserInfo g2sUserInfo = GameBox2SDKUtil
                                                                .exchangeUserInfo(phone, password, 0);
                                                        GameBox2SDKUtil.insertUserInfo(getBaseContext(), g2sUserInfo);
                                                        ToastUtil.toast2(getBaseContext(), getMessage(resultInfo.message,
                                                                "修改成功"));
                                                        EventBus.getDefault().post(EventBusMessage.FINISH);
                                                        finish();

                                                    } else {
                                                        ToastUtil.toast2(getBaseContext(), getMessage(resultInfo.message, "修改失败"));
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
}
