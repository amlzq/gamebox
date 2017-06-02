package com.gamebox_idtkown.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.core.GameBox;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.utils.CheckUtil;
import com.gamebox_idtkown.utils.PingUtil;
import com.gamebox_idtkown.utils.StateUtil;
import com.gamebox_idtkown.utils.TaskUtil;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.game.UMGameAgent;

import java.io.IOException;

import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;


/**
 * Created by zhangkai on 16/9/28.
 */
public class SplashActiviy extends InstrumentedActivity {
    private TextView tvJump;
    private TextView initCount;
    private TextView text;
    private ImageView lanuchImageView;
    private boolean isJump;
    private ImageView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_splash);
        View view = findViewById(android.R.id.content);
        lanuchImageView = (ImageView) findViewById(R.id.lanuch);
        loading = (ImageView) findViewById(R.id.loading);
        initCount = (TextView) findViewById(R.id.initCount);
        text = (TextView) findViewById(R.id.text);


        tvJump = (TextView) findViewById(R.id.tvJump);
        tvJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });


//        loading.startAnimation(AnimationUtil.rotaAnimation());

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                init();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });

        view.startAnimation(animation);


    }

    public void startGameDetailActivity(GameInfo gameInfo) {
        Intent intent = new Intent(getBaseContext(), GameDetailActivity.class);
        intent.putExtra("game_info", gameInfo);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        isJump = true;
        super.onBackPressed();

    }

    private void init() {
        int type = CheckUtil.getNetworkType(SplashActiviy.this);

        if (type == CheckUtil.NETTYPE_NO) {
            String msg = "您需要连接网络才能访问" + SplashActiviy.this.getResources().getString(R.string.app_name);
            show(msg);
            return;
        }
        GameBox.getImpl().initCount = 0;
        initCount.setText("");
        GameBox.getImpl().init2(SplashActiviy.this, new Runnable() {
            @Override
            public void run() {
                TaskUtil.getImpl().runTask(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final Bitmap bitmap = Picasso.with(getBaseContext()).load(GoagalInfo.getInItInfo()
                                    .launch_img).get();
                            SplashActiviy.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (bitmap != null) {
                                        lanuchImageView.setImageBitmap(bitmap);
                                        lanuchImageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (GoagalInfo.getInItInfo().getType() != null) {
                                                    goToMainActivity("jump");
                                                }

                                            }
                                        });
                                    }

                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                StateUtil.setRipple(tvJump);
                tvJump.setVisibility(View.VISIBLE);
                loading.clearAnimation();
                loading.setVisibility(View.GONE);
                text.setText("");
                initCount.setText("");
                tvJump.setTextColor(GoagalInfo.getInItInfo().androidColor);
                lanuchImageView.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (!isJump) {
                                                        goToMainActivity();
                                                    }
                                                }
                                            }

                        , 4000);



            }
        }, new Runnable() {
            @Override
            public void run() {
                String initStr = "";
                for (int i = 1; i <= GameBox.getImpl().initCount; i++) {
                    initStr += ".";
                }
                initCount.setText(initStr);
                if (GameBox.getImpl().initCount >= 3) {
                    TaskUtil.getImpl().runTask(new Runnable() {
                        @Override
                        public void run() {
                            String msg = "";
                            if (!PingUtil.ping("www.baidu.com")) {
                                msg = "您的网络状态不佳";
                            } else {
                                msg = "服务器出现异常";
                            }
                            final String fmsg = msg;
                            SplashActiviy.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    show(fmsg);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    private void goToMainActivity() {

        goToMainActivity(null);
    }

    private void goToMainActivity(String jump) {
        isJump = true;

        if (GBApplication.isLogin()) {
            GBApplication.login(getBaseContext());
        }
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        if (jump != null && !jump.isEmpty()) {
            intent.putExtra("jump", jump);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    private void openNetSetting(Context context) {
        //判断手机系统的版本  即API大于10 就是3.0或以上版本
        startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 1);
    }

    private void show(String msg) {
        MaterialDialog dialog = new MaterialDialog.Builder(SplashActiviy.this)
                .title("无法连接网络")
                .content(msg)
                .positiveColorRes(R.color.black)
                .negativeColorRes(R.color.gray_light)
                .positiveText("设置网络")
                .negativeText("退出")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (which == DialogAction.POSITIVE) {
                            openNetSetting(SplashActiviy.this);
                            dialog.dismiss();
                            return;
                        }
                        System.exit(0);
                    }
                }).build();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                System.exit(0);
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog,
                                 int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    System.exit(0);
                }
                return true;
            }
        });
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 集成基本统计分析,初始化 Session
        UMGameAgent.onResume(this);
        JPushInterface.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // //集成基本统计分析, 结束 Session
        UMGameAgent.onPause(this);
        JPushInterface.onPause(this);

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, Intent data) {
        if (requestCode == 1) {
            init();
        }
    }

}
