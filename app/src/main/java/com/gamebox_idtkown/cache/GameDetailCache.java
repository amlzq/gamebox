package com.gamebox_idtkown.cache;

import android.content.Context;

import com.gamebox_idtkown.core.DbUtil;
import com.gamebox_idtkown.core.db.greendao.GameDetail;
import com.gamebox_idtkown.core.db.greendao.dao.GameDetailDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by zhangkai on 16/10/20.
 */
public class GameDetailCache {
    public static void setCache(Context context,GameDetail gameDetail, String gameid ){
        if (gameDetail != null) {
            GameDetailDao gameDetailDao = DbUtil.getSession(context).getGameDetailDao();
            QueryBuilder queryBuilder = gameDetailDao.queryBuilder();
            queryBuilder.where(GameDetailDao.Properties.GameId.eq(gameid));
            List list = queryBuilder.list();
            gameDetailDao.deleteInTx(list);
            gameDetail.setGameId(gameid);
            DbUtil.getSession(context).insert(gameDetail);
        }
    }

    public static List<GameDetail> getCache(Context context, String gameid){
        GameDetailDao gameDetailDao = DbUtil.getSession(context).getGameDetailDao();
        QueryBuilder queryBuilder = gameDetailDao.queryBuilder();
        queryBuilder.where(GameDetailDao.Properties.GameId.eq(gameid));
        return queryBuilder.list();
    }
}
