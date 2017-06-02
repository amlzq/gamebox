package com.gamebox_idtkown.activitys;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.core.GameBox;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.domain.InItInfo;
import com.gamebox_idtkown.engin.InitEngin;
import com.gamebox_idtkown.security.Base64;
import com.gamebox_idtkown.security.Encrypt;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.PathUtil;

import java.io.File;

/**
 * Created by zhangkai on 16/11/16.
 */
public class SDKBridgerActivity extends Activity {
    private SDKBridegerInfo sdkBridegerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = this.getIntent();
        GoagalInfo.setGoagalInfo(this, PathUtil.getGolgalDir());
        InitEngin initEngin = new InitEngin(this);
        InItInfo inItInfo = initEngin.get();
        if (inItInfo != null) {
            inItInfo.setThemeColor();
            GoagalInfo.setInitInfo(this, inItInfo);
            main(intent);
        } else {
            GameBox.getImpl().init2(this, new Runnable() {
                @Override
                public void run() {
                    main(intent);
                }
            }, new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }

    private void main(Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();
            if (uri != null) {
                sdkBridegerInfo = new SDKBridegerInfo();
                sdkBridegerInfo.phone = uri.getQueryParameter("phone");
                sdkBridegerInfo.username = uri.getQueryParameter("username");
                sdkBridegerInfo.pwd = uri.getQueryParameter("pwd");
                sdkBridegerInfo.act = uri.getQueryParameter("act");
                sdkBridegerInfo.data = uri.getQueryParameter("data");
                sdkBridegerInfo.title = uri.getQueryParameter("title");
                try {
                    sdkBridegerInfo.tab = Integer.parseInt(uri.getQueryParameter("tab"));
                } catch (Exception e) {

                }
            }
        } else {
            if (intent != null) {
                sdkBridegerInfo = new SDKBridegerInfo();
                sdkBridegerInfo.phone = intent.getStringExtra("phone");
                sdkBridegerInfo.username = intent.getStringExtra("username");
                sdkBridegerInfo.pwd = intent.getStringExtra("pwd");
                sdkBridegerInfo.act = intent.getStringExtra("act");
                sdkBridegerInfo.data = intent.getStringExtra("data");
                sdkBridegerInfo.title = intent.getStringExtra("title");
                sdkBridegerInfo.tab = intent.getIntExtra("tab", 0);
            }
        }

        if (sdkBridegerInfo == null || sdkBridegerInfo.act == null || sdkBridegerInfo.act.isEmpty()) {
            finish();
            return;
        }

        if (sdkBridegerInfo.phone != null && sdkBridegerInfo.pwd != null) {
            String pwd = Encrypt.decode(new String(Base64.decode(sdkBridegerInfo.pwd)));
            GBApplication.login(this, sdkBridegerInfo.phone, sdkBridegerInfo.username, pwd, new Runnable() {
                @Override
                public void run() {
                    SDKBridgerActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            goToActivity(sdkBridegerInfo.act, sdkBridegerInfo.tab);
                        }
                    });
                }
            }, new Runnable() {
                @Override
                public void run() {
                    SDKBridgerActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LogUtil.msg("登录失败");
                            finish();
                        }
                    });
                }
            });
        } else {
            goToActivity(sdkBridegerInfo.act, sdkBridegerInfo.tab);
        }
    }

    private void goToActivity(String act, int tab) {
        try {
            Class clazz = Class.forName("com.gamebox_idtkown.activitys." + act);
            Intent new_intent = new Intent(getBaseContext(), clazz);
            new_intent.putExtra("tab", tab);
            new_intent.putExtra("data", sdkBridegerInfo.data == null ? "" : sdkBridegerInfo.data);
            startActivity(new_intent);
        } catch (Exception e) {
        } finally {
            finish();
        }
    }

    class SDKBridegerInfo {
        public String phone;
        public String username;
        public String pwd;
        public String act;
        public String data;
        public String title;
        public int tab = 0;
    }
}
