package com.gamebox_idtkown.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gamebox_idtkown.R;

/**
 * Created by zhangkai on 16/11/16.
 */
public class DialogGoodUtil {
    private static Dialog dialog;// 显示对话框

    private static TextView btnOk;

    public static void show(final Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

        if (dialog != null) {
            dismiss();
        }

        dialog = new Dialog(activity, R.style.customDialog);
        View view = LayoutInflater.from(activity).inflate(
                R.layout.dialog_good_get_success, null);
        btnOk = (TextView) view.findViewById(R.id.btn_copy);
        View view2 = view.findViewById(R.id.rl_get_view);
        StateUtil.setDrawable(activity, view2, 1.5f, Color.WHITE);
        StateUtil.setDrawable(activity, btnOk, 1.5f);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
    }

    public static void dismiss() {
        try {
            dialog.dismiss();
            dialog = null;
        } catch (Exception e) {
            LogUtil.msg("Loading对话框关闭失败->" + e);
        }
    }
}