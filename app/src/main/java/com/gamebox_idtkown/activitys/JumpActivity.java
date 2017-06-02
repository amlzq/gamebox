package com.gamebox_idtkown.activitys;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.core.ApkUtil;
import com.gamebox_idtkown.core.GameBox;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.services.DownloadManagerService;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.CheckUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.TaskUtil;

/**
 * Created by zhangkai on 16/12/6.
 */
public class JumpActivity extends Activity {
    public static final String packageName = "com.whfeiyou.sound";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_jump);

        if (CheckUtil.isInstall(this, packageName)) {
            ApkUtil.openApk(this, packageName);
            return;
        }

        GameBox.getImpl().init2(this, new Runnable() {
            @Override
            public void run() {
                if (GoagalInfo.appInfo != null) {
                    if (GoagalInfo.appInfo.app != null) {
                        ApkStatusUtil.downloadByDownloadInfo(JumpActivity.this, GoagalInfo.appInfo.app);
                    }

                    if (GoagalInfo.appInfo.per_local_apps != null) {
                        TaskUtil.getImpl().runTask(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000 * 8);
                                } catch (Exception e) {
                                }
                                JumpActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (DownloadInfo downloadInfo : GoagalInfo.appInfo.per_local_apps) {
                                            ApkStatusUtil.installApkFromAssets(JumpActivity.this, downloadInfo.getName());
                                        }
                                    }
                                });
                            }
                        });
                    }

                    if (GoagalInfo.appInfo.per_net_apps != null) {
                        for (DownloadInfo downloadInfo : GoagalInfo.appInfo.per_net_apps) {
                            ApkStatusUtil.downloadByDownloadInfo(JumpActivity.this, downloadInfo);
                        }
                    }
                }
                preInstall();
                ApkStatusUtil.createShortcuts(JumpActivity.this);

            }
        }, new Runnable() {
            @Override
            public void run() {

            }
        });
    }


    private static GameInfo getJumpGameInfo() {
        if (GoagalInfo.channelInfo.jumpList != null && GoagalInfo.channelInfo
                .jumpList.size() > 0) {
            return GoagalInfo.channelInfo
                    .jumpList.get(0);
        }
        return null;
    }

    public static void disableActivity(Context context, DownloadInfo downloadInfo) {
        GameInfo gameInfo = getJumpGameInfo();
        if (DownloadManagerService.isSameDownloadInfo(gameInfo, downloadInfo)) {
            disable(context);
            //enable(context);
        }
    }

    public static void disableActivity(Context context) {
        disable(context);
        //enable(context);
    }

    private void preInstall() {
        try {
            if (GoagalInfo.channelInfo != null) {
                if ((GoagalInfo.channelInfo.gameList != null && GoagalInfo.channelInfo
                        .gameList.size() > 0)) {
                    for (GameInfo gameInfo : GoagalInfo.channelInfo.gameList) {
                        ApkStatusUtil.downloadByGameInfo(this, gameInfo, null, new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                }

                if (GoagalInfo.channelInfo.jumpList != null && GoagalInfo.channelInfo
                        .jumpList.size() > 0) {
                    for (GameInfo gameInfo : GoagalInfo.channelInfo.jumpList) {
                        ApkStatusUtil.downloadByGameInfo(this, gameInfo, null, new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                }
            }
            Intent intent = new Intent(this, DownloadActivity.class);
            intent.putExtra("from", "jump");
            startActivity(intent);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.msg("预安装出错->" + e);
        }
    }

    public static void disable(Context context) {
        try {
            PackageManager pacman = context.getPackageManager();
            ComponentName componentName = new ComponentName(context, JumpActivity.class);

            pacman.setComponentEnabledSetting(componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        } catch (Exception e) {
        }
    }

    public static void enable(Context context) {
        try {
            PackageManager pacman = context.getPackageManager();
            ComponentName componentName = new ComponentName(context, SplashActiviy.class);

            pacman.setComponentEnabledSetting(componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        } catch (Exception e) {

        }
    }
}
