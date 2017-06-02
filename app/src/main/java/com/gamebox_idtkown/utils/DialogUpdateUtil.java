package com.gamebox_idtkown.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.core.ApkStatus;
import com.gamebox_idtkown.core.ApkUtil;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.domain.VersionInfo;
import com.gamebox_idtkown.services.DownloadManagerService;
import com.liulishuo.filedownloader.FileDownloader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/12/1.
 */
public class DialogUpdateUtil {
    private Dialog dialog;// 显示对话框

    private TextView tvTitle;
    private TextView tvTitle2;
    private TextView btnCopy;
    private View processView;
    private int width;

    @BindView(R.id.speed)
    TextView tvSpeed;

    @BindView(R.id.size)
    TextView tvSize;

    @BindView(R.id.version)
    TextView tvVersion;

    DownloadInfo downloadInfo;

    public static DialogUpdateUtil dialogUpdateUtil;

    private DialogUpdateUtil() {
        EventBus.getDefault().register(this);
    }

    public static DialogUpdateUtil getImpl() {
        if (dialogUpdateUtil == null) {
            dialogUpdateUtil = new DialogUpdateUtil();
        }
        return dialogUpdateUtil;
    }

    public void show(final Activity activity, String title, final boolean isStrong) {
        if (dialog != null) {
            dismiss();
        }
        width = ScreenUtil.getWidth(activity) - ScreenUtil.dip2px(activity, 80);

        dialog = new Dialog(activity, R.style.customDialog);
        View view = LayoutInflater.from(activity).inflate(
                R.layout.dialog_update, null);
        tvTitle = (TextView) view.findViewById(R.id.title);
        tvTitle2 = (TextView) view.findViewById(R.id.title2);
        tvVersion = (TextView) view.findViewById(R.id.version);
        tvSpeed = (TextView) view.findViewById(R.id.speed);
        tvSize = (TextView) view.findViewById(R.id.size);

        processView = view.findViewById(R.id.process);
        btnCopy = (TextView) view.findViewById(R.id.btn_copy);
        View view2 = view.findViewById(R.id.rl_get_view);
        StateUtil.setDrawable(activity, view2, 1.5f, Color.WHITE);
        StateUtil.setDrawable(activity, btnCopy, 1.5f);
        StateUtil.setDrawable(activity, processView, 4);
        tvTitle.setText(title);
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (btnCopy.getText().equals("安装") && downloadInfo != null) {
                    ApkUtil.installApk(activity, PathUtil.getApkPath(downloadInfo.name));
                }
                runOnStrong(isStrong);

            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog,
                                 int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    runOnStrong(isStrong);
                }
                return true;
            }
        });
        dialog.show();
        update(activity);
    }

    private void runOnStrong(boolean isStrong) {
        if (isStrong) {
            FileDownloader.getImpl().pauseAll();
            DownloadManagerService.deleteDownloadInfo(downloadInfo, false);
            System.exit(0);
        }
    }

    private void update(Activity activity) {
        if (GoagalInfo.getInItInfo() != null) {
            String version = GoagalInfo.packageInfo == null ? "未知" : GoagalInfo.packageInfo.versionName;
            VersionInfo versionInfo = GoagalInfo.getInItInfo().update_info;
            if (GoagalInfo.getInItInfo().is_update && versionInfo != null && !version.equals(versionInfo.version)) {
                downloadInfo = new DownloadInfo();
                downloadInfo.status = ApkStatus.WAIT_LISTENER;

                downloadInfo.name = activity.getResources().getString(R.string.app_name) + version;
                downloadInfo.packageName = activity.getPackageName();
                downloadInfo.iconUrl = "{self}";
                downloadInfo.url = GoagalInfo.getInItInfo().update_info.url;

                tvVersion.setText(Html.fromHtml("&nbsp;&nbsp;" + version + "<font androidColor=" + GoagalInfo.getInItInfo().themeColor +
                        ">->" +
                        GoagalInfo.getInItInfo()
                                .update_info
                                .version + "</font>"));

                File file = new File(PathUtil.getApkPath(downloadInfo.name));
                if (file.exists()) {
                    tvTitle2.setText(downloadInfo.name);
                    tvSize.setText("100%");
                    tvSpeed.setText("已完成");
                    btnCopy.setText("安装");
                    setProcess(1);
                    return;
                }

                ApkStatusUtil.downloadByDownloadInfo(activity, downloadInfo);
                tvTitle2.setText(downloadInfo.name);
                tvSize.setText("0M/0M");
                tvSpeed.setText("等待中");
            }
        }
    }

    public void setProcess(float precent) {
        try {
            FrameLayout.LayoutParams l = (FrameLayout.LayoutParams) processView.getLayoutParams();
            l.width = (int) (width * precent);
            processView.setLayoutParams(l);
        } catch (Exception e) {
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DownloadInfo downloadInfo) {
        if (downloadInfo != null) {
            setProcess(downloadInfo.getPrecent());
            if (downloadInfo.status == ApkStatus.DOWNLOADED) {
                tvSize.setText("100%");
                btnCopy.setText("安装");
            } else {
                tvSize.setText(CheckUtil.checkStr(downloadInfo.size, "0M/0M"));
            }
            tvSpeed.setText(CheckUtil.checkStr(downloadInfo.speed, "等待中"));
        }
    }

    public void dismiss() {
        try {
            dialog.dismiss();
            dialog = null;
        } catch (Exception e) {
            LogUtil.msg("Loading对话框关闭失败->" + e);
        }
    }
}
