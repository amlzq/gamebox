package com.gamebox_idtkown.engin;

import android.content.Context;

import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.core.listeners.Callback;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhangkai on 16/11/8.
 */
public class GameUpdateEngin extends GameListEngin {
    private static GameUpdateEngin gameUpdateEngin;

    public GameUpdateEngin(Context context) {
        super(context);
    }

    public static GameUpdateEngin getImpl(Context context){
        if(gameUpdateEngin == null){
            synchronized (GameUpdateEngin.class) {
                gameUpdateEngin = new GameUpdateEngin(context);
            }
        }
        return gameUpdateEngin;
    }

    public void getUpdaetGames(String json, Callback<List<GameInfo>> callback){
        HashMap<String, String> params = new HashMap<>();
        params.put("packages", json);
        agetResultInfo(true, params, callback);
    }

    @Override
    public String getUrl() {
        return Config.GAMES_UPDATE;
    }

}
