package com.gamebox_idtkown.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static com.lody.virtual.client.core.InstallStrategy.TERMINATE_IF_EXIST;

/**
 * Created by zhangkai on 2017/3/1.
 */

public class FreeInstallUtil {

    public static final int userId = 0;

    public static void openApkFromAssets(final Activity context,final String apkName, final String packageName) {
        String apkpath = Environment.getExternalStorageDirectory() + "/freeapks";
        File dir = new File(apkpath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        apkpath = apkpath + apkName;
        copyFromAssets(context, apkName, apkpath);
        openApkFromPath(apkpath, packageName);
    }

    public static boolean openApkFromPath(final String apkpath, final String packageName) {
        File file = new File(apkpath);
        if(!file.exists() && !VirtualCore.get().isAppInstalled(packageName)) {
            return false;
        }
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                installApp(apkpath, packageName);
                return packageName;
            }

            @Override
            protected void onPostExecute(String packageName) {
                openApp(packageName);
            }
        }.execute();
        return true;
    }

    public static void installApp(String apkpath, final String packageName) {
        try {
            if (!VirtualCore.get().isAppInstalled(packageName)) {
                VirtualCore.get().installApp(apkpath, TERMINATE_IF_EXIST);
            }
        } catch (Throwable e) {

        }
    }

    public static void openApp(final String packageName) {
        Intent intent = VirtualCore.get().getLaunchIntent(packageName, userId);
        VActivityManager.get().startActivity(intent, userId);
    }

    private static void copyFromAssets(final Context context, final String name, String path) {
        File file = new File(path);
        if (file.exists()) return;
        AssetManager assetManager = context.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(name);
            out = new FileOutputStream(path);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        } catch (Exception e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                    in = null;
                } catch (Exception e) {
                }
            }
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                    out = null;
                } catch (Exception e) {
                }
            }
        }
    }


}
