package com.gamebox_idtkown.domain;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Color;

import com.alibaba.fastjson.JSON;
import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.engin.InitEngin;
import com.gamebox_idtkown.utils.FileUtil;
import com.gamebox_idtkown.utils.GameBox2SDKUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.PathUtil;
import com.gamebox_idtkown.utils.PreferenceUtil;


import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by zhangkai on 16/9/19.
 */
public class GoagalInfo {
    public static final String TAG = "6071Box";

    public static String publicKey = "-----BEGIN PUBLIC KEY-----" +
            "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAsDodkyEPbvjtJzYE9LUO"
            + "IK7lqgMHoCMSdC/dPjAT+e63tYC/Zq0lOoMj3UYst4pReSCDTI5V+AsByskDZUs3"
            + "DTn2gUm0GsntRTDlMSB/4ZeeyTBfKehNeP20wHlrN9olndedo7kyf8mdM+5IKIcI"
            + "knW1yJq+ZW/0yzHkSTZ8T0pJ0egHTp+sG6wWbvpFQGkXHqZ2ItNSMuT/UNqGRH3e"
            + "ugcJxaITCgKMK/bCiyaRl7NU80qmWuXVvYcDGuo9iIFF/CAm2gF3fZuBNHVZJuaY"
            + "LT+61F0fckoMcqNXvU9GnAbyDw32RN8LsPhIRxeGIKzDv/UoB9SL2+CoKaOACG0x"
            + "Jz22MgtSowf+jEPHc3x8KrjfmGkvJNW0wJuDEQIRZw+S/h9r/OrWhz4J/+JJrt+a"
            + "gjMewuet0Ch0yIRcpecbRUWjk8rg2d4UeQgqk4bxoMjKuF5dDnZgyPxxnS671TgH"
            + "19E7vmajJ8fn2+vcO/QDk0/4Qq8h4HQ6d0XY8xj+WtMDbKBQqYOr7KDVnk3zllAS"
            + "2us97aqEPVspu3EBiIYP4mJi9ENxSA+A9RkGYTq7x2RY8dp5YZgRulevUMQcESCP"
            + "/DO8fJNTIU6fq7uTsuvemjtyMZ8Z3Qmafsbf/0CPfksX4qqNLfHalBgiyrjZjkb9"
            + "t5XISoQ1s3S+oye4FeMT7ycCAwEAAQ=="
            + "-----END PUBLIC KEY-----";

    public static String channel = "default";
    public static ChannelInfo channelInfo = null;
    private static InItInfo inItInfo = null;
    public static PackageInfo packageInfo = null;
    public static BatInfo appInfo;
    public static String uuid = "";

    public static InItInfo getInItInfo() {
        if (inItInfo == null) {
            inItInfo = new InitEngin(mContext).get();
        }
        if (inItInfo == null) {
            inItInfo = new InItInfo();
            inItInfo.androidColor = Color.parseColor("#2AB1F2");
            inItInfo.themeColor = "#2AB1F2";
        }
        inItInfo.setThemeColor();
        return inItInfo;
    }

