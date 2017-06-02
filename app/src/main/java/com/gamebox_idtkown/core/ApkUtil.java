package com.gamebox_idtkown.core;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.utils.CheckUtil;
import com.gamebox_idtkown.utils.PathUtil;
import com.gamebox_idtkown.utils.SizeUitl;
import com.gamebox_idtkown.utils.TaskUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhangkai on 16/9/12.
 */

/**
 * ApkUtil is a series of oprations for apk.
 */
public class ApkUtil {
    public static List<String> packageNames = new ArrayList();

    public static void setPackageNames(Context context) {
        if (packageNames.size() > 0) {
            return;
        }
        List<ApplicationInfo> applicationInfos = context.getPackageManager().getInstalledApplications(0);
        for (int i = 0; i < applicationInfos.size(); i++) {
            ApplicationInfo applicationInfo = applicationInfos.get(i);
            packageNames.add(applicationInfo.packageName);
        }
    }


    /**
     * Open a apk file ready to install
     *
     * @param context the context of Activity
     * @param apkPath the path of apk file
     */
    private static void openFile(Context context, String apkPath) {
        File file = new File(apkPath);
        if (file.exists()) {
            // TODO Auto-generated method stub
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");

            context.startActivity(intent);
        }
    }

    /**
     * Install a apk file
     *
     * @param context The context of Activity
     * @param apkPath The path of apk file
     */
    public static void installApk(final Context context, final String apkPath) {
        if (isRoot()) {
            TaskUtil.getImpl().runTask(new Runnable() {
                @Override
                public void run() {
                    if (!installApk(apkPath)) {
                        openFile(context, apkPath);
                    }
                }
            });

        } else {
            openFile(context, apkPath);
        }
    }

    /**
     * 判断手机是否root，不弹出root请求框<br/>
     */
    private static boolean isRoot() {
        String binPath = "/system/bin/su";
        String xBinPath = "/system/xbin/su";
        if (new File(binPath).exists() && isExecutable(binPath))
            return true;
        if (new File(xBinPath).exists() && isExecutable(xBinPath))
            return true;
        return false;
    }

    private static boolean isExecutable(String filePath) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("ls -l " + filePath);
            // 获取返回内容
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String str = in.readLine();
            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x')
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
        return false;
    }

    /**
     * Install a apk file with root
     *
     * @param apkPath The path of apk file
     */
    public static boolean installApk(String apkPath) {
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            // 申请su权限
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            String command = "pm install -r " + apkPath + "\n";
            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String msg = "";
            String line;
            // 读取命令的执行结果
            while ((line = errorStream.readLine()) != null) {
                msg += line;
            }
            Log.d("TAG", "install msg is " + msg);
            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
            if (!msg.contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("TAG", e.getMessage(), e);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                Log.e("TAG", e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * Open a apk with lanuch activity
     *
     * @param packageName The package name of apk file
     */
    public static void openApk(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Get apk status
     *
     * @param context  The context of Activity
     * @param gameInfo The apkInfo is a model of apk info
     * @return The status of the apk
     * @see ApkStatus
     */
    public static int getApkStatus(Context context, GameInfo gameInfo) {
        String packName = gameInfo.getPackageName();
        if (packName != null) {
            if (CheckUtil.isInstall(context, packName)) return ApkStatus.INSTALLED;
        }
        String path = PathUtil.getApkPath(gameInfo.getName());
        File file = new File(path);
        if (file.exists()) {
            return ApkStatus.DOWNLOADED;
        }
        return ApkStatus.UNDOWNLOAD;
    }

    /**
     * Get apk status
     *
     * @param context The context of Activity
     * @return The status of the apk
     * @see ApkStatus
     */
    public static int getApkStatus(Context context, DownloadInfo downloadInfo) {
        if (downloadInfo.getStatus() == ApkStatus.INSTALLED && !downloadInfo.getIsUpdate()) {
            return ApkStatus.INSTALLED;
        }
        if (downloadInfo.packageName != null && !downloadInfo.getIsUpdate()) {
            if (!downloadInfo.packageName.equals(context.getPackageName())) {
                if (CheckUtil.isInstall(context, downloadInfo.packageName)) return ApkStatus.INSTALLED;
            }
        }
        int status = downloadInfo.status;
        if (downloadInfo.getStatus() == ApkStatus.DOWNLOADED) {
            String path = PathUtil.getApkPath(downloadInfo.name);
            File file = new File(path);
            if (file.exists()) {
                String packageName = getApkPackageName(context, file);
                downloadInfo.setSize(SizeUitl.getMKBStr((int) file.length()));
                if (!packageName.equals(downloadInfo.packageName)) {
                    downloadInfo.packageName = packageName;
                    if (CheckUtil.isInstall(context, packageName)) {
                        status = ApkStatus.INSTALLED;
                    } else {
                        status = ApkStatus.DOWNLOADED;
                    }
                } else {
                    status = ApkStatus.DOWNLOADED;
                }
            } else {
                downloadInfo.isDelete = true;
            }
        }
//      else {
//            File tempfile = new File(PathUtil.getApkPath(downloadInfo.name) + ".temp");
//            if (tempfile.exists()) {
//                downloadInfo.isDelete = false;
//                status = downloadInfo.status;
//            } else {
//                //文件下载完成 状态未更新
//                String path = PathUtil.getApkPath(downloadInfo.name);
//                File file = new File(path);
//                if (file.exists()) {
//                    String packageName = getApkPackageName(context, file);
//                    downloadInfo.setSize(SizeUitl.getMKBStr((int) file.length()));
//                    if (!packageName.equals(downloadInfo.packageName)) {
//                        downloadInfo.packageName = packageName;
//                        if (CheckUtil.isInstall(context, packageName)) {
//                            status = ApkStatus.INSTALLED;
//                        } else {
//                            status = ApkStatus.DOWNLOADED;
//                        }
//                    } else {
//                        status = ApkStatus.DOWNLOADED;
//                    }
//                } else {
//                    downloadInfo.isDelete = true;
//                }
//            }
//        }
        return status;
    }


    public static String getApkPackageName(Context context, File file) {
        String packeName = "";
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(file.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
        if (info != null && info.packageName != null) {
            packeName = info.packageName;

        }
        return packeName;
    }

    public static PackageInfo getApkPackageInfo(Context context, File file) {
        String packeName = "";
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(file.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
        return info;
    }

    public static String getApkVersion(Context context, String packageName) {
        String version = "1.0";
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(packageName, 0);
            version = info.versionName;
        } catch (Exception e) {

        }
        return version;
    }

}
