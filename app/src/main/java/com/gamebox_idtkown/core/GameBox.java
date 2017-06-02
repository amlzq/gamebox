package com.gamebox_idtkown.core;

import android.app.Activity;
import android.content.Context;

import com.gamebox_idtkown.engin.InitEngin;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.PathUtil;
import com.liulishuo.filedownloader.FileDownloader;

import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.utils.TaskUtil;

/**
 * Created by zhangkai on 16/9/20.
 */

/**
 * GameBox is the first start up object in app, get the base info use in below.
 *
 * */
public class GameBox {
    private static GameBox gameBox = new GameBox();
    public int initCount = 0;
    private GameBox(){}

    public static GameBox getImpl(){
        return gameBox;
    }

    /**
     * Game Box init in application.
     * @param context The context of application context
     * @see FileDownloader
     * more {@see https://github.com/lingochamp/FileDownloader/}
     * */
    public void init(final Context context){
        ///< 0.初始文件下载器
        FileDownloader.init(context);
        FileDownloader.disableAvoidDropFrame();
    }

    /**
     * Game Box init in startup.
     * @param context The context of Activity
     * @see GoagalInfo
     * */
    public void init2(final Activity context, final Runnable runnable, final Runnable runnable2){
        if(runnable == null) throw  new NullPointerException("runnable == null");
       ///< 1.获取全局信息
       TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                GoagalInfo.setGoagalInfo(context, PathUtil.getGolgalDir());
                InitEngin initEngin = new InitEngin(context);
                initEngin.run();
                initCount ++;
                if(GoagalInfo.getInItInfo() == null){
                    context.runOnUiThread(runnable2);
                    if(initCount >= 3){
                        return;
                    }
                    init2(context, runnable, runnable2);
                }
                GoagalInfo.getInItInfo().setThemeColor();
                LogUtil.msg("6071Box初始化成功");
                context.runOnUiThread(runnable);
            }
        });
    }
}
