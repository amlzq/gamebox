package com.gamebox_idtkown.cache;

import android.content.Context;

import com.gamebox_idtkown.core.DbUtil;
import com.gamebox_idtkown.core.db.greendao.GoodList;
import com.gamebox_idtkown.core.db.greendao.dao.GoodListDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by zhangkai on 16/11/30.
 */
public class GoodListCache {
    public static void setCache(Context context, List<GoodList> dataInfos, String cacheType) {
        if (dataInfos != null) {
            GoodListDao goodListDao = DbUtil.getSession(context).getGoodListDao();
            QueryBuilder queryBuilder = goodListDao.queryBuilder();
            queryBuilder.where(GoodListDao.Properties.Type_id.eq(cacheType));
            List list = queryBuilder.list();
            goodListDao.deleteInTx(list);
            for (GoodList goodList : dataInfos) {
                goodListDao.insert(goodList);
            }
        }
    }

    public static List<GoodList> getCache(Context context, String cacheType) {
        GoodListDao goodListDao = DbUtil.getSession(context).getGoodListDao();
        QueryBuilder queryBuilder = goodListDao.queryBuilder();
        queryBuilder.where(GoodListDao.Properties.Type_id.eq(cacheType));
        return queryBuilder.list();
    }
}
