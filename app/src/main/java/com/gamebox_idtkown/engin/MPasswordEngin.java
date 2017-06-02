package com.gamebox_idtkown.engin;

import android.content.Context;

import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.listeners.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangkai on 16/10/28.
 */
public class MPasswordEngin extends BaseEngin<String> {

    private static MPasswordEngin mPasswordEngin;

    public MPasswordEngin(Context context) {
        super(context);
    }

    public static MPasswordEngin getImpl(Context context) {
        if (mPasswordEngin == null) {
            synchronized (MPasswordEngin.class) {
                mPasswordEngin = new MPasswordEngin(context);
            }
        }
        return mPasswordEngin;
    }

    public void mPassword(String pwd, Callback<String> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", GBApplication.userInfo.getUserId());
        params.put("pwd", pwd);
        agetResultInfo(true, String.class, params, callback);
    }

    @Override
    public String getUrl() {
        return Config.MPASSWORD_URL;
    }
}