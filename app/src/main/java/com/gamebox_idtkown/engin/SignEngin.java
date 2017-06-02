package com.gamebox_idtkown.engin;

import android.content.Context;

import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.listeners.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangkai on 16/10/27.
 */
public class SignEngin extends BaseEngin<String> {

    private static SignEngin signEngin;

    public SignEngin(Context context) {
        super(context);
    }

    public static SignEngin getImpl(Context context) {
        if (signEngin == null) {
            synchronized (SignEngin.class) {
                signEngin = new SignEngin(context);
            }
        }
        return signEngin;
    }

    public void sign(Callback<String> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", GBApplication.userInfo.getUserId());
        agetResultInfo(true, String.class, params, callback);
    }

    @Override
    public String getUrl() {
        return Config.SIGN_URL;
    }
}
