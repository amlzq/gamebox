package com.gamebox_idtkown.activitys;

import android.app.Activity;
import android.os.Bundle;

import com.gamebox_idtkown.utils.FreeInstallUtil;
import com.umeng.analytics.game.UMGameAgent;


/**
 * Created by zhangkai on 2017/3/2.
 */

public class FreeInstallActivity extends Activity {
    public static final String packageName = "com.yicen.ttkb";
    public static final String apkName = "tt.apk";
    private boolean first_come_in = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        FreeInstallUtil.openApkFromAssets(this, apkName, packageName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!first_come_in) {
            FreeInstallUtil.openApp(packageName);
        }
        first_come_in = false;
        UMGameAgent.onResume(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        // //集成基本统计分析, 结束 Session
        UMGameAgent.onPause(this);
    }
}
