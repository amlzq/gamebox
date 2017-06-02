package com.gamebox_idtkown;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.net.http.HttpResponseCache;

import com.gamebox_idtkown.cache.UserInfoCache;
import com.gamebox_idtkown.core.ApkUtil;
import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.DbUtil;
import com.gamebox_idtkown.core.GameBox;
import com.gamebox_idtkown.core.db.greendao.UserInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.InitEngin;
import com.gamebox_idtkown.engin.LoginEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.services.DownloadManagerService;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.Blur;
import com.gamebox_idtkown.utils.CircleTransform;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.PathUtil;
import com.gamebox_idtkown.utils.PreferenceUtil;
import com.gamebox_idtkown.utils.ScreenUtil;
import com.gamebox_idtkown.utils.TaskUtil;
import com.lody.virtual.client.core.VirtualCore;
import com.squareup.picasso.Picasso;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by zhangkai on 16/9/22.
 */
public class GBApplication extends TinkerApplication {
    public static boolean is_swich = false;


    public GBApplication() {
        super(
                //tinkerFlags, tinker支持的类型，dex,library，还是全部都支持！
                ShareConstants.TINKER_ENABLE_ALL,
                //ApplicationLike的实现类，只能传递字符串
                "com.gamebox_idtkown.GBApplicationLike",
                //Tinker的加载器，一般来说用默认的即可
                "com.tencent.tinker.loader.TinkerLoader",
                //tinkerLoadVerifyFlag, 运行加载时是否校验dex与,ib与res的Md5
                false);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        CrashHandler.getInstance().initCrashHandler();
//        PluginHelper.getInstance().applicationOnCreate(getBaseContext());

//
//        if (Config.DEBUG) {
//            JPushInterface.setDebugMode(Config.DEBUG);
//            JPushInterface.init(this);
//        }

        UMGameAgent.setDebugMode(Config.DEBUG);
        UMGameAgent.init(this);
        UMGameAgent.setPlayerLevel(1);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        GameBox.getImpl().init(this.getApplicationContext());
        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                Context context = getBaseContext();
                ApkUtil.setPackageNames(context);
                try {
                    Bitmap icon = Picasso.with(getBaseContext()).load(R.mipmap.gift_detail_bg).get();
                    GBApplication.icon = Blur.fastblur(getBaseContext(), icon, 20);
                } catch (Exception e) {
                    LogUtil.msg("礼品详细页背景加载出错->" + e);
                }

            }
        });

        DownloadManagerService.initVars();
        Intent intent = new Intent(this, DownloadManagerService.class);
        startService(intent);

        userInfo = UserInfoCache.getCache(getBaseContext());


        installCache(getBaseContext());

        freeSO();

    }

    private void freeSO() {
        String subPath = PathUtil.getSOPath("substrate");

        File file = new File(subPath);
        if (!file.exists()) {
            ApkStatusUtil.copyFromAssets(getBaseContext(), "libsubstrate.so", subPath);
        }

        String speedPath = PathUtil.getSOPath("speed");
        File file2 = new File(speedPath);
        if (!file2.exists()) {
            ApkStatusUtil.copyFromAssets(getBaseContext(), "libspeed.so", speedPath);
        }
    }

    public static Bitmap icon = null;

    public static void installCache(Context context) {
        try {
            File httpCacheDir = new File(context.getCacheDir(), "http");
            long httpCacheSize = 100 * 1024 * 1024; // 100 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            LogUtil.msg("HTTP response cache installation failed:" + e);
        }
    }

    public static UserInfo userInfo = null;

    public static void setUserInfo(final Context context) {
        try {
            UserInfoCache.setCache(context, userInfo);
            EventBus.getDefault().post(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.msg("插入用户信息失败->" + e);
        }
    }

    public static boolean  avatarLoaded = true;

    public static void getUserBitmap(final Context context) {
        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                try {
                    avatarLoaded = false;
                    final Bitmap bitmap = Picasso.with(context).load(userInfo.getAvatar()).get();

                    if (bitmap != null) {
                        final int w = ScreenUtil.dip2px(context, 30);
                        userInfo.avatarBitmp = Bitmap.createScaledBitmap(bitmap, w, w, false);
                        userInfo.avatarBitmp = new CircleTransform().transform(userInfo.avatarBitmp);
                        EventBus.getDefault().post(userInfo.avatarBitmp);
                        LogUtil.msg("userInfo->userInfo.avatarBitmp");
                    }
                } catch (Exception e) {

                }
                avatarLoaded = true;
            }
        });
    }

    public static void updateUserInfo(Context context) {
        try {
            DbUtil.getSession(context).update(GBApplication.userInfo);
            EventBus.getDefault().post(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.msg("更新用户信息失败->" + e);
        }
    }

    public static void logout(Context context) {
        if (!GBApplication.isLogin()) {
            return;
        }
        try {
            DbUtil.getSession(context).delete(userInfo);
            userInfo = null;
        } catch (Exception e) {
            LogUtil.msg("退出登录出错->" + e);
        }
    }


    public static void login(final Context context) {
        login(context, userInfo.getMobile(), userInfo.getName(), userInfo.getPwd(), null, null);
    }

    public static void login(final Context context, String phone, String name, String pwd) {
        login(context, phone, name, pwd, null, null);
    }

    public static void login(final Context context, String phone, String name, String pwd, final Runnable runnable,
                             final Runnable runnable2) {
        LoginEngin.getImpl(context).login(phone, name,
                pwd, new Callback<UserInfo>() {
                    @Override
                    public void onSuccess(final ResultInfo<UserInfo> resultInfo) {
                        if (resultInfo == null) {
                            logout(context);
                            runnable2.run();
                            return;
                        }
                        if (resultInfo.code == 1) {
                            if (resultInfo.data != null) {
                                userInfo = resultInfo.data;
                                reInit(context);
                                setUserInfo(context);
                                getUserBitmap(context);
                            }
                        } else {
                            if (userInfo != null) {
                                logout(context);
                            }
                        }
                        if (runnable != null) {
                            runnable.run();
                        }
                    }

                    @Override
                    public void onFailure(Response response) {
                        if (runnable2 != null) {
                            runnable2.run();
                        }
                    }
                });
    }


    public static boolean isLogin() {
        if (userInfo == null) {
            return false;
        }
        return true;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            VirtualCore.get().startup(base);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public static void reInit(final Context context) {
        String lastLogin = PreferenceUtil.getImpl(context).getString("lastLogin", "");
        if (lastLogin.isEmpty() || !lastLogin.equals(GBApplication
                .userInfo
                .getUserId())) {
            TaskUtil.getImpl().runTask(new Runnable() {
                @Override
                public void run() {
                    new InitEngin(context).run(GBApplication.userInfo.getUserId());
                }
            });

        }
    }

}
