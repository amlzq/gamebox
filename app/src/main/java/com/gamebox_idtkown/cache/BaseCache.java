package com.gamebox_idtkown.cache;

import android.content.Context;

import com.gamebox_idtkown.core.DbUtil;
import com.gamebox_idtkown.core.db.greendao.dao.DaoSession;
import com.gamebox_idtkown.utils.LogUtil;

import org.greenrobot.greendao.AbstractDao;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by zhangkai on 16/11/30.
 */
public class BaseCache<M> {
    public AbstractDao getInfoDao(Context context, Class mClazz) {
        Object object = null;
        DaoSession daoSession = DbUtil.getSession(context);
        String methodName = "get" + mClazz.getSimpleName() + "Dao";
        LogUtil.msg(methodName);
        Class clazz = DaoSession.class;
        try {
            Method method = clazz.getMethod(methodName);
            object = method.invoke(daoSession);
        } catch (Exception e) {
            LogUtil.msg("getInfoDao->" + e);
        }
        return (AbstractDao) object;
    }

    public void setCache(Context context, List<M> dataInfos, Class mClazz) {
        if (dataInfos != null && dataInfos.size() > 0) {
            AbstractDao dao = getInfoDao(context, mClazz);
            dao.deleteAll();
            for (M m : dataInfos) {
                DbUtil.getSession(context).insert(m);
            }
        }
    }

    public List<M> getCache(Context context, Class mClazz) {
        AbstractDao dao = getInfoDao(context, mClazz);
        return dao.loadAll();
    }

}
