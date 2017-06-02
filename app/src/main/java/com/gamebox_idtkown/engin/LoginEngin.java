package com.gamebox_idtkown.engin;

import android.content.Context;

import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.db.greendao.UserInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.utils.DeviceUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangkai on 16/10/26.
 */
public class LoginEngin extends BaseEngin<UserInfo> {
    private static LoginEngin loginEngin;

    public LoginEngin(Context context) {
        super(context);
    }

    public static LoginEngin getImpl(Context context){
        if(loginEngin == null){
            synchronized (LoginEngin.class) {
                loginEngin = new LoginEngin(context);
            }
        }
        return loginEngin;
    }

    public void login(String mobile, String user_name ,String pwd, Callback<UserInfo> callback){
        Map<String, String> params = new HashMap<>();
        if(mobile== null || mobile.isEmpty()){
            mobile = user_name;
        }
        params.put("mobile", mobile);
        params.put("user_name", user_name);
        params.put("pwd", pwd);
        params.put("imeil", DeviceUtil.getDeviceIMEI(context));
        params.put("device_type", "2");
        params.put("sys_version", DeviceUtil.getDeviceSystemVersion());
        agetResultInfo(true, UserInfo.class, params, callback);
    }

    @Override
    public String getUrl() {
        return Config.LOGIN_URL;
    }
}
