package com.gamebox_idtkown.domain;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import com.gamebox_idtkown.utils.VUiKit;

import org.jdeferred.Promise;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;


/**
 * @author Lody
 */
public class AppRepository implements AppDataSource {

    private static final Collator COLLATOR = Collator.getInstance(Locale.CHINA);
    private static final List<String> sdCardScanPaths = Arrays.asList(
            "",
            "wandoujia/app",
            "tencent/tassistant/apk",
            "BaiduAsa9103056",
            "360Download",
            "pp/downloader",
            "pp/downloader/apk",
            "pp/downloader/silent/apk");

    private Context mContext;

    public AppRepository(Context context) {
        mContext = context;
    }

    private static boolean isSystemApplication(PackageInfo packageInfo) {
        return (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    @Override
    public Promise<List<AppModel>, Throwable, Void> getVirtualApps() {
        return VUiKit.defer().when(new Callable<List<AppModel>>() {
            @Override
            public List<AppModel> call() throws Exception {
                // TODO: 2019/3/28 vappC++问题所以注释
//				List<AppSetting> infos = VirtualCore.get().getAllApps();
                List<AppModel> models = new ArrayList<AppModel>();
//				for (AppSetting info : infos) {
//					if (VirtualCore.get().getLaunchIntent(info.packageName, VUserHandle.USER_OWNER) != null) {
//						models.add(new AppModel(mContext, info));
//					}
//				}
//				Collections.sort(models, new Comparator<AppModel>() {
//					@Override
//					public int compare(AppModel lhs, AppModel rhs) {
//						return COLLATOR.compare(lhs.name, rhs.name);
//					}
//				});
                return models;
            }
        });
    }

    @Override
    public Promise<List<AppModel>, Throwable, Void> getInstalledApps(final Context context) {
        return VUiKit.defer().when(new Callable<List<AppModel>>() {
            @Override
            public List<AppModel> call() throws Exception {
                return pkgInfosToAppModels(context, context.getPackageManager().getInstalledPackages(0), true);
            }
        });
    }

    @Override
    public Promise<List<AppModel>, Throwable, Void> getStorageApps(final Context context, final File rootDir) {
        return VUiKit.defer().when(new Callable<List<AppModel>>() {
            @Override
            public List<AppModel> call() throws Exception {
                return pkgInfosToAppModels(context, findAndParseAPKs(context, rootDir, sdCardScanPaths), false);
            }
        });
    }

    private List<PackageInfo> findAndParseAPKs(Context context, File rootDir, List<String> paths) {
        List<PackageInfo> pkgs = new ArrayList<>();
        if (paths == null)
            return pkgs;
        for (String path : paths) {
            File[] dirFiles = new File(rootDir, path).listFiles();
            if (dirFiles == null)
                continue;
            for (File f : dirFiles) {
                if (!f.getName().toLowerCase().endsWith(".apk"))
                    continue;
                PackageInfo pkgInfo = null;
                try {
                    pkgInfo = context.getPackageManager().getPackageArchiveInfo(f.getAbsolutePath(), 0);
                    pkgInfo.applicationInfo.sourceDir = f.getAbsolutePath();
                    pkgInfo.applicationInfo.publicSourceDir = f.getAbsolutePath();
                } catch (Exception e) {
                    // Ignore
                }
                if (pkgInfo != null)
                    pkgs.add(pkgInfo);
            }
        }
        return pkgs;
    }

    private List<AppModel> pkgInfosToAppModels(Context context, List<PackageInfo> pkgList, boolean fastOpen) {
        List<AppModel> models = new ArrayList<>(pkgList.size());
        // TODO: 2019/3/28 vappC++问题所以注释
//        String hostPkg = VirtualCore.get().getHostPkg();
//        for (PackageInfo pkg : pkgList) {
//            if (hostPkg.equals(pkg.packageName)) {
//                continue;
//            }
//            if (isSystemApplication(pkg)) {
//                continue;
//            }
//            if (VirtualCore.get().isAppInstalled(pkg.packageName)) {
//                continue;
//            }
//            AppModel model = new AppModel(context, pkg);
//            model.fastOpen = fastOpen;
//            models.add(model);
//        }
        Collections.sort(models, new Comparator<AppModel>() {
            @Override
            public int compare(AppModel lhs, AppModel rhs) {
                return COLLATOR.compare(lhs.name, rhs.name);
            }
        });
        return models;
    }

    @Override
    public void addVirtualApp(AppModel app) throws Throwable {
        // TODO: 2019/3/28 vappC++问题所以注释
//        int flags = InstallStrategy.TERMINATE_IF_EXIST;
//        if (app.fastOpen) {
//            flags |= InstallStrategy.DEPEND_SYSTEM_IF_EXIST;
//        }
//        VirtualCore.get().installApp(app.path, flags);
    }

    @Override
    public void removeVirtualApp(AppModel app) throws Throwable {
        // TODO: 2019/3/28 vappC++问题所以注释
//        VirtualCore.get().uninstallApp(app.packageName);
    }

}
