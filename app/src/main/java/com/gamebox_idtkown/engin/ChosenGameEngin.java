package com.gamebox_idtkown.engin;

import android.content.Context;

import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.core.listeners.Callback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangkai on 16/10/18.
 */
public class ChosenGameEngin extends GameListEngin {
    private static ChosenGameEngin chosenGameEngin;

    private ChosenGameEngin(Context context) {
        super(context);
    }

    public static ChosenGameEngin getImpl(Context context){
        if(chosenGameEngin == null){
            synchronized (ChosenGameEngin.class) {
                chosenGameEngin = new ChosenGameEngin(context);
            }
        }
        return chosenGameEngin;
    }

    public void getChosenGames(int id, int page, int limit, Callback<List<GameInfo>> callback){
        Map<String, String> params = new HashMap<>();
        params.put("special_id", id+"");
        params.put("page", page+"");
        params.put("limit", limit+"");
        agetResultInfo(true, params, callback);
    }

    @Override
    public String getUrl() {
        return Config.CHOSEN_GAMES_URL;
    }
}
