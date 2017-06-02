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
public class SearchEngin extends GameListEngin {

    private static SearchEngin searchEngin;

    private SearchEngin(Context context) {
        super(context);
    }

    public static SearchEngin getImpl(Context context){
        if(searchEngin == null){
            synchronized (GameListEngin.class) {
                searchEngin = new SearchEngin(context);
            }
        }
        return searchEngin;
    }

    public void getInfosByKeyword(int page, int limit, String keyword, Callback<List<GameInfo>> callback){
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("page", page + "");
        params.put("limit", limit + "");
        agetResultInfo(true, params, callback);
    }

    @Override
    public String getUrl() {
        return Config.GAME_SEARCH_URL;
    }
}
