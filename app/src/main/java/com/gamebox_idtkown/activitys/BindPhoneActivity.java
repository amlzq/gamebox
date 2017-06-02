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
import com.gamebox_idtkown.engin.SendCodeEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.LoadingUtil;
import com.gamebox_idtkown.utils.StateUtil;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.widgets.GBActionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/10/28.
 */
public class BindPhoneActivity extends BaseActionBarActivity<GBActionBar> {
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
        return R.layout.activity_bindphone;
    }

    @Override
    public void initViews() {
        super.initViews();



        view.setBackgroundColor(Color.WHITE);
        setBackListener();
        actionBar.setTitle("解除绑定");
        actionBar.hideMenuItem();
        StateUtil.setDrawable(this, btnSendCode, r);
        StateUtil.setDrawable(this, btnOk, r);
        StateUtil.setCursorDrawableColor(getBaseContext(),etPhone, GoagalInfo.getInItInfo().androidColor);
        StateUtil.setCursorDrawableColor(getBaseContext(),etCheckCode, GoagalInfo.getInItInfo().androidColor);

        EventBus.getDefault().register(this);

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
            SendCodeEngin.getImpl(this).send(phone, GBApplication.userInfo.getName(), DescConstans.SEND_TYPE5, new Callback<String>() {
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
        CheckCodeEngin.getImpl(this).check(phone, GBApplication.userInfo.getName(), code, DescConstans.SEND_TYPE5, new Callback<String>() {
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
                            Intent intent = new Intent(BindPhoneActivity.this, BindPhone2Activity.class);
                            intent.putExtra("send_types", "6");
                            startActivity(intent);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final Integer type) {
        if (type == EventBusMessage.FINISH) {
            finish();
        }
    }

}
