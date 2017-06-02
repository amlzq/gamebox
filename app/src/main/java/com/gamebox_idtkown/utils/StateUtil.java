package com.gamebox_idtkown.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.gamebox_idtkown.domain.GoagalInfo;

import java.lang.reflect.Field;

/**
 * Created by zhangkai on 16/10/17.
 */
public class StateUtil {
    public static void setRipple(View view) {
         MaterialRippleLayout.on(view)
                .rippleColor(GoagalInfo.getInItInfo().androidColor)
                .rippleAlpha(0.2f)
                .rippleHover(true)
                .rippleDuration(200)
                .create();
    }

    public static void setDrawable(Context context, View view) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(GoagalInfo.getInItInfo().androidColor);
        drawable.setCornerRadius(ScreenUtil.dip2px(context, 10));
        view.setBackground(drawable);
    }


    public static void setDrawable(Context context, View view, float n) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(GoagalInfo.getInItInfo().androidColor);
        drawable.setCornerRadius(ScreenUtil.dip2px(context, n));
        view.setBackground(drawable);
    }

    public static void setDrawable(Context context, View view, float n, int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(ScreenUtil.dip2px(context, n));
        view.setBackground(drawable);
    }

    public static void setStorke(Context context, View view, float n) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setStroke(ScreenUtil.dip2px(context, 1), GoagalInfo.getInItInfo().androidColor);
        drawable.setCornerRadius(ScreenUtil.dip2px(context, n));
        view.setBackground(drawable);
    }

    public static void setCursorDrawableColor(Context context, EditText editText, int color) {
        try {
            Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            int mCursorDrawableRes = fCursorDrawableRes.getInt(editText);
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(editText);
            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);
            Drawable[] drawables = new Drawable[2];
            drawables[0] = ContextCompat.getDrawable(context, mCursorDrawableRes);
            drawables[1] = ContextCompat.getDrawable(context, mCursorDrawableRes);
            drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            drawables[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            fCursorDrawable.set(editor, drawables);
        } catch (Throwable ignored) {
        }
    }

    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private static void setGradientDrawable(View view) {
        int colors[] = {0xe0a6c0cd, 0xe0255779, 0xe03e7492, 0xe000afff};
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BL_TR, colors);
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        view.setBackground(gradientDrawable);
    }

    public static void softKey(Window window) {
        WindowManager.LayoutParams attrs = window.getAttributes();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setAttributes(params);
    }


}
