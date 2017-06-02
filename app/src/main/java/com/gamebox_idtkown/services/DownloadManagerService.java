package com.gamebox_idtkown.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.WindowManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.activitys.JumpActivity;
import com.gamebox_idtkown.activitys.MainActivity;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.ApkStatus;
import com.gamebox_idtkown.core.ApkUtil;
import com.gamebox_idtkown.core.DbUtil;
import com.gamebox_idtkown.core.DownloadUtil;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.CheckUtil;
import com.gamebox_idtkown.utils.LoadingUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.PathUtil;
import com.gamebox_idtkown.utils.PingUtil;
import com.gamebox_idtkown.utils.PreferenceUtil;
import com.gamebox_idtkown.utils.SizeUitl;
import com.gamebox_idtkown.utils.TaskUtil;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.utils.ZipUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import org.greenrobot.eventbus.EventBus;


import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhangkai on 16/10/10.
 */
public class DownloadManagerService extends Service {
    public static List<DownloadInfo> downloadingInfoList;
    public static List<DownloadInfo> downloadedInfoList;

    public static List<DownloadInfo> updateList;

    public static FileDownloadListener fileDownloadListener;
    public static Context context;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    /* ******************Override Not End********************** */


    /* *****************Override Main Start*********************** */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        run();
        return super.onStartCommand(intent, flags, startId);
    }


    public void run() {
        context = getBaseContext();
        initDonwloadManager();
    }

    public DownloadManagerService() {
    }

    private static DownloadInfo getDownloadInfo(BaseDownloadTask task) {
        if (task == null) return null;
        DownloadInfo downloadInfo = null;
        boolean flag = false;
        for (int i = 0; i < downloadingInfoList.size(); i++) {
            downloadInfo = downloadingInfoList.get(i);
            if (isSameDownloadInfo(downloadInfo, task)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            for (int i = 0; i < updateList.size(); i++) {
                downloadInfo = updateList.get(i);
                if (isSameDownloadInfo(downloadInfo, task)) {
                    break;
                }
            }
        }
        return downloadInfo;
    }

    public static DownloadInfo getDownloadInfo(GameInfo gameInfo) {
        if (gameInfo == null) {
            return null;
        }
        DownloadInfo downloadInfo = null;
        boolean isGet = false;

        if (gameInfo.isUpdate) {
            for (int i = 0; i < updateList.size(); i++) {
                DownloadInfo tmpdownloadInfo = updateList.get(i);
                if (isSameDownloadInfo(gameInfo, tmpdownloadInfo)) {
                    downloadInfo = tmpdownloadInfo;
                    break;
                }
            }
            return downloadInfo;
        }

        for (int i = 0; i < downloadingInfoList.size(); i++) {
            DownloadInfo tmpdownloadInfo = downloadingInfoList.get(i);
            if (isSameDownloadInfo(gameInfo, tmpdownloadInfo)) {
                downloadInfo = tmpdownloadInfo;
                isGet = true;
                break;
            }
        }
        if (!isGet) {
            for (int i = 0; i < downloadedInfoList.size(); i++) {
                DownloadInfo tmpdownloadInfo = downloadedInfoList.get(i);
                if (isSameDownloadInfo(gameInfo, tmpdownloadInfo)) {
                    downloadInfo = tmpdownloadInfo;
                    break;
                }
            }
        }


        return downloadInfo;
    }

    public static boolean checkDownloadInfoExist(BaseDownloadTask task) {
        boolean flag = false;

        for (int i = 0; i < downloadingInfoList.size(); i++) {
            DownloadInfo downloadInfo = downloadingInfoList.get(i);
            flag = isSameDownloadInfo(downloadInfo, task);
            if (flag) {
                break;
            }
        }
        if (!flag) {
            for (int i = 0; i < downloadedInfoList.size(); i++) {
                DownloadInfo downloadInfo = downloadedInfoList.get(i);
                flag = isSameDownloadInfo(downloadInfo, task);
                if (flag) {
                    break;
                }
            }
        }
        if (!flag) {
            for (int i = 0; i < updateList.size(); i++) {
                DownloadInfo downloadInfo = updateList.get(i);
                flag = isSameDownloadInfo(downloadInfo, task);
                if (flag) {
                    break;
                }
            }
        }
        return flag;
    }


    public static boolean isSameDownloadInfo(DownloadInfo downloadInfo, DownloadInfo downloadInfo2) {
        String murl = getKey(downloadInfo);
        String murl2 = getKey(downloadInfo2);
        return murl.equals(murl2);
    }

    public static boolean isSameDownloadInfo(GameInfo gameInfo, DownloadInfo downloadInfo) {
        String murl = getKey(downloadInfo);
        String murl2 = getKey(gameInfo);
        return murl.equals(murl2);
    }

    public static boolean isSameDownloadInfo(GameInfo gameInfo, GameInfo gameInfo2) {
        String murl = getKey(gameInfo);
        String murl2 = getKey(gameInfo2);
        return murl.equals(murl2);
    }

    private static String getKey(GameInfo gameInfo) {
        return gameInfo.getUrl() + gameInfo.getName();
    }

    private static String getKey(DownloadInfo downloadInfo) {
        return downloadInfo.getUrl() + downloadInfo.getName();
    }


    public static boolean isSameDownloadInfo(DownloadInfo downloadInfo, BaseDownloadTask task) {
        if (task == null) return false;
        String murl = task.getUrl();
        String murl2 = downloadInfo.getUrl();
        return murl.equals(murl2);
    }


    public static boolean getDownloadIn234G() {
        if (downloadingInfoList == null && updateList == null) {
            return false;
        }
        int dcount = 0;
        for (int i = 0; i < downloadingInfoList.size(); i++) {
            DownloadInfo downloadInfo = downloadingInfoList.get(i);
            if (downloadInfo.getStatus() != ApkStatus.Stop) {
                dcount++;
            }
        }
        for (int i = 0; i < updateList.size(); i++) {
            DownloadInfo downloadInfo = updateList.get(i);
            if (downloadInfo.getStatus() != ApkStatus.Stop) {
                dcount++;
            }
        }
        return canDownloadIn234G() && (dcount > 0);
    }

    public static boolean canDownloadIn234G() {
        return (context == null ? false : (PreferenceUtil.getImpl(context).getBoolean("4g", false)));
    }

    public static void pauseAll() {
        FileDownloader.getImpl().pauseAll();
        for (DownloadInfo downloadInfo : downloadingInfoList) {
            downloadInfo.status = ApkStatus.Stop;
            updateDownloadInfo(downloadInfo);
            EventBus.getDefault().post(downloadInfo);
        }
        for (DownloadInfo downloadInfo : updateList) {
            downloadInfo.status = ApkStatus.Stop;
            updateDownloadInfo(downloadInfo);
            EventBus.getDefault().post(downloadInfo);
        }
    }

    public static synchronized void downloadIn234G(final Context context, final DownloadInfo downloadInfo, final Runnable runnable) {
        if (CheckUtil.getNetworkType(context) == CheckUtil.NETTYPE_TYPE_MOBILE && !getDownloadIn234G()) {
            pause(downloadInfo);
            handle.post(new Runnable() {
                @Override
                public void run() {
                    MaterialDialog dialog = new MaterialDialog.Builder(context)
                            .title("提示")
                            .content("当前为非WIFI环境,继续下载会消耗流量，是否继续下载？")
                            .positiveColor(GoagalInfo.getInItInfo().androidColor)
                            .negativeColorRes(R.color.gray_light)
                            .positiveText("继续")
                            .negativeText("取消")
                            .backgroundColor(Color.WHITE)
                            .contentColor(Color.GRAY)
                            .titleColor(Color.BLACK)
                            .onAny(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    if (which == DialogAction.POSITIVE) {
                                        downloadInfo.canDownloadIn234G = true;
                                        restartTask(downloadInfo);
                                    }
                                }
                            }).build();

                    if(Build.VERSION.SDK_INT > 18) {
                        dialog.getWindow().setType(WindowManager.LayoutParams
                                .TYPE_TOAST);
                    }else {
                        dialog.getWindow().setType(WindowManager.LayoutParams
                                .TYPE_SYSTEM_ALERT);
                    }
                    dialog.show();
                }
            });
        } else {
            runnable.run();
        }
    }


    public static void initVars() {
        downloadingInfoList = new ArrayList<>();
        downloadedInfoList = new ArrayList<>();
        updateList = new ArrayList<>();
    }

    public void initDonwloadManager() {
        initVars();

        fileDownloadListener = new FileDownloadListener() {
            @Override
            protected void pending(final BaseDownloadTask task, final int soFarBytes, final int totalBytes) {
                setDownloadPending(task, soFarBytes, totalBytes);
            }

            @Override
            protected void progress(final BaseDownloadTask task, final int soFarBytes, final int totalBytes) {
                setDownloadInfo(task, soFarBytes, totalBytes);
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                setDownloadComplete(task);
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                setDownloadPause(task, soFarBytes, totalBytes);
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                setDownloadError(task);
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                setDownloadError(task);
            }
        };

        List<DownloadInfo> dataInfos = DbUtil.getSession(getBaseContext()).getDownloadInfoDao().loadAll();
        ApkStatusUtil.getStatuss2(context, dataInfos);
        for (int i = 0; i < dataInfos.size(); i++) {
            final DownloadInfo downloadInfo = dataInfos.get(i);
            if (downloadInfo.getIsUpdate()) {
                updateList.add(downloadInfo);
                DownloadUtil.downloadApk(downloadInfo, fileDownloadListener);
                if (downloadInfo.getStatus() != ApkStatus.Stop) {
                    startTask(downloadInfo);
                }
                continue;
            }
            if (downloadInfo.getStatus() == ApkStatus.DOWNLOADED || downloadInfo.getStatus() == ApkStatus.INSTALLED) {
                if (!downloadInfo.isDelete) {
                    downloadedInfoList.add(downloadInfo);
                } else {
                    deleteDownloadInfo(downloadInfo, true);
                }
            } else {
                downloadingInfoList.add(downloadInfo);
                DownloadUtil.downloadApk(downloadInfo, fileDownloadListener);
                if (downloadInfo.getStatus() != ApkStatus.Stop) {
                    startTask(downloadInfo);
                }
            }
        }
    }

    public static void restartAllTask() {
        if(downloadingInfoList != null) {
            for (int i = 0; i < downloadingInfoList.size(); i++) {
                DownloadInfo downloadInfo = downloadingInfoList.get(i);
                if (downloadInfo.getStatus() == ApkStatus.Stop) {
                    restartTask(downloadInfo);
                }
            }
        }
        if(updateList != null) {
            for (int i = 0; i < updateList.size(); i++) {
                DownloadInfo downloadInfo = updateList.get(i);
                if (downloadInfo.getStatus() == ApkStatus.Stop) {
                    restartTask(downloadInfo);
                }
            }
        }
    }

    public static void startTask(DownloadInfo downloadInfo) {
        try {
            downloadInfo.downloadTask.start();
        } catch (Exception e) {
            LogUtil.msg("任务开始下载失败->" + e.getMessage());
        }
    }

    public static void pause(DownloadInfo downloadInfo) {
        try {
            if (!downloadInfo.downloadTask.pause()) {
                downloadInfo.downloadTask = null;
                DownloadUtil.downloadApk(downloadInfo, fileDownloadListener);
                restartTask(downloadInfo);
            }
        } catch (Exception e) {
            downloadInfo.downloadTask = null;
            DownloadUtil.downloadApk(downloadInfo, fileDownloadListener);
            restartTask(downloadInfo);
            LogUtil.msg("任务暂停失败->" + e.getMessage());
        }
    }

    public static void restartTask(DownloadInfo downloadInfo) {
        try {
            if (downloadInfo.downloadTask != null && !downloadInfo.downloadTask.isRunning()) {
                downloadInfo.downloadTask.reuse();
                downloadInfo.downloadTask.start();
            }
        } catch (Exception e) {
            LogUtil.msg("重新开始下载任务失败->" + e.getMessage());
            startTask(downloadInfo);
        }
    }

    public static void updateDownloadInfo(DownloadInfo downloadInfo) {
        try {
            DbUtil.getSession(context).update(downloadInfo);
        } catch (Exception e) {
            LogUtil.msg("更新下载信息失败->" + e.getMessage());
        }
    }

    public static synchronized void addDownloadInfo(DownloadInfo downloadInfo) {
        try {
            DbUtil.getSession(context).insert(downloadInfo);
            if (downloadInfo.getIsUpdate()) {
                updateList.add(downloadInfo);
            } else {
                downloadingInfoList.add(downloadInfo);
            }
        } catch (Exception e) {
            LogUtil.msg("添加下载信息失败->" + e.getMessage());
        }
    }

    public static synchronized void deleteDownloadInfo(DownloadInfo downloadInfo, boolean isDeleteFile) {
        try {
            String path = "";
            if (downloadInfo.getStatus() == ApkStatus.DOWNLOADED || downloadInfo.getStatus() == ApkStatus.INSTALLED) {
                path = PathUtil.getApkPath(downloadInfo.name);
            } else {
                if (downloadInfo.status == ApkStatus.DOWNLOADING || downloadInfo.status == ApkStatus.WAITING) {
                    DownloadManagerService.pause(downloadInfo);
                }
                path = PathUtil.getApkPath(downloadInfo.name) + ".temp";
            }

            boolean delete = false;
            if (isDeleteFile) {
                try {
                    delete = false;
                    if (downloadInfo.downloadTask != null) {
                        delete = FileDownloader.getImpl().clear(downloadInfo.downloadTask.getId(), path);
                    }
                    if (!delete) {
                        File file = new File(path);
                        if (file.exists()) {
                            file.delete();
                        }
                        delete = true;
                    }
                } catch (Exception e) {
                    LogUtil.msg("删除文件失败->" + e);
                }
            }
            DbUtil.getSession(context).getDownloadInfoDao().deleteByKey(downloadInfo.getId());
            if (downloadInfo.getStatus() == ApkStatus.DOWNLOADED || downloadInfo.getStatus() == ApkStatus.INSTALLED) {
                downloadedInfoList.remove(downloadInfo);
            } else {
                if (downloadInfo.getIsUpdate()) {
                    updateList.remove(downloadInfo);
                } else {
                    downloadingInfoList.remove(downloadInfo);
                }
            }
            if (delete) {
                downloadInfo.status = ApkStatus.UNDOWNLOAD;
            } else {
                if (downloadInfo.status == ApkStatus.INSTALLED) {
                    downloadInfo.status = ApkStatus.UNDOWNLOAD;
                }
            }
            EventBus.getDefault().post(EventBusMessage.BADGE);
            EventBus.getDefault().post(EventBusMessage.REFRESH_INFO);
            EventBus.getDefault().post(EventBusMessage.DOWNLIST_STATUS_CHANGE);
        } catch (Exception e) {
            LogUtil.msg("删除下载信息失败->" + e.getMessage());
        }
    }

    public void setDownloadInfo(final BaseDownloadTask task, final int soFarBytes, final int totalBytes) {
        final DownloadInfo downloadInfo = getDownloadInfo(task);
        if (downloadInfo != null) {
            if (!downloadInfo.canDownloadIn234G) {
                downloadIn234G(context, downloadInfo, new Runnable() {
                    @Override
                    public void run() {
                        downloadInfo.speed = SizeUitl.getSpeedStr(task.getSpeed()) + "/s";
                        setDownloadingInfoSize(downloadInfo, soFarBytes, totalBytes);
                        downloadInfo.status = ApkStatus.DOWNLOADING;
                        setDownloadingInfoPrecent(downloadInfo, soFarBytes, totalBytes);
                        updateDownloadInfo(downloadInfo);
                        EventBus.getDefault().post(downloadInfo);
                    }
                });
            } else {
                downloadInfo.speed = SizeUitl.getSpeedStr(task.getSpeed()) + "/s";
                setDownloadingInfoSize(downloadInfo, soFarBytes, totalBytes);
                downloadInfo.status = ApkStatus.DOWNLOADING;
                setDownloadingInfoPrecent(downloadInfo, soFarBytes, totalBytes);
                updateDownloadInfo(downloadInfo);
                EventBus.getDefault().post(downloadInfo);
            }
        }

    }


    public void setDownloadingInfoSize(DownloadInfo downloadInfo, int soFarBytes, int totalBytes) {
        if (totalBytes != -1) {
            downloadInfo.size = SizeUitl.getMKBStr(soFarBytes) + "/" + SizeUitl.getMKBStr(totalBytes);
        } else {
            if (downloadInfo.size.split("/").length > 1) {
                downloadInfo.size = SizeUitl.getMKBStr(soFarBytes) + "/" + downloadInfo.size.split("/")[1];
            } else {
                downloadInfo.size = SizeUitl.getMKBStr(soFarBytes) + "/" + downloadInfo.size;
            }
        }
    }

    public void setDownloadingInfoPrecent(DownloadInfo downloadInfo, int soFarBytes, int totalBytes) {
        if (totalBytes == -1) {
            downloadInfo.precent = 0.0f;
        } else {
            downloadInfo.precent = (float) soFarBytes / (float) totalBytes;

        }
    }


    public void setDownloadPause(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        DownloadInfo downloadInfo = getDownloadInfo(task);
        if (downloadInfo != null) {
            downloadInfo.speed = "已暂停";
//            setDownloadingInfoSize(downloadInfo, soFarBytes, totalBytes);
//            setDownloadingInfoPrecent(downloadInfo, soFarBytes, totalBytes);
            downloadInfo.status = ApkStatus.Stop;
            updateDownloadInfo(downloadInfo);
            EventBus.getDefault().post(downloadInfo);
        }
    }

    public void setDownloadComplete(BaseDownloadTask task) {
        final DownloadInfo downloadInfo = getDownloadInfo(task);
        if (downloadInfo != null) {
            TaskUtil.getImpl().runTask(new Runnable() {
                @Override
                public void run() {
                    downloadInfo.precent = 1.0f;
                    downloadInfo.status = ApkStatus.DOWNLOADED;
                    downloadInfo.speed = "已完成";
                    if (!downloadInfo.getIsUpdate()) {
                        downloadedInfoList.add(downloadInfo);
                        downloadingInfoList.remove(downloadInfo);
                    }

                    EventBus.getDefault().post(downloadInfo);
                    downloadInfo.status = ApkUtil.getApkStatus(context, downloadInfo);
                    updateDownloadInfo(downloadInfo);

                    final String filePath = PathUtil.getApkPath(downloadInfo.getName());
                    File file = new File(filePath);
                    if (ZipUtil.isArchiveFile(file)) {
                        handle.post(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    if (!PreferenceUtil.getImpl(context).getBoolean(DescConstans.NO_INSTALL, false)
                                            && downloadInfo.isInstall) {
                                        ApkUtil.installApk(context, filePath);
                                        LogUtil.msg("执行了安装");
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    LogUtil.msg("安装apk失败->" + e);
                                }
                            }
                        });
                    } else {
                        downloadInfo.precent = 0.0f;
                        deleteDownloadInfo(downloadInfo, true);
                        handle.post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast(context, downloadInfo.getName() + ".apk文件无效, 无法进行正常安装");
                            }
                        });
                        checkNetState();
                    }
                    JumpActivity.disableActivity(context, downloadInfo);
                }
            });
        }
    }


    private static final Handler handle = new Handler();

    public void setDownloadPending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        final DownloadInfo downloadInfo = getDownloadInfo(task);
        if (downloadInfo != null) {
            downloadInfo.speed = "等待中";
            downloadInfo.status = ApkStatus.WAITING;
            setDownloadingInfoPrecent(downloadInfo, soFarBytes, totalBytes);
            setDownloadingInfoSize(downloadInfo, soFarBytes, totalBytes);
            updateDownloadInfo(downloadInfo);
            EventBus.getDefault().post(downloadInfo);
        }
    }

    public void setDownloadError(BaseDownloadTask task) {
        final DownloadInfo downloadInfo = getDownloadInfo(task);
        if (downloadInfo != null) {
            downloadInfo.speed = "已出错";
            downloadInfo.status = ApkStatus.Error;
            updateDownloadInfo(downloadInfo);
            EventBus.getDefault().post(downloadInfo);
        }

    }

    public void checkNetState() {
        handle.post(new Runnable() {
            @Override
            public void run() {
                LoadingUtil.show2(context, "正在检测网络状态...");
            }
        });
        if (!PingUtil.ping("www.baidu.com")) {
            handle.post(new Runnable() {
                @Override
                public void run() {
                    showNetErrorDialog();
                }
            });
        }
        handle.post(new Runnable() {
            @Override
            public void run() {
                ToastUtil.toast2(context, "网络状态正常");
                LoadingUtil.dismiss();
            }
        });
    }

    private void openNetSetting(Context context) {
        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }

    public void showNetErrorDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title("无法连接到网络")
                .content("检测当前网络无法连接到网络")
                .positiveColor(GoagalInfo.getInItInfo().androidColor)
                .negativeColorRes(R.color.gray_light)
                .positiveText("去设置")
                .negativeText("取消")
                .backgroundColor(Color.WHITE)
                .contentColor(Color.GRAY)
                .titleColor(Color.BLACK)
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (which == DialogAction.POSITIVE) {
                            openNetSetting(MainActivity.getImpl());
                        }
                    }
                })
                .build();
        if (Build.VERSION.SDK_INT > 18) {
            dialog.getWindow().setType(WindowManager.LayoutParams
                    .TYPE_TOAST);
        } else {
            dialog.getWindow().setType(WindowManager.LayoutParams
                    .TYPE_SYSTEM_ALERT);
        }
        dialog.show();
    }

}
