package com.gamebox_idtkown.engin;

import android.content.Context;

import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.listeners.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangkai on 16/12/15.
 */
public class GameDownEngin extends BaseEngin<String> {
    private static GameDownEngin gameDownEngin;

    public GameDownEngin(Context context) {
        super(context);
    }

    public static GameDownEngin getImpl(Context context) {
        if (gameDownEngin == null) {
            synchronized (GameDownEngin.class) {
                gameDownEngin = new GameDownEngin(context);
            }
        }
        return gameDownEngin;
    }

    public void statGameDown(String game_id, String  type,  Callback<String> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("game_id", game_id + "");
        params.put("type", type + "");
        agetResultInfo(true, String.class, params, callback);
    }

    @Override
    public String getUrl() {
        return Config.DOWNLOAD_STAT_URL;
    }
}
