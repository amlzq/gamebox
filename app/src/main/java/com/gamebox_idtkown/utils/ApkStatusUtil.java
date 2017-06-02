package com.gamebox_idtkown.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.activitys.LoadingActivity;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.ApkStatus;
import com.gamebox_idtkown.core.ApkUtil;
import com.gamebox_idtkown.core.DownloadUtil;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.AppModel;
import com.gamebox_idtkown.domain.AppRepository;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.domain.ShortCutInfo;
import com.gamebox_idtkown.engin.GameDownEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.services.DownloadManagerService;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhangkai on 16/10/11.
 */
public class ApkStatusUtil {

    public static void enableButtonState(Context context, TextView view, int color) {
        view.setClickable(true);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(ScreenUtil.dip2px(context, 1.5f));
        view.setBackground(drawable);
    }

    public static void setButtonStatus(Context context, TextView view, int status) {
        if (view == null) {
            return;
        }
        if (status == ApkStatus.WAIT_LISTENER) {
            enableButtonState(context, view, Color.parseColor("#999999"));
            view.setText("等待中");
        }
        if (status == ApkStatus.DOWNLOADING || status == ApkStatus.WAITING) {
            enableButtonState(context, view, Color.parseColor("#999999"));
            view.setText("暂停");
        } else if (status == ApkStatus.Error) {
            enableButtonState(context, view, Color.parseColor("#ff9900"));
            view.setText("重试");
        } else if (status == ApkStatus.Stop) {
            enableButtonState(context, view, GoagalInfo.getInItInfo().androidColor);
            view.setText("继续");
        } else if (status == ApkStatus.DOWNLOADED) {
            if (PreferenceUtil.getImpl(context).getBoolean(DescConstans.NO_INSTALL, false)) {
                enableButtonState(context, view, Color.parseColor("#31DA71"));
                view.setText("极速玩");
                return;
            } else {
                enableButtonState(context, view, GoagalInfo.getInItInfo().androidColor);
                view.setText("安装");
            }
        } else if (status == ApkStatus.INSTALLED) {
            enableButtonState(context, view, Color.parseColor("#31DA71"));
            view.setText("打开");
        } else if (status == ApkStatus.UNDOWNLOAD) {
            enableButtonState(context, view, GoagalInfo.getInItInfo().androidColor);
            view.setText("下载");
        }
    }

    public static Signature getPackageSignature(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> it = apps.iterator();
        while (it.hasNext()) {
            PackageInfo info = it.next();
            if (info.packageName.equals(packageName)) {
                return info.signatures[0];
            }
        }
        return null;
    }


    public static synchronized void downloadByGameInfo2(Context context, GameInfo gameInfo, TextView view, Runnable
            runnable) {
        DownloadInfo downloadInfo = DownloadUtil.downloadApk(gameInfo, DownloadManagerService.fileDownloadListener);
        if (downloadInfo.downloadTask != null && !DownloadManagerService.checkDownloadInfoExist(downloadInfo
                .downloadTask)) {
            downloadInfo.gameId = gameInfo.getGameId();
            downloadInfo.type = gameInfo.getType();
            downloadInfo.name = gameInfo.getName();
            downloadInfo.size = gameInfo.getSize_text();
            downloadInfo.iconUrl = gameInfo.getIconUrl() + "";
            downloadInfo.url = gameInfo.getUrl() + "";
            downloadInfo.desc = gameInfo.getDesc() + "";
            downloadInfo.packageName = gameInfo.getPackageName() + "";
            downloadInfo.status = ApkStatus.DOWNLOADING;
            downloadInfo.precent = 0.0f;
            downloadInfo.speed = "";
            downloadInfo.isUpdate = gameInfo.isUpdate;
            downloadInfo.status = ApkStatus.WAIT_LISTENER;
            gameInfo.setStatus(ApkStatus.WAIT_LISTENER);
            setButtonStatus(context, view, ApkStatus.WAIT_LISTENER);
            DownloadManagerService.addDownloadInfo(downloadInfo);
            DownloadManagerService.startTask(downloadInfo);
            EventBus.getDefault().post(EventBusMessage.BADGE);
            EventBus.getDefault().post(downloadInfo);
            GameDownEngin.getImpl(context).statGameDown(gameInfo.getGameId(), gameInfo.getType(), new Callback<String>() {
                @Override
                public void onSuccess(ResultInfo<String> resultInfo) {

                }

                @Override
                public void onFailure(Response response) {

                }
            });
            runnable.run();
        } else {
            ToastUtil.toast(context, "下载任务已存在");
        }
    }

