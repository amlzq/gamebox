package com.gamebox_idtkown.engin;

import android.content.Context;

import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.listeners.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangkai on 16/11/30.
 */
public class UnInstallEngin extends BaseEngin<String> {
    private static UnInstallEngin unInstallEngin;

    public UnInstallEngin(Context context) {
        super(context);
    }

    public static UnInstallEngin getImpl(Context context) {
        if (unInstallEngin == null) {
            synchronized (UnInstallEngin.class) {
                unInstallEngin = new UnInstallEngin(context);
            }
        }
        return unInstallEngin;
    }

    public void uninstall(String gameid, String type, String pacekageName, Callback<String> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("game_id", gameid);
        params.put("type", type);
        params.put("package_name", pacekageName);
        agetResultInfo(true, String.class, params, callback);
    }

    @Override
    public String getUrl() {
        return Config.UNINSTALL_URL;
    }
}