    public static void setInitInfo(Context context, InItInfo _inItInfo) {
        try {
            if (_inItInfo == null || _inItInfo.agent_info == null || _inItInfo.agent_info.isEmpty())
                ;
            else {
                GoagalInfo.channelInfo = JSON.parseObject(_inItInfo.agent_info, ChannelInfo.class);
                String name = "gamechannel.json";
                FileUtil.writeInfoInSDCard(context, _inItInfo.agent_info, PathUtil.getGolgalDir(), name);
                if (GBApplication.userInfo != null) {
                    PreferenceUtil.getImpl(context).putString("lastLogin", GBApplication.userInfo.getUserId());
                }
                _inItInfo.setThemeColor();
                EventBus.getDefault().post(EventBusMessage.RE_INIT);

            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.msg("init channelInfo ->" + e);
        }
        inItInfo = _inItInfo;
    }

    private static Context mContext;

    ///< 获取渠道信息
    public static void setGoagalInfo(Context context, String dir) {
        mContext = context;
        String result1 = null;
        String result2 = null;
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        ZipFile zf = null;

        String name = "rsa_public_key.pem";
        try {
            zf = new ZipFile(sourceDir);
            ZipEntry ze1 = zf.getEntry("META-INF/gamechannel.json");
            InputStream in1 = zf.getInputStream(ze1);
            result1 = FileUtil.readString(in1);
            LogUtil.msg("渠道->" + result1);

            result2 = FileUtil.readInfoInSDCard(context, dir, name);
        } catch (Exception e) {
            LogUtil.msg("apk中gamechannel或rsa_public_key文件不存在", LogUtil.W);
        }
        try {
            if (zf == null) {
                zf = new ZipFile(sourceDir);
            }
            ZipEntry ze1 = zf.getEntry("META-INF/app.json");
            InputStream in1 = zf.getInputStream(ze1);
            String result = FileUtil.readString(in1);
            appInfo = JSON.parseObject(result, BatInfo.class);
            LogUtil.msg("批量打包数据->" + result);
        } catch (Exception e) {

            LogUtil.msg("批量打包数据解析失败->" + e, LogUtil.W);
        }
        name = "gamechannel.json";
        if (result1 != null) {
            FileUtil.writeInfoInSDCard(context, result1, dir, name);
        } else {
            result1 = FileUtil.readInfoInSDCard(context, dir, name);
        }

        if (result1 != null) {
            GoagalInfo.channel = result1;
        }

        name = "rsa_public_key.pem";
        if (result2 != null) {
            GoagalInfo.publicKey = getPublicKey(result2);
            FileUtil.writeInfoInSDCard(context, result2, dir, name);
        } else {
            ZipEntry ze2 = zf.getEntry("META-INF/rsa_public_key.pem");
            InputStream in2 = null;
            try {
                in2 = zf.getInputStream(ze2);
                result2 = FileUtil.readString(in2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogUtil.msg("公钥->" + result2);
            if (result2 != null) {
                GoagalInfo.publicKey = getPublicKey(result2);
            }
        }


        if (zf != null) {
            try {
                zf.close();
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
        }


        GoagalInfo.channelInfo = GoagalInfo.getChannelInfo();
        GoagalInfo.packageInfo = GoagalInfo.getPackageInfo(context);
        GoagalInfo.uuid = GameBox2SDKUtil.getUid(context);
    }

    private static ChannelInfo getChannelInfo() {
        try {
            ChannelInfo channelInfo = JSON.parseObject(GoagalInfo.channel, ChannelInfo.class);
            try {
                channelInfo.gameList = JSON.parseArray(channelInfo.gameInfos, GameInfo.class);
                channelInfo.jumpList = JSON.parseArray(channelInfo.jumpInfos, GameInfo.class);
                channelInfo.iconList = JSON.parseArray(channelInfo.iconInfos, ShortCutInfo.class);
            } catch (Exception e) {

            }
            LogUtil.msg("channelInfo.iconList->" + channelInfo.iconList);
            return channelInfo;
        } catch (Exception e) {
            LogUtil.msg("渠道信息解析错误->" + e.getMessage());
        }
        return null;
    }

    ///< 从输入流获取公钥
    private static String getPublicKey(InputStream in) {
        String result = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                if (mLine.startsWith("----")) {
                    continue;
                }
                result += mLine;
            }
        } catch (Exception e) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e2) {
                }
            }
        }
        return result;
    }

    public static String getPublicKey() {
        GoagalInfo.publicKey = getPublicKey(GoagalInfo.publicKey);
        return GoagalInfo.publicKey;
    }

    public static String getPublicKey(String key) {
        return key.replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\r", "")
                .replace("\n", "");
    }

    public static PackageInfo getPackageInfo(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo;
        } catch (Exception e) {
        }
        return null;
    }
}
