package com.gamebox_idtkown.core;

import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.utils.PathUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;


/**
 * Created by zhangkai on 16/9/19.
 */

/**
 * DownloadUtil is a apk download util with FileDownloader.
 */
public class DownloadUtil {

    /**
     * Download apk with FileDownloader.
     *
     * @param gameInfo The info is a model of game info
     * @param fileDownloadListener The fileDownloadListener is a listener of download task
     * @return DownloadInfo is wrapper, both downloadId and downloadTask
     * @see FileDownloader
     * @see FileDownloadListener
     * @see DownloadInfo
     */
    public static final int AutoRetryTimes = 20;
    public static final int MinIntervalUpdateSpeed = 1000;
    public static final int CallbackProgressMinInterval = 200;
    public static final int CallbackProgressTimes = 10000;

    public static DownloadInfo downloadApk(GameInfo gameInfo, FileDownloadListener fileDownloadListener, int times) {
        DownloadInfo downloadInfo = new DownloadInfo();
        BaseDownloadTask task = FileDownloader.getImpl().create(gameInfo.getUrl());
        downloadInfo.downloadTask = task;
        task.setSyncCallback(true).setAutoRetryTimes(AutoRetryTimes).setMinIntervalUpdateSpeed(MinIntervalUpdateSpeed).setPath(PathUtil
                .getApkPath(gameInfo
                        .getName()), false).setCallbackProgressTimes(times).setCallbackProgressMinInterval
                (CallbackProgressMinInterval).setListener
                (fileDownloadListener);
        return downloadInfo;
    }

    public static DownloadInfo downloadApk(GameInfo gameInfo, FileDownloadListener fileDownloadListener) {
        return downloadApk(gameInfo, fileDownloadListener, CallbackProgressTimes);
    }

    /**
     * Download apk with FileDownloader.
     *
     * @param downloadInfo         The info is a model of download info
     * @param fileDownloadListener The fileDownloadListener is a listener of download task
     * @return DownloadInfo is wrapper, both downloadId and downloadTask
     * @see FileDownloader
     * @see FileDownloadListener
     * @see DownloadInfo
     */
    public static void downloadApk(DownloadInfo downloadInfo, FileDownloadListener fileDownloadListener, int times) {
        BaseDownloadTask task = FileDownloader.getImpl().create(downloadInfo.getUrl());
        downloadInfo.downloadTask = task;
        task.setSyncCallback(true).setAutoRetryTimes(AutoRetryTimes)
                .setMinIntervalUpdateSpeed(MinIntervalUpdateSpeed)
                .setCallbackProgressMinInterval(CallbackProgressMinInterval)
                .setPath(PathUtil.getApkPath(downloadInfo.getName()), false)
                .setCallbackProgressTimes(CallbackProgressTimes)
                .setListener(fileDownloadListener);
    }

    public static void downloadApk(DownloadInfo downloadInfo, FileDownloadListener fileDownloadListener) {
        downloadApk(downloadInfo, fileDownloadListener, CallbackProgressTimes);
    }


    /**
     * pause the download task.
     *
     * @param downloadInfo
     * @see DownloadInfo
     */
    public static void pause(DownloadInfo downloadInfo) {

        downloadInfo.downloadTask.pause();
    }

    /**
     * pause all of the download task.
     */
    public static void pauseAll() {
        FileDownloader.getImpl().pauseAll();
    }

    /**
     * clear the download task
     *
     * @param downloadInfo
     * @see DownloadInfo
     */
    public static void clear(DownloadInfo downloadInfo) {
        FileDownloader.getImpl().clear(downloadInfo.downloadTask.getId(), downloadInfo.downloadTask.getTargetFilePath());
    }

    /**
     * get status of the download task
     *
     * @param downloadInfo
     * @see DownloadInfo
     */
    public static byte getStatus(DownloadInfo downloadInfo) {
        return downloadInfo.downloadTask.getStatus();
    }

    /**
     * get speed of the download task
     *
     * @param downloadInfo
     * @see DownloadInfo
     */
    public static int getSpeed(DownloadInfo downloadInfo) {
        return downloadInfo.downloadTask.getSpeed();
    }


}
