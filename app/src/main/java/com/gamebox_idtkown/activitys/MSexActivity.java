package com.gamebox_idtkown.activitys;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.MUserInfoEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.LoadingUtil;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.widgets.GBActionBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/10/28.
 */
public class MSexActivity  extends BaseActionBarActivity<GBActionBar> {
    @BindView(R.id.sex)
    RelativeLayout rlSex;

    @BindView(R.id.sex2)
    RelativeLayout rlSex2;

    @BindView(R.id.tv_select)
    TextView tvSelect;

    @BindView(R.id.tv_select2)
    TextView tvSelect2;


    @Override
    public int getLayoutID() {
        return R.layout.activity_sex;
    }

    @Override
    public boolean isNeedLogin() {
        return true;
    }

    @Override
    public void initViews() {
        super.initViews();
        setBackListener();
        actionBar.setTitle("性别");
        actionBar.hideMenuItem();

        rlSex.setTag("1");
        rlSex2.setTag("2");

        try {
            int sex = Integer.parseInt(GBApplication.userInfo.getSex());
            if (sex == 1) {
                tvSelect.setVisibility(View.VISIBLE);
                tvSelect.setTextColor(GoagalInfo.getInItInfo().androidColor);
            } else if (sex == 2) {
                tvSelect2.setVisibility(View.VISIBLE);
                tvSelect2.setTextColor(GoagalInfo.getInItInfo().androidColor);
            }
        } catch (Exception e) {

        }

    }

    @OnClick({R.id.sex, R.id.sex2})
    public void onClick(View view){
        final String sex = view.getTag()+"";
        LoadingUtil.show(this, "正在修改...");
        MUserInfoEngin.getImpl(this).updateSex(sex, new Callback<String>() {
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
                            GBApplication.userInfo.setSex(sex);
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
