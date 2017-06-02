package com.gamebox_idtkown.cache;

import android.content.Context;

import com.gamebox_idtkown.core.DbUtil;
import com.gamebox_idtkown.core.db.greendao.GiftDetail;
import com.gamebox_idtkown.core.db.greendao.dao.GiftDetailDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by zhangkai on 16/11/30.
 */
public class GiftListCache {
    public static void setCache(Context context, List<GiftDetail> dataInfos, String cacheType) {
        if (dataInfos != null) {
            GiftDetailDao detailDao = DbUtil.getSession(context).getGiftDetailDao();
            QueryBuilder queryBuilder = detailDao.queryBuilder();
            queryBuilder.where(GiftDetailDao.Properties.GameId.eq(cacheType));
            List list = queryBuilder.list();
            detailDao.deleteInTx(list);
            for (GiftDetail giftDetail : dataInfos) {
                giftDetail.setGameId(cacheType);
                detailDao.insert(giftDetail);
            }
        }
    }

    public static List<GiftDetail> getCache(Context context, String cacheType) {
        GiftDetailDao detailDao = DbUtil.getSession(context).getGiftDetailDao();
        QueryBuilder queryBuilder = detailDao.queryBuilder();
        queryBuilder.where(GiftDetailDao.Properties.GameId.eq(cacheType));
        return queryBuilder.list();
    }
}
