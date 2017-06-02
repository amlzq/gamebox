package com.gamebox_idtkown.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.CheckCodeEngin;
import com.gamebox_idtkown.engin.MUserInfoEngin;
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
 * Created by zhangkai on 16/10/28.
 */
public class BindPhone2Activity extends BaseActionBarActivity<GBActionBar> {
    @BindView(R.id.ok)
    TextView btnOk;

    @BindView(R.id.sendcode)
    TextView btnSendCode;

    @BindView(R.id.et_phone)
    EditText etPhone;

    @BindView(R.id.et_checkcode)
    EditText etCheckCode;

    @Override
    public int getLayoutID() {
        return R.layout.activity_bindphone2;
    }

    @Override
    public void initViews() {
        super.initViews();

        Intent intent = getIntent();
        if (intent != null) {
            String send_types = intent.getStringExtra("send_types");
            if (send_types != null && !send_types.isEmpty()) {
                DescConstans.SEND_TYPE4 = send_types;
            }
        }

        view.setBackgroundColor(Color.WHITE);
        setBackListener();
        actionBar.setTitle("绑定手机号");
        actionBar.hideMenuItem();
        StateUtil.setDrawable(this, btnSendCode, r);
        StateUtil.setDrawable(this, btnOk, r);

        StateUtil.setCursorDrawableColor(getBaseContext(), etPhone, GoagalInfo.getInItInfo().androidColor);
        StateUtil.setCursorDrawableColor(getBaseContext(), etCheckCode, GoagalInfo.getInItInfo().androidColor);

    }

    private String phone;
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
            SendCodeEngin.getImpl(this).send(phone, GBApplication.userInfo.getName(), DescConstans.SEND_TYPE4, new
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


    @OnClick({R.id.sendcode, R.id.ok})
    public void onClick(View view) {
        if (view.getId() == btnSendCode.getId()) {
            sendCode();
            return;
        }

        if (view.getId() == btnOk.getId()) {
            ok();
            return;
        }
    }

    private void ok() {
        if (!checkPhone()) {
            return;
        }

        if (!checkCode()) {
            return;
        }

        LoadingUtil.show(this, "正在验证...");
        CheckCodeEngin.getImpl(this).check(phone, GBApplication.userInfo.getName(), code, DescConstans.SEND_TYPE4, new Callback<String>() {
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
                            LoadingUtil.show(BindPhone2Activity.this, "正在修改...");
                            MUserInfoEngin.getImpl(getBaseContext()).updatePhone(phone, new Callback<String>() {
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
                                                GBApplication.userInfo.setMobile(phone);
                                                GBApplication.userInfo.setIs_vali_mobile(true);
                                                GBApplication.updateUserInfo(getBaseContext());
                                                ToastUtil.toast2(getBaseContext(), getMessage(resultInfo.message,
                                                        "修改成功"));
                                                GameBox2SDKUtil.UserInfo g2sUserInfo = GameBox2SDKUtil
                                                        .exchangeUserInfo(GBApplication.userInfo, 0);
                                                GameBox2SDKUtil.insertUserInfo(getBaseContext(), g2sUserInfo);
                                                EventBus.getDefault().post(EventBusMessage.FINISH);
                                                finish();
                                            } else {
                                                ToastUtil.toast2(getBaseContext(), getMessage(resultInfo.message,
                                                        "修改失败"));
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