package com.gamebox_idtkown.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamebox_idtkown.R;


/**
 * Created by zhangkai on 16/10/25.
 */
public class LoadingUtil {
    private static Dialog dialog;// 显示对话框

    private static TextView tvMsg;
    private static ImageView ivCircle;


    public static void show(Context activity, String msg) {
        if (dialog != null) {
            dismiss();
        }
        dialog = new Dialog(activity, R.style.customDialog);
        View view = LayoutInflater.from(activity).inflate(
                R.layout.view_loading, null);
        tvMsg = (TextView) view.findViewById(R.id.tv_msg);
        ivCircle = (ImageView) view.findViewById(R.id.iv_circle);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        tvMsg.setText(msg);
        ivCircle.startAnimation(AnimationUtil.rotaAnimation());
        dialog.show();
    }

    public static void show2(Context activity, String msg) {
        if (dialog != null) {
            dismiss();
        }
        dialog = new Dialog(activity, R.style.customDialog);
        View view = LayoutInflater.from(activity).inflate(
                R.layout.view_loading, null);
        tvMsg = (TextView) view.findViewById(R.id.tv_msg);
        ivCircle = (ImageView) view.findViewById(R.id.iv_circle);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        tvMsg.setText(msg);
        ivCircle.startAnimation(AnimationUtil.rotaAnimation());
        if (Build.VERSION.SDK_INT > 18) {
            dialog.getWindow().setType(WindowManager.LayoutParams
                    .TYPE_TOAST);
        } else {
            dialog.getWindow().setType(WindowManager.LayoutParams
                    .TYPE_SYSTEM_ALERT);
        }
        dialog.show();
    }


    public static void dismiss() {
        try {
            ivCircle.clearAnimation();
            dialog.dismiss();
            dialog = null;
            tvMsg = null;
            ivCircle = null;
        } catch (Exception e) {
            LogUtil.msg("Loading对话框关闭失败->" + e);
        }
    }
}
