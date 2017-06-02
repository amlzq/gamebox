package com.gamebox_idtkown.utils;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

/**
 * Created by zhangkai on 16/11/9.
 */
public class SystemUtil {
    public static long getAvailableBytes() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = 0;
        if (Build.VERSION.SDK_INT > 17) {
            bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        } else {
            bytesAvailable = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();

        }
        return bytesAvailable;
    }
}
