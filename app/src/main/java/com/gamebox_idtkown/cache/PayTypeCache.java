package com.gamebox_idtkown.cache;

import android.content.Context;

import com.gamebox_idtkown.core.DbUtil;
import com.gamebox_idtkown.core.db.greendao.PayTypeInfo;
import com.gamebox_idtkown.core.db.greendao.dao.PayTypeInfoDao;

import java.util.List;

/**
 * Created by zhangkai on 16/10/29.
 */
public class PayTypeCache  {
    public static void setCache(Context context, List<PayTypeInfo> dataInfos) {
        if (dataInfos != null) {
            PayTypeInfoDao payTypeInfoDao = DbUtil.getSession(context).getPayTypeInfoDao();
            payTypeInfoDao.deleteAll();
            for (PayTypeInfo payTypeInfo : dataInfos) {
                DbUtil.getSession(context).insert(payTypeInfo);
            }
        }
    }

    public static List<PayTypeInfo> getCache(Context context) {
        PayTypeInfoDao payTypeInfoDao = DbUtil.getSession(context).getPayTypeInfoDao();
        return payTypeInfoDao.loadAll();
    }
}
