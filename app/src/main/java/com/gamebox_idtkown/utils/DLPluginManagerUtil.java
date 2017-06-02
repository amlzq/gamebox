//package com.gamebox_idtkown.utils;
//
////import com.ryg.dynamicload.internal.DLIntent;
////import com.ryg.dynamicload.internal.DLPluginManager;
////import com.ryg.dynamicload.internal.DLPluginPackage;
//
//
///**
// * Created by zhangkai on 16/11/19.
// */
//public class DLPluginManagerUtil {
//    public static class PluginInfo {
//        ///< 服务器字段
//        public String url;
//        public String name;
//        public String paceageName;
//        public String initActivityName;
//        public String loginActiviyName;
//        public String payActivityName;
//        public String version = "1.0";
//
//        ///< 本地字段
//        public boolean isInstall;
//        public String path;
//    }
//
//    public static PluginInfo mPluginInfo = null;
//
//    public static boolean installPluginApk(final Context context, final PluginInfo pluginInfo) {
//        if (pluginInfo == null) {
//            return false;
//        }
//
//        mPluginInfo = pluginInfo;
//
//        PluginInfo tPluginInfo = getPluginInfo(context, pluginInfo);
//
//        pluginInfo.path = PathUtil.getPluginPath(pluginInfo.name);
//        File file = new File(pluginInfo.path);
//
//        if (tPluginInfo != null && !tPluginInfo.version.equals(pluginInfo.version)) {
//            file.delete();
//            LogUtil.msg("插件版本不一致已删除");
//        }
//
//        if (file.exists()) {
//            pluginInfo.isInstall = installPlugin(context, pluginInfo.path) == null ? false : true;
//            savePluginInfo(context, pluginInfo);
//            logMsg(pluginInfo);
//            return pluginInfo.isInstall;
//        }
//
//        if (downLoadPlugin(pluginInfo)) {
//            pluginInfo.isInstall = installPlugin(context, pluginInfo.path) == null ? false : true;
//            savePluginInfo(context, pluginInfo);
//            logMsg(pluginInfo);
//            return pluginInfo.isInstall;
//        }
//        pluginInfo.isInstall = false;
//        savePluginInfo(context, pluginInfo);
//        return pluginInfo.isInstall;
//    }
//
//    private static void logMsg(PluginInfo pluginInfo) {
//        if (pluginInfo.isInstall) {
//            LogUtil.msg("插件已安装,版本" + pluginInfo.version);
//        } else {
//            LogUtil.msg("插件安装失败,版本" + pluginInfo.version);
//        }
//    }
//
//    private static void savePluginInfo(Context context, PluginInfo pluginInfo) {
//        if (pluginInfo != null) {
//            String plugin = JSON.toJSONString(pluginInfo);
//            try {
//                PreferenceUtil.getImpl(context).putString(pluginInfo.name, plugin);
//            } catch (Exception e) {
//                LogUtil.msg(e.getMessage());
//            }
//        }
//    }
//
//    public static Class loadClass(Context context, String packageName, String className) {
//        DLPluginPackage pluginPackage = getPluginPackage(context, packageName);
//        Class clazz = null;
//        try {
//            clazz = pluginPackage.classLoader.loadClass(className);
//        } catch (Exception e) {
//            LogUtil.msg("加载插件中的" + className + "失败");
//        }
//        return clazz;
//    }
//
//    private static PluginInfo getPluginInfo(Context context, PluginInfo pluginInfo) {
//        PluginInfo tPluginInfo = null;
//        try {
//            String initInfoStr = PreferenceUtil.getImpl(context).getString(pluginInfo.name, "");
//            tPluginInfo = JSON.parseObject(initInfoStr, PluginInfo.class);
//        } catch (Exception e) {
//            LogUtil.msg(e.getMessage());
//        }
//        return tPluginInfo;
//    }
//
//    public static DLPluginPackage installPlugin(final Context context, String path) {
//        LogUtil.msg("正在安装插件");
//        return DLPluginManager.getInstance(context).loadApk(path);
//    }
//
//    public static int startActivity(Context context, String paceageName, String activityName) {
//        DLPluginManager pluginManager = DLPluginManager.getInstance(context);
//        return pluginManager.startPluginActivity(context, new DLIntent(paceageName, activityName));
//    }
//
//    public static int startActivity(Context context, DLIntent dlIntent) {
//        DLPluginManager pluginManager = DLPluginManager.getInstance(context);
//        return pluginManager.startPluginActivity(context, dlIntent);
//    }
//
//    public static int startService(Context context, String paceageName, String serviceName) {
//        DLPluginManager pluginManager = DLPluginManager.getInstance(context);
//        return pluginManager.startPluginService(context, new DLIntent(paceageName, serviceName));
//    }
//
//    public static DLPluginPackage getPluginPackage(Context context, String paceageName) {
//        DLPluginManager pluginManager = DLPluginManager.getInstance(context);
//        return pluginManager.getPackage(paceageName);
//    }
//
//    public static int startInitActivity(Context context, DLIntent dlIntent) {
//        dlIntent.setPluginPackage(mPluginInfo.paceageName);
//        dlIntent.setPluginPackage(mPluginInfo.initActivityName);
//        return startActivity(context, dlIntent);
//    }
//
//    public static int startLoginActivity(Context context, DLIntent dlIntent) {
//        dlIntent.setPluginPackage(mPluginInfo.paceageName);
//        dlIntent.setPluginPackage(mPluginInfo.loginActiviyName);
//        return startActivity(context, dlIntent);
//    }
//
//    public static int startPayActivity(Context context, DLIntent dlIntent) {
//        dlIntent.setPluginPackage(mPluginInfo.paceageName);
//        dlIntent.setPluginPackage(mPluginInfo.payActivityName);
//        return startActivity(context, dlIntent);
//    }
//
//    public static boolean downLoadPlugin(PluginInfo pluginInfo) {
//        boolean flag = false;
//        if (pluginInfo.path == null || pluginInfo.path.isEmpty()) {
//            pluginInfo.path = PathUtil.getPluginPath(pluginInfo.name);
//        }
//        HttpURLConnection conn = null;
//        InputStream is = null;
//        OutputStream os = null;
//        try {
//            final File file = new File(pluginInfo.path);
//            URL url = new URL(pluginInfo.url);
//            LogUtil.msg("插件正在下载...");
//            conn = (HttpURLConnection) url
//                    .openConnection();
//            if (file.exists() && file.length() == conn.getContentLength()) {
//                flag = true;
//                conn.disconnect();
//            }
//            is = conn.getInputStream();
//            os = new FileOutputStream(file);
//            byte[] bs = new byte[1024];
//            int len;
//            while ((len = is.read(bs)) != -1) {
//                os.write(bs, 0, len);
//            }
//            flag = true;
//            LogUtil.msg("插件下载完成");
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.msg("插件下载失败");
//
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.disconnect();
//                } catch (Exception e) {
//                }
//            }
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (Exception e) {
//                }
//            }
//            if (os != null) {
//                try {
//                    os.close();
//                } catch (Exception e) {
//                }
//            }
//        }
//        return flag;
//    }
//
//    public static void setOnPaymentListener(Context context, Object object) {
//        Class clazz = loadClass(context, mPluginInfo.paceageName, mPluginInfo.payActivityName);
//        try {
//            Field field = clazz.getField("paymentListener");
//            field.setAccessible(true);
//            field.set(clazz.newInstance(), object);
//        } catch (Exception e) {
//            LogUtil.msg("setOnPaymentListener->" + e);
//        }
//    }
//
//    public static void setOnInitListener(Context context, Object object) {
//        Class clazz = loadClass(context, mPluginInfo.paceageName, mPluginInfo.initActivityName);
//        try {
//            Field field = clazz.getField("initListener");
//            field.setAccessible(true);
//            field.set(clazz.newInstance(), object);
//        } catch (Exception e) {
//            LogUtil.msg("setOnInitListener->" + e);
//        }
//    }
//
//    public static PluginInfo requestPluginInfo(String initUrl) {
//        final int timeout = 10 * 1000;
//        PluginInfo pluginInfo = null;
//        HttpURLConnection conn = null;
//        InputStream is = null;
//        try {
//            URL url = new URL(initUrl);
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Content-length", "0");
//            conn.setUseCaches(false);
//            conn.setAllowUserInteraction(false);
//            conn.setConnectTimeout(timeout);
//            conn.setReadTimeout(timeout);
//            conn.connect();
//            int status = conn.getResponseCode();
//            if (status == 200) {
//                is = conn.getInputStream();
//                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                StringBuilder sb = new StringBuilder();
//                String line;
//                while ((line = br.readLine()) != null) {
//                    sb.append(line + "\n");
//                }
//                LogUtil.msg(sb.toString());
//                pluginInfo = JSON.parseObject(sb.toString(), PluginInfo.class);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.msg("requestPluginInfo ->" + e);
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.disconnect();
//                } catch (Exception e) {
//                }
//            }
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (Exception e) {
//                }
//            }
//        }
//        return pluginInfo;
//    }
//
//
//}
