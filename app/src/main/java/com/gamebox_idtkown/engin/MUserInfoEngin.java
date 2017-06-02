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
public class MUserInfoEngin extends BaseEngin<String> {

    private static MUserInfoEngin mUserInfoEngin;

    public MUserInfoEngin(Context context) {
        super(context);
    }

    public static MUserInfoEngin getImpl(Context context) {
        if (mUserInfoEngin == null) {
            synchronized (MUserInfoEngin.class) {
                mUserInfoEngin = new MUserInfoEngin(context);
            }
        }
        return mUserInfoEngin;
    }

    public void updateNickName(String nick_name, Callback<String> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", GBApplication.userInfo.getUserId());
        params.put("nick_name", nick_name);
        agetResultInfo(true, String.class, params, callback);
    }

    public void updateQQ(String qq, Callback<String> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", GBApplication.userInfo.getUserId());
        params.put("qq", qq);
        agetResultInfo(true, String.class, params, callback);
    }


    public void updateEmail(String email, Callback<String> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", GBApplication.userInfo.getUserId());
        params.put("email", email);
        agetResultInfo(true, String.class, params, callback);
    }

    public void updatePhone(String mobile, Callback<String> callback){
        Map<String, String> params = new HashMap<>();
        params.put("user_id", GBApplication.userInfo.getUserId());
        params.put("mobile", mobile);
        agetResultInfo(true, String.class, params, callback);
    }

    public void updateSex(String sex, Callback<String> callback){
        Map<String, String> params = new HashMap<>();
        params.put("user_id", GBApplication.userInfo.getUserId());
        params.put("sex", sex);
        agetResultInfo(true, String.class, params, callback);
    }


    @Override
    public String getUrl() {
        return Config.UPDATE_URL;
    }
}
