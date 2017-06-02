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
public class RegisterEngin extends BaseEngin<UserInfo>  {

    private static RegisterEngin registerEngin;

    public RegisterEngin(Context context) {
        super(context);
    }

    public static RegisterEngin getImpl(Context context){
        if(registerEngin == null){
            synchronized (RegisterEngin.class) {
                registerEngin = new RegisterEngin(context);
            }
        }
        return registerEngin;
    }

    public void register(String mobile, String user_name,String pwd, String fromId, Callback<UserInfo> callback){
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("user_name", user_name);
        params.put("pwd", pwd);
        params.put("imeil", DeviceUtil.getDeviceIMEI(context));
        params.put("device_type", "2");
        params.put("sys_version", DeviceUtil.getDeviceSystemVersion());
        params.put("from_id", fromId);
        agetResultInfo(true, UserInfo.class, params, callback);
    }

    @Override
    public String getUrl() {
        return Config.REGISTER_URL;
    }
}
