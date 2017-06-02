package com.gamebox_idtkown.cache;

import android.content.Context;

import com.gamebox_idtkown.core.DbUtil;
import com.gamebox_idtkown.core.db.greendao.GameType;
import com.gamebox_idtkown.core.db.greendao.dao.GameTypeDao;

import java.util.List;

/**
 * Created by zhangkai on 16/10/20.
 */
public class GameTypeCache {
    public static void setCache(Context context, List<GameType> dataInfos){
        if (dataInfos != null) {
            GameTypeDao gameTypeDao = DbUtil.getSession(context).getGameTypeDao();
            gameTypeDao.deleteAll();
            for (GameType gameType : dataInfos) {
                DbUtil.getSession(context).insert(gameType);
            }
        }
    }

    public static List<GameType> getCache(Context context){
        GameTypeDao gameTypeDao = DbUtil.getSession(context).getGameTypeDao();
        return gameTypeDao.loadAll();
    }
}
