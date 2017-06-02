package com.gamebox_idtkown.utils;

import com.gamebox_idtkown.core.Config;

import java.io.File;

/**
 * Created by zhangkai on 16/9/20.
 */
public class PathUtil {
    /**
     * get the path of download apk cache.
     */
    public static String getPluginPath(String name) {
        makeBaseDir();
        File dir = new File(Config.PATH + "/plugins");
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getAbsolutePath() + "/" + name + ".apk";
    }

    /**
     * get the path of download apk cache.
     *
     * @param name the name of file is apk
     * @return the apk file path
     */
    public static String getApkPath(String name) {
        makeBaseDir();
        File dir = new File(Config.PATH + "/apks");
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getAbsolutePath() + "/" + name.replace(".apk", "") + ".apk";
    }

    /**
     * get the path of dy so.
     *
     * @param name the name of file is apk
     * @return the apk file path
     */
    public static String getSOPath(String name) {
        makeBaseDir();
        File dir = new File(Config.PATH + "/so");
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getAbsolutePath() + "/" + name.replace(".so", "") + ".so";
    }

    /**
     * get the path of theme cache.
     *
     * @return the theme file path
     */
    public static String getThemeDir() {
        makeBaseDir();
        File dir = new File(Config.PATH + "/themes/");
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getAbsolutePath();
    }

    /**
     * get the path of goagal cache.
     *
     * @return the golgal file path
     */
    public static String getGolgalDir() {
        makeBaseDir();
        File dir = new File(Config.PATH + "/goagal/");
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getAbsolutePath();
    }


    private static void makeBaseDir() {
        File dir = new File(Config.PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }
}
