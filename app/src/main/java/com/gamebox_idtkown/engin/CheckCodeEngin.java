package com.gamebox_idtkown.engin;

import android.content.Context;

import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.utils.DeviceUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangkai on 16/10/26.
 */
public class CheckCodeEngin extends BaseEngin<String> {

    private static CheckCodeEngin checkCodeEngin;


    public CheckCodeEngin(Context context) {
        super(context);
    }

    public static CheckCodeEngin getImpl(Context context){
        if(checkCodeEngin == null){
            synchronized (CheckCodeEngin.class) {
                checkCodeEngin = new CheckCodeEngin(context);
            }
        }
        return checkCodeEngin;
    }

    public void check(String mobile, String user_name, String code, String type, Callback<String> callback){
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("user_name", user_name);
        params.put("type", type);
        params.put("code", code);
        params.put("imeil", DeviceUtil.getDeviceIMEI(context));
        agetResultInfo(true, String.class, params, callback);
    }

    @Override
    public String getUrl() {
        return Config.CHECK_CODE_URL;
    }
}
