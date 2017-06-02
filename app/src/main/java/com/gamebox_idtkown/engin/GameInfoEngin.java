package com.gamebox_idtkown.engin;

import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.listeners.Callback;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by zhangkai on 16/12/21.
 */

public class GameInfoEngin extends BaseEngin<String> {

    @Inject
    public GameInfoEngin(){}

    public void getGameInfo(String game_id, String type, Callback<String> callback){
        Map<String, String> params = new HashMap<>();
        params.put("game_id", game_id);
        params.put("type", type);
        agetResultInfo(true, String.class, params, callback);
    }

    @Override
    public String getUrl() {
        return Config.GAME_INFO_URL;
    }
}
