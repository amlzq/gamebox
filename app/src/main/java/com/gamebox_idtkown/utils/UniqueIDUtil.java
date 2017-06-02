package com.gamebox_idtkown.utils;

/**
 * Created by zhangkai on 16/11/16.
 */

import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class UniqueIDUtil {
    private static String uid = null;

    public static String getUid(Context context) {
        if (uid == null || uid.isEmpty()) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            uid = telephonyManager.getDeviceId();
        }
        if (uid == null || uid.isEmpty()) {
            uid = Secure.getString(context.getContentResolver(),
                    Secure.ANDROID_ID);
        }
        return uid;
    }
}