    public static synchronized void downloadByGameInfo(final Context context, final GameInfo gameInfo, final TextView
            view,
                                                       final
                                                       Runnable runnable) {
//        downloadIn234G(context, new Runnable() {
//            @Override
//            public void run() {
        downloadByGameInfo2(context, gameInfo, view, runnable);
//            }
//        });
    }

    public static synchronized void downloadByDownloadInfo2(Context context, DownloadInfo downloadInfo, TextView view) {
        DownloadUtil.downloadApk(downloadInfo, DownloadManagerService.fileDownloadListener);
        if (!DownloadManagerService.checkDownloadInfoExist(downloadInfo.downloadTask)) {
            downloadInfo.precent = 0.0f;
            downloadInfo.speed = "";
            downloadInfo.isUpdate = false;
            downloadInfo.status = ApkStatus.WAIT_LISTENER;
            setButtonStatus(context, view, ApkStatus.WAIT_LISTENER);
            DownloadManagerService.addDownloadInfo(downloadInfo);
            DownloadManagerService.startTask(downloadInfo);
            EventBus.getDefault().post(EventBusMessage.BADGE);
            EventBus.getDefault().post(downloadInfo);
        }
    }

    public static synchronized void downloadByDownloadInfo2(Context context, DownloadInfo downloadInfo) {
        DownloadUtil.downloadApk(downloadInfo, DownloadManagerService.fileDownloadListener);
        if (!DownloadManagerService.checkDownloadInfoExist(downloadInfo.downloadTask)) {
            downloadInfo.status = ApkStatus.WAIT_LISTENER;
            downloadInfo.precent = 0.0f;
            downloadInfo.speed = "";
            if (!downloadInfo.getIsUpdate()) {
                downloadInfo.isUpdate = false;
            }
            DownloadManagerService.addDownloadInfo(downloadInfo);
            DownloadManagerService.startTask(downloadInfo);
            EventBus.getDefault().post(EventBusMessage.BADGE);
            EventBus.getDefault().post(downloadInfo);
        }
    }

    public static synchronized void downloadByDownloadInfo(final Context context, final DownloadInfo downloadInfo
    ) {
//        downloadIn234G(context, new Runnable() {
//            @Override
//            public void run() {
        downloadByDownloadInfo2(context, downloadInfo);
//            }
//        });
    }

    public static synchronized void downloadByDownloadInfo(final Context context, final DownloadInfo downloadInfo,
                                                           final TextView
                                                                   view) {
//        downloadIn234G(context, new Runnable() {
//            @Override
//            public void run() {
        downloadByDownloadInfo2(context, downloadInfo, view);
//            }
//        });
    }

//    public static void downloadIn234G(Context context, final Runnable runnable) {
//        if (CheckUtil.getNetworkType(context) == CheckUtil.NETTYPE_TYPE_MOBILE && !DownloadManagerService
//                .canDownloadIn234G()) {
//            new MaterialDialog.Builder(context)
//                    .title("提示")
//                    .content("当前为非WIFI环境,继续下载会消耗流量，是否继续下载？")
//                    .positiveColor(GoagalInfo.getInItInfo().androidColor)
//                    .negativeColorRes(R.color.gray_light)
//                    .positiveText("继续")
//                    .negativeText("放弃")
//                    .onAny(new MaterialDialog.SingleButtonCallback() {
//                        @Override
//                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            if (which == DialogAction.POSITIVE) {
//                                DownloadManagerService.setDownloadIn234G(true);
//                                runnable.run();
//                            }
//                        }
//                    })
//                    .show();
//            return;
//        } else {
//            runnable.run();
//        }
//    }

    static AppRepository appRepository;

    public static void install(final Context context, final GameInfo gameInfo) {
        if (appRepository == null) {
            appRepository = new AppRepository(context);
        }
        String apkPath = PathUtil.getApkPath(gameInfo.getName());
        if (!PreferenceUtil.getImpl(context).getBoolean(DescConstans.NO_INSTALL, false)) {
            ApkUtil.installApk(context, apkPath);
            return;
        }
        openApk(context, apkPath, gameInfo.getIconUrl(), gameInfo.getName());
    }

