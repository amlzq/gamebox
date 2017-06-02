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
public class UpdateAvatarEngin extends BaseEngin<String> {

    private static UpdateAvatarEngin avatarEngin;

    public UpdateAvatarEngin(Context context) {
        super(context);
    }

    public static UpdateAvatarEngin getImpl(Context context) {
        if (avatarEngin == null) {
            synchronized (UpdateAvatarEngin.class) {
                avatarEngin = new UpdateAvatarEngin(context);
            }
        }
        return avatarEngin;
    }

    public void updateAvatar(String base64, Callback<String> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", GBApplication.userInfo.getUserId());
        params.put("img", base64);
        agetResultInfo(String.class, params, callback);
    }

    @Override
    public String getUrl() {
        return Config.AVATAR_URL;
    }
}
