package com.gamebox_idtkown.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gamebox_idtkown.core.db.greendao.dao.DaoMaster;
import com.gamebox_idtkown.core.db.greendao.dao.DaoSession;
import com.gamebox_idtkown.utils.LogUtil;

/**
 * Created by zhangkai on 16/9/12.
 */

/**
 *  DbUtil is a orm for sqlite base on Greendao.
 *
 * */
public class DbUtil {
    /**
     *  Get a Greendao Session, now you can opration models.
     *
     *  @param context  The context of Application or Activity,  and so on.
     *  @return DaoSession The Session of GreenDao Session
     * */

    public static DaoSession getSession(Context context){
        if(daoSession == null) {
            try {
                DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, context.getPackageName()
                        +"_6071box-db", null);
                SQLiteDatabase db = helper.getWritableDatabase();
                DaoMaster daoMaster = new DaoMaster(db);
                daoSession = daoMaster.newSession();
            }catch (Exception e){
                e.printStackTrace();
                LogUtil.msg("数据库session获取失败");
            }
        }
        return daoSession;
    }

    public static DaoSession daoSession = null;
}
