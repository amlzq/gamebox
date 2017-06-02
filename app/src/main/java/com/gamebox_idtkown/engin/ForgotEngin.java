package com.gamebox_idtkown.engin;

import android.content.Context;

import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.listeners.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangkai on 16/10/27.
 */
public class ForgotEngin extends BaseEngin<String> {
    private static ForgotEngin forgotEngin;

    public ForgotEngin(Context context) {
        super(context);
    }

    public static ForgotEngin getImpl(Context context) {
        if (forgotEngin == null) {
            synchronized (ForgotEngin.class) {
                forgotEngin = new ForgotEngin(context);
            }
        }
        return forgotEngin;
    }

    public void forgot(String mobile, String pwd, Callback<String> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("pwd", pwd);
        agetResultInfo(true, String.class, params, callback);
    }

    @Override
    public String getUrl() {
        return Config.FORGOT_URL;
    }
}
