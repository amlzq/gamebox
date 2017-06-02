package com.gamebox_idtkown.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.gamebox_idtkown.R;

/**
 * Created by zhangkai on 16/11/16.
 */
public class DialogShareUtil {
    private static Dialog dialog;// 显示对话框

    private static RelativeLayout share1;
    private static RelativeLayout share2;
    private static RelativeLayout share3;

    public static void show(final Activity activity, int type) {
        if (dialog != null) {
            dismiss();
        }
        dialog = new Dialog(activity, R.style.customDialog);
        View view = LayoutInflater.from(activity).inflate(
                R.layout.dialog_loading_share, null);
        View view2 = view.findViewById(R.id.rl_get_view);
        StateUtil.setDrawable(activity, view2, 1.5f, Color.WHITE);

        share1 = (RelativeLayout) view.findViewById(R.id.rlShare1);

        share2 = (RelativeLayout) view.findViewById(R.id.rlShare2);

        share3 = (RelativeLayout) view.findViewById(R.id.rlShare3);

        StateUtil.setRipple(share1);
        StateUtil.setRipple(share2);
        StateUtil.setRipple(share3);

        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
    }

    public static void setOnClickListener(View.OnClickListener clickListener) {
        share1.setTag("1");
        share2.setTag("0");
        share3.setTag("2");
        share1.setOnClickListener(clickListener);
        share2.setOnClickListener(clickListener);
        share3.setOnClickListener(clickListener);
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