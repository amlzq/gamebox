package com.gamebox_idtkown.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.security.Md5;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.ShareUtil;
import com.gamebox_idtkown.views.widgets.GBActionBar5;
import com.gamebox_idtkown.views.widgets.GBBaseActionBar;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/10/10.
 */
public class GameWebActivity extends BaseActionBarActivity<GBActionBar5> {

    @BindView(R.id.web)
    WebView webView;

    private String url;
    private String title;
    private boolean showMenu;

    private int page = 0;

    @Override
    public int getLayoutID() {
        return R.layout.activity_web;
    }

    @Override
    public void initVars() {
        super.initVars();
        Intent intent = this.getIntent();
        if (intent != null) {
            url = intent.getStringExtra("url");
            title = intent.getStringExtra("title");
            showMenu = intent.getBooleanExtra("showMenu", false);
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        actionBar.setBackListener(new GBBaseActionBar.OnBackListener() {
            @Override
            public void onBack(View view) {
                back();
            }
        });

        if (title != null) {
            actionBar.setTitle(title + "");
        }
        if (showMenu) {
            actionBar.showMenuItem();
            actionBar.setOnItemClickListener(new GBActionBar5.OnItemClickListener() {
                @Override
                public void onItemClick(View view) {
                    actionBar.hideMenuItem();
                    //webView.loadUrl(getExtraUrl(Config.MY_GOOD_URL));
                }

                @Override
                public void onClose(View view) {
                    finish();
                }
            });
        } else {
            actionBar.hideMenuItem();
        }

        WebSettings webSettings = webView.getSettings();
        webSettings.setUserAgentString("6071GameBox");
        webSettings.setLoadsImagesAutomatically(false);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setNeedInitialFocus(false);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);   // 默认使用缓存
        webSettings.setAppCacheMaxSize(8 * 1024 * 1024);   //缓存最多可以有8M
        webSettings.setAllowFileAccess(true);   // 可以读取文件缓存(manifest生效)
        String appCaceDir = this.getDir("cache", Context.MODE_PRIVATE).getPath();
        webSettings.setAppCachePath(appCaceDir);
        if ((!Build.MANUFACTURER.toLowerCase().contains("xiaomi")) && (Build.MANUFACTURER.toLowerCase().contains("huawei"))) {

        }
        if ((Build.VERSION.SDK_INT >= 11) && (Build.MANUFACTURER.toLowerCase().contains("lenovo")))
            webView.setLayerType(1, null);

        webView.addJavascriptInterface(new AndroidJSObject(), "AndroidJSObject");

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //页面开始显示
                if (newProgress >= 70) {
                    removeProcessView();
                    loaded = 1;
                    if (title == null || title.isEmpty()) {
                        webView.loadUrl("javascript:AndroidJSObject.getTitle(document.title);");
                    }
                    stopRefreshing(1);
                }
            }
        });


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showProcessView();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                removeProcessView();
                loaded = 0;
                page++;
                if (page > 5) {
                    actionBar.showClose();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                removeProcessView();
            }
        });

        //刷新
        if (refreshLayout != null) {
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    webView.reload();
                }
            });
        }


        webView.loadUrl(getExtraUrl(url));
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (webView!=null && webView.canGoBack()) {
            if (showMenu) {
                actionBar.showMenuItem();
            }
            webView.goBack();
        } else {
            finish();
        }
    }

    private String getExtraUrl(String url) {
        String key = "gamebox";
        String timestamp = System.currentTimeMillis() + "";
        String extraUrl = "";
        extraUrl = url + "?user=" + GBApplication.userInfo.getUserId() + "&timestamp=" + timestamp + "&sign=" + Md5
                .md5(GBApplication.userInfo.getUserId() + timestamp + key);
        LogUtil.msg(extraUrl);
        return extraUrl;
    }

    class AndroidJSObject {
        @JavascriptInterface
        public void goToPage(final String url) {
            bindView(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getBaseContext(), GameWebActivity.class);
                    intent.putExtra("title", "");
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            });
        }

        @JavascriptInterface
        public void startPage(final String className) {
            bindView(new Runnable() {
                @Override
                public void run() {
                    try {
                        Class clazz = Class.forName(getBaseContext().getPackageName() + ".activitys." + className);
                        Intent intent = new Intent(getBaseContext(), clazz);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.msg("打开activity失败->" + e);
                    }
                }
            });
        }

        @JavascriptInterface
        public void getTitle(String _title) {
            if (title != null) {
                title = _title;
                bindView(new Runnable() {
                    @Override
                    public void run() {
                        actionBar.setTitle(title + "");
                    }
                });
            }
        }

        public void sign() {
            if (GBApplication.isLogin()) {
                GBApplication.userInfo.setSigned(true);
                GBApplication.updateUserInfo(getBaseContext());
            }
        }

        public void point(float point) {
            if (GBApplication.isLogin()) {
                float tpoint = Float.parseFloat(GBApplication.userInfo.getPoint());
                tpoint += point;
                GBApplication.userInfo.setPoint(tpoint + "");
                GBApplication.updateUserInfo(getBaseContext());
            }
        }

        public void share(final String content, final String img) {
            bindView(new Runnable() {
                @Override
                public void run() {
                    ShareUtil.openWXShareWithImage(GameWebActivity.this, content, img, 1);
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        webView.stopLoading();
    }
}