    public static void openApk(final Context context,final String apkPath, final String iconPath, final String name){
        LoadingUtil.show(context, "请稍后...");
        new AsyncTask<Void, Void, AppModel>() {
            @Override
            protected AppModel doInBackground(Void... voids) {
                AppModel appModel =  null;
                File file = new File(apkPath);
                PackageInfo packageInfo = ApkUtil.getApkPackageInfo(context, file);
                if (packageInfo != null) {
                    appModel = new AppModel(context, packageInfo);
                    appModel.fastOpen = false;
                    appModel.iconPath = iconPath;
                    try {
                        FreeInstallUtil.installApp(apkPath, packageInfo.packageName);
                    } catch (Throwable e) {
                        LogUtil.msg(e.getMessage());
                    }
                }
                return appModel;
            }

            @Override
            protected void onPostExecute(AppModel appModel) {
                 LoadingUtil.dismiss();
                 appModel.name = name;
                 LoadingActivity.launch(context, appModel, 0);
            }
        }.execute();
    }


    public static void install(final Context context, final DownloadInfo downloadInfo) {
        String apkPath = PathUtil.getApkPath(downloadInfo.getName());
        if (!PreferenceUtil.getImpl(context).getBoolean(DescConstans.NO_INSTALL, false)) {
            ApkUtil.installApk(context, apkPath);
            return;
        }
        openApk(context, apkPath, downloadInfo.getIconUrl(), downloadInfo.getName());
    }

    public static void install(final Context context, final GameInfo gameInfo, TextView view, Runnable
            runnable) {
        final File file = new File(PathUtil.getApkPath(gameInfo.getName()));
        if (file.exists()) {
            if (gameInfo.getPackageName() == null || gameInfo.getPackageName().isEmpty()) {
                gameInfo.setPackageName(ApkUtil.getApkPackageName(context, file));
            }
            install(context, gameInfo);
        } else {
            ToastUtil.toast(context, "文件已被删除，已经开始重新下载");
            DownloadInfo downloadInfo = DownloadManagerService.getDownloadInfo(gameInfo);
            if (downloadInfo != null) {
                downloadInfo.setStatus(ApkStatus.UNDOWNLOAD);
                DownloadManagerService.deleteDownloadInfo(downloadInfo, false);
            }
            downloadByGameInfo(context, gameInfo, view, runnable);
        }
    }

    public static void install2(Context context, DownloadInfo downloadInfo, TextView view) {
        File file = new File(PathUtil.getApkPath(downloadInfo.getName()));
        if (file.exists()) {
            install(context, downloadInfo);
        } else {
            ToastUtil.toast(context, "文件已被删除，已经开始重新下载");
            DownloadManagerService.deleteDownloadInfo(downloadInfo, false);
            downloadInfo.setStatus(ApkStatus.UNDOWNLOAD);
            downloadByDownloadInfo(context, downloadInfo, view);
            EventBus.getDefault().post(EventBusMessage.DOWNLIST_STATUS_CHANGE);
        }
    }

    public static void open(Context context, GameInfo gameInfo, TextView view, Runnable runnable) {
        try {
            ApkUtil.openApk(context, gameInfo.getPackageName());
        } catch (Exception e) {
            File file = new File(PathUtil.getApkPath(gameInfo.getName()));
            if (file.exists()) {
                String packageName = ApkUtil.getApkPackageName(context, file);
                if (!packageName.isEmpty()) {
                    try {
                        ApkUtil.openApk(context, packageName);
                    } catch (Exception e2) {
                        ToastUtil.toast(context, "文件已被卸载，已经开始重新安装");
                        DownloadInfo downloadInfo = DownloadManagerService.getDownloadInfo(gameInfo);
                        if (downloadInfo != null) {
                            downloadInfo.status = ApkStatus.DOWNLOADED;
                        }
                        ApkUtil.installApk(context, PathUtil.getApkPath(gameInfo.getName()));
                    }
                }
            } else {
                ToastUtil.toast(context, "文件已被删除，已经开始重新下载");
                DownloadInfo downloadInfo = DownloadManagerService.getDownloadInfo(gameInfo);
                if (downloadInfo != null) {
                    downloadInfo.setStatus(ApkStatus.UNDOWNLOAD);
                    DownloadManagerService.deleteDownloadInfo(downloadInfo, false);
                }
                downloadByGameInfo(context, gameInfo, view, runnable);
            }
        }
    }

