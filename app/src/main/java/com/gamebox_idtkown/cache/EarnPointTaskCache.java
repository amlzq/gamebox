package com.gamebox_idtkown.cache;

import android.content.Context;

import com.gamebox_idtkown.core.DbUtil;
import com.gamebox_idtkown.core.db.greendao.EarnPointTaskInfo;
import com.gamebox_idtkown.core.db.greendao.dao.EarnPointTaskInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by zhangkai on 16/11/30.
 */
public class EarnPointTaskCache {
    public static void setCache(Context context, List<EarnPointTaskInfo> dataInfos, String cacheType) {
        if (dataInfos != null) {
            EarnPointTaskInfoDao earnPointTaskInfoDao = DbUtil.getSession(context).getEarnPointTaskInfoDao();
            QueryBuilder queryBuilder = earnPointTaskInfoDao.queryBuilder();
            queryBuilder.where(EarnPointTaskInfoDao.Properties.Type.eq(cacheType));
            List list = queryBuilder.list();
            earnPointTaskInfoDao.deleteInTx(list);
            for (EarnPointTaskInfo earnPointTaskInfo : dataInfos) {
                earnPointTaskInfo.setId(null);
                earnPointTaskInfo.setType(cacheType);
                earnPointTaskInfoDao.insert(earnPointTaskInfo);
            }
        }
    }

    public static List<EarnPointTaskInfo> getCache(Context context, String cacheType) {
        EarnPointTaskInfoDao earnPointTaskInfoDao = DbUtil.getSession(context).getEarnPointTaskInfoDao();
        QueryBuilder queryBuilder = earnPointTaskInfoDao.queryBuilder();
        queryBuilder.where(EarnPointTaskInfoDao.Properties.Type.eq(cacheType));
        return queryBuilder.list();
    }
}
