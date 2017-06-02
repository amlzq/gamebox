package com.gamebox_idtkown;

import com.gamebox_idtkown.core.Config;

/**
 * Created by zhangkai on 16/11/14.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static CrashHandler mAppCrashHandler;

    private Thread.UncaughtExceptionHandler mDefaultHandler;


    public void initCrashHandler() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static CrashHandler getInstance() {
        if (mAppCrashHandler == null) {
            mAppCrashHandler = new CrashHandler();
        }
        return mAppCrashHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {

        }
        ex.printStackTrace();
//        if (!Config.DEBUG) {
//            System.exit(0);
//        }
    }

    /**
     * 错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 自定义处理错误信息
        return true;
    }
}