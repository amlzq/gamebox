package com.gamebox_idtkown.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gamebox_idtkown.R;

/**
 * Created by zhangkai on 16/10/25.
 */
public class DialogGiftUtil {
    private static Dialog dialog ;// 显示对话框

    private static TextView tvTitle;
    private static TextView tvCode;
    private static TextView btnCopy;

    public static void show(final Activity activity,final String code, String title){
        if(dialog != null){
            dismiss();
        }
        dialog = new Dialog(activity, R.style.customDialog);
        View view = LayoutInflater.from(activity).inflate(
                R.layout.dialog_gift_get_success, null);
        tvTitle = (TextView)view.findViewById(R.id.title);
        tvCode = (TextView)view.findViewById(R.id.code);
        btnCopy = (TextView)view.findViewById(R.id.btn_copy);
        View view2 = view.findViewById(R.id.rl_get_view);
        StateUtil.setDrawable(activity, view2, 1.5f, Color.WHITE);
        StateUtil.setDrawable(activity, btnCopy, 1.5f);
        tvTitle.setText(title);
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("礼包码", code);
                clipboard.setPrimaryClip(clip);
                ToastUtil.toast2(activity, "复制成功");
                dismiss();
            }
        });
        tvCode.setText("礼包码: " + code);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        StateUtil.softKey(dialog.getWindow());
        dialog.show();
    }

    public static void show2(final Activity activity, String code){
        if(dialog != null){
            dismiss();
        }
        dialog = new Dialog(activity, R.style.customDialog);
        View view = LayoutInflater.from(activity).inflate(
                R.layout.dialog_gift_get_success, null);
        tvTitle  = (TextView)view.findViewById(R.id.title);
        tvCode = (TextView)view.findViewById(R.id.code);
        btnCopy = (TextView)view.findViewById(R.id.btn_copy);
        View view2 = view.findViewById(R.id.rl_get_view);
        StateUtil.setDrawable(activity, view2, 1.5f, Color.WHITE);
        StateUtil.setDrawable(activity, btnCopy, 1.5f);
        btnCopy.setText("确定");
        tvTitle.setText("复制成功");
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvCode.setText("礼包码：" + code);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
    }

    public static void show3(final Activity activity, String code, String title, String btnTititle){
        if(dialog != null){
            dismiss();
        }
        dialog = new Dialog(activity, R.style.customDialog);
        View view = LayoutInflater.from(activity).inflate(
                R.layout.dialog_gift_get_success, null);
        tvTitle  = (TextView)view.findViewById(R.id.title);
        tvCode = (TextView)view.findViewById(R.id.code);
        btnCopy = (TextView)view.findViewById(R.id.btn_copy);
        View view2 = view.findViewById(R.id.rl_get_view);
        StateUtil.setDrawable(activity, view2, 1.5f, Color.WHITE);
        StateUtil.setDrawable(activity, btnCopy, 1.5f);
        btnCopy.setText(btnTititle);
        tvTitle.setText(title);
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvCode.setText(code);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
    }

    public static void dismiss(){
        try {
            dialog.dismiss();
            dialog = null;
        }catch (Exception e){
            LogUtil.msg("Loading对话框关闭失败->" + e);
        }
    }
}
