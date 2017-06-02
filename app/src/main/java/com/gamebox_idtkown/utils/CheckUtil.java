package com.gamebox_idtkown.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.gamebox_idtkown.core.ApkUtil;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangkai on 16/9/19.
 */
public class CheckUtil {


    ///< 表单检测
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }


    public static boolean isPhone(String phone) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }


    ///< 安装包检测
    public static boolean isInstall(Context context, String packageName) {
        if (packageName == null) {
            return false;
        }
        for (int i = 0; i < ApkUtil.packageNames.size(); i++) {
            if (packageName.equals(ApkUtil.packageNames.get(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWeixinAvilible(Context context) {
        if (ApkUtil.packageNames != null) {
            for (int i = 0; i < ApkUtil.packageNames.size(); i++) {
                String pn = ApkUtil.packageNames.get(i);
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean isQQClientAvailable(Context context) {
        if (ApkUtil.packageNames != null) {
            for (int i = 0; i < ApkUtil.packageNames.size(); i++) {
                String pn = ApkUtil.packageNames.get(i);
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    ///< url检测
    public static boolean is404NotFound(String urlStr) {
        boolean result = false;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            int responseCode = httpURLConnection.getResponseCode();
            httpURLConnection.disconnect();
            result = responseCode != HttpURLConnection.HTTP_OK ? true : false;
        } catch (Exception e) {
            result = true;
        }
        return result;
    }

    public static String checkDesc(String desc) {
        String tmp = desc;
        if (desc == null || desc.isEmpty() || desc.equals("null")) {
            tmp = "暂无描述";
        }
        return tmp;
    }

    public static String checkStr(String desc, String dest) {
        String tmp = desc;
        if (desc == null || desc.isEmpty() || desc.equals("null")) {
            tmp = dest;
        }
        return tmp;
    }


    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if(context == null){ return false;}
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络   1：WIFI网络   2：2G/3G/4G
     */
    public static final int NETTYPE_NO = 0x00;
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_TYPE_MOBILE = 0x02;

    public static int getNetworkType(Context context) {
        int netType = NETTYPE_NO;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (extraInfo != null && !extraInfo.isEmpty()) {
                netType = NETTYPE_TYPE_MOBILE;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    public static boolean isInternetAvailable(String domin) {
        try {
            InetAddress ipAddr = InetAddress.getByName(domin); //You can replace it with your name
            return !ipAddr.equals("");
        } catch (Exception e) {
            return false;
        }
    }
}
