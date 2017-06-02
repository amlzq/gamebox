package com.gamebox_idtkown.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.MUserInfoEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.LoadingUtil;
import com.gamebox_idtkown.utils.StateUtil;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.widgets.GBActionBar;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/10/28.
 */
public class MUserInfoActivity extends BaseActionBarActivity<GBActionBar> {

    @BindView(R.id.ok)
    TextView btnOk;

    @BindView(R.id.et_data)
    TextView etData;

    @Override
    public int getLayoutID() {
        return R.layout.activity_m_userinfo;
    }

    private String title;
    private String data;
    private String type;

    @Override
    public void initVars() {
        super.initVars();
        Intent intent = this.getIntent();
        if (intent != null) {
            title = intent.getStringExtra("title");
            type = intent.getStringExtra("type");
            data = intent.getStringExtra("data");
        }
    }

    @Override
    public boolean isNeedLogin() {
        return true;
    }

    @Override
    public void initViews() {
        super.initViews();
        if (title == null) {
            finish();
            return;
        }
        view.setBackgroundColor(Color.WHITE);
        setBackListener();
        actionBar.setTitle(title);
        actionBar.hideMenuItem();
        StateUtil.setDrawable(this, btnOk, r);

        etData.setText(data);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("0")) {
                    updateNickName();
                } else if (type.equals("1")) {
                    updateEmail();
                } else if (type.equals("2")) {
                    updateQQ();
                    etData.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                }
            }
        });
    }

    private void updateNickName() {
        final String nickName = etData.getText().toString();
        if (nickName.trim().isEmpty()) {
            ToastUtil.toast2(this, "用户昵称不能为空");
            return;
        }
        LoadingUtil.show(this, "正在修改...");
        MUserInfoEngin.getImpl(this).updateNickName(nickName, new Callback<String>() {
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
                            GBApplication.userInfo.setNick_name(nickName);
                            GBApplication.updateUserInfo(getBaseContext());
                            ToastUtil.toast2(getBaseContext(), getMessage(resultInfo.message, "修改成功"));
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
    }

    private void updateEmail(){
        final String eamil = etData.getText().toString();
        if (eamil.trim().isEmpty()) {
            ToastUtil.toast2(this, "邮箱不能为空");
            return;
        }
        LoadingUtil.show(this, "正在修改...");
        MUserInfoEngin.getImpl(this).updateEmail(eamil, new Callback<String>() {
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
                            GBApplication.userInfo.setEmail(eamil);
                            GBApplication.updateUserInfo(getBaseContext());
                            ToastUtil.toast2(getBaseContext(), getMessage(resultInfo.message, "修改成功"));
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
    }

    private void updateQQ(){
        final String qq = etData.getText().toString();
        if (qq.trim().isEmpty()) {
            ToastUtil.toast2(this, "QQ不能为空");
            return;
        }
        LoadingUtil.show(this, "正在修改...");
        MUserInfoEngin.getImpl(this).updateQQ(qq, new Callback<String>() {
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
                            GBApplication.userInfo.setQq(qq);
                            GBApplication.updateUserInfo(getBaseContext());
                            ToastUtil.toast2(getBaseContext(), getMessage(resultInfo.message, "修改成功"));
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
    }
}
