package com.gamebox_idtkown.cache;

import android.content.Context;

import com.gamebox_idtkown.core.DbUtil;
import com.gamebox_idtkown.core.db.greendao.GameImage;
import com.gamebox_idtkown.core.db.greendao.dao.GameImageDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by zhangkai on 16/10/21.
 */
public class GameImageCache {
    public static void setCache(Context context, List<GameImage> dataInfos, String gameid ){
        if (dataInfos != null) {
            GameImageDao gameImageDao = DbUtil.getSession(context).getGameImageDao();
            QueryBuilder queryBuilder = gameImageDao.queryBuilder();
            queryBuilder.where(GameImageDao.Properties.GameId.eq(gameid));
            List list = queryBuilder.list();
            gameImageDao.deleteInTx(list);
            for (GameImage gameImage : dataInfos) {
                gameImage.setGameId(gameid);
                DbUtil.getSession(context).insert(gameImage);
            }
        }
    }

    public static List<GameImage> getCache(Context context, String gameid){
        GameImageDao gameImageDao = DbUtil.getSession(context).getGameImageDao();
        QueryBuilder queryBuilder = gameImageDao.queryBuilder();
        queryBuilder.where(GameImageDao.Properties.GameId.eq(gameid));
        return queryBuilder.list();
    }
}
