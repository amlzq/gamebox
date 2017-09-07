package com.gamebox_idtkown.cache;

import android.content.Context;

import com.gamebox_idtkown.core.DbUtil;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.core.db.greendao.dao.GameInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by zhangkai on 16/10/20.
 */
public class GameInfoCache {
    public static void setCache(Context context, List<GameInfo> dataInfos, String cacheType) {
        if (dataInfos != null) {
            GameInfoDao gameInfoDao = DbUtil.getSession(context).getGameInfoDao();
            QueryBuilder queryBuilder = gameInfoDao.queryBuilder();
            queryBuilder.where(GameInfoDao.Properties.CacheType.eq(cacheType));
            List list = queryBuilder.list();
            gameInfoDao.deleteInTx(list);
            for (GameInfo gameInfo : dataInfos) {
                gameInfo.setCacheType(cacheType);
                DbUtil.getSession(context).insert(gameInfo);
            }
        }
    }

    public static List<GameInfo> getCache(Context context, String cacheType) {
        GameInfoDao gameInfoDao = DbUtil.getSession(context).getGameInfoDao();
        QueryBuilder queryBuilder = gameInfoDao.queryBuilder();
        queryBuilder.where(GameInfoDao.Properties.CacheType.eq(cacheType));
        return queryBuilder.list();
    }
}
