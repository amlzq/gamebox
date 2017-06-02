package com.gamebox_idtkown.engin;

import android.content.Context;

import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.listeners.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangkai on 16/11/30.
 */
public class InstallEngin extends BaseEngin<String> {
    private static InstallEngin installEngin;

    public InstallEngin(Context context) {
        super(context);
    }

    public static InstallEngin getImpl(Context context) {
        if (installEngin == null) {
            synchronized (InstallEngin.class) {
                installEngin = new InstallEngin(context);
            }
        }
        return installEngin;
    }

    public void install(String gameid, String type, String pacekageName, Callback<String> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("game_id", gameid);
        params.put("type", type);
        params.put("package_name", pacekageName);
        agetResultInfo(true, String.class, params, callback);
    }

    @Override
    public String getUrl() {
        return Config.INSTALL_URL;
    }
}