    public static void open2(Context context, DownloadInfo downloadInfo, TextView view) {
        try {
            ApkUtil.openApk(context, downloadInfo.packageName);
        } catch (Exception e) {
            File file = new File(PathUtil.getApkPath(downloadInfo.getName()));
            if (file.exists()) {
                String packageName = ApkUtil.getApkPackageName(context, file);
                if (!packageName.isEmpty()) {
                    try {
                        ApkUtil.openApk(context, packageName);
                    } catch (Exception e2) {
                        ToastUtil.toast(context, "文件已被卸载，已经开始重新安装");
                        downloadInfo.status = ApkStatus.DOWNLOADED;
                        EventBus.getDefault().post(downloadInfo);
                        ApkUtil.installApk(context, PathUtil.getApkPath(downloadInfo.getName()));
                    }
                }
            } else {
                ToastUtil.toast(context, "文件已被删除，已经开始重新下载");
                DownloadManagerService.deleteDownloadInfo(downloadInfo, false);
                downloadInfo.setStatus(ApkStatus.UNDOWNLOAD);
                downloadByDownloadInfo(context, downloadInfo, view);
                EventBus.getDefault().post(EventBusMessage.DOWNLIST_STATUS_CHANGE);
            }
        }
    }


    public static void actionByStatus(final Context context, final GameInfo gameInfo, final TextView view, final
    Runnable
            runnable) {
        int status = gameInfo.getStatus();
        if (status == ApkStatus.UNDOWNLOAD) {
            try {
                if (gameInfo.getTotalSize() > SystemUtil.getAvailableBytes()) {
                    new MaterialDialog.Builder(context)
                            .title("提示")
                            .content(DescConstans.NO_MEMORY)
                            .positiveColor(GoagalInfo.getInItInfo().androidColor)
                            .negativeColorRes(R.color.gray_light)
                            .positiveText("知道了")
                            .show();
                    return;
                }
                if (gameInfo.isUpdate) {
                    DownloadInfo downloadInfo = DownloadManagerService.getDownloadInfo(gameInfo);
                    if (downloadInfo != null) {
                        DownloadManagerService.deleteDownloadInfo(downloadInfo, true);
                    } else {
                        File file = new File(PathUtil.getApkPath(gameInfo.getName()));
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
                downloadByGameInfo(context, gameInfo, view, runnable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (status == ApkStatus.DOWNLOADED) {
            install(context, gameInfo, view, runnable);
        } else if (status == ApkStatus.INSTALLED) {
            open(context, gameInfo, view, runnable);
        } else if (status == ApkStatus.Error || status == ApkStatus.Stop) {
            DownloadInfo downloadInfo = DownloadManagerService.getDownloadInfo(gameInfo);
            if (downloadInfo != null) {
                if (downloadInfo.downloadTask == null) {
                    DownloadUtil.downloadApk(downloadInfo, DownloadManagerService
                            .fileDownloadListener);
                    DownloadManagerService.startTask(downloadInfo);
                } else {
                    DownloadManagerService.restartTask(downloadInfo);
                }
                gameInfo.setStatus(ApkStatus.DOWNLOADING);
                downloadInfo.status = ApkStatus.DOWNLOADING;
                setButtonStatus(context, view, ApkStatus.DOWNLOADING);
                runnable.run();
            }
        } else if (status == ApkStatus.DOWNLOADING || status == ApkStatus.WAITING) {
            DownloadInfo downloadInfo = DownloadManagerService.getDownloadInfo(gameInfo);
            if (downloadInfo != null) {
                DownloadManagerService.pause(downloadInfo);
                downloadInfo.status = ApkStatus.Stop;
                gameInfo.setStatus(ApkStatus.Stop);
                setButtonStatus(context, view, ApkStatus.Stop);
                runnable.run();
            }
        }
    }

    public static void actionByStatus2(Context context, DownloadInfo downloadInfo, TextView view) {
        int status = downloadInfo.status;
        if (status == ApkStatus.DOWNLOADED) {
            install2(context, downloadInfo, view);
        } else if (status == ApkStatus.INSTALLED) {
            open2(context, downloadInfo, view);
        } else if (status == ApkStatus.Error || status == ApkStatus.Stop) {
            DownloadManagerService.restartTask(downloadInfo);
            downloadInfo.status = ApkStatus.DOWNLOADING;
            EventBus.getDefault().post(downloadInfo);
        } else if (status == ApkStatus.DOWNLOADING || status == ApkStatus.WAITING) {
            DownloadManagerService.pause(downloadInfo);
            downloadInfo.status = ApkStatus.Stop;
            setButtonStatus(context, view, ApkStatus.Stop);
            EventBus.getDefault().post(downloadInfo);
        }
    }

    public static void getStatuss2(Context context, List<DownloadInfo> downloadInfos) {
        if (downloadInfos == null || downloadInfos.size() <= 0) return;
        for (DownloadInfo downloadInfo : downloadInfos) {
            downloadInfo.status = ApkUtil.getApkStatus(context, downloadInfo);
        }
    }

    public static void getStatuss(Context context, List<GameInfo> gameInfos) {
        if (gameInfos == null || gameInfos.size() <= 0) return;

        for (int j = 0; j < gameInfos.size(); j++) {
            GameInfo gameInfo = gameInfos.get(j);
            gameInfo.setStatus(ApkStatus.UNDOWNLOAD);

            if (gameInfo.isUpdate) {
                for (int i = 0; i < DownloadManagerService.updateList.size(); i++) {
                    DownloadInfo downloadInfo = DownloadManagerService.updateList.get(i);
                    if (DownloadManagerService.isSameDownloadInfo(gameInfo, downloadInfo)) {
                        gameInfo.setStatus(downloadInfo.status);
                        if (downloadInfo.status == ApkStatus.INSTALLED) {
                            gameInfo.setPackageName(downloadInfo.packageName);
                            break;
                        }
                    }
                }

            } else {
                gameInfo.setStatus(ApkUtil.getApkStatus(context, gameInfo));
                boolean flag = false;

                for (int i = 0; i < DownloadManagerService.downloadingInfoList.size(); i++) {
                    DownloadInfo downloadInfo = DownloadManagerService.downloadingInfoList.get(i);
                    if (DownloadManagerService.isSameDownloadInfo(gameInfo, downloadInfo)) {
                        gameInfo.setStatus(downloadInfo.status);
                        flag = true;
                        break;
                    }

                }

                if (flag) {
                    continue;
                }

                for (int i = 0; i < DownloadManagerService.downloadedInfoList.size(); i++) {
                    DownloadInfo downloadInfo = DownloadManagerService.downloadedInfoList.get(i);
                    if (DownloadManagerService.isSameDownloadInfo(gameInfo, downloadInfo)) {
                        gameInfo.setStatus(downloadInfo.status);
                        if (downloadInfo.status == ApkStatus.INSTALLED) {
                            gameInfo.setPackageName(downloadInfo.packageName);
                            flag = true;
                            break;
                        }
                    }
                }

                if (flag) {
                    continue;
                }

                if (CheckUtil.isInstall(context, gameInfo.getPackageName()) && !gameInfo.isUpdate) {
                    gameInfo.setStatus(ApkStatus.INSTALLED);
                }
            }
        }
    }


    /**
     * 删除快捷方式
     */
    private static void deleteShortcut(final Activity context, final ShortCutInfo shortCutInfo) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(shortCutInfo.url));
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final Intent putShortCutIntent = new Intent();
        putShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        putShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortCutInfo.name);
        putShortCutIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        context.sendBroadcast(putShortCutIntent);
    }

    /**
     * 创建快捷方式
     */
    public static void createShortcut(final Activity context, final ShortCutInfo shortCutInfo) {

        if (hasShortcut(context, shortCutInfo)) {
            LogUtil.msg("快捷方式已存在");
            return;
        }
        LogUtil.msg("开始创建快捷方式");
        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                try {
                    shortCutInfo.icon = Picasso.with(context).load(shortCutInfo.iconUrl).get();
                    LogUtil.msg("下载快捷方式");
                    if (shortCutInfo.icon != null) {
                        LogUtil.msg("下载快捷成功");
                        Intent intent = new Intent();
                        intent.setData(Uri.parse(shortCutInfo.url));
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        final Intent putShortCutIntent = new Intent();
                        putShortCutIntent.putExtra("duplicate", false);
                        putShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
                        putShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortCutInfo.name);
                        putShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, shortCutInfo.icon);
                        putShortCutIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                        context.sendBroadcast(putShortCutIntent);
                        LogUtil.msg("创建快捷方式任务执行");
                    } else {
                        LogUtil.msg("下载快捷失败");
                    }
                } catch (Exception e) {
                    LogUtil.msg("创建快捷方式失败->" + e);
                }
            }
        });
    }

    /**
     * 根据 title 判断快捷方式是否存在
     *
     * @return
     */
    private static boolean hasShortcut(Context context, final ShortCutInfo shortCutInfo) {
        String url;
        if (getSystemVersion() < 8) {
            url = "content://com.android.launcher.settings/favorites?notify=true";
        } else {
            url = "content://com.android.launcher2.settings/favorites?notify=true";
        }

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Uri.parse(url), new String[]{"title", "iconResource"},
                "title=?", new String[]{shortCutInfo.name}, null);
        if (cursor != null && cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取系统的SDK版本号
     *
     * @return
     */
    private static int getSystemVersion() {
        return Build.VERSION.SDK_INT;
    }


    public static void createShortcuts(final Activity activity) {
        try {
            if (PreferenceUtil.getImpl(activity).getBoolean("createShortcuts", false)) {
                return;
            }
            PreferenceUtil.getImpl(activity).putBoolean("createShortcuts", true);
            if (GoagalInfo.channelInfo != null && GoagalInfo.channelInfo.iconList != null && GoagalInfo.channelInfo
                    .iconList.size() > 0) {
                for (ShortCutInfo shortCutInfo : GoagalInfo.channelInfo.iconList) {
                    createShortcut(activity, shortCutInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.msg("创建桌面快捷方式失败->" + e);
        }
    }

    public static void copyFromAssets(final Context context, final String name, String path) {
        AssetManager assetManager = context.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(name);
            out = new FileOutputStream(path);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        } catch (Exception e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                    in = null;
                } catch (Exception e) {
                }
            }
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                    out = null;
                } catch (Exception e) {
                }
            }
        }
    }

    public static void installApkFromAssets(final Activity context, final String name) {
        File file = new File(PathUtil.getApkPath(name));
        if (!file.exists()) {
            copyFromAssets(context, name, file.getAbsolutePath());
        }
        final String packageName = ApkUtil.getApkPackageName(context, file);
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!CheckUtil.isInstall(context, packageName)) {
                    ApkUtil.installApk(context, PathUtil.getApkPath(name));
                }
            }
        });
    }

    public static void freeOpenApkFromAssets(final Activity context, final String name) {
//        File file =new  File(PathUtil.getApkPath(name));
//        if(!file.exists()){
//            copyFromAssets(context, name, file.getAbsolutePath());
//        }
//        final DownloadInfo downloadInfo = new DownloadInfo();
//        downloadInfo.name = name;
//        downloadInfo.packageName = ApkUtil.getApkPackageName(context, file);
//        new AsyncTask<Void, Void, Integer>() {
//            @Override
//            protected Integer doInBackground(Void... params) {
//                int state = FreeInstallUtil.installApk(downloadInfo);
//                return state;
//            }
//
//            @Override
//            protected void onPostExecute(Integer integer) {
//                super.onPostExecute(integer);
//                if (integer == FreeInstallUtil.SUCCESS || integer == FreeInstallUtil.INSTALLED) {
//                    try {
//                        FreeInstallUtil.openApk(context, downloadInfo.getPackageName());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    if (integer == FreeInstallUtil.INSTALL_CONNECTED_FAIL) {
//                        ToastUtil.toast2(context, "连接失败");
//                    } else if (integer == FreeInstallUtil.INSTALLED_FAIL) {
//                        ToastUtil.toast2(context, "安装失败");
//                    } else if (integer == FreeInstallUtil.NO_REQUESTEDPERMISSION) {
//                        ToastUtil.toast2(context, "权限不足");
//                    }
//                    ApkUtil.installApk(context, PathUtil.getApkPath(downloadInfo.getName()));
//                }
//            }
//        }.execute();
    }

    public static void deadDownloadInfo(Context context, String ico, String name, String url, boolean ishiden,
                                        boolean isInstall) {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.iconUrl = ico;
        downloadInfo.name = name;
        downloadInfo.url = url;
        downloadInfo.isUpdate = ishiden;
        downloadInfo.isInstall = isInstall;
        downloadByDownloadInfo(context, downloadInfo);
    }

}
