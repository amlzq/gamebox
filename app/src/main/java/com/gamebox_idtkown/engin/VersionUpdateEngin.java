package com.gamebox_idtkown.engin;

import android.content.Context;

import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.VersionInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangkai on 16/10/28.
 */
public class VersionUpdateEngin extends BaseEngin<VersionInfo> {

    private static VersionUpdateEngin versionUpdateEngin;

    public VersionUpdateEngin(Context context) {
        super(context);
    }

    public static VersionUpdateEngin getImpl(Context context) {
        if (versionUpdateEngin == null) {
            synchronized (VersionUpdateEngin.class) {
                versionUpdateEngin = new VersionUpdateEngin(context);
            }
        }
        return versionUpdateEngin;
    }

    public void checkUpdate(Callback<VersionInfo> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("update", "true");
        agetResultInfo(true, VersionInfo.class, params, callback);
    }

    @Override
    public String getUrl() {
        return Config.VERSION_URL;
    }
}
