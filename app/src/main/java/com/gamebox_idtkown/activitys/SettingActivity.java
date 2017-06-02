package com.gamebox_idtkown.activitys;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.http.HttpResponseCache;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.db.greendao.UserInfo;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.utils.DialogUpdateUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.PreferenceUtil;
import com.gamebox_idtkown.utils.SizeUitl;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.widgets.GBActionBar;
import com.gamebox_idtkown.views.widgets.GBSettingItem;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/10/25.
 */
public class SettingActivity extends BaseActionBarActivity<GBActionBar> {
    @BindView(R.id.smart_install)
    GBSettingItem smartInstall;

    @BindView(R.id.g4)
    GBSettingItem g4Item;

    @BindView(R.id.cache)
    GBSettingItem cacheItem;

    @BindView(R.id.version)
    GBSettingItem versionItem;

    @BindView(R.id.logout)
    TextView btnLogout;

    @BindView(R.id.no_install)
    GBSettingItem noInstall;

    @BindView(R.id.rl_logout)
    RelativeLayout rlLogout;

    @Override
    public int getLayoutID() {
        return R.layout.activity_setting;
    }

    @Override
    public void initViews() {
        super.initViews();

        setBackListener();
        actionBar.setTitle("设置");
        actionBar.hideMenuItem();

        g4Item.showSwitch();
        g4Item.hideArrow();
        g4Item.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtil.getImpl(getBaseContext()).putBoolean("4g", isChecked);
            }
        });

        noInstall.showSwitch();
        noInstall.hideArrow();
        noInstall.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtil.getImpl(getBaseContext()).putBoolean(DescConstans.NO_INSTALL, isChecked);
                EventBus.getDefault().post(EventBusMessage.REFRESH_INFO);
            }
        });

        setInfo();

        if (GBApplication.isLogin()) {
            rlLogout.setVisibility(View.VISIBLE);
        } else {
            rlLogout.setVisibility(View.GONE);
        }

        smartInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        smartInstall.setDesc("安装搬运工");
    }

    private void setInfo() {
        g4Item.switchButton.setCheckedImmediately(PreferenceUtil.getImpl(this).getBoolean("4g", false));
        noInstall.switchButton.setCheckedImmediately(PreferenceUtil.getImpl(this).getBoolean(DescConstans.NO_INSTALL, false));
        try {
            cacheItem.setDesc(SizeUitl.getMKBStr((int) HttpResponseCache.getInstalled().size()));
        } catch (Exception e) {
            cacheItem.setDesc(SizeUitl.getMKBStr((int) HttpResponseCache.getInstalled().size()));
        }
        if (GoagalInfo.packageInfo == null) {
            GoagalInfo.packageInfo = GoagalInfo.getPackageInfo(this);
        }
        versionItem.setDesc("当前版本:" + GoagalInfo.packageInfo == null ? "未知" : GoagalInfo.packageInfo.versionName);
    }


    @OnClick({R.id.cache, R.id.version, R.id.logout})
    public void onClick(View view) {
        if (view.getId() == cacheItem.getId()) {
            try {
                HttpResponseCache.getInstalled().delete();
                GBApplication.installCache(this);
                cacheItem.setDesc(SizeUitl.getMKBStr((int) HttpResponseCache.getInstalled().size()));
                ToastUtil.toast2(this, "清除缓存成功");
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.msg("删除缓存出错->" + e);
            }
            return;
        }
        if (view.getId() == versionItem.getId()) {
            checkVersion();
            return;
        }
        if (view.getId() == btnLogout.getId()) {
            GBApplication.logout(this);
            EventBus.getDefault().post(new UserInfo());
            EventBus.getDefault().post(BitmapFactory.decodeResource(this.getResources(), R.mipmap
                    .avatar_default));
            finish();
            return;
        }
    }

    private void checkVersion() {
        if (GoagalInfo.getInItInfo().is_update && GoagalInfo.getInItInfo().update_info != null) {
            DialogUpdateUtil.getImpl().show(this, "版本更新", false);
        } else {
            ToastUtil.toast2(getBaseContext(), "当前是最新版本");
        }

    }
}
