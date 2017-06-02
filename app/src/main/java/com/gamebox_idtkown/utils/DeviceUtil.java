package com.gamebox_idtkown.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * Created by zhangkai on 16/10/26.
 */
public class DeviceUtil {
    /**
     * 获取手机IMEI号 * add <uses-permission android:name="android.permission.READ_PHONE_STATE" /> in AndroidManifest.xml * @param context * @return
     */
    public static String getDeviceIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获取手机厂商 * @return
     */
    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取手机型号 * @return
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机系统版本号 * @return
     */
    public static String getDeviceSystemVersion() {
        return Build.VERSION.RELEASE;
    }

}
