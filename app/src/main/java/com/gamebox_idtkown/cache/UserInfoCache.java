package com.gamebox_idtkown.cache;

import android.content.Context;

import com.gamebox_idtkown.core.DbUtil;
import com.gamebox_idtkown.core.db.greendao.UserInfo;
import com.gamebox_idtkown.core.db.greendao.dao.UserInfoDao;

import java.util.List;

/**
 * Created by zhangkai on 16/10/25.
 */
public class UserInfoCache  {

    public static void setCache(Context context, UserInfo userInfo){
        if (userInfo != null) {
            UserInfoDao userInfoDao = DbUtil.getSession(context).getUserInfoDao();
            userInfoDao.deleteAll();
            DbUtil.getSession(context).insert(userInfo);
        }
    }

    public static UserInfo getCache(Context context){
        UserInfo userInfo = null;
        UserInfoDao userInfoDao = DbUtil.getSession(context).getUserInfoDao();
        List<UserInfo> userInfos = userInfoDao.loadAll();
        if(userInfos.size() > 0){
            userInfo = userInfos.get(0);
        }
        return userInfo;
    }
}
