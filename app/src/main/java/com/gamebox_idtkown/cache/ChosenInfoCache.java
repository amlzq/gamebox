package com.gamebox_idtkown.cache;

import android.content.Context;

import com.gamebox_idtkown.core.DbUtil;
import com.gamebox_idtkown.core.db.greendao.ChosenInfo;
import com.gamebox_idtkown.core.db.greendao.dao.ChosenInfoDao;

import java.util.List;

/**
 * Created by zhangkai on 16/10/20.
 */
public class ChosenInfoCache {
    public static void setCache(Context context, List<ChosenInfo> dataInfos) {
        if (dataInfos != null) {
            ChosenInfoDao chosenInfoDao = DbUtil.getSession(context).getChosenInfoDao();
            chosenInfoDao.deleteAll();
            for (ChosenInfo chosenInfo : dataInfos) {
                DbUtil.getSession(context).insert(chosenInfo);
            }
        }
    }

    public static List<ChosenInfo> getCache(Context context) {
        ChosenInfoDao chosenInfoDao = DbUtil.getSession(context).getChosenInfoDao();
        return chosenInfoDao.loadAll();
    }
}
