package com.gamebox_idtkown.utils;

import org.jdeferred.android.AndroidDeferredManager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author Lody
 *         <p>
 *         A set of tools for UI.
 */
public class VUiKit {
	private static final AndroidDeferredManager gDM = new AndroidDeferredManager();
	private static final Handler gUiHandler = new Handler(Looper.getMainLooper());

	public static AndroidDeferredManager defer() {
		return gDM;
	}

	public static int dpToPx(Context context, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}

	public static void post(Runnable r) {
		gUiHandler.post(r);
	}

	public static void postDelayed(long delay, Runnable r) {
		gUiHandler.postDelayed(r, delay);
	}

	public static void setWindowAttributes(Window window){
		WindowManager.LayoutParams attrs = window.getAttributes();
		WindowManager.LayoutParams params = window.getAttributes();
		params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
		window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
		window.setAttributes(params);
	}

}
