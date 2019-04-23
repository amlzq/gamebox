package com.gamebox_idtkown.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.ApkStatus;
import com.gamebox_idtkown.core.ApkUtil;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.InstallEngin;
import com.gamebox_idtkown.engin.UnInstallEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.services.DownloadManagerService;
import com.gamebox_idtkown.utils.PathUtil;
import com.gamebox_idtkown.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by zhangkai on 16/10/11.
 */
public class PackageReceiver extends BroadcastReceiver {
    private static final String[] install_open_list = new String[]{"com.whfeiyou.sound"};

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        String packageName = intent.getDataString();

        if (packageName.length() > 8) {
            packageName = packageName.substring(8);
        }
        boolean flag = false;


        if (ApkUtil.packageNames == null || DownloadManagerService.downloadingInfoList == null || DownloadManagerService
                .downloadedInfoList == null) {
            return;
        }

        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)
                || intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {

            EventBus.getDefault().post(EventBusMessage.REFRESH_INFO);

            for (int i = 0; i < DownloadManagerService.downloadedInfoList.size(); i++) {
                DownloadInfo downloadInfo = DownloadManagerService.downloadedInfoList.get(i);
                if (packageName.equals(downloadInfo.packageName)) {
                    downloadInfo.status = ApkStatus.INSTALLED;
                    DownloadManagerService.updateDownloadInfo(downloadInfo);
                    ToastUtil.toast(context, downloadInfo.name + "安装成功");
                    EventBus.getDefault().post(EventBusMessage.BADGE);
                    InstallEngin.getImpl(context).install(downloadInfo.gameId, downloadInfo.type, packageName, new
                            Callback<String>() {
                                @Override
                                public void onSuccess(ResultInfo<String> resultInfo) {

                                }

                                @Override
                                public void onFailure(Response response) {

                                }
                            });
                    flag = true;
                    break;
                }
            }

            if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
                ApkUtil.packageNames.add(packageName);
            }

            if (!flag) {
                InstallEngin.getImpl(context).install("-1", "-1", packageName, new
                        Callback<String>() {
                            @Override
                            public void onSuccess(ResultInfo<String> resultInfo) {

                            }

                            @Override
                            public void onFailure(Response response) {

                            }
                        });
            }

            for (String pk : install_open_list) {
                if (pk.equals(packageName)) {
                    ApkUtil.openApk(context, packageName);
                }
            }
        } else if (intent.getAction().equals(Intent
                .ACTION_PACKAGE_REMOVED)) {
            for (int i = 0; i < DownloadManagerService.downloadedInfoList.size(); i++) {
                DownloadInfo downloadInfo = DownloadManagerService.downloadedInfoList.get(i);
                if (packageName.equals(downloadInfo.packageName)) {
                    downloadInfo.status = ApkStatus.DOWNLOADED;
                    ToastUtil.toast(context, downloadInfo.name + "卸载成功");
                    File file = new File(PathUtil.getApkPath(downloadInfo.name));
                    if (!file.exists()) {
                        DownloadManagerService.deleteDownloadInfo(downloadInfo, false);
                    } else {
                        DownloadManagerService.updateDownloadInfo(downloadInfo);
                    }
                    EventBus.getDefault().post(EventBusMessage.BADGE);
                    UnInstallEngin.getImpl(context).uninstall(downloadInfo.gameId, downloadInfo.type, packageName, new
                            Callback<String>() {
                                @Override
                                public void onSuccess(ResultInfo<String> resultInfo) {

                                }

                                @Override
                                public void onFailure(Response response) {

                                }
                            });
                    flag = true;
                    break;
                }
            }

            if (!flag) {
                UnInstallEngin.getImpl(context).uninstall("-1", "-1", packageName, new
                        Callback<String>() {
                            @Override
                            public void onSuccess(ResultInfo<String> resultInfo) {

                            }

                            @Override
                            public void onFailure(Response response) {

                            }
                        });
            }
            ApkUtil.packageNames.remove(packageName);
        }
        EventBus.getDefault().post(EventBusMessage.REFRESH_INFO);

    }
}
