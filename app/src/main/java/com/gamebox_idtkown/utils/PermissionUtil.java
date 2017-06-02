package com.gamebox_idtkown.utils;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;

/**
 * Created by zhangkai on 16/9/29.
 */
public class PermissionUtil {
    public static void requestPermission(Activity context, String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(context,
                new String[]{permissionName}, permissionRequestCode);
    }
}
